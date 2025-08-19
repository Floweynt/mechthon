package com.floweytf.mechthon.bindings.truffle.objects;

import com.floweytf.mechthon.bindings.compiled.CompiledMember;
import com.oracle.truffle.api.interop.InteropLibrary;
import com.oracle.truffle.api.interop.TruffleObject;
import com.oracle.truffle.api.library.ExportLibrary;
import com.oracle.truffle.api.library.ExportMessage;

@ExportLibrary(InteropLibrary.class)
public record InstanceFunctionTO(CompiledMember member, Object receiver) implements TruffleObject {
    @ExportMessage
    public boolean isExecutable() {
        return member.isExecutable();
    }

    @ExportMessage
    public Object execute(Object[] arguments) {
        return member.invoke(receiver, arguments == null ? new Object[0] : arguments);
    }
}