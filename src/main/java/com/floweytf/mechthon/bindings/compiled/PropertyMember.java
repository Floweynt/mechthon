package com.floweytf.mechthon.bindings.compiled;

import com.floweytf.mechthon.util.Util;
import java.lang.invoke.MethodHandle;
import org.jetbrains.annotations.Nullable;

public final class PropertyMember extends CompiledMember {
    private final MethodHandle getter;
    private final @Nullable MethodHandle setter;

    public PropertyMember(MethodHandle getter, @Nullable MethodHandle setter) {
        super(setter != null, false, false, -1);
        this.getter = getter;
        this.setter = setter;
    }

    @Override
    public Object read(Object receiver) {
        try {
            return getter.invokeExact(receiver);
        } catch (Throwable e) {
            throw Util.sneakyThrow(e);
        }
    }

    @Override
    public void write(Object receiver, Object value) {
        assert setter != null;
        try {
            setter.invokeExact(receiver, value);
        } catch (Throwable e) {
            Util.sneakyThrow(e);
        }
    }
}