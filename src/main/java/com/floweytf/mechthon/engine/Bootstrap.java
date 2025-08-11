package com.floweytf.mechthon.engine;

import org.graalvm.polyglot.Context;
import org.graalvm.polyglot.Value;

class Bootstrap {
    private final Value pyLoadScript;
    private final Value pyDefinePythonModule;
    private final Value pyLoadImpl;

    Bootstrap(Context context) {
        context.eval("python", StaticSources.BOOTSTRAP);

        pyLoadScript = context.getBindings("python").getMember("load_script");
        pyLoadImpl = context.getBindings("python").getMember("load_impl");
        pyDefinePythonModule = context.getBindings("python").getMember("define_python_module");
    }

    Value loadImpl(String name, String source) {
        return pyLoadImpl.execute(name, source);
    }

    void loadScript(ScriptInstance instance, String source) {
        pyLoadScript.executeVoid(instance, source);
    }

    void definePythonModule(String pkg, String name, String source, boolean isPackage) {
        pyDefinePythonModule.executeVoid(pkg, name, source, isPackage);
    }
}
