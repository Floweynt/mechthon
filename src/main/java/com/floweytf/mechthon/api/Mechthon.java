package com.floweytf.mechthon.api;

import com.floweytf.mechthon.MechthonApiImpl;
import org.bukkit.plugin.Plugin;

public interface Mechthon {
    static Mechthon getInstance() {
        return MechthonApiImpl.INSTANCE;
    }

    /**
     * Registers a python module to the global scope.
     * <p>
     * The python code for the plugin will be loaded from the plugin jar at {@param path}. This will
     * be importable as {@param moduleName}.
     *
     * @param plugin     The plugin registering this module.
     * @param moduleName The name of the module.
     * @param path       The path of the directory containing the module in the jar.
     */
    void registerPythonModule(Plugin plugin, String moduleName, String path);
}
