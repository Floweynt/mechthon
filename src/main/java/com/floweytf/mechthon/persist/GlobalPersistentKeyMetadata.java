package com.floweytf.mechthon.persist;

import com.google.gson.JsonObject;
import java.util.Locale;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;

public class GlobalPersistentKeyMetadata {
    private int refcount;
    private final Component displayName;
    private final Component description;
    private final KeyTarget target;

    public GlobalPersistentKeyMetadata(JsonObject json) {
        this.displayName = MiniMessage.miniMessage().deserialize(json.get("display_name").getAsString());
        this.description = MiniMessage.miniMessage().deserialize(json.get("description").getAsString());
        this.target = KeyTarget.valueOf(json.get("target").getAsString().toUpperCase(Locale.ROOT));
    }
}
