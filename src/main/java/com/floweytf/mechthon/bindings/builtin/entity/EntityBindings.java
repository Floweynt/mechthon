package com.floweytf.mechthon.bindings.builtin.entity;

import com.floweytf.mechthon.api.bindings.Bindings;
import com.floweytf.mechthon.reflect.EntityVariables;

public class EntityBindings {
    public static void initialize(Bindings bindings) {
        EntityVariables.scan();
    }
}
