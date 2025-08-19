package com.floweytf.mechthon.test.bindings;

import com.floweytf.mechthon.bindings.impl.InstanceBindingBuilderImpl;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class BaseInstanceBindingBuilderImplTest {
    @Test
    void namespacedNull() {
        final var inst = new InstanceBindingBuilderImpl<>(String.class, "root");
        assertThrows(NullPointerException.class, () -> inst.getOrCreateNamespaced(null));
    }

    @Test
    void namespacedCreate() {
        final var inst = new InstanceBindingBuilderImpl<>(String.class, "root");
        var child = inst.getOrCreateNamespaced("child");
        assertInstanceOf(InstanceBindingBuilderImpl.class, child);
        assertEquals("child", ((InstanceBindingBuilderImpl<?>) child).getName());
    }

    @Test
    void extendFromAddsParent() {
        final var inst = new InstanceBindingBuilderImpl<>(String.class, "root");
        final var other = new InstanceBindingBuilderImpl<>(Object.class, "other");
        inst.extendFrom(other);
        assertEquals(1, inst.getParents().size());
        assertSame(other, inst.getParents().get(0));
    }
}
