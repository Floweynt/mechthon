package com.floweytf.mechthon.bindings.impl;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.VarHandle;
import org.jetbrains.annotations.Nullable;

public sealed interface BindingMember permits BindingBuilderImpl, BindingMember.Function, BindingMember.MHProperty, BindingMember.VHProperty {
    record VHProperty(VarHandle vh, boolean readOnly) implements BindingMember {
    }

    record MHProperty(MethodHandle getter, @Nullable MethodHandle setter) implements BindingMember {
    }

    record Function(MethodHandle mh) implements BindingMember {
    }

    static BindingMember property(VarHandle field) {
        return property(field, false);
    }

    static BindingMember property(VarHandle field, boolean isReadOnly) {
        return new BindingMember.VHProperty(field, isReadOnly);
    }

    static BindingMember property(MethodHandle getter, @Nullable MethodHandle setter) {
        return new BindingMember.MHProperty(getter, setter);
    }

    static BindingMember property(MethodHandle getter) {
        return property(getter, null);
    }

    static BindingMember function(MethodHandle mh) {
        return new BindingMember.Function(mh);
    }
}