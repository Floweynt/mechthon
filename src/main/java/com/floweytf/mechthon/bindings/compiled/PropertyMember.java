package com.floweytf.mechthon.bindings.compiled;

import com.floweytf.mechthon.util.Util;
import java.lang.invoke.MethodHandle;
import org.graalvm.polyglot.Value;
import org.jetbrains.annotations.Nullable;

public final class PropertyMember extends CompiledMember {
    private final MethodHandle getter;
    private final @Nullable MethodHandle setter;
    private final @Nullable Class<?> type;

    public PropertyMember(MethodHandle getter, @Nullable MethodHandle setter) {
        super(-1);
        this.getter = getter;
        this.setter = setter;
        if (setter != null) {
            type = setter.type().lastParameterType();
        } else {
            type = null;
        }
    }

    @Override
    public Object read(Object receiver) {
        try {
            return getter.invokeExact(receiver);
        } catch (Throwable e) {
            throw Util.sneakyThrow(e);
        }
    }

    public void write(Object receiver, Value value) {
        if (setter == null) {
            super.write(receiver, value);
            return;
        }

        assert type != null;

        try {
            setter.invokeExact(receiver, value.as(type));
        } catch (Throwable e) {
            Util.sneakyThrow(e);
        }
    }
}