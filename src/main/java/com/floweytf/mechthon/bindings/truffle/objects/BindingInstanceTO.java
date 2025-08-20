package com.floweytf.mechthon.bindings.truffle.objects;

import com.floweytf.mechthon.bindings.compiled.CompiledBinding;
import com.floweytf.mechthon.bindings.compiled.CompiledMember;
import com.floweytf.mechthon.bindings.compiled.FunctionMember;
import com.floweytf.mechthon.bindings.truffle.nodes.FieldGetNode;
import com.floweytf.mechthon.bindings.truffle.nodes.InvokeNode;
import com.floweytf.mechthon.bindings.truffle.nodes.LookupNode;
import com.oracle.truffle.api.dsl.Bind;
import com.oracle.truffle.api.dsl.Cached;
import com.oracle.truffle.api.interop.ArityException;
import com.oracle.truffle.api.interop.InteropLibrary;
import com.oracle.truffle.api.interop.TruffleObject;
import com.oracle.truffle.api.interop.UnknownIdentifierException;
import com.oracle.truffle.api.interop.UnsupportedMessageException;
import com.oracle.truffle.api.interop.UnsupportedTypeException;
import com.oracle.truffle.api.library.ExportLibrary;
import com.oracle.truffle.api.library.ExportMessage;
import com.oracle.truffle.api.nodes.Node;
import com.oracle.truffle.api.profiles.InlinedBranchProfile;

@ExportLibrary(InteropLibrary.class)
public record BindingInstanceTO(Object receiver, boolean canUnwrap, CompiledBinding bindings) implements TruffleObject {
    public static BindingInstanceTO of(Object target, CompiledBinding bindings) {
        return new BindingInstanceTO(target, true, bindings);
    }

    public static BindingInstanceTO ofNamespaceProxy(Object target, CompiledBinding bindings) {
        return new BindingInstanceTO(target, false, bindings);
    }

    @ExportMessage
    public boolean isNull() {
        return receiver == null;
    }

    @ExportMessage
    public boolean hasMembers() {
        return !isNull();
    }

    @ExportMessage
    public Object getMembers(boolean includeInternal) throws UnsupportedMessageException {
        if (isNull()) {
            throw UnsupportedMessageException.create();
        }

        return bindings.getCachedMemberNames();
    }

    @ExportMessage
    public boolean isMemberReadable(String member) {
        return !isNull() && bindings.hasMember(member);
    }

    @ExportMessage
    public boolean isMemberModifiable(
        String member,
        @Bind("$node") Node node,
        @Cached.Shared("lookupNode") @Cached LookupNode lookupNode
    ) {
        if (isNull()) {
            return false;
        }

        CompiledMember cm = lookupNode.execute(node, bindings, member);
        return cm != null && cm.isWriteable();
    }

    @ExportMessage
    public boolean isMemberInvocable(
        String member, @Bind("$node") Node node,
        @Cached.Shared("lookupNode") @Cached LookupNode lookupNode
    ) {
        if (isNull()) {
            return false;
        }

        CompiledMember cm = lookupNode.execute(node, bindings, member);
        return cm != null && cm.isExecutable();
    }

    @ExportMessage
    public boolean isMemberInsertable(String member) {
        return false;
    }

    @ExportMessage
    Object readMember(
        String member,
        @Bind("$node") Node node,
        @Cached.Shared("lookupNode") @Cached LookupNode lookupNode,
        @Cached(inline = true) FieldGetNode getNode,
        @Cached.Shared("error") @Cached InlinedBranchProfile error
    ) throws UnknownIdentifierException, UnsupportedMessageException {
        if (isNull()) {
            error.enter(node);
            throw UnsupportedMessageException.create();
        }

        CompiledMember cm = lookupNode.execute(node, bindings, member);

        if (cm == null) {
            error.enter(node);
            throw UnknownIdentifierException.create(member);
        }

        return getNode.execute(node, cm, receiver);
    }

    @ExportMessage
    void writeMember(
        String member, Object value,
        @Bind("$node") Node node,
        @Cached.Shared("lookupNode") @Cached LookupNode lookupNode,
        @Cached.Shared("error") @Cached InlinedBranchProfile error
    ) throws UnknownIdentifierException, UnsupportedMessageException {
        if (isNull()) {
            error.enter(node);
            throw UnsupportedMessageException.create();
        }

        CompiledMember cm = lookupNode.execute(node, bindings, member);

        if (cm == null) {
            error.enter(node);
            throw UnknownIdentifierException.create(member);
        }

        cm.write(receiver, value);
    }

    @ExportMessage
    Object invokeMember(
        String member,
        Object[] args,
        @Bind("$node") Node node,
        @Cached.Shared("lookupNode") @Cached LookupNode lookupNode,
        @Cached InvokeNode invokeNode,
        @Cached.Shared("error") @Cached InlinedBranchProfile error
    ) throws UnknownIdentifierException, UnsupportedMessageException, ArityException, UnsupportedTypeException {
        if (isNull()) {
            error.enter(node);
            throw UnsupportedMessageException.create();
        }

        CompiledMember cm = lookupNode.execute(node, bindings, member);

        if (cm == null) {
            error.enter(node);
            throw UnknownIdentifierException.create(member);
        }

        if (cm.getArity() != args.length) {
            error.enter(node);
            throw ArityException.create(cm.getArity(), cm.getArity(), args.length);
        }

        return invokeNode.execute(node, (FunctionMember) cm, receiver, args);
    }
}