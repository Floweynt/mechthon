package com.floweytf.mechthon.bindings.impl;

import com.floweytf.mechthon.api.bindings.builders.BindingBuilder;
import com.floweytf.mechthon.api.bindings.builders.RootBindingBuilder;

public final class RootBindingBuilderImpl<T> extends BaseInstanceBindingBuilderImpl<T> implements RootBindingBuilder<T> {
    private final Class<T> clazz;
    private final String name;
    private final String submodule;
    private final BindingBuilderImpl statics = new StaticBindingBuilderImpl();

    public RootBindingBuilderImpl(Class<T> clazz, String name, String submodule) {
        super(clazz);
        this.clazz = clazz;
        this.name = name;
        this.submodule = submodule;
    }

    @Override
    public BindingBuilder staticBindings() {
        return statics;
    }

    public Class<T> getClazz() {
        return clazz;
    }

    public String getName() {
        return name;
    }

    public String getSubmodule() {
        return submodule;
    }

    public BindingBuilderImpl getStatics() {
        return statics;
    }
}
