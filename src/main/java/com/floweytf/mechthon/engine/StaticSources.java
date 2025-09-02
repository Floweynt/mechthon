package com.floweytf.mechthon.engine;

import com.floweytf.mechthon.util.Util;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

public class StaticSources {
    private static String read(String name) {
        try (final var s = Objects.requireNonNull(StaticSources.class.getResource(name)).openStream()) {
            return new String(s.readAllBytes(), StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw Util.sneakyThrow(e);
        }
    }

    public static final String BOOTSTRAP = read("/mechthon/bootstrap.py");
    public static final String BINDINGS = read("/mechthon/bindings.py");
}
