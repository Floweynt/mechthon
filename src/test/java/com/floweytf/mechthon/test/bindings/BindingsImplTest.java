package com.floweytf.mechthon.test.bindings;

import com.floweytf.mechthon.bindings.impl.BindingsImpl;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class BindingsImplTest {
    @Test
    void defineAndGet() {
        BindingsImpl bs = new BindingsImpl();
        var rb = bs.defineClassBinding(String.class, "mod", "name");
        assertNotNull(rb);
        assertSame(rb, bs.getClassBindings(String.class));
    }

    @Test
    void defineNulls() {
        BindingsImpl bs = new BindingsImpl();
        assertThrows(NullPointerException.class, () -> bs.defineClassBinding(null, "m", "n"));
        assertThrows(NullPointerException.class, () -> bs.defineClassBinding(String.class, null, "n"));
        assertThrows(NullPointerException.class, () -> bs.defineClassBinding(String.class, "m", null));
    }

    @Test
    void defineDuplicate() {
        BindingsImpl bs = new BindingsImpl();
        bs.defineClassBinding(Integer.class, "m", "n");
        assertThrows(IllegalArgumentException.class, () -> bs.defineClassBinding(Integer.class, "m", "n2"));
    }

    @Test
    void getMissing() {
        BindingsImpl bs = new BindingsImpl();
        assertThrows(IllegalArgumentException.class, () -> bs.getClassBindings(Long.class));
    }

    @Test
    void getNull() {
        BindingsImpl bs = new BindingsImpl();
        assertThrows(NullPointerException.class, () -> bs.getClassBindings(null));
    }
}
