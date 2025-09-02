package com.floweytf.mechthon;

import com.floweytf.mechthon.api.Mechthon;
import com.floweytf.mechthon.util.Paths;
import com.floweytf.mechthon.util.Util;
import com.google.common.base.Preconditions;
import it.unimi.dsi.fastutil.Pair;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;
import org.bukkit.plugin.Plugin;

public class MechthonApiImpl implements Mechthon {
    public static final MechthonApiImpl INSTANCE = new MechthonApiImpl();

    private boolean isInit = false;
    private final Map<String, Pair<Plugin, String>> modules = new Object2ObjectOpenHashMap<>();

    @Override
    public void registerPythonModule(Plugin plugin, String moduleName, String path) {
        Preconditions.checkState(
            !isInit,
            "registerPythonModule() called after init, is the plugin set to load before?"
        );

        final var res = modules.putIfAbsent(moduleName, Pair.of(plugin, path));

        if (res != null) {
            throw new IllegalArgumentException("module '%s' already registered by plugin '%s'".formatted(
                moduleName,
                res.first().getName()
            ));
        }
    }

    public void initialize(Paths paths) throws IOException {
        Preconditions.checkState(!isInit, "initialize() called twice");
        isInit = true;

        try {
            Util.rmRecursive(paths.libs());
        } catch (IOException ignored) {
        }

        // try and delete the dir
        Files.createDirectories(paths.libs());

        for (final var toLoad : modules.entrySet()) {
            final var plugin = toLoad.getValue().first();
            final var moduleName = toLoad.getKey();
            final var path = toLoad.getValue().second();

            final var url = plugin.getClass().getProtectionDomain().getCodeSource().getLocation();

            try (final var fs = FileSystems.newFileSystem(Path.of(url.toURI()))) {
                Util.copyFolder(
                    fs.getPath(path),
                    paths.libs().resolve(moduleName)
                );
            } catch (URISyntaxException e) {
                throw Util.sneakyThrow(e);
            }
        }
    }
}
