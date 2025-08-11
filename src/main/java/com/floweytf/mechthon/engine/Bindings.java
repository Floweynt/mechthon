package com.floweytf.mechthon.engine;

import net.kyori.adventure.text.Component;
import org.bukkit.entity.Entity;
import org.graalvm.polyglot.Context;
import org.graalvm.polyglot.Value;

public class Bindings {
    private final Value modules;
    private final Value entityConstructor;
    private final Value messageConstructor;

    private Value fetch(String module, String name) {
        return modules.getHashValue(module).getMember(name);
    }

    Bindings(Context context) {
        this.modules = context.getBindings("python").getMember("sys").getMember("modules");
        this.entityConstructor = fetch("mechs.entity", "Entity");
        this.messageConstructor = fetch("mechs.types", "Message");
    }

    public Value createEntity(Entity entity) {
        return entityConstructor.execute(entity);
    }

    public Value createComponent(Component component) {
        return messageConstructor.execute(component);
    }
}
