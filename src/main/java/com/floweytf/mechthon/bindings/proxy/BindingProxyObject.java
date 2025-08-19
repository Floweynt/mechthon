package com.floweytf.mechthon.bindings.proxy;

import com.floweytf.mechthon.bindings.compiled.CompiledBinding;
import org.graalvm.polyglot.Value;
import org.graalvm.polyglot.proxy.ProxyObject;

public class BindingProxyObject implements ProxyObject {
    private final Object wrapped;
    private final CompiledBinding bindings;
    private final Object[] cacheSlots;

    public BindingProxyObject(Object wrapped, CompiledBinding bindings) {
        this.wrapped = wrapped;
        this.bindings = bindings;
        this.cacheSlots = new Object[bindings.getMaxCacheSlots()];
    }

    @Override
    public Object getMember(String key) {
        final var member = bindings.getMember(key);
        if (member == null) {
            return null;
        }

        return member.read(wrapped);
    }

    @Override
    public Object getMemberKeys() {
        return bindings.getCachedMemberNames();
    }

    @Override
    public boolean hasMember(String key) {
        return bindings.getMember(key) != null;
    }

    @Override
    public void putMember(String key, Value value) {
        final var member = bindings.getMember(key);

        if (member == null) {
            throw new UnsupportedOperationException("cannot add member");
        }

        member.write(wrapped, value);
    }

    @Override
    public String toString() {
        return super.toString();
    }
}
