package com.floweytf.mechthon.engine;

import com.floweytf.mechthon.MechthonPlugin;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.graalvm.polyglot.Value;

public record APIAccess(MechthonPlugin plugin, ScriptEngine engine, Bindings bindings) {
    private static final MiniMessage MINI_MESSAGE = MiniMessage.builder().tags(TagResolver.standard()).build();

    public int entityGetScore(Entity entity, String name) {
        final var objective = Bukkit.getScoreboardManager().getMainScoreboard().getObjective(name);

        if (objective == null) {
            throw new IllegalArgumentException("score " + name + " does not exist!");
        }

        return objective.getScoreFor(entity).getScore();
    }

    public void entitySetScore(Entity entity, String name, int value) {
        final var objective = Bukkit.getScoreboardManager().getMainScoreboard().getObjective(name);

        if (objective == null) {
            throw new IllegalArgumentException("score " + name + " does not exist!");
        }

        objective.getScoreFor(entity).setScore(value);
    }

    public Value entityInvoke(Entity entity, String name) {
        return engine.invokeScript(entity, name);
    }

    public Value entityTrigger(Entity entity, String scriptName, String triggerName) {
        return engine.invokeTrigger(entity, scriptName, triggerName);
    }

    public boolean entityScoreExists(String name) {
        return Bukkit.getScoreboardManager().getMainScoreboard().getObjective(name) != null;
    }

    public Value componentFromMini(String mini) {
        return bindings.createComponent(MINI_MESSAGE.deserialize(mini));
    }

    public String componentToRaw(Component component) {
        return PlainTextComponentSerializer.plainText().serialize(component);
    }
}
