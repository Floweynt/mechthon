package com.floweytf.mechthon.bindings.compiler;

import com.floweytf.mechthon.bindings.compiled.CompiledBinding;
import com.floweytf.mechthon.bindings.compiled.CompiledMember;
import com.floweytf.mechthon.bindings.compiled.FunctionMember;
import com.floweytf.mechthon.bindings.compiled.PropertyMember;
import com.floweytf.mechthon.bindings.impl.BaseBindingBuilder;
import com.floweytf.mechthon.bindings.impl.BindingMember;
import com.floweytf.mechthon.bindings.impl.ImplBindings;
import com.google.common.base.Preconditions;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.Reference2ObjectOpenHashMap;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodType;
import java.lang.invoke.VarHandle;
import java.util.Arrays;
import java.util.Collections;
import java.util.Map;
import org.jetbrains.annotations.Nullable;

class BindingBuilderCompiler {
    private final Map<BaseBindingBuilder, CompiledBinding> compileCache = new Reference2ObjectOpenHashMap<>();
    private final ImplBindings bindings;

    BindingBuilderCompiler(ImplBindings bindings) {
        this.bindings = bindings;
    }

    CompiledBinding compileBinding(BaseBindingBuilder bindingBuilder) {
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

    private CompiledBinding compileBinding0(BaseBindingBuilder bindingBuilder) {
        final Object2ObjectOpenHashMap<String, CompiledMember> members = new Object2ObjectOpenHashMap<>();

        for (final var builder : bindingBuilder.getParents()) {
            members.putAll(compileBinding(builder).getMembers());
        }

        for (final var en : bindingBuilder.getMembers().entrySet()) {
            final var name = en.getKey();
            final var bm = en.getValue();
            if (bm instanceof BindingMember.Function f) {
                verifyType(f.mh());

                final var original = f.mh();
                final int arity = original.type().parameterCount() - 1;
                final var adapted =
                    original.asSpreader(Object[].class, arity).asType(MethodType.methodType(Object.class,
                        Object.class, Object[].class));
                members.put(name, new FunctionMember(adapted, Arrays.copyOfRange(original.type().parameterArray(), 1,
                    arity + 1), arity));
            } else if (bm instanceof BindingMember.Property mhp) {
                verifyType(mhp.getter());
                if (mhp.setter() != null) {
                    verifyType(mhp.setter());
                }

                final var getter = mhp.getter();
                final var setter = mhp.setter();
                members.put(name, createPropertyMember(getter, setter));
            } else if (bm instanceof BaseBindingBuilder childBuilder) {
                members.put(name, compileBinding(childBuilder));
            }
        }

        return new CompiledBinding(Collections.unmodifiableMap(members));
    }
}
