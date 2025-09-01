package com.floweytf.mechthon.util;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.Comparator;
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

    public static void rmRecursive(Path path) throws IOException {
        try (final var paths = Files.walk(path)) {
            paths.sorted(Comparator.reverseOrder()).forEach(o -> {
                try {
                    Files.delete(o);
                } catch (IOException e) {
                    throw Util.sneakyThrow(e);
                }
            });
        }
    }

    public static void copyFolder(Path source, Path target) throws IOException {
        try (final var stream = Files.walk(source)) {
            stream.forEach(path -> {
                try {
                    final var dst = target.resolve(source.relativize(path).toString());
                    if (Files.isDirectory(path)) {
                        if (!Files.exists(dst)) {
                            Files.createDirectories(dst);
                        }
                    } else {
                        Files.copy(path, dst, StandardCopyOption.REPLACE_EXISTING);
                    }
                } catch (IOException e) {
                    throw Util.sneakyThrow(e);
                }
            });
        }
    }
}
