package com.floweytf.mechthon.api.bindings;

import com.floweytf.mechthon.api.bindings.builders.RootBindingBuilder;

/**
 * Provides the central API for defining and retrieving class bindings
 * between Java types and a target scripting or binding environment.
 * <p>
 * A {@code Bindings} instance manages the mapping between Java classes and their
 * exposed properties, methods, and static members in the guest language.
 */
public interface Bindings {
    /**
     * Defines a new binding for a Java class.
     * <p>
     * This creates a {@link RootBindingBuilder} for the given class, allowing
     * registration of instance and static bindings.
     *
     * @param clazz     the Java class to bind
     * @param submodule the submodule to place this binding under
     * @param name      the name to expose in the guest language
     * @param <T>       the type of the class being bound
     * @return a {@link RootBindingBuilder} for configuring bindings of the class
     */
    <T> RootBindingBuilder<T> defineClassBinding(Class<T> clazz, String submodule, String name);

    /**
     * Retrieves the existing bindings for a given class.
     * <p>
     * Returns the {@link RootBindingBuilder} previously created for the class,
     * allowing further modification of its bindings.
     *
     * @param clazz the Java class whose bindings are being retrieved
     * @param <T>   the type of the class
     * @return the {@link RootBindingBuilder} for the class
     */
    <T> RootBindingBuilder<T> getClassBindings(Class<T> clazz);
}
