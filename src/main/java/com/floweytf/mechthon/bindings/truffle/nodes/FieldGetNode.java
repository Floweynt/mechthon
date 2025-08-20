package com.floweytf.mechthon.bindings.truffle.nodes;

import com.floweytf.mechthon.bindings.compiled.CompiledMember;
import com.oracle.truffle.api.dsl.Cached;
import com.oracle.truffle.api.dsl.GenerateCached;
import com.oracle.truffle.api.dsl.GenerateInline;
import com.oracle.truffle.api.dsl.GenerateUncached;
import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.nodes.Node;

@GenerateCached
@GenerateUncached
@GenerateInline
public abstract class FieldGetNode extends Node {
    public abstract Object execute(Node inliningTarget, CompiledMember member, Object receiver);

    @Specialization(
        guards = {
            "member == cachedMember",
            "receiver == cachedReceiver",
            "member.isCacheable()"
        },
        limit = "8"
    )
    protected static Object doCached(
        Node inliningTarget,
        CompiledMember member,
        Object receiver,
        @Cached("member") CompiledMember cachedMember,
        @Cached("receiver") Object cachedReceiver,
        @Cached("member.read(receiver)") Object cachedValue) {
        return cachedValue;
    }

    @Specialization(replaces = "doCached")
    protected static Object doUncached(Node inliningTarget, CompiledMember member, Object receiver) {
        return member.read(receiver);
    }
}
