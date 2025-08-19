package com.floweytf.mechthon.engine;

import com.floweytf.mechthon.util.Util;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Objects;

public class StaticSources {
    public record Binding(String name, String source, boolean isPackage) {
    }

    private static String read(String name) {
        try (final var s = Objects.requireNonNull(StaticSources.class.getResource(name)).openStream()) {
            return new String(s.readAllBytes(), StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw Util.sneakyThrow(e);
        }
    }

    public static final String BOOTSTRAP = read("/mechthon/bootstrap.py");

    public static final List<Binding> BINDINGS = List.of(
        new Binding("_internal", read("/mechthon/mechs/_internal.py"), true),
        new Binding("mechs", read("/mechthon/mechs/__init__.py"), true),
        new Binding("mechs.types", read("/mechthon/mechs/types.py"), true),
        new Binding("mechs.scheduler", read("/mechthon/mechs/scheduler.py"), true),
        new Binding("mechs.entity", read("/mechthon/mechs/entity.py"), true),
        new Binding("mechs.attachment", read("/mechthon/mechs/attachment.py"), true)
    );
}
