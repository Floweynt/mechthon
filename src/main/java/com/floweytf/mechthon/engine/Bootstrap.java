package com.floweytf.mechthon.engine;

import org.graalvm.polyglot.Context;
import org.graalvm.polyglot.Value;

class Bootstrap {
    private final Value pyLoadScript;
    private final Value pyExecSandboxed;

    Bootstrap(Context context) {
        context.eval("python", StaticSources.BOOTSTRAP);

        pyExecSandboxed = context.getBindings("python").getMember("exec_sandboxed");
        pyLoadScript = context.getBindings("python").getMember("load_script");
    }

    Value execSandboxed(String source, String fileName) {
        return pyExecSandboxed.execute(source, fileName);
    }

    void loadScript(ScriptInstance instance, String source) {
        pyLoadScript.executeVoid(instance, source);
    }
}
