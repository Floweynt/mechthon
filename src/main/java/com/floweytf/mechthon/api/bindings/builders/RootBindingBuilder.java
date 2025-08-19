package com.floweytf.mechthon.api.bindings.builders;

/**
 * Extends {@link InstanceBindingBuilder} to provide bindings for root-level types.
 * <p>
 * A {@code RootBindingBuilder} allows defining both instance-level and static bindings
 * for a given type. It also supports establishing superclass relationships in the guest
 * language.
 *
 * @param <T> the type of the root instance being bound
 */
public interface RootBindingBuilder<T> extends InstanceBindingBuilder<T> {
    /**
     * Retrieves the {@link BindingBuilder} for static bindings.
     * <p>
     * Static bindings are associated with the type itself (the metaobject) rather than
     * individual instances.
     *
     * @return the {@link BindingBuilder} for static members
     */
    BindingBuilder staticBindings();
}
