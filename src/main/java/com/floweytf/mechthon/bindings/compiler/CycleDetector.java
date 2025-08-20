package com.floweytf.mechthon.bindings.compiler;

import com.floweytf.mechthon.bindings.impl.BaseBindingBuilder;
import it.unimi.dsi.fastutil.objects.ReferenceOpenHashSet;
import java.util.Set;

class CycleDetector {
    private final Set<BaseBindingBuilder> visiting = new ReferenceOpenHashSet<>();
    private final Set<BaseBindingBuilder> visited = new ReferenceOpenHashSet<>();

    static void detectCycles(final Set<BaseBindingBuilder> nodes) {
        final var state = new CycleDetector();

        for (final var n : nodes) {
            if (!state.visited.contains(n)) {
                if (state.detectCyclesDfs(n)) {
                    throw new IllegalArgumentException("Cycle detected in extension order.");
                }
            }
        }
    }

    private boolean detectCyclesDfs(BaseBindingBuilder node) {
        if (visited.contains(node)) {
            return false;
        }

        if (visiting.contains(node)) {
            return true;
        }

        visiting.add(node);
        try {
            final var parents = node.getParents();

            for (final var p : parents) {
                if (detectCyclesDfs(p)) {
                    return true;
                }
            }

            visited.add(node);
            return false;
        } finally {
            visiting.remove(node);
        }
    }
}
