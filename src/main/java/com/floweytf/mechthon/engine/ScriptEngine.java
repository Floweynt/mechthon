package com.floweytf.mechthon.engine;

import com.floweytf.mechthon.util.Util;
import com.google.common.base.Preconditions;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collections;
import java.util.Map;
import net.kyori.adventure.key.Key;
import org.bukkit.entity.Entity;
import org.graalvm.polyglot.Context;
import org.graalvm.polyglot.HostAccess;
import org.graalvm.polyglot.PolyglotException;
import org.graalvm.polyglot.Value;

public class ScriptEngine implements AutoCloseable {

    private static final String EXTENSION = ".py";

    private final Context context;
    private final Bootstrap bootstrap;
    private final Bindings bindings;
    private final Map<String, ScriptInstance> scripts = new Object2ObjectOpenHashMap<>();

    public ScriptEngine(Path root, LoadHandler events) {
        this.context = Context.newBuilder("python")
            .allowAllAccess(false)
            .allowHostAccess(HostAccess.ALL)
            .option("engine.WarnInterpreterOnly", "false")
            .build();

        bootstrap = Util.profile(() -> new Bootstrap(context), events::perfBootstrap);

        bindings = Util.profile(() -> {
            StaticSources.BINDINGS.forEach(binding -> bootstrap.definePythonModule(
                "mechs", binding.name(), binding.source(), binding.isPackage()
            ));

            return new Bindings(bootstrap);
        }, events::perfBindings);

        context.getBindings("python").putMember("__api", new APIAccess(this, bindings));

        try {
            Files.createDirectories(root);
        } catch (IOException e) {
            Util.sneakyThrow(e);
        }

        try (final var stream = Files.walk(root)) {
            final var loadTime = Util.profile(() -> stream.filter(Files::isRegularFile).forEach(path -> {
                if (!path.getFileName().toString().endsWith(EXTENSION)) {
                    events.warnIllegalExtension(path); // "Non-python script file found, skipping: {}",
                    return;
                }

                final var relativeDir = root.relativize(path).toString();
                final var scriptName = relativeDir.substring(0, relativeDir.length() - EXTENSION.length());

                if (!Key.parseableNamespace(scriptName)) {
                    // "Bad script name, skipping: {}",
                    events.warnBadName(scriptName, path);
                    return;
                }

                try {
                    final var script = new ScriptInstance(scriptName);
                    bootstrap.loadScript(script, Files.readString(path));
                    script.freeze();
                    scripts.put(scriptName, script);
                } catch (IOException e) {
                    events.warnIOException(scriptName, path, e);
                } catch (PolyglotException e) {
                    events.warnPolyglotException(scriptName, path, e);
                }
            }));

            events.perfLoad(scripts.size(), loadTime);
        } catch (IOException e) {
            events.warnException(e);
        }
    }

    @Override
    public void close() {
        context.close();
    }

    public Map<String, ScriptInstance> getScripts() {
        return Collections.unmodifiableMap(scripts);
    }

    public Value invokeScript(Entity entity, String name) {
        final var script = scripts.get(name);
        Preconditions.checkArgument(script != null, "unknown script '%s'", name);
        Preconditions.checkArgument(script.getMain() != null, "script '%s' does not have an entry point", name);
        return script.getMain().execute(bindings.createEntity(entity));
    }

    public Value invokeTrigger(Entity entity, String name, String triggerName) {
        final var script = scripts.get(name);
        Preconditions.checkArgument(script != null, "unknown script '%s'", name);
        final var trigger = script.getTriggerable().get(triggerName);
        Preconditions.checkArgument(trigger != null, "script '%s' does not have trigger '%s'", name, triggerName);
        return trigger.execute(bindings.createEntity(entity));
    }
}
