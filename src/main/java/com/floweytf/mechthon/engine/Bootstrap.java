package com.floweytf.mechthon.engine;

import org.graalvm.polyglot.Context;
import org.graalvm.polyglot.Value;

class Bootstrap {
    private final Value pyLoadScript;

    Bootstrap(Context context) {
        context.eval("python", StaticSources.BOOTSTRAP);
        pyLoadScript = context.getBindings("python").getMember("load_script");
    }

    void loadScript(ScriptInstance instance, String source) {
        pyLoadScript.executeVoid(instance, source);
    }
}
