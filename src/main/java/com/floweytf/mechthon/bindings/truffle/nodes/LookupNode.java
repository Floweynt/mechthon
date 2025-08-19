package com.floweytf.mechthon.bindings.truffle.nodes;

import com.floweytf.mechthon.bindings.compiled.CompiledBinding;
import com.floweytf.mechthon.bindings.compiled.CompiledMember;
import com.oracle.truffle.api.dsl.Cached;
import com.oracle.truffle.api.dsl.GenerateCached;
import com.oracle.truffle.api.dsl.GenerateInline;
import com.oracle.truffle.api.dsl.GenerateUncached;
import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.nodes.Node;

@GenerateCached(false)
@GenerateUncached
@GenerateInline
public abstract class LookupNode extends Node {
    public abstract CompiledMember execute(Node inliningTarget, CompiledBinding compiled, String member);

    @Specialization(guards = {"compiled == cachedCompiled", "member.equals(cachedMember)"}, limit = "6")
    static CompiledMember doCached(
        CompiledBinding compiled, String member,
        @Cached("compiled") CompiledBinding cachedCompiled,
        @Cached("member") String cachedMember,
        @Cached("cachedCompiled.getMember(cachedMember)") CompiledMember cachedMemberObj
    ) {
        return cachedMemberObj;
    }

    @Specialization(replaces = "doCached")
    static CompiledMember doGeneric(CompiledBinding compiled, String member) {
        return compiled.getMember(member);
    }
}