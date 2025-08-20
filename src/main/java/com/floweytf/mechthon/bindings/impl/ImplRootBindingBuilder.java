package com.floweytf.mechthon.bindings.impl;

import com.floweytf.mechthon.api.bindings.builders.BindingBuilder;
import com.floweytf.mechthon.api.bindings.builders.RootBindingBuilder;
import java.util.function.Consumer;

public final class ImplRootBindingBuilder<T> extends ImplInstanceBindingBuilder<T> implements RootBindingBuilder<T> {
    private final Class<T> clazz;
    private final String name;
    private final String submodule;
    private final ImplStaticBindingBuilder statics = new ImplStaticBindingBuilder();

    public ImplRootBindingBuilder(Class<T> clazz, String name, String submodule) {
        super(clazz);
        this.clazz = clazz;
        this.name = name;
        this.submodule = submodule;
    }

    @Override
    public BindingBuilder staticBindings() {
        return statics;
    }

    @Override
    public void visitChildren(Consumer<BaseBindingBuilder> visitor) {
        super.visitChildren(visitor);
        visitor.accept(statics);
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

    public ImplStaticBindingBuilder getStatics() {
        return statics;
    }
}
