package com.floweytf.mechthon.api.bindings.annotations;

import com.floweytf.mechthon.api.bindings.builders.BindingBuilder;

/**
 * Marks a method in a bound class to be exposed as an invokable function
 * in the target scripting or binding environment.
 * <p>
 * The annotated method should generally be of the form:
 * <pre>{@code
 * public static R methodName(T self, ...args);
 * }</pre>
 * where {@code T} is the type of the <i>bound class</i> and {@code R} is the return type
 * (use {@code void} for no return value).
 * <p>
 * The method must be {@code static}, as it will be invoked with the instance passed explicitly
 * as the first argument. Additional parameters after {@code self} represent the arguments
 * provided from the scripting environment.
 * <p>
 * When {@link #value()} is not provided (empty string), the exposed function name defaults
 * to the method name. Names are exposed according to the standard name translation algorithm;
 * see {@link BindingBuilder#fromClass(Class)}.
 *
 * <h3>Example</h3>
 * <pre>{@code
 * public class PlayerExtension {
 *     @BindingMethod("increaseScore")
 *     public static void addScore(Player self, int amount) {
 *         self.score += amount;
 *     }
 * }
 * }</pre>
 * <p>
 * This would make the {@code increaseScore} method available in the scripting
 * environment, allowing scripts to modify the {@code score} field of a {@code Player}.
 *
 * @see BindingBuilder#fromClass(Class)
 */
public @interface BindingMethod {
    /**
     * The name to expose the method as in the scripting environment.
     * <p>
     * If left as an empty string, the method name will be used directly.
     *
     * @return the exposed method name, or an empty string to use the method name.
     */
    String value() default "";
}
