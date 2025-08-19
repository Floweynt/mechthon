package com.floweytf.mechthon;

import com.destroystokyo.paper.event.server.ServerTickStartEvent;
import com.floweytf.mechthon.bindings.compiled.CompiledBinding;
import com.floweytf.mechthon.bindings.compiler.BindingCompiler;
import com.floweytf.mechthon.bindings.impl.BindingsImpl;
import com.floweytf.mechthon.bindings.truffle.objects.BindingInstanceTO;
import com.floweytf.mechthon.engine.LoadHandler;
import com.floweytf.mechthon.engine.ScriptEngine;
import com.floweytf.mechthon.reflect.EntityVariables;
import com.floweytf.mechthon.util.ReloadableResource;
import com.floweytf.mechthon.util.Util;
import com.google.common.base.Preconditions;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Zombie;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import org.graalvm.polyglot.Context;
import org.graalvm.polyglot.HostAccess;
import org.graalvm.polyglot.Value;

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

        Preconditions.checkState(reloadEngine(
            LoadHandler.logging(getSLF4JLogger()),
            () -> {
            }
        ));

        EntityVariables.scan();

        System.out.println(System.getProperty("jdk.module.upgrade.path"));

        Commands.register(this);
    }

    @Override
    public void onEnable() {
        getSLF4JLogger().info("mechthon loading blocked main thread for {}ms", Util.profile(this::awaitEngineReload));

        Bukkit.getServer().getPluginManager().registerEvents(new Listener() {
            final Context context = Context.newBuilder("python")
                .allowAllAccess(false)
                .allowHostAccess(HostAccess.ALL)
                .build();

            final Value v;
            final CompiledBinding cb;

            {
                class Foo {
                    public static int tell(Entity e, int msg) {
                        e.sendMessage("Flowey is a cis man " + msg);
                        return 0;
                    }
                }

                final var bindings = new BindingsImpl();
                final var aBindings = bindings.defineClassBinding(Entity.class, "meow", "A");
                try {
                    aBindings.function("tell", MethodHandles.lookup().findStatic(Foo.class, "tell",
                        MethodType.methodType(int.class, Entity.class, int.class)));
                } catch (Exception e) {
                    throw Util.sneakyThrow(e);
                }

                final var built = BindingCompiler.compile(bindings);

                context.eval("python", """
                    def main():
                        for i in range(0, 100000):
                            player.tell(33)
                    """);

                v = context.getBindings("python").getMember("main");
                cb = built.knownBindingTypes().get(Entity.class);
            }

            @EventHandler
            public void tick(ServerTickStartEvent event) {
                final var zombie =
                    Bukkit.getServer().getWorlds().get(0).getEntitiesByClass(Zombie.class).stream().findFirst();

                if (zombie.isEmpty()) {
                    return;
                }

                {
                    context.getBindings("python").putMember("player", new BindingInstanceTO(zombie.orElseThrow(), cb, null));
                    final var start = System.nanoTime();
                    v.executeVoid();
                    final var end = System.nanoTime();
                }

                {
                    final var start = System.nanoTime();
                    Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "execute as " + zombie.orElseThrow().getUniqueId() + " run function test:a");
                    final var end = System.nanoTime();
                }
            }
        }, this);
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
