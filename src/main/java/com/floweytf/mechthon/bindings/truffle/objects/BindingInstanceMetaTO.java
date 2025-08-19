package com.floweytf.mechthon.bindings.truffle.objects;

import com.oracle.truffle.api.interop.InteropLibrary;
import com.oracle.truffle.api.interop.TruffleObject;
import com.oracle.truffle.api.library.ExportLibrary;
import com.oracle.truffle.api.library.ExportMessage;
import java.util.Set;

@ExportLibrary(InteropLibrary.class)
public record BindingInstanceMetaTO(
    String simpleName, String fullName, ObjectArrayTO parents,
    Set<BindingInstanceMetaTO> allParents
) implements TruffleObject {
    @ExportMessage
    boolean isMetaObject() {
        return true;
    }

    @ExportMessage
    Object getMetaSimpleName() {
        return simpleName;
    }

    @ExportMessage
    public Object getMetaQualifiedName() {
        return fullName;
    }

    @ExportMessage
    boolean hasMetaParents() {
        return parents.parents().length > 0;
    }

    @ExportMessage
    Object getMetaParents() {
        return parents;
    }

    @SuppressWarnings("SuspiciousMethodCalls")
    @ExportMessage
    boolean isMetaInstance(Object instance) {
        return allParents.contains(instance);
    }
}
