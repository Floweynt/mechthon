package com.floweytf.mechthon.util;

import java.util.ArrayDeque;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.LongConsumer;
import java.util.function.Supplier;

public class Util {
    @SuppressWarnings("unchecked")
    public static <E extends Throwable> RuntimeException sneakyThrow(Throwable e) throws E {
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

    public static <T> Set<T> bfs(Set<T> visited, T ent, BiConsumer<T, Consumer<T>> iter) {
        visited.add(ent);
        final var queue = new ArrayDeque<T>();
        queue.push(ent);

        while (!queue.isEmpty()) {
            final var first = queue.poll();
            iter.accept(first, t -> {
                if (visited.add(t)) {
                    queue.add(t);
                }
            });
        }

        return visited;
    }
}
