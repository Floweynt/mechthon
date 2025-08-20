package com.floweytf.mechthon.bindings.compiled;

import com.oracle.truffle.api.CompilerDirectives;

public sealed abstract class CompiledMember permits CompiledBinding, FunctionMember, PropertyMember {
    public static final int FLAG_WRITE = 1;
    public static final int FLAG_EXECUTE = 2;
    public static final int FLAG_CACHEABLE = 4;

    private final int flags;
    private final int arity;

    protected CompiledMember(boolean write, boolean execute, boolean canCache, int arity) {
        this.flags = (write ? FLAG_WRITE : 0) | (execute ? FLAG_EXECUTE : 0) | (canCache ? FLAG_CACHEABLE : 0);
        this.arity = arity;
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

    public final boolean isCacheable() {
        return (flags & FLAG_CACHEABLE) != 0;
    }

    public final int getArity() {
        return arity;
    }
}