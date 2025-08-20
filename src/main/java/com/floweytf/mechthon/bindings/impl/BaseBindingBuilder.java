package com.floweytf.mechthon.bindings.impl;

import com.floweytf.mechthon.api.bindings.builders.BindingBuilder;
import com.floweytf.mechthon.util.MethodHandlesLookup;
import com.google.common.base.Preconditions;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import org.apache.commons.lang3.NotImplementedException;
import org.jetbrains.annotations.Nullable;

public sealed abstract class BaseBindingBuilder implements BindingBuilder permits ImplInstanceBindingBuilder,
    ImplStaticBindingBuilder {
    private static final MethodHandlesLookup LOOKUP = new MethodHandlesLookup(MethodHandles.lookup());

    protected final Class<?> clazz;
    protected final Map<String, BindingMember> members = new Object2ObjectOpenHashMap<>();

    protected BaseBindingBuilder(Class<?> clazz) {
        this.clazz = clazz;
    }

    private void member(String name, BindingMember member) {
        Preconditions.checkNotNull(name, "name cannot be null");
        Preconditions.checkArgument(!name.isBlank(), "name cannot be empty or blank");
        Preconditions.checkArgument(!members.containsKey(name), "member '%s' already exists", name);
        members.put(name, member);
    }

    private void property(String name, Type type, MethodHandle getter, @Nullable MethodHandle setter) {
        Preconditions.checkNotNull(type, "generic type must not be null");
        getter = fixupMH(getter);
        // handle receiver type checks here
        Preconditions.checkArgument(getter.type().parameterArray()[0].isAssignableFrom(clazz));

        if (setter != null) {
            setter = fixupMH(setter);
            Preconditions.checkArgument(setter.type().parameterArray()[0].isAssignableFrom(clazz));
        }

        member(name, new BindingMember.Property(type, getter, setter));
    }

    @Override
    public void property(String name, Field field, boolean isReadOnly) {
        Preconditions.checkNotNull(field, "field must not be null");

        property(
            name,
            field.getGenericType(),
            LOOKUP.unreflectGetter(field),
            isReadOnly ? null : LOOKUP.unreflectSetter(field)
        );
    }

    @Override
    public void property(String name, Method getter, @Nullable Method setter) {
        Preconditions.checkNotNull(getter, "getter must not be null");
        Preconditions.checkArgument(getter.getParameterCount() == 0, "getter must take no parameters");
        final var propertyType = getter.getGenericReturnType();
        Preconditions.checkArgument(propertyType != void.class, "getter must not return void");

        if (setter != null) {
            // validate types
            Preconditions.checkArgument(setter.getParameterCount() == 1, "setter must take one argument");
            final var setterPropertyType = setter.getGenericParameterTypes()[0];

            Preconditions.checkArgument(
                setterPropertyType.equals(propertyType),
                "setter type %s does not match getter type %s", setterPropertyType, propertyType
            );
        }

        property(
            name,
            propertyType,
            LOOKUP.unreflect(getter),
            setter == null ? null : LOOKUP.unreflect(setter)
        );
    }

    @Override
    public void function(String name, Method method) {
        Preconditions.checkNotNull(method, "method must not be null");

        final var mh = fixupMH(LOOKUP.unreflect(method));
        Preconditions.checkArgument(mh.type().parameterArray()[0].isAssignableFrom(clazz)); // check receiver

        member(name, new BindingMember.Function(method.getGenericReturnType(), method.getGenericParameterTypes(), mh));
    }

    @Override
    public void fromClass(Class<?> extensions) {
        throw new NotImplementedException("TODO");
    }

    /**
     * Fixes up the MH so that the receiver type is correct (only useful for statics)
     *
     * @param handle the handle
     * @return the fixed handle
     */
    protected abstract MethodHandle fixupMH(MethodHandle handle);

    public abstract List<? extends BaseBindingBuilder> getParents();

    public void visitChildren(Consumer<BaseBindingBuilder> visitor) {
        getParents().forEach(visitor);
        members.forEach((s, member) -> {
            if (member instanceof BaseBindingBuilder baseBindingBuilder) {
                visitor.accept(baseBindingBuilder);
            }
        });
    }

    public final Map<String, BindingMember> getMembers() {
        return members;
    }
}
