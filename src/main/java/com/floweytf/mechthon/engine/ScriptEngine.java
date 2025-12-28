package com.floweytf.mechthon.engine;

import com.floweytf.mechthon.MechthonPlugin;
import com.floweytf.mechthon.util.Paths;
import com.floweytf.mechthon.util.Util;
import com.google.common.base.Preconditions;
import java.io.IOException;
import java.nio.file.Files;
import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.graalvm.polyglot.Context;
import org.graalvm.polyglot.Value;

public class ScriptEngine implements AutoCloseable {
    private final Context context;
    private final Bootstrap bootstrap;
    private final Bindings bindings;
    private final ScriptManager scriptManager;
    private final MechthonPlugin plugin;

    private boolean closed;

    public ScriptEngine(MechthonPlugin plugin, Paths paths, LoadHandler events) {
        this.plugin = plugin;

        context = Context.newBuilder("python")
            .allowAllAccess(true)
            .hostClassLoader(ScriptEngine.class.getClassLoader())
            .option("engine.WarnInterpreterOnly", "false")
            .option("python.DontWriteBytecodeFlag", "false")
            .option("python.PyCachePrefix", paths.cache().toAbsolutePath().toString())
            .build();

        context.getBindings("python").putMember("__library_dir", paths.libs().toAbsolutePath().toString());

        bootstrap = Util.profile(() -> new Bootstrap(context), events::perfBootstrap);
        bindings = Util.profile(() -> new Bindings(bootstrap), events::perfBindings);

        context.getBindings("python").putMember("__api", new APIAccess(plugin, this, bindings));

        try {
            Files.createDirectories(paths.scripts());
        } catch (IOException e) {
            throw Util.sneakyThrow(e);
        }

        scriptManager = new ScriptManager(plugin, paths, bootstrap, this, events);
    }

    @Override
    public void close() {
        Preconditions.checkState(Bukkit.isPrimaryThread());

        if (closed) {
            return;
        }

        closed = true;
        context.close(true);
    }

    public boolean isClosed() {
        return closed;
    }

    public ScriptManager scriptManager() {
        return scriptManager;
    }

    public Value invokeScript(Entity entity, String name) {
        Preconditions.checkState(Bukkit.isPrimaryThread());
        Preconditions.checkState(!isClosed());
        Preconditions.checkArgument(entity != null);
        Preconditions.checkArgument(name != null);

        final var scripts = scriptManager.scripts();
        final var script = scripts.get(name);
        Preconditions.checkArgument(script != null, "unknown script '%s'", name);
        Preconditions.checkArgument(script.getMain() != null, "script '%s' does not have an entry point", name);
        return script.getMain().execute(bindings.createEntity(entity));
    }

    public Value invokeTrigger(Entity entity, String name, String triggerName) {
        Preconditions.checkState(Bukkit.isPrimaryThread());
        Preconditions.checkState(!isClosed());
        Preconditions.checkArgument(entity != null);
        Preconditions.checkArgument(name != null);
        Preconditions.checkArgument(triggerName != null);

        final var scripts = scriptManager.scripts();
        final var script = scripts.get(name);
        Preconditions.checkArgument(script != null, "unknown script '%s'", name);
        final var trigger = script.triggerables().get(triggerName);
        Preconditions.checkArgument(trigger != null, "script '%s' does not have trigger '%s'", name, triggerName);
        return trigger.execute(bindings.createEntity(entity));
    }
}
