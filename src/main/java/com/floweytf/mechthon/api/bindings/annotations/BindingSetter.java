package com.floweytf.mechthon.api.bindings.annotations;

import com.floweytf.mechthon.api.bindings.builders.BindingBuilder;

/**
 * Marks a method as a setter for a specific property in a bound class.
 * <p>
 * The annotated method should be of the form:
 * <pre>{@code
 * public static void func(T self, U value);
 * }</pre>
 * where {@code T} is the type of the <i>bound class</i> and {@code U} is the type of the property being set.
 * <p>
 * The method must be {@code static}, as it will be invoked with the instance passed explicitly as the
 * first argument, followed by the new property value as the second argument.
 * <p>
 * When {@link #value()} is not provided (empty string), the property name defaults to the method name
 * without its {@code set} prefix (if present).
 * <p>
 * Names are exposed in the scripting language according to the standard name translation algorithm;
 * see {@link BindingBuilder#fromClass(Class)}.
 *
 * <h3>Example</h3>
 * <pre>{@code
 * public class PlayerExtension {
 *     @BindingSetter("score")
 *     public static void setScore(Player self, int newScore) {
 *         self.score = newScore;
 *     }
 * }
 * }</pre>
 * <p>
 * This would expose the {@code score} property for assignment in the scripting
 * environment, allowing scripts to change its value.
 *
 * @see BindingBuilder#fromClass(Class)
 */
public @interface BindingSetter {
    /**
     * The name of the property this setter modifies.
     * <p>
     * If left as an empty string, the method name will be used to infer the property name.
     *
     * @return the property name, or an empty string to use the method name.
     */
    String value() default "";
}
