package com.floweytf.mechthon.test.bindings;

import com.floweytf.mechthon.bindings.impl.BindingMember;
import com.floweytf.mechthon.bindings.impl.InstanceBindingBuilderImpl;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.invoke.VarHandle;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class BindingBuilderImplTest {
    @Test
    void addVarHandle() throws Throwable {
        final var b = new InstanceBindingBuilderImpl<>(TestTargets.Sample.class, "");
        final var vh = MethodHandles.lookup().findVarHandle(TestTargets.Sample.class, "x", int.class);
        b.property("x", vh);
        final var m = b.getMembers();
        assertTrue(m.containsKey("x"));
        assertInstanceOf(BindingMember.MHProperty.class, m.get("x"));
    }

    @Test
    void addGetterSetter() throws Throwable {
        final var b = new InstanceBindingBuilderImpl<>(TestTargets.Sample.class, "");
        final var getter = MethodHandles.lookup().findStatic(
            TestTargets.class, "getter",
            MethodType.methodType(int.class, TestTargets.Sample.class)
        );
        final var setter = MethodHandles.lookup().findStatic(
            TestTargets.class, "setter",
            MethodType.methodType(void.class, TestTargets.Sample.class, int.class)
        );
        b.property("p", getter, setter);
        assertInstanceOf(BindingMember.MHProperty.class, b.getMembers().get("p"));
    }

    @Test
    void nullFieldThrows() {
        final var b = new InstanceBindingBuilderImpl<>(TestTargets.Sample.class, "");
        assertThrows(NullPointerException.class, () -> b.property("a", (VarHandle) null));
    }

    @Test
    void nullGetterThrows() {
        final var b = new InstanceBindingBuilderImpl<>(TestTargets.Sample.class, "");
        assertThrows(NullPointerException.class, () -> b.property("a", (MethodHandle) null));
    }

    @Test
    void nullFunctionThrows() {
        final var b = new InstanceBindingBuilderImpl<>(TestTargets.Sample.class, "");
        assertThrows(NullPointerException.class, () -> b.function("f", null));
    }

    @Test
    void duplicateMemberThrows() throws Throwable {
        final var b = new InstanceBindingBuilderImpl<>(TestTargets.Sample.class, "");
        final var vh = MethodHandles.lookup().findVarHandle(TestTargets.Sample.class, "x", int.class);
        b.property("dup", vh);
        assertThrows(IllegalArgumentException.class, () -> b.property("dup", vh));
    }

    @Test
    void fromClassTodo() {
        final var b = new InstanceBindingBuilderImpl<>(TestTargets.Sample.class, "");
        assertThrows(RuntimeException.class, () -> b.fromClass(TestTargets.class));
        assertThrows(NullPointerException.class, () -> b.fromClass(null));
    }
}
