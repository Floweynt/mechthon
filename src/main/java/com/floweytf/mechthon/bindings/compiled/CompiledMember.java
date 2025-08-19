package com.floweytf.mechthon.bindings.compiled;

import com.oracle.truffle.api.CompilerDirectives;
import java.util.function.IntSupplier;

public sealed abstract class CompiledMember permits CompiledBinding, FunctionMember, PropertyMember {
    public static final int FLAG_WRITE = 1;
    public static final int FLAG_EXECUTE = 2;

    private final int flags;
    private final int arity;
    private final int cacheSlot;

    protected CompiledMember(boolean write, boolean execute, int arity, int cacheSlot) {
        this.flags = (write ? FLAG_WRITE : 0) | (execute ? FLAG_EXECUTE : 0);
        this.arity = arity;
        this.cacheSlot = cacheSlot;
    }

    public CompiledMember clone(IntSupplier cacheSlotGetter) {
        return this;
    }

    public Object invoke(Object receiver, Object[] args) {
        throw CompilerDirectives.shouldNotReachHere("write() unimplemented");
    }

    public void write(Object receiver, Object value) {
        throw CompilerDirectives.shouldNotReachHere("write() unimplemented");
    }

    public abstract Object read(Object receiver);

    public final boolean isExecutable() {
        return (flags & FLAG_EXECUTE) != 0;
    }

    public final boolean isWriteable() {
        return (flags & FLAG_WRITE) != 0;
    }

    public final int getCacheSlot() {
        return cacheSlot;
    }

    public final int getArity() {
        return arity;
    }
}