package com.floweytf.mechthon.bindings.compiled;

import com.floweytf.mechthon.bindings.truffle.objects.InstanceFunctionTO;
import com.floweytf.mechthon.util.Util;
import java.lang.invoke.MethodHandle;

public final class FunctionMember extends CompiledMember {
    private final MethodHandle adapted; // (Object receiver, Object[] args) -> Object
    private final Class<?>[] invokerArgs;

    public FunctionMember(MethodHandle adapted, Class<?>[] invokerArgs, int arity) {
        super(false, true, true, arity);
        this.adapted = adapted;
        this.invokerArgs = invokerArgs;
    }

    public Class<?>[] getInvokerArgs() {
        return invokerArgs;
    }

    @Override
    public Object invoke(Object receiver, Object[] args) {
        try {
            return adapted.invokeExact(receiver, args);
        } catch (Throwable e) {
            throw Util.sneakyThrow(e);
        }
    }

    @Override
    public Object read(Object receiver) {
        return new InstanceFunctionTO(this, receiver);
    }
}