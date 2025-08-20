package com.floweytf.mechthon.bindings.impl;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.util.List;

public final class ImplStaticBindingBuilder extends BaseBindingBuilder {
    private static final class Token {
    }

    public ImplStaticBindingBuilder() {
        super(Token.class);
    }

    @Override
    protected MethodHandle fixupMH(MethodHandle handle) {
        return MethodHandles.insertArguments(handle, 0, Token.class);
    }

    @Override
    public List<? extends BaseBindingBuilder> getParents() {
        return List.of();
    }
}
