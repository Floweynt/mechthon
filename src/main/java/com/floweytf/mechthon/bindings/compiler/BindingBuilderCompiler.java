package com.floweytf.mechthon.bindings.compiler;

import com.floweytf.mechthon.bindings.compiled.CompiledBinding;
import com.floweytf.mechthon.bindings.compiled.CompiledMember;
import com.floweytf.mechthon.bindings.compiled.FunctionMember;
import com.floweytf.mechthon.bindings.compiled.PropertyMember;
import com.floweytf.mechthon.bindings.impl.BindingBuilderImpl;
import com.floweytf.mechthon.bindings.impl.BindingMember;
import com.floweytf.mechthon.bindings.impl.BindingsImpl;
import com.google.common.base.Preconditions;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.Reference2ObjectOpenHashMap;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodType;
import java.lang.invoke.VarHandle;
import java.util.Arrays;
import java.util.Collections;
import java.util.Map;
import java.util.function.IntSupplier;
import org.jetbrains.annotations.Nullable;

class BindingBuilderCompiler {
    private final Map<BindingBuilderImpl, CompiledBinding> compileCache = new Reference2ObjectOpenHashMap<>();
    private final BindingsImpl bindings;

    BindingBuilderCompiler(BindingsImpl bindings) {
        this.bindings = bindings;
    }

    CompiledBinding compileBinding(BindingBuilderImpl bindingBuilder) {
        return compileCache.computeIfAbsent(bindingBuilder, this::compileBinding0);
    }

    private void verifyType(Class<?> clazz) {
        Preconditions.checkArgument(
            clazz.isPrimitive() || clazz == String.class || bindings.getClassBindings().containsKey(clazz),
            "Type %s is not bound and thus may not be exposed as a method!",
            clazz
        );
    }

    private void verifyType(MethodHandle mh) {
        for (final var cl : mh.type().parameterArray()) {
            verifyType(cl);
        }
        verifyType(mh.type().returnType());
    }

    private void verifyType(VarHandle vh) {
        verifyType(vh.varType());
    }

    private PropertyMember createPropertyMember(MethodHandle get, @Nullable MethodHandle set) {
        get = get.asType(MethodType.methodType(Object.class, Object.class));

        if (set != null) {
            set = set.asType(MethodType.methodType(void.class, Object.class, Object.class));
        }

        return new PropertyMember(get, set);
    }

    private CompiledBinding compileBinding0(BindingBuilderImpl bindingBuilder) {
        final int[] slotCounter = new int[1];
        final IntSupplier nextSlot = () -> slotCounter[0]++;

        final Object2ObjectOpenHashMap<String, CompiledMember> members = new Object2ObjectOpenHashMap<>();

        for (final var builder : bindingBuilder.getParents()) {
            final var parentCompiled = compileBinding(builder);
            for (final var entry : parentCompiled.getMembers().entrySet()) {
                final var k = entry.getKey();
                final var v = entry.getValue();
                members.put(k, v.clone(nextSlot));
            }
        }

        for (final var en : bindingBuilder.getMembers().entrySet()) {
            final var name = en.getKey();
            final var bm = en.getValue();
            if (bm instanceof BindingMember.Function f) {
                verifyType(f.mh());

                final var original = f.mh();
                final int arity = original.type().parameterCount() - 1;
                final var adapted = original.asSpreader(Object[].class, arity).asType(MethodType.methodType(Object.class, Object.class, Object[].class));
                final int assignedLocalSlot = nextSlot.getAsInt();
                members.put(name, new FunctionMember(adapted, Arrays.copyOfRange(original.type().parameterArray(), 1, arity + 1), arity, assignedLocalSlot));
            } else if (bm instanceof BindingMember.MHProperty mhp) {
                verifyType(mhp.getter());
                if (mhp.setter() != null) {
                    verifyType(mhp.setter());
                }

                final var getter = mhp.getter();
                final var setter = mhp.setter();
                members.put(name, createPropertyMember(getter, setter));
            } else if (bm instanceof BindingMember.VHProperty vhp) {
                verifyType(vhp.vh());

                final var vh = vhp.vh();
                final boolean readOnly = vhp.readOnly();
                final var getter = vh.toMethodHandle(VarHandle.AccessMode.GET);
                final var setter = readOnly ? null : vh.toMethodHandle(VarHandle.AccessMode.SET);
                members.put(name, createPropertyMember(getter, setter));
            } else if (bm instanceof BindingBuilderImpl childBuilder) {
                members.put(name, compileBinding(childBuilder).clone(nextSlot));
            }
        }

        return new CompiledBinding(Collections.unmodifiableMap(members), -1, slotCounter[0]);
    }
}
