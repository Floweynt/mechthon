package com.floweytf.mechthon.bindings.impl;

import com.floweytf.mechthon.api.bindings.builders.InstanceBindingBuilder;
import com.google.common.base.Preconditions;
import java.util.ArrayList;
import java.util.List;

public sealed abstract class BaseInstanceBindingBuilderImpl<T>
    extends BindingBuilderImpl
    implements InstanceBindingBuilder<T>
    permits InstanceBindingBuilderImpl, RootBindingBuilderImpl {

    private final List<BaseInstanceBindingBuilderImpl<? super T>> parents = new ArrayList<>();

    public BaseInstanceBindingBuilderImpl(Class<?> clazz) {
        super(clazz);
    }

    @SuppressWarnings("unchecked")
    @Override
    public InstanceBindingBuilder<T> getOrCreateNamespaced(String name) {
        Preconditions.checkNotNull(name, "name must not be null");

        final var inst = members.computeIfAbsent(name, s -> new InstanceBindingBuilderImpl<>(clazz, s));
        Preconditions.checkArgument(
            inst instanceof InstanceBindingBuilder<?>,
            "member '%s' already defined as non-namespaced binding", name
        );
        return (InstanceBindingBuilder<T>) inst;
    }

    @Override
    public void extendFrom(InstanceBindingBuilder<? super T> other) {
        Preconditions.checkNotNull(other, "other must not be null");
        parents.add((BaseInstanceBindingBuilderImpl<? super T>) other);
    }

    @Override
    public List<BaseInstanceBindingBuilderImpl<? super T>> getParents() {
        return parents;
    }
}
