package com.floweytf.mechthon.util;

import com.google.common.base.Preconditions;
import com.google.common.base.Supplier;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;

public class ReloadableResource<T extends AutoCloseable> implements AutoCloseable {
    private volatile T instance;
    private volatile CompletableFuture<Void> pendingReload;
    private volatile boolean closed = false;

    public ReloadableResource() {
    }

    public synchronized boolean reload(Supplier<T> reloader, Runnable onComplete) {
        Objects.requireNonNull(onComplete, "onComplete cannot be null");
        Objects.requireNonNull(reloader, "Reloader cannot be null");

        if (closed || (pendingReload != null && !pendingReload.isDone())) {
            return false;
        }

        pendingReload = CompletableFuture
            .supplyAsync(() -> Objects.requireNonNull(reloader.get(), "Reloader returned null"))
            .thenAccept(newInstance -> {
                T oldInstance;

                synchronized (this) {
                    oldInstance = this.instance;
                    this.instance = newInstance;
                }

                try {
                    onComplete.run();
                } finally {
                    if (oldInstance != null) {
                        try {
                            oldInstance.close();
                        } catch (Exception e) {
                            Util.sneakyThrow(e);
                        }
                    }
                }
            });

        return true;
    }

    public void awaitReload() {
        CompletableFuture<Void> future = pendingReload;
        if (future != null) {
            future.join();
        }
    }

    public T get() {
        Preconditions.checkState(instance != null, "Resource not loaded yet");
        return instance;
    }

    @Override
    public void close() {
        CompletableFuture<Void> future;

        synchronized (this) {
            if (closed) {
                return;
            }

            closed = true;
            future = pendingReload;
        }

        try {
            if (future != null) {
                future.join();
            }
        } finally {
            T inst = instance;
            instance = null;
            if (inst != null) {
                try {
                    inst.close();
                } catch (Exception e) {
                    Util.sneakyThrow(e);
                }
            }
        }
    }
}
