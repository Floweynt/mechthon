package com.floweytf.mechthon.engine;

import com.floweytf.mechthon.MechthonPlugin;
import com.floweytf.mechthon.persist.GlobalPersistentKeyMetadata;
import io.papermc.paper.threadedregions.scheduler.EntityScheduler;
import org.bukkit.entity.Entity;
import org.graalvm.polyglot.Value;

public record APIAccess(MechthonPlugin plugin, ScriptEngine engine, Bindings bindings) {
    public Value entityInvoke(Entity entity, String name) {
        return engine.invokeScript(entity, name);
    }

    public Value entityTrigger(Entity entity, String scriptName, String triggerName) {
        return engine.invokeTrigger(entity, scriptName, triggerName);
    }

    public void scheduleEntityTask(EntityScheduler entityScheduler, Value value, long delay) {
        entityScheduler.execute(
            plugin,
            () -> {
                if (!engine.isClosed()) {
                    value.executeVoid();
                }
            },
            null,
            delay
        );
    }

    public GlobalPersistentKeyMetadata getGlobalPersistentKey(String name) {
        return engine.scriptManager().globalPersistentKeys().get(name);
    }
}
