package com.floweytf.mechthon.bindings.impl;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.VarHandle;
import org.jetbrains.annotations.Nullable;

public final class StaticBindingBuilderImpl extends BindingBuilderImpl {
    public StaticBindingBuilderImpl() {
        super(Object.class);
    }

    @Override
    public void property(String name, VarHandle field, boolean isReadOnly) {
        property(
            name,
            MethodHandles.dropArguments(field.toMethodHandle(VarHandle.AccessMode.GET), 0, Object.class),
            isReadOnly ? null : MethodHandles.dropArguments(field.toMethodHandle(VarHandle.AccessMode.SET), 0,
                Object.class)
        );
    }

    @Override
    public void property(String name, MethodHandle getter, @Nullable MethodHandle setter) {
        super.property(
            name,
            MethodHandles.dropArguments(getter, 0, Object.class),
            setter == null ? null : MethodHandles.dropArguments(setter, 0, Object.class)
        );
    }

    @Override
    public void function(String name, MethodHandle mh) {
        super.function(name, MethodHandles.dropArguments(mh, 0, Object.class));
    }
}
