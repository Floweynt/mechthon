package com.floweytf.mechthon.bindings.impl;

import com.floweytf.mechthon.api.bindings.Bindings;
import com.floweytf.mechthon.api.bindings.builders.RootBindingBuilder;
import com.google.common.base.Preconditions;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import java.util.Map;

public class ImplBindings implements Bindings {
    private final Map<Class<?>, ImplRootBindingBuilder<?>> classBindings = new Object2ObjectOpenHashMap<>();

    @Override
    public <T> RootBindingBuilder<T> defineClassBinding(Class<T> clazz, String submodule, String name) {
        Preconditions.checkNotNull(clazz, "clazz must not be null");
        Preconditions.checkArgument(clazz.getTypeParameters().length == 0, "clazz must not be generic");
        Preconditions.checkNotNull(submodule, "submodule must not be null");
        Preconditions.checkNotNull(name, "name must not be null");

        Preconditions.checkArgument(
            !classBindings.containsKey(clazz),
            "bindings for class %s already defined, use getClassBindings!", clazz.getName()
        );

        final var binding = new ImplRootBindingBuilder<>(clazz, submodule, name);
        classBindings.put(clazz, binding);
        return binding;
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> RootBindingBuilder<T> getClassBindings(Class<T> clazz) {
        Preconditions.checkNotNull(clazz, "clazz must not be null");

        final var res = classBindings.get(clazz);
        Preconditions.checkArgument(
            res != null,
            "bindings for class %s not defined, use defineClassBinding!", clazz.getName()
        );

        return (RootBindingBuilder<T>) res;
    }

    public Map<Class<?>, ImplRootBindingBuilder<?>> getClassBindings() {
        return classBindings;
    }
}
