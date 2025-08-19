package com.floweytf.mechthon.bindings.impl;

import com.floweytf.mechthon.api.bindings.builders.BindingBuilder;
import com.google.common.base.Preconditions;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.VarHandle;
import java.util.List;
import java.util.Map;
import org.jetbrains.annotations.Nullable;

public abstract sealed class BindingBuilderImpl
    implements BindingBuilder, BindingMember
    permits BaseInstanceBindingBuilderImpl, StaticBindingBuilderImpl {
    protected final Class<?> clazz;
    protected final Map<String, BindingMember> members = new Object2ObjectOpenHashMap<>();

    protected BindingBuilderImpl(Class<?> clazz) {
        this.clazz = clazz;
    }

    protected void member(String name, BindingMember member) {
        Preconditions.checkNotNull(name, "name cannot be null");
        Preconditions.checkArgument(!members.containsKey(name), "member '%s' already exists", name);
        members.put(name, member);
    }

    @Override
    public void property(String name, VarHandle field) {
        property(name, field, false);
    }

    @Override
    public void property(String name, VarHandle field, boolean isReadOnly) {
        Preconditions.checkNotNull(field, "field cannot be null");
        final var rt = field.accessModeType(VarHandle.AccessMode.GET).parameterArray()[0];
        Preconditions.checkArgument(
            rt.isAssignableFrom(clazz),
            "Varhandle receiver type is invalid: %s is not assignable from %s", rt, clazz
        );
        member(name, BindingMember.property(field, isReadOnly));
    }

    @Override
    public void property(String name, MethodHandle getter, @Nullable MethodHandle setter) {
        Preconditions.checkNotNull(getter, "getter cannot be null");
        Preconditions.checkArgument(getter.type().parameterCount() == 1, "getter must only take the receiver");
        Preconditions.checkArgument(getter.type().returnType() != void.class, "getter cannot return void");

        final var getRecv = getter.type().parameterArray()[0];
        Preconditions.checkArgument(
            getRecv.isAssignableFrom(clazz),
            "Getter receiver type is invalid: %s is not assignable from %s", getRecv, clazz
        );

        if (setter != null) {
            Preconditions.checkArgument(
                setter.type().parameterCount() == 2,
                "setter must only take the receiver and value"
            );

            final var setRecv = setter.type().parameterArray()[0];
            Preconditions.checkArgument(
                getRecv.isAssignableFrom(clazz),
                "Setter receiver type is invalid: %s is not assignable from %s", setRecv, clazz
            );

            final var setterType = setter.type().parameterArray()[1];
            final var getterType = getter.type().returnType();

            Preconditions.checkArgument(
                setterType == getterType,
                "setter type '%s' and getter type '%s' differ", setterType, getterType
            );
        }

        member(name, BindingMember.property(getter, setter));
    }

    @Override
    public void property(String name, MethodHandle getter) {
        property(name, getter, null);
    }

    @Override
    public void function(String name, MethodHandle mh) {
        Preconditions.checkNotNull(mh, "method handle cannot be null");
        member(name, BindingMember.function(mh));
    }

    @Override
    public void fromClass(Class<?> extensions) {
        Preconditions.checkNotNull(extensions, "extensions class cannot be null");
        throw new RuntimeException("TODO");
    }

    public final Map<String, BindingMember> getMembers() {
        return members;
    }

    public List<? extends BindingBuilderImpl> getParents() {
        return List.of();
    }
}
