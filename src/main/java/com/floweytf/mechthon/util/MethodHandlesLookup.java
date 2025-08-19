package com.floweytf.mechthon.util;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.reflect.Method;

public record MethodHandlesLookup(MethodHandles.Lookup lookup) {
    public MethodHandle unreflect(Method m) {
        try {
            return lookup.unreflect(m);
        } catch (IllegalAccessException e) {
            throw Util.sneakyThrow(e);
        }
    }
}
