package com.floweytf.mechthon.bindings.truffle.objects;

import com.floweytf.mechthon.bindings.compiled.FunctionMember;
import com.floweytf.mechthon.bindings.truffle.nodes.InvokeNode;
import com.oracle.truffle.api.dsl.Bind;
import com.oracle.truffle.api.dsl.Cached;
import com.oracle.truffle.api.interop.ArityException;
import com.oracle.truffle.api.interop.InteropLibrary;
import com.oracle.truffle.api.interop.TruffleObject;
import com.oracle.truffle.api.interop.UnsupportedTypeException;
import com.oracle.truffle.api.library.ExportLibrary;
import com.oracle.truffle.api.library.ExportMessage;
import com.oracle.truffle.api.nodes.Node;

@ExportLibrary(InteropLibrary.class)
public record InstanceFunctionTO(FunctionMember member, Object receiver) implements TruffleObject {
    @ExportMessage
    public boolean isExecutable() {
        return true;
    }

    @ExportMessage
    public Object execute(
        Object[] arguments,
        @Bind("$node") Node node,
        @Cached InvokeNode invokeNode
    ) throws UnsupportedTypeException, ArityException {
        return invokeNode.execute(node, member, receiver, arguments);
    }
}