package com.floweytf.mechthon;

import com.floweytf.mechthon.engine.LoadHandler;
import com.floweytf.mechthon.engine.ScriptEngine;
import com.floweytf.mechthon.entity.EntityManager;
import com.floweytf.mechthon.util.Paths;
import com.floweytf.mechthon.util.Util;
import com.google.common.base.Preconditions;
import java.io.IOException;
import java.util.Arrays;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;
import org.bukkit.Bukkit;
import org.bukkit.entity.EntityType;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.Nullable;

@SuppressWarnings("unused")
public class MechthonPlugin extends JavaPlugin {
    private static MechthonPlugin instance = null;

    @Nullable
    private CompletableFuture<ScriptEngine> engineFuture;
    private final EntityManager entityManager = new EntityManager(this);
    private final Paths paths = new Paths(getDataFolder().toPath());

    // initialization logic
    @Override
    public void onLoad() {
        Preconditions.checkState(instance == null);
        instance = this;

        try {
            paths.makeDirs();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        getSLF4JLogger().info("unpacking libraries...");
        MechthonApiImpl.INSTANCE.registerPythonModule(this, "mechs", "/mechthon/mechs/");

        try {
            MechthonApiImpl.INSTANCE.initialize(paths);
        } catch (IOException e) {
            throw Util.sneakyThrow(e);
        }

        final var s = Arrays.stream(EntityType.values())
            .map(EntityType::getEntityClass)
            .collect(Collectors.toSet());

        engineFuture = CompletableFuture.supplyAsync(() -> new ScriptEngine(
            this,
            paths,
            LoadHandler.logging(getSLF4JLogger())
        ));

        Commands.register(this);
    }

    @Override
    public void onEnable() {
        Bukkit.getPluginManager().registerEvents(entityManager, this);

        getSLF4JLogger().info(
            "mechthon loading blocked main thread for {}ms",
            Util.profile(() -> Objects.requireNonNull(engineFuture).join())
        );
    }

    @Override
    public void onDisable() {
        Preconditions.checkState(instance != null);
        instance = null;
        engine().close();
        engineFuture = null;
    }

    public static MechthonPlugin instance() {
        Preconditions.checkState(instance != null);
        return instance;
    }

    public ScriptEngine engine() {
        Preconditions.checkState(engineFuture != null, "getEngine() called before onLoad()");
        final var res = engineFuture.getNow(null);
        Preconditions.checkState(engineFuture != null, "getEngine() called before onEnable()");
        return res;
    }

    public EntityManager entityManager() {
        return entityManager;
    }
}
