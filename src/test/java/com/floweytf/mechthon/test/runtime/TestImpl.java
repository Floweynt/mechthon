package com.floweytf.mechthon.test.runtime;

import com.floweytf.mechthon.bindings.compiler.BindingCompiler;
import com.floweytf.mechthon.bindings.impl.ImplBindings;
import com.floweytf.mechthon.bindings.truffle.objects.BindingInstanceTO;
import it.unimi.dsi.fastutil.longs.LongArrayList;
import java.lang.invoke.MethodHandles;
import java.util.List;
import org.graalvm.polyglot.Context;
import org.graalvm.polyglot.HostAccess;
import org.graalvm.polyglot.Value;
import org.graalvm.polyglot.proxy.ProxyObject;

public class TestImpl {
    public static class A {
        public int b;
        public long c;
        public double d;
        public int f;
        public int e0;
        public int e1;
        public int e2;
        public int e3;
        public int e4;
    }

    private static String formatStats(LongArrayList longs) {
        final var avg = longs.longStream().average().orElse(0);
        final var sqDiff = longs.longStream().mapToDouble(x -> (x - avg) * (x - avg)).sum();

        final var stddev = Math.sqrt(sqDiff / (longs.size() - 1));
        final var stderr = stddev / Math.sqrt(longs.size());

        final var avgS = avg / 1000000;
        final var stderrS = stderr / 1000000;

        return String.format("%.6g +/-%.6g", avgS, stderrS);
    }

    public static void benchmark() throws NoSuchFieldException, IllegalAccessException {
        final var bindings = new ImplBindings();
        final var aBindings = bindings.defineClassBinding(A.class, "meow", "A");
        aBindings.property("b", MethodHandles.lookup().findVarHandle(A.class, "b", int.class));
        aBindings.property("c", MethodHandles.lookup().findVarHandle(A.class, "c", long.class));
        aBindings.property("d", MethodHandles.lookup().findVarHandle(A.class, "d", double.class));
        aBindings.property("f", MethodHandles.lookup().findVarHandle(A.class, "f", int.class));
        aBindings.property("e0", MethodHandles.lookup().findVarHandle(A.class, "e0", int.class));
        aBindings.property("e1", MethodHandles.lookup().findVarHandle(A.class, "e1", int.class));
        aBindings.property("e2", MethodHandles.lookup().findVarHandle(A.class, "e2", int.class));
        aBindings.property("e3", MethodHandles.lookup().findVarHandle(A.class, "e3", int.class));
        aBindings.property("e4", MethodHandles.lookup().findVarHandle(A.class, "e4", int.class));
        final var built = BindingCompiler.compile(bindings);

        final var ab = built.knownBindingTypes().get(A.class);

        try (final var ctx = Context.newBuilder("python")
            .allowAllAccess(false)
            .allowHostAccess(HostAccess.ALL)
            .build()) {

            final var tgt = new A();

            tgt.b = 32;

            ctx.getBindings("python").putMember("meow_custom", BindingInstanceTO.of(tgt, ab));
            ctx.getBindings("python").putMember("meow_hostinterop", tgt);
            ctx.getBindings("python").putMember("meow_proxy", new ProxyObject() {
                private final A a = new A();

                @Override
                public Object getMember(String key) {
                    return switch (key) {
                        case "b" -> a.b;
                        case "c" -> a.c;
                        case "d" -> a.d;
                        case "f" -> a.f;
                        case "e0" -> a.e0;
                        case "e1" -> a.e1;
                        case "e2" -> a.e2;
                        case "e3" -> a.e3;
                        case "e4" -> a.e4;
                        default -> throw new IllegalStateException("Unexpected value: " + key);
                    };
                }

                @Override
                public Object getMemberKeys() {
                    return List.of("b", "c", "d", "f", "e0", "e1", "e2", "e3", "e4");
                }

                @Override
                public boolean hasMember(String key) {
                    return switch (key) {
                        case "b", "f", "c", "d", "e0", "e1", "e3", "e2", "e4" -> true;
                        default -> false;
                    };
                }

                @Override
                public void putMember(String key, Value value) {
                    switch (key) {
                    case "b" -> a.b = value.asInt();
                    case "c" -> a.c = value.asLong();
                    case "d" -> a.d = value.asDouble();
                    case "f" -> a.f = value.asInt();
                    case "e0" -> a.e0 = value.asInt();
                    case "e1" -> a.e1 = value.asInt();
                    case "e2" -> a.e2 = value.asInt();
                    case "e3" -> a.e3 = value.asInt();
                    case "e4" -> a.e4 = value.asInt();
                    default -> throw new IllegalStateException("Unexpected value: " + key);
                    }
                }

                @Override
                public String toString() {
                    return a.toString();
                }
            });

            LongArrayList customTime = new LongArrayList();
            LongArrayList hostInteropTime = new LongArrayList();
            LongArrayList proxyTime = new LongArrayList();

            ctx.eval("python", "print(meow_proxy)");

            for (int i = 0; i < 100; i++) {
                final var a = System.nanoTime();
                ctx.eval("python", """
                    for i in range(0, 10000):
                        meow_custom.b = meow_custom.b + 1
                        meow_custom.c = meow_custom.c + 1
                        meow_custom.d = meow_custom.d + 1
                        meow_custom.f = meow_custom.f + 1
                        meow_custom.e0 = meow_custom.e0 + 1
                        meow_custom.e1 = meow_custom.e1 + 1
                        meow_custom.e2 = meow_custom.e2 + 1
                        meow_custom.e3 = meow_custom.e3 + 1
                        meow_custom.e4 = meow_custom.e4 + 1
                    """);
                final var b = System.nanoTime();
                ctx.eval("python", """
                    for i in range(0, 10000):
                        meow_hostinterop.b = meow_hostinterop.b + 1
                        meow_hostinterop.c = meow_hostinterop.c + 1
                        meow_hostinterop.d = meow_hostinterop.d + 1
                        meow_hostinterop.f = meow_hostinterop.f + 1
                        meow_hostinterop.e0 = meow_hostinterop.e0 + 1
                        meow_hostinterop.e1 = meow_hostinterop.e1 + 1
                        meow_hostinterop.e2 = meow_hostinterop.e2 + 1
                        meow_hostinterop.e3 = meow_hostinterop.e3 + 1
                        meow_hostinterop.e4 = meow_hostinterop.e4 + 1
                    """);
                final var c = System.nanoTime();
                ctx.eval("python", """
                    for i in range(0, 10000):
                        meow_proxy.b = meow_proxy.b + 1
                        meow_proxy.c = meow_proxy.c + 1
                        meow_proxy.d = meow_proxy.d + 1
                        meow_proxy.f = meow_proxy.f + 1
                        meow_proxy.e0 = meow_proxy.e0 + 1
                        meow_proxy.e1 = meow_proxy.e1 + 1
                        meow_proxy.e2 = meow_proxy.e2 + 1
                        meow_proxy.e3 = meow_proxy.e3 + 1
                        meow_proxy.e4 = meow_proxy.e4 + 1
                    """);
                final var d = System.nanoTime();
                customTime.add(b - a);
                hostInteropTime.add(c - b);
                proxyTime.add(d - c);
            }

            System.out.printf("%s %s %s\n", formatStats(customTime), formatStats(hostInteropTime), formatStats(proxyTime));
        }
    }
}
