package com.floweytf.mechthon.bindings.compiler;

import com.floweytf.mechthon.bindings.compiled.CompiledBinding;
import com.floweytf.mechthon.bindings.impl.BindingBuilderImpl;
import com.floweytf.mechthon.bindings.impl.BindingsImpl;
import com.floweytf.mechthon.bindings.impl.RootBindingBuilderImpl;
import com.floweytf.mechthon.bindings.impl.RuntimeBindingContext;
import com.floweytf.mechthon.util.Util;
import it.unimi.dsi.fastutil.objects.Reference2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.ReferenceOpenHashSet;
import java.util.Collections;

public final class BindingCompiler {
    public static RuntimeBindingContext compile(BindingsImpl bindings) {
        final var allNodes = new ReferenceOpenHashSet<BindingBuilderImpl>();
        final var comp = new BindingBuilderCompiler(bindings);
        final var bindingMap = new Reference2ObjectOpenHashMap<Class<?>, CompiledBinding>();

        for (final var root : bindings.getClassBindings().values()) {
            Util.bfs(allNodes, root, (node, reporter) -> {
                node.getParents().forEach(reporter);
                node.getMembers().values().forEach(s -> {
                    if (s instanceof BindingBuilderImpl nested) {
                        reporter.accept(nested);
                    }
                });
            });
        }

        CycleDetector.detectCycles(allNodes);

        for (final var e : bindings.getClassBindings().entrySet()) {
            final Class<?> clazz = e.getKey();
            final RootBindingBuilderImpl<?> builder = e.getValue();
            bindingMap.put(clazz, comp.compileBinding(builder));
        }

        return new RuntimeBindingContext(Collections.unmodifiableMap(bindingMap));
    }
}
