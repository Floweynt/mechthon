package com.floweytf.mechthon.bindings.truffle.objects;

import com.oracle.truffle.api.dsl.Bind;
import com.oracle.truffle.api.dsl.Cached;
import com.oracle.truffle.api.interop.InteropLibrary;
import com.oracle.truffle.api.interop.InvalidArrayIndexException;
import com.oracle.truffle.api.interop.TruffleObject;
import com.oracle.truffle.api.library.ExportLibrary;
import com.oracle.truffle.api.library.ExportMessage;
import com.oracle.truffle.api.nodes.Node;
import com.oracle.truffle.api.profiles.InlinedBranchProfile;

@ExportLibrary(InteropLibrary.class)
public record ObjectArrayTO(Object[] parents) implements TruffleObject {
    @ExportMessage
    public boolean hasArrayElements() {
        return true;
    }

    @ExportMessage
    public long getArraySize() {
        return parents.length;
    }

    @ExportMessage
    public boolean isArrayElementReadable(long index) {
        return index >= 0 && index < parents.length;
    }

    @ExportMessage
    public Object readArrayElement(
        long idx, @Bind("$node") Node node, @Cached InlinedBranchProfile error
    ) throws InvalidArrayIndexException {
        if (!isArrayElementReadable(idx)) {
            error.enter(node);
            throw InvalidArrayIndexException.create(idx);
        }

        return parents[(int) idx];
    }
}