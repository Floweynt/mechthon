package com.floweytf.mechthon.engine;

import com.floweytf.mechthon.MechthonPlugin;
import com.floweytf.mechthon.util.Paths;
import com.floweytf.mechthon.util.Util;
import com.google.common.base.Preconditions;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Collections;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;
import net.kyori.adventure.key.Key;
import org.bukkit.Bukkit;
import org.graalvm.polyglot.PolyglotException;

public class ScriptManager {
    private static final String EXTENSION = ".py";

    private final MechthonPlugin plugin;
    private final Paths paths;
    private final Bootstrap bootstrap;
    private final ScriptEngine engine;

    private boolean isCurrentlyReloading = false;
    private Map<String, ScriptInstance> scripts;

    ScriptManager(MechthonPlugin plugin, Paths paths, Bootstrap bootstrap, ScriptEngine engine, LoadHandler handler) {
        this.plugin = plugin;
        this.paths = paths;
        this.bootstrap = bootstrap;
        this.engine = engine;

        this.scripts = loadScripts(handler);
    }

    private Map<String, ScriptInstance> loadScripts(LoadHandler loadHandler) {
        final var scripts = new Object2ObjectOpenHashMap<String, ScriptInstance>();

        try (final var stream = Files.walk(paths.scripts())) {
            final var loadTime = Util.profile(() -> stream.filter(Files::isRegularFile).forEach(path -> {
                if (!path.getFileName().toString().endsWith(EXTENSION)) {
                    loadHandler.warnIllegalExtension(path);
                    return;
                }

                final var relativeDir = paths.scripts().relativize(path).toString();
                final var scriptName = relativeDir.substring(0, relativeDir.length() - EXTENSION.length());

                if (!Key.parseableNamespace(scriptName)) {
                    loadHandler.warnBadName(scriptName, path);
                    return;
                }

                try {
                    final var script = new ScriptInstance(scriptName);
                    bootstrap.loadScript(script, Files.readString(path));
                    script.freeze();
                    scripts.put(scriptName, script);
                } catch (IOException e) {
                    loadHandler.warnIOException(scriptName, path, e);
                } catch (PolyglotException e) {
                    loadHandler.warnPolyglotException(scriptName, path, e);
                }
            }));

            loadHandler.perfLoad(scripts.size(), loadTime);
        } catch (IOException e) {
            loadHandler.warnException(e);
        }

        return scripts;
    }

    public boolean reloadScripts(LoadHandler loadHandler, Runnable onComplete, Consumer<Throwable> onError) {
        Preconditions.checkState(Bukkit.isPrimaryThread());
        Preconditions.checkState(!engine.isClosed());

        if (engine.isClosed() || isCurrentlyReloading) {
            return false;
        }

        isCurrentlyReloading = true;

        CompletableFuture.runAsync(() -> {
            try {
                Map<String, ScriptInstance> newScripts = loadScripts(loadHandler);

                Bukkit.getScheduler().runTask(plugin, () -> {
                    this.scripts = newScripts;
                    isCurrentlyReloading = false;
                    onComplete.run();
                });
            } catch (Throwable e) {
                Bukkit.getScheduler().runTask(plugin, () -> {
                    isCurrentlyReloading = false;
                    onError.accept(e);
                });
            }
        });

        return true;
    }

    public Map<String, ScriptInstance> getScripts() {
        return Collections.unmodifiableMap(scripts);
    }
}