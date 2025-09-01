package com.floweytf.mechthon.engine;

import com.floweytf.mechthon.MechthonPlugin;
import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.graalvm.polyglot.Value;

public record APIAccess(MechthonPlugin plugin, ScriptEngine engine, Bindings bindings) {
    public boolean entityScoreExists(String name) {
        return Bukkit.getScoreboardManager().getMainScoreboard().getObjective(name) != null;
    }

    public Value entityInvoke(Entity entity, String name) {
        return engine.invokeScript(entity, name);
    }

    public Value entityTrigger(Entity entity, String scriptName, String triggerName) {
        return engine.invokeTrigger(entity, scriptName, triggerName);
    }
}
