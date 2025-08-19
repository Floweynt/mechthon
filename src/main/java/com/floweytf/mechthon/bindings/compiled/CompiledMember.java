package com.floweytf.mechthon.bindings.compiled;

import java.util.function.IntSupplier;
import org.graalvm.polyglot.Value;

public sealed abstract class CompiledMember permits CompiledBinding, FunctionMember, PropertyMember {
    private final int cacheSlot;

    protected CompiledMember(int cacheSlot) {
        this.cacheSlot = cacheSlot;
    }

    public CompiledMember clone(IntSupplier cacheSlotGetter) {
        return this;
    }

    public void write(Object receiver, Value value) {
        throw new UnsupportedOperationException("cannot write to this member");
    }

    public abstract Object read(Object receiver);

    public final int getCacheSlot() {
        return cacheSlot;
    }
}