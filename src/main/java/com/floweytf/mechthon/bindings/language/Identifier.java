package com.floweytf.mechthon.bindings.language;

import java.util.List;

public record Identifier(List<String> parts) {
    public static Identifier of(String string) {

    }
}
