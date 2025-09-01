package com.floweytf.mechthon.codegen;

import org.jetbrains.annotations.Nullable;

public record Property(String name, String getterName, @Nullable String setterName, Class<?> type, boolean nullable) {
}
