package com.floweytf.mechthon.bindings.compiled;

import com.floweytf.mechthon.util.Util;
import java.lang.invoke.MethodHandle;
import java.util.function.IntSupplier;
import org.graalvm.polyglot.proxy.ProxyExecutable;

public final class FunctionMember extends CompiledMember {
    private final MethodHandle adapted; // (Object receiver, Object[] args) -> Object
    private final Class<?>[] argTypes;

    public FunctionMember(MethodHandle adapted, int cacheSlot, Class<?>[] types) {
        super(cacheSlot);
        this.adapted = adapted;
        this.argTypes = types;
    }

    @Override
    public CompiledMember clone(IntSupplier cacheSlotGetter) {
        return new FunctionMember(adapted, cacheSlotGetter.getAsInt(), argTypes);
    }

    @Override
    public Object read(Object receiver) {
        return (ProxyExecutable) arguments -> {
            if (arguments.length != argTypes.length) {
                throw new UnsupportedOperationException(
                    "arity mismatch: expected %s but got %s arguments".formatted(argTypes.length, arguments.length)
                );
            }

            final var obj = new Object[arguments.length];
            // downconvert
            for (int i = 0; i < arguments.length; i++) {
                obj[i] = arguments[i].as(argTypes[i]);
            }

            try {
                return adapted.invokeExact(receiver, obj);
            } catch (Throwable e) {
                throw Util.sneakyThrow(e);
            }
        };
    }
}