package com.floweytf.mechthon.util;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

public record MethodHandlesLookup(MethodHandles.Lookup lookup) {
    public MethodHandle unreflect(Method m) {
        try {
            return lookup.unreflect(m);
        } catch (IllegalAccessException e) {
            throw Util.sneakyThrow(e);
        }
    }

    public MethodHandle unreflectGetter(Field f) {
        try {
            return lookup.unreflectGetter(f);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public MethodHandle unreflectSetter(Field f) {
        try {
            return lookup.unreflectSetter(f);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
}
