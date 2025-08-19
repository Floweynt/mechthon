package com.floweytf.mechthon.bindings.truffle.nodes;

import com.floweytf.mechthon.bindings.compiled.FunctionMember;
import com.oracle.truffle.api.dsl.Cached;
import com.oracle.truffle.api.dsl.GenerateCached;
import com.oracle.truffle.api.dsl.GenerateInline;
import com.oracle.truffle.api.dsl.GenerateUncached;
import com.oracle.truffle.api.dsl.ReportPolymorphism;
import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.interop.ArityException;
import com.oracle.truffle.api.interop.UnsupportedTypeException;
import com.oracle.truffle.api.nodes.ExplodeLoop;
import com.oracle.truffle.api.nodes.Node;
import com.oracle.truffle.api.profiles.InlinedBranchProfile;

@ReportPolymorphism
@GenerateUncached
@GenerateInline
@GenerateCached(false)
public abstract class InvokeNode extends Node {
    public abstract Object execute(Node node, FunctionMember method, Object obj, Object[] args) throws UnsupportedTypeException, ArityException;

    static UnwrapNode[] createToHost(int argsLength) {
        UnwrapNode[] toJava = new UnwrapNode[argsLength];
        for (int i = 0; i < argsLength; i++) {
            toJava[i] = UnwrapNodeGen.create();
        }
        return toJava;
    }

    static UnwrapNode[] createUncached(int argsLength) {
        UnwrapNode[] toJava = new UnwrapNode[argsLength];
        for (int i = 0; i < argsLength; i++) {
            toJava[i] = UnwrapNodeGen.getUncached();
        }
        return toJava;
    }

    @SuppressWarnings("unused")
    @ExplodeLoop
    @Specialization(guards = {"method == cachedMethod"}, limit = "3")
    static Object doFixed(
        Node node, FunctionMember method, Object obj, Object[] args,
        @Cached("method") FunctionMember cachedMethod,
        @Cached(value = "createToHost(method.getArity())", uncached = "createUncached(method.getArity())") UnwrapNode[] toJavaNodes,
        @Cached InlinedBranchProfile errorBranch
    ) throws ArityException {
        int arity = cachedMethod.getArity();

        if (args.length != arity) {
            errorBranch.enter(node);
            throw ArityException.create(arity, arity, args.length);
        }

        Object[] convertedArguments = new Object[args.length];

        for (int i = 0; i < toJavaNodes.length; i++) {
            convertedArguments[i] = toJavaNodes[i].execute(toJavaNodes[i], args[i], cachedMethod.getInvokerArgs()[i]);
        }

        return method.invoke(obj, convertedArguments);
    }
}