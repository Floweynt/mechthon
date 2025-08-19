package com.floweytf.mechthon.bindings.impl;

public final class InstanceBindingBuilderImpl<T> extends BaseInstanceBindingBuilderImpl<T> {
    private final String name;

    public InstanceBindingBuilderImpl(Class<T> clazz, String name) {
        super(clazz);
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
