package com.floweytf.mechthon.util;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public record Paths(Path root, Path scripts, Path globalKeys, Path libs, Path cache) {
    public Paths(Path root) {
        this(
            root,
            root.resolve("scripts"),
            root.resolve("metadata/global_keys"),
            root.resolve("internal/libraries"),
            root.resolve("internal/cache")
        );
    }

    public void makeDirs() throws IOException {
        Files.createDirectories(root);
        Files.createDirectories(scripts);
        Files.createDirectories(globalKeys);
        Files.createDirectories(libs);
        Files.createDirectories(cache);
    }
}
