package com.floweytf.mechthon.bindings.impl;

import com.floweytf.mechthon.api.bindings.builders.BindingBuilder;
import com.floweytf.mechthon.api.bindings.builders.InstanceBindingBuilder;
import com.google.common.base.Preconditions;
import java.lang.invoke.MethodHandle;
import java.util.ArrayList;
import java.util.List;

public sealed class ImplInstanceBindingBuilder<T> extends BaseBindingBuilder
    implements InstanceBindingBuilder<T>, BindingBuilder, BindingMember permits ImplRootBindingBuilder {

    private final List<ImplInstanceBindingBuilder<? super T>> parents = new ArrayList<>();
    protected Class<T> clazz;

    protected ImplInstanceBindingBuilder(Class<T> clazz) {
        super(clazz);
        this.clazz = clazz;
    }

    @Override
    protected MethodHandle fixupMH(MethodHandle handle) {
        return handle;
    }

    @SuppressWarnings("unchecked")
    @Override
    public InstanceBindingBuilder<T> getOrCreateNamespaced(String name) {
        Preconditions.checkNotNull(name, "name is null");
        Preconditions.checkArgument(!name.isBlank(), "name cannot be blank");

        final var entry = members.computeIfAbsent(name, i -> new ImplInstanceBindingBuilder<>(clazz));

        Preconditions.checkArgument(
            entry instanceof InstanceBindingBuilder<?>,
            "member registered at %s is not a namespaced binding", name
        );

        return (InstanceBindingBuilder<T>) entry;
    }

    @Override
    public void extendFrom(InstanceBindingBuilder<? super T> other) {
        parents.add((ImplInstanceBindingBuilder<? super T>) other);
    }

    @Override
    public List<ImplInstanceBindingBuilder<? super T>> getParents() {
        return parents;
    }
}
