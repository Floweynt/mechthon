package com.floweytf.mechthon.test.bindings;

import com.floweytf.mechthon.bindings.impl.BindingMember;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class BindingMemberTest {
    @Test
    void mhProp() throws Throwable {
        final var getter = MethodHandles.lookup().findStatic(
            TestTargets.class, "getter",
            MethodType.methodType(int.class, TestTargets.Sample.class)
        );
        final var setter = MethodHandles.lookup().findStatic(
            TestTargets.class, "setter",
            MethodType.methodType(void.class, TestTargets.Sample.class, int.class)
        );
        final var bm = BindingMember.property(getter, setter);
        assertInstanceOf(BindingMember.MHProperty.class, bm);
        final var record = (BindingMember.MHProperty) bm;
        assertEquals(getter, record.getter());
        assertEquals(setter, record.setter());
    }

    @Test
    void function() throws Throwable {
        final var mh = MethodHandles.lookup().findStatic(
            TestTargets.class, "staticEcho",
            MethodType.methodType(String.class, String.class)
        );
        final var bm = BindingMember.function(mh);
        assertInstanceOf(BindingMember.Function.class, bm);
        final var record = (BindingMember.Function) bm;
        assertEquals(mh, record.mh());
    }
}
