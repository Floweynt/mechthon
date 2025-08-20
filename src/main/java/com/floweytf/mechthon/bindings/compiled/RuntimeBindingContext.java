package com.floweytf.mechthon.bindings.compiled;

import java.util.Map;

public record RuntimeBindingContext(Map<Class<?>, CompiledBinding> knownBindingTypes) {
}
