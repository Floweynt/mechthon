package com.floweytf.mechthon.engine;

import com.floweytf.mechthon.MechthonPlugin;
import com.floweytf.mechthon.persist.GlobalPersistentKeyMetadata;
import com.floweytf.mechthon.util.Paths;
import com.floweytf.mechthon.util.Util;
import com.google.common.base.Preconditions;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collections;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;
import net.kyori.adventure.key.Key;
import org.bukkit.Bukkit;
import org.graalvm.polyglot.PolyglotException;

public class ScriptManager {
    private interface Loader<T> {
        T load(String name, Path path) throws IOException;
    }

    private final MechthonPlugin plugin;
    private final Paths paths;
    private final Bootstrap bootstrap;
    private final ScriptEngine engine;

    private boolean isCurrentlyReloading = false;
    private Map<String, ScriptInstance> scripts;
    private Map<String, GlobalPersistentKeyMetadata> globalPersistentKeys;

    ScriptManager(MechthonPlugin plugin, Paths paths, Bootstrap bootstrap, ScriptEngine engine, LoadHandler handler) {
        this.plugin = plugin;
        this.paths = paths;
        this.bootstrap = bootstrap;
        this.engine = engine;

        this.scripts = loadScripts(handler);
        this.globalPersistentKeys = loadGlobalPersistentKeys(handler);
    }

    private static <T> Map<String, T> load(
        LoadHandler.LoadType type,
        Path p,
        LoadHandler loadHandler,
        Loader<T> loader
    ) {
        final var results = new Object2ObjectOpenHashMap<String, T>();

        try (final var stream = Files.walk(p)) {
            final var loadTime = Util.profile(() -> stream.filter(Files::isRegularFile).forEach(path -> {
                if (!path.getFileName().toString().endsWith("." + type.extension())) {
                    loadHandler.warnIllegalExtension(type, path);
                    return;
                }

                final var relativeDir = p.relativize(path).toString();
                final var name = relativeDir.substring(0, relativeDir.length() - type.extension().length() - 1);

                if (!Key.parseableNamespace(name)) {
                    loadHandler.warnBadName(type, name, path);
                    return;
                }

                try {
                    results.put(name, loader.load(name, path));
                } catch (IOException e) {
                    loadHandler.warnIOException(type, name, path, e);
                } catch (PolyglotException e) {
                    loadHandler.warnPolyglotException(type, name, path, e);
                }
            }));

            loadHandler.perfLoad(type, results.size(), loadTime);
        } catch (IOException e) {
            loadHandler.warnException(type, e);
        }

        return results;
    }

    private Map<String, ScriptInstance> loadScripts(LoadHandler loadHandler) {
        return load(LoadHandler.LoadType.SCRIPT, paths.scripts(), loadHandler, (name, path) -> {
            final var script = new ScriptInstance(name);
            bootstrap.loadScript(script, Files.readString(path));
            script.freeze();
            return script;
        });
    }

    private Map<String, GlobalPersistentKeyMetadata> loadGlobalPersistentKeys(LoadHandler loadHandler) {
        return load(LoadHandler.LoadType.GLOBAL_PERSISTENT_KEY, paths.scripts(), loadHandler, (name, path) -> {
            throw new RuntimeException("TOOD");
        });
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
                final var newScripts = loadScripts(loadHandler);
                final var newGlobalPersistentKeys = loadGlobalPersistentKeys(loadHandler);

                Bukkit.getScheduler().runTask(plugin, () -> {
                    this.scripts = newScripts;
                    this.globalPersistentKeys = newGlobalPersistentKeys;
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

    public Map<String, ScriptInstance> scripts() {
        return Collections.unmodifiableMap(scripts);
    }

    public Map<String, GlobalPersistentKeyMetadata> globalPersistentKeys() {
        return Collections.unmodifiableMap(globalPersistentKeys);
    }
}