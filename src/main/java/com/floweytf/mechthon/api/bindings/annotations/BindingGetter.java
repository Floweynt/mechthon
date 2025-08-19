package com.floweytf.mechthon.api.bindings.annotations;

import com.floweytf.mechthon.api.bindings.builders.BindingBuilder;

/**
 * Marks a method as a getter for a specific property in a bound class.
 * <p>
 * The annotated method should be of the form:
 * <pre>{@code
 * public static U func(T self);
 * }</pre>
 * where {@code T} is the type of the <i>bound class</i> and {@code U} is the return type of the property.
 * <p>
 * The method must be {@code static}, as it will be invoked with the instance passed explicitly as the
 * first argument.
 * <p>
 * When {@link #value()} is not provided (empty string), the property name defaults to the method name
 * without its {@code get} prefix (if present).
 * <p>
 * Names are exposed in the scripting language according to the standard name translation algorithm; see
 * {@link BindingBuilder#fromClass(Class)}.
 *
 * <h3>Example</h3>
 * <pre>{@code
 * public class PlayerExtension {
 *     @BindingGetter("name")
 *     public static String getName(Player self) {
 *         return self.name;
 *     }
 * }
 * }</pre>
 * <p>
 * This would expose the {@code name} property for binding frameworks or scripting environments.
 *
 * @see BindingBuilder#fromClass(Class)
 */
public @interface BindingGetter {
    /**
     * The name of the property this getter exposes.
     * <p>
     * If left as an empty string, the method name will be used to infer the property name.
     *
     * @return the property name or an empty string to use the method name.
     */
    String value() default "";
}
