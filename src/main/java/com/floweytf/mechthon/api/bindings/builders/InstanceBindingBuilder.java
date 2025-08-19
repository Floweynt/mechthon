package com.floweytf.mechthon.api.bindings.builders;

/**
 * Extends {@link BindingBuilder} with instance-specific capabilities.
 * <p>
 * These bindings apply to objects with actual instances, unlike
 * {@link RootBindingBuilder#staticBindings()}, which only apply to the
 * exposed metaobject and thus lack a backing Java instance.
 * <p>
 * This interface supports namespaced bindings and soft extension of
 * other instance bindings.
 *
 * @param <T> the type of the instance being bound
 */
public interface InstanceBindingBuilder<T> extends BindingBuilder {
    /**
     * Registers or retrieves a namespaced binding under this instance.
     * <p>
     * Namespaced bindings can be accessed in the guest language via:
     * <pre>{@code obj.<namespace>.<member>}</pre>
     * <p>
     * Otherwise, these bindings behave identically to regular bindings.
     *
     * @param name the namespace name to create or retrieve
     * @return an {@link InstanceBindingBuilder} for the specified namespace
     */
    InstanceBindingBuilder<T> getOrCreateNamespaced(String name);

    /**
     * Soft-extends another instance binding, allowing this instance to reuse
     * properties and functions from {@code other}.
     * <p>
     * This does not affect the type hierarchy in the guest language.
     *
     * @param other the other {@link InstanceBindingBuilder} to extend from
     */
    void extendFrom(InstanceBindingBuilder<? super T> other);
}
