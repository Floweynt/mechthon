package com.floweytf.mechthon.bindings.compiler;

import com.floweytf.mechthon.bindings.compiled.CompiledBinding;
import com.floweytf.mechthon.bindings.compiled.RuntimeBindingContext;
import com.floweytf.mechthon.bindings.impl.BaseBindingBuilder;
import com.floweytf.mechthon.bindings.impl.ImplBindings;
import com.floweytf.mechthon.bindings.impl.ImplRootBindingBuilder;
import com.floweytf.mechthon.util.Util;
import it.unimi.dsi.fastutil.objects.Reference2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.ReferenceOpenHashSet;
import java.util.Collections;

public final class BindingCompiler {
    public static RuntimeBindingContext compile(ImplBindings bindings) {
        final var allNodes = new ReferenceOpenHashSet<BaseBindingBuilder>();
        final var comp = new BindingBuilderCompiler(bindings);
        final var bindingMap = new Reference2ObjectOpenHashMap<Class<?>, CompiledBinding>();

        for (final var root : bindings.getClassBindings().values()) {
            Util.bfs(allNodes, root, BaseBindingBuilder::visitChildren);
        }

        CycleDetector.detectCycles(allNodes);

        for (final var e : bindings.getClassBindings().entrySet()) {
            final Class<?> clazz = e.getKey();
            final ImplRootBindingBuilder<?> builder = e.getValue();
            bindingMap.put(clazz, comp.compileBinding(builder));
        }

        return new RuntimeBindingContext(Collections.unmodifiableMap(bindingMap));
    }
}
