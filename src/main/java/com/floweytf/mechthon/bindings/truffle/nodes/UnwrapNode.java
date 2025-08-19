package com.floweytf.mechthon.bindings.truffle.nodes;

import com.oracle.truffle.api.dsl.Cached;
import com.oracle.truffle.api.dsl.GenerateCached;
import com.oracle.truffle.api.dsl.GenerateInline;
import com.oracle.truffle.api.dsl.GenerateUncached;
import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.nodes.Node;
import com.oracle.truffle.api.strings.AbstractTruffleString;
import com.oracle.truffle.api.strings.TruffleString;

@GenerateUncached
@GenerateInline
@GenerateCached
public abstract class UnwrapNode extends Node {
    public abstract Object execute(Node inliningTarget, Object value, Class<?> targetType);

    @Specialization
    public Object doUncached(Node inliningTarget, Object value, Class<?> targetType,
                             @Cached(inline = false) TruffleString.ToJavaStringNode toJavaString) {
        if (value instanceof AbstractTruffleString ts) {
            return toJavaString.execute(ts);
        }

        return value;
    }
}
