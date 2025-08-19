package com.floweytf.mechthon.test.bindings;

import com.floweytf.mechthon.bindings.impl.RootBindingBuilderImpl;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.VarHandle;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class RootBindingBuilderImplTest {
    @Test
    void basicProps() {
        RootBindingBuilderImpl<String> r = new RootBindingBuilderImpl<>(String.class, "name", "sub");
        assertEquals(String.class, r.getClazz());
        assertEquals("name", r.getName());
        assertEquals("sub", r.getSubmodule());
    }

    @Test
    void staticBindingsExpose() throws Throwable {
        RootBindingBuilderImpl<String> r = new RootBindingBuilderImpl<>(String.class, "n", "s");
        var statics = r.staticBindings();
        VarHandle vh = MethodHandles.lookup().findVarHandle(TestTargets.Sample.class, "x", int.class);
        statics.property("sprop", vh);
        assertTrue(r.getStatics().getMembers().containsKey("sprop"));
    }
}
