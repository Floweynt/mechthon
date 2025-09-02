package com.floweytf.mechthon.util;

import java.nio.file.Path;

public record Paths(Path root, Path libs, Path scripts) {
    public Paths(Path root) {
        this(root, root.resolve("libraries"), root.resolve("scripts"));
    }
}
