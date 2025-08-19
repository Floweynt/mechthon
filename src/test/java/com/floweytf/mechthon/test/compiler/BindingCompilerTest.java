package com.floweytf.mechthon.test.compiler;

import com.floweytf.mechthon.bindings.compiler.BindingCompiler;
import com.floweytf.mechthon.bindings.impl.BindingsImpl;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import org.junit.jupiter.api.Test;

public class BindingCompilerTest {
    private static final MethodHandles.Lookup LOOKUP = MethodHandles.lookup();

    @Test
    void testValid() throws Throwable {
        final var s = new BindingsImpl();

        final var testTypeBinding = s.defineClassBinding(TestType.class, "test", "TestType");
        testTypeBinding.function("otherThing", LOOKUP.findVirtual(TestType.class, "otherThing",
            MethodType.methodType(int.class, TestType.B.class, long.class, char.class, String.class)));
        testTypeBinding.function("someThing", LOOKUP.findVirtual(TestType.class, "someThing",
            MethodType.methodType(int.class, TestType.A.class)));

        final var aBinding = s.defineClassBinding(TestType.A.class, "test", "A");
        aBinding.function("bar", LOOKUP.findVirtual(TestType.A.class, "bar", MethodType.methodType(void.class,
            TestType.B.class)));
        aBinding.function("foo", LOOKUP.findVirtual(TestType.A.class, "foo", MethodType.methodType(void.class,
            TestType.A.class)));
        aBinding.property("a", LOOKUP.findVarHandle(TestType.A.class, "a", TestType.A.class));
        aBinding.property("b", LOOKUP.findVarHandle(TestType.A.class, "b", TestType.B.class));

        final var bBinding = s.defineClassBinding(TestType.B.class, "test", "B");
        bBinding.function("bar", LOOKUP.findVirtual(TestType.B.class, "bar", MethodType.methodType(void.class,
            TestType.B.class)));
        bBinding.function("foo", LOOKUP.findVirtual(TestType.B.class, "foo", MethodType.methodType(void.class,
            TestType.A.class)));
        bBinding.property("a", LOOKUP.findVarHandle(TestType.B.class, "a", TestType.A.class));
        bBinding.property("b", LOOKUP.findVarHandle(TestType.B.class, "b", TestType.B.class));

        bBinding.property("a_prop",
            LOOKUP.findVirtual(TestType.B.class, "getA", MethodType.methodType(TestType.A.class)),
            LOOKUP.findVirtual(TestType.B.class, "setA", MethodType.methodType(void.class, TestType.A.class))
        );

        bBinding.property("b_prop",
            LOOKUP.findVirtual(TestType.B.class, "getB", MethodType.methodType(TestType.B.class))
        );

        final var res = BindingCompiler.compile(s);
        System.out.println(res);
    }
}
