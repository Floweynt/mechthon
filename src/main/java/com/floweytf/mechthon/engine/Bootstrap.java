package com.floweytf.mechthon.engine;

import org.graalvm.polyglot.Context;
import org.graalvm.polyglot.Value;

class Bootstrap {
    private final Value pyLoadScript;
    private final Value pyDefinePythonModule;

    Bootstrap(Context context) {
        context.eval("python", StaticSources.BOOTSTRAP);

        pyLoadScript = context.getBindings("python").getMember("load_script");
        pyDefinePythonModule = context.getBindings("python").getMember("define_python_module");
    }

    void loadScript(ScriptInstance instance, String source) {
        pyLoadScript.executeVoid(instance, source);
    }

    void definePythonModule(String pkg, String name, String source, boolean isPackage) {
        pyDefinePythonModule.executeVoid(pkg, name, source, isPackage);
    }
}
