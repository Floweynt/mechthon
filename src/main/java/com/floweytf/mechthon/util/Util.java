package com.floweytf.mechthon.util;

import java.util.function.LongConsumer;
import java.util.function.Supplier;

public class Util {
    @SuppressWarnings("unchecked")
    public static <E extends Throwable, T> T sneakyThrow(Throwable e) throws E {
        throw (E) e;
    }

    public static long profile(Runnable task) {
        final var start = System.currentTimeMillis();
        task.run();
        final var end = System.currentTimeMillis();
        return end - start;
    }

    public static <T> T profile(Supplier<T> supplier, LongConsumer timeConsumer) {
        final var start = System.currentTimeMillis();
        final var res = supplier.get();
        final var end = System.currentTimeMillis();
        timeConsumer.accept(end - start);
        return res;
    }
}
