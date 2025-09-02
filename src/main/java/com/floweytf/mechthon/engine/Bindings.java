package com.floweytf.mechthon.engine;

import org.bukkit.entity.Entity;
import org.graalvm.polyglot.Value;

public class Bindings {
    private final Value entityConstructor;

    Bindings(Bootstrap bootstrap) {
        final var res = bootstrap.execSandboxed(StaticSources.BINDINGS, "@builtin/bootstrap.py");
        entityConstructor = res.getHashValue("wrap_entity");
    }

    public Value createEntity(Entity entity) {
        return entityConstructor.execute(entity);
    }
}
