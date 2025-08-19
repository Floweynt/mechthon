package com.floweytf.mechthon.bindings.compiler;

import com.floweytf.mechthon.bindings.impl.BindingBuilderImpl;
import it.unimi.dsi.fastutil.objects.ReferenceOpenHashSet;
import java.util.Set;

class CycleDetector {
    private final Set<BindingBuilderImpl> visiting = new ReferenceOpenHashSet<>();
    private final Set<BindingBuilderImpl> visited = new ReferenceOpenHashSet<>();

    static void detectCycles(final Set<BindingBuilderImpl> nodes) {
        final var state = new CycleDetector();

        for (final var n : nodes) {
            if (!state.visited.contains(n)) {
                if (state.detectCyclesDfs(n)) {
                    throw new IllegalArgumentException("Cycle detected in extension order.");
                }
            }
        }
    }

    private boolean detectCyclesDfs(BindingBuilderImpl node) {
        if (visited.contains(node)) {
            return false;
        }

        if (visiting.contains(node)) {
            return true;
        }

        visiting.add(node);
        try {
            final var parents = node.getParents();
            if (parents != null) {
                for (final var p : parents) {
                    if (detectCyclesDfs(p)) {
                        return true;
                    }
                }
            }

            visited.add(node);
            return false;
        } finally {
            visiting.remove(node);
        }
    }
}
