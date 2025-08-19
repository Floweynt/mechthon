package com.floweytf.mechthon.bindings.truffle.objects;

import com.floweytf.mechthon.bindings.compiled.CompiledBinding;
import com.floweytf.mechthon.bindings.compiled.CompiledMember;
import com.floweytf.mechthon.bindings.truffle.nodes.LookupNode;
import com.oracle.truffle.api.dsl.Bind;
import com.oracle.truffle.api.dsl.Cached;
import com.oracle.truffle.api.interop.ArityException;
import com.oracle.truffle.api.interop.InteropLibrary;
import com.oracle.truffle.api.interop.TruffleObject;
import com.oracle.truffle.api.interop.UnknownIdentifierException;
import com.oracle.truffle.api.interop.UnsupportedMessageException;
import com.oracle.truffle.api.library.ExportLibrary;
import com.oracle.truffle.api.library.ExportMessage;
import com.oracle.truffle.api.nodes.Node;
import com.oracle.truffle.api.profiles.InlinedBranchProfile;
import org.apache.commons.lang3.NotImplementedException;
import org.jetbrains.annotations.Nullable;

@ExportLibrary(InteropLibrary.class)
public final class BindingInstanceTO implements TruffleObject {
    private final Object target;
    private final CompiledBinding compiled;
    private final Object[] cache;
    private final @Nullable BindingInstanceTO metaObject;

    public BindingInstanceTO(Object target, CompiledBinding compiled, @Nullable BindingInstanceTO metaObject) {
        this.target = target;
        this.compiled = compiled;
        this.cache = new Object[compiled.getMaxCacheSlots()];
        this.metaObject = metaObject;
    }

    public CompiledBinding getCompiled() {
        return compiled;
    }

    @ExportMessage
    public boolean isNull() {
        return target == null;
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

        return compiled.getCachedMemberNames();
    }

    @ExportMessage
    public boolean isMemberReadable(String member) {
        return !isNull() && compiled.hasMember(member);
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

        CompiledMember cm = lookupNode.execute(node, compiled, member);
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

        CompiledMember cm = lookupNode.execute(node, compiled, member);
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
        @Cached.Shared("error") @Cached InlinedBranchProfile error
    ) throws UnknownIdentifierException, UnsupportedMessageException {
        if (isNull()) {
            error.enter(node);
            throw UnsupportedMessageException.create();
        }

        CompiledMember cm = lookupNode.execute(node, compiled, member);

        if (cm == null) {
            error.enter(node);
            throw UnknownIdentifierException.create(member);
        }

        // cached read for specific things
        if (cm.getCacheSlot() != -1) {
            if (cache[cm.getCacheSlot()] == null) {
                cache[cm.getCacheSlot()] = cm.read(target);
            }

            return cache[cm.getCacheSlot()];
        } else {
            return cm.read(target);
        }
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

        CompiledMember cm = lookupNode.execute(node, compiled, member);

        if (cm == null) {
            error.enter(node);
            throw UnknownIdentifierException.create(member);
        }

        cm.write(target, value);
    }

    @ExportMessage
    Object invokeMember(
        String member,
        Object[] args,
        @Bind("$node") Node node,
        @Cached.Shared("lookupNode") @Cached LookupNode lookupNode,
        @Cached.Shared("error") @Cached InlinedBranchProfile error
    ) throws UnknownIdentifierException, UnsupportedMessageException, ArityException {
        if (isNull()) {
            error.enter(node);
            throw UnsupportedMessageException.create();
        }

        CompiledMember cm = lookupNode.execute(node, compiled, member);

        if (cm == null) {
            error.enter(node);
            throw UnknownIdentifierException.create(member);
        }

        if (cm.getArity() != args.length) {
            error.enter(node);
            throw ArityException.create(cm.getArity(), cm.getArity(), args.length);
        }

        return cm.invoke(target, args);
    }

    @ExportMessage
    boolean isMetaObject() {
        return !isNull() && false;
    }

    @ExportMessage
    Object getMetaSimpleName(
        @Bind("$node") Node node,
        @Cached.Shared("error") @Cached InlinedBranchProfile error
    ) throws UnsupportedMessageException {
        if (!isMetaObject()) {
            error.enter(node);
            throw UnsupportedMessageException.create();
        }
        throw new NotImplementedException("TODO");
        // return ((CompiledClassBinding) target).getSimpleName();
    }

    @ExportMessage
    Object getMetaQualifiedName(
        @Bind("$node") Node node,
        @Cached.Shared("error") @Cached InlinedBranchProfile error
    ) throws UnsupportedMessageException {
        if (!isMetaObject()) {
            error.enter(node);
            throw UnsupportedMessageException.create();
        }
        throw new NotImplementedException("TODO");
        //return ((CompiledClassBinding) target).getQualifiedName();
    }

    @ExportMessage
    boolean hasMetaParents() {
        if (!isMetaObject()) {
            return false;
        }

        throw new NotImplementedException("TODO");
    }

    @ExportMessage
    Object getMetaParents(
        @Bind("$node") Node node,
        @Cached.Shared("error") @Cached InlinedBranchProfile error
    ) throws UnsupportedMessageException {
        if (!isMetaObject()) {
            error.enter(node);
            throw UnsupportedMessageException.create();
        }

        throw new NotImplementedException("TODO");
    }

    @SuppressWarnings("SuspiciousMethodCalls")
    @ExportMessage
    boolean isMetaInstance(
        Object instance,
        @Bind("$node") Node node,
        @Cached.Shared("error") @Cached InlinedBranchProfile error
    ) throws UnsupportedMessageException {
        if (!isMetaObject()) {
            error.enter(node);
            throw UnsupportedMessageException.create();
        }

        throw new NotImplementedException("TODO");
    }

    @ExportMessage
    public boolean hasMetaObject() {
        return !isNull() && metaObject != null;
    }

    @ExportMessage
    public Object getMetaObject(
        @Bind("$node") Node node,
        @Cached.Shared("error") @Cached InlinedBranchProfile error
    ) throws UnsupportedMessageException {
        if (isNull() || metaObject == null) {
            error.enter(node);
            throw UnsupportedMessageException.create();
        }

        return metaObject;
    }
}