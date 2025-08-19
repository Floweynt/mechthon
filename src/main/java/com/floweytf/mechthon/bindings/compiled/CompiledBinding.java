package com.floweytf.mechthon.bindings.compiled;

import com.floweytf.mechthon.bindings.truffle.objects.BindingInstanceTO;
import com.floweytf.mechthon.bindings.truffle.objects.ObjectArrayTO;
import java.util.Map;
import java.util.function.IntSupplier;

public final class CompiledBinding extends CompiledMember {
    private final Map<String, CompiledMember> members;
    private final ObjectArrayTO cachedMemberNames;
    private final int maxCacheSlots;

    public CompiledBinding(Map<String, CompiledMember> members, int cacheSlot, int maxCacheSlots) {
        super(false, false, -1, cacheSlot);
        this.members = members;
        this.cachedMemberNames = new ObjectArrayTO(members.keySet().toArray(String[]::new));
        this.maxCacheSlots = maxCacheSlots;
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

    public int getMaxCacheSlots() {
        return maxCacheSlots;
    }

    @Override
    public CompiledMember clone(IntSupplier cacheSlotGetter) {
        return new CompiledBinding(members, cacheSlotGetter.getAsInt(), maxCacheSlots);
    }

    @Override
    public Object read(Object receiver) {
        return new BindingInstanceTO(receiver, this);
    }

    public Map<String, CompiledMember> getMembers() {
        return members;
    }
}