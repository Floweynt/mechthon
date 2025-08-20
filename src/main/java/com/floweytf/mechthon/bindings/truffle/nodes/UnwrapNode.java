package com.floweytf.mechthon.bindings.truffle.nodes;

import com.floweytf.mechthon.bindings.truffle.objects.BindingInstanceTO;
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
    public Object doUncached(Node inliningTarget, BindingInstanceTO value, Class<?> targetType) {
        if (value.canUnwrap()) {
            return value.receiver();
        }

        throw new UnsupportedOperationException("can't unwrap");
    }

    @Specialization
    public Object doUncached(Node inliningTarget, AbstractTruffleString value, Class<?> targetType,
                             @Cached(inline = false) TruffleString.ToJavaStringNode toJavaString) {
        return toJavaString.execute(value);
    }

    @Specialization
    public Object doUncached(Node inliningTarget, Number number, Class<?> targetType) {
        if (targetType == byte.class || targetType == Byte.class) {
            return number.byteValue();
        } else if (targetType == short.class || targetType == Short.class) {
            return number.shortValue();
        } else if (targetType == int.class || targetType == Integer.class) {
            return number.intValue();
        } else if (targetType == long.class || targetType == Long.class) {
            return number.longValue();
        } else if (targetType == float.class || targetType == Float.class) {
            return number.floatValue();
        } else if (targetType == double.class || targetType == Double.class) {
            return number.doubleValue();
        }

        throw new UnsupportedOperationException("can't unwrap");
    }

    final boolean hasIntrinsic(Object o) {
        return o instanceof AbstractTruffleString || o instanceof BindingInstanceTO || o instanceof Number;
    }

    @Specialization(guards = {"!hasIntrinsic(value)"})
    public Object doUncached(Node inliningTarget, Object value, Class<?> targetType) {
        return value;
    }
}
