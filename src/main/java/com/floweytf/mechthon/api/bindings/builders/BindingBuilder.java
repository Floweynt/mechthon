package com.floweytf.mechthon.api.bindings.builders;

import com.floweytf.mechthon.api.bindings.annotations.BindingGetter;
import com.floweytf.mechthon.api.bindings.annotations.BindingMethod;
import com.floweytf.mechthon.api.bindings.annotations.BindingSetter;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.VarHandle;
import org.jetbrains.annotations.Nullable;

/**
 * Defines an API for constructing bindings between Java classes and a target
 * scripting or binding environment.
 * <p>
 * A {@code BindingBuilder} instance allows properties and functions to be registered,
 * typically for use in scripting languages or other runtime environments that
 * access Java objects in a reflective or dynamic way.
 *
 * <h2>Property registration</h2>
 * Properties can be added by directly supplying {@link VarHandle} instances
 * (with optional read-only flags), or by providing getter/setter {@link MethodHandle}s.
 *
 * <h2>Function registration</h2>
 * Functions are registered by name using {@link MethodHandle} objects.
 *
 * <h2>Extension parsing</h2>
 * The {@link #fromClass(Class)} method allows automatic registration of
 * properties and functions from a class annotated with {@link BindingGetter},
 * {@link BindingSetter}, and {@link BindingMethod}.
 */
public interface BindingBuilder {
    /**
     * Registers a property using a {@link VarHandle} with both read and write access.
     *
     * @param name  the name of the property in the scripting environment
     * @param field the {@link VarHandle} for the property
     */
    void property(String name, VarHandle field);

    /**
     * Registers a property using a {@link VarHandle} with optional read-only access.
     *
     * @param name       the name of the property in the scripting environment
     * @param field      the {@link VarHandle} for the property
     * @param isReadOnly {@code true} to expose the property as read-only; {@code false} to allow writes
     */
    void property(String name, VarHandle field, boolean isReadOnly);

    /**
     * Registers a property using explicit getter and setter method handles.
     *
     * @param name   the name of the property in the scripting environment
     * @param getter a {@link MethodHandle} to retrieve the property value
     * @param setter a {@link MethodHandle} to set the property value, or {@code null} for read-only properties
     */
    void property(String name, MethodHandle getter, @Nullable MethodHandle setter);

    /**
     * Registers a read-only property using a getter method handle.
     *
     * @param name   the name of the property in the scripting environment
     * @param getter a {@link MethodHandle} to retrieve the property value
     */
    void property(String name, MethodHandle getter);

    /**
     * Registers a function in the scripting environment.
     *
     * @param name the name of the function in the scripting environment
     * @param mh   the {@link MethodHandle} for the function implementation
     */
    void function(String name, MethodHandle mh);

    /**
     * Parses and registers extension data from a class.
     * <p>
     * This method scans the specified class for public static methods annotated with
     * {@link BindingGetter}, {@link BindingSetter}, and {@link BindingMethod},
     * and registers them as properties or functions accordingly.
     * <p>
     * <h3>Name Translation</h3>
     * Names are tokenized and reformatted according to the target environment's
     * naming conventions. For inferred property names, {@code get} or {@code set}
     * prefixes are removed before translation.
     *
     * @param extensions the class containing extension methods and properties
     * @see BindingGetter
     * @see BindingSetter
     * @see BindingMethod
     */
    void fromClass(Class<?> extensions);
}
