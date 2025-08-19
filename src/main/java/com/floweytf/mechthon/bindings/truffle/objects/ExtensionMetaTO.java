package com.floweytf.mechthon.bindings.truffle.objects;

import com.oracle.truffle.api.interop.InteropLibrary;
import com.oracle.truffle.api.interop.TruffleObject;
import com.oracle.truffle.api.library.ExportLibrary;
import com.oracle.truffle.api.library.ExportMessage;

@ExportLibrary(InteropLibrary.class)
public record ExtensionMetaTO(String simpleName, String fullName) implements TruffleObject {
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
    boolean isMetaInstance(Object instance) {
        return this == instance;
    }
}
