package com.floweytf.mechthon.engine;

import net.kyori.adventure.text.Component;
import org.bukkit.entity.Entity;
import org.graalvm.polyglot.Value;

public class Bindings {
    private final Value entityConstructor;
    private final Value messageConstructor;

    Bindings(Bootstrap bootstrap) {
        {
            final var res = bootstrap.loadImpl("impl_entity", StaticSources.IMPL_ENTITY);
            entityConstructor = res.getHashValue("EntityImpl");
        }

        {
            final var res = bootstrap.loadImpl("impl_message.py", StaticSources.IMPL_MESSAGE);
            messageConstructor = res.getHashValue("MessageImpl");
        }
    }

    public Value createEntity(Entity entity) {
        return entityConstructor.execute(entity);
    }

    public Value createComponent(Component component) {
        return messageConstructor.execute(component);
    }
}
