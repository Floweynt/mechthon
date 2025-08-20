package com.floweytf.mechthon.bindings.compiled;

import com.floweytf.mechthon.bindings.truffle.objects.BindingInstanceTO;
import com.floweytf.mechthon.bindings.truffle.objects.ObjectArrayTO;
import java.util.Map;
import java.util.function.IntSupplier;

public final class CompiledBinding extends CompiledMember {
    public static final CompiledBinding EMPTY = new CompiledBinding(Map.of());

    private final Map<String, CompiledMember> members;
    private final ObjectArrayTO cachedMemberNames;

    public CompiledBinding(Map<String, CompiledMember> members) {
        super(false, false, true, -1);
        this.members = members;
        this.cachedMemberNames = new ObjectArrayTO(members.keySet().toArray(String[]::new));
    }

    public CompiledMember getMember(String name) {
        return members.get(name);
    }

    public boolean hasMember(String name) {
        return members.containsKey(name);
    }

    public ObjectArrayTO getCachedMemberNames() {
        return cachedMemberNames;
    }

    @Override
    public Object read(Object receiver) {
        return BindingInstanceTO.ofNamespaceProxy(receiver, this);
    }

    public Map<String, CompiledMember> getMembers() {
        return members;
    }
}