package com.floweytf.mechthon.bindings.impl;

import java.lang.invoke.MethodHandle;
import java.lang.reflect.Type;
import org.jetbrains.annotations.Nullable;

public sealed interface BindingMember permits ImplInstanceBindingBuilder, BindingBuilderImpl, BindingMember.Function, BindingMember.Property {
    record Property(Type type, MethodHandle getter, @Nullable MethodHandle setter) implements BindingMember {
    }

    record Function(Type returnType, Type[] argTypes, MethodHandle mh) implements BindingMember {
    }
}