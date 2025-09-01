package com.floweytf.mechthon;

import com.floweytf.mechthon.engine.LoadHandler;
import com.floweytf.mechthon.engine.ScriptEngine;
import com.floweytf.mechthon.util.ReloadableResource;
import com.floweytf.mechthon.util.Util;
import com.google.common.base.Preconditions;
import java.io.IOException;
import java.util.Arrays;
import java.util.stream.Collectors;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.EntityType;
import org.bukkit.plugin.java.JavaPlugin;

@SuppressWarnings("unused")
public class MechthonPlugin extends JavaPlugin {
    private static final Component BASE = Component.text("[")
        .append(Component.text("ScriptEngine", NamedTextColor.GOLD))
        .append(Component.text("] "));
    private static MechthonPlugin instance = null;

    private final ReloadableResource<ScriptEngine> engine = new ReloadableResource<>();

    // initialization logic
    @Override
    public void onLoad() {
        Preconditions.checkState(instance == null);
        instance = this;

        getSLF4JLogger().info("unpacking libraries...");
        MechthonApiImpl.INSTANCE.registerPythonModule(this, "mechs", "/mechthon/mechs/");

        try {
            MechthonApiImpl.INSTANCE.initialize(getDataFolder().toPath());
        } catch (IOException e) {
            throw Util.sneakyThrow(e);
        }

        final var s = Arrays.stream(EntityType.values())
            .map(EntityType::getEntityClass)
            .collect(Collectors.toSet());

        Preconditions.checkState(reloadEngine(
            LoadHandler.logging(getSLF4JLogger()),
            () -> {
            }
        ));

        Commands.register(this);
    }

    @Override
    public void onEnable() {
        getSLF4JLogger().info("mechthon loading blocked main thread for {}ms", Util.profile(this::awaitEngineReload));
    }

    @Override
    public void onDisable() {
        Preconditions.checkState(instance != null);
        instance = null;

        engine.close();
    }

    public static MechthonPlugin instance() {
        Preconditions.checkState(instance != null);
        return instance;
    }

    public static NamespacedKey key(String value) {
        return new NamespacedKey("mechthon", value);
    }

    public static void sendMessage(Audience audience, TextColor color, String message, Object... args) {
        audience.sendMessage(BASE.append(Component.text(String.format(message, args), color)));
    }

    public boolean reloadEngine(LoadHandler handler, Runnable onComplete) {
        return engine.reload(() -> new ScriptEngine(
            this,
            getDataFolder().toPath().resolve("scripts"),
            handler
        ), onComplete);
    }

    public void awaitEngineReload() {
        engine.awaitReload();
    }

    public ScriptEngine getEngine() {
        return engine.get();
    }
}
