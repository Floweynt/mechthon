package com.floweytf.mechthon.bindings.impl;

import com.floweytf.mechthon.bindings.compiled.CompiledBinding;
import java.util.Map;

public record RuntimeBindingContext(Map<Class<?>, CompiledBinding> knownBindingTypes) {
}
