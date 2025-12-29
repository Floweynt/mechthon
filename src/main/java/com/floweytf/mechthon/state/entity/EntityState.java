package com.floweytf.mechthon.state.entity;

import com.floweytf.mechthon.MechthonPlugin;
import com.floweytf.mechthon.util.Util;
import com.google.gson.Gson;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Entity;

public class EntityState {
    private static final NamespacedKey PDC_KEY = Util.key("data/v1");
    private static final Gson GSON = new Gson();

    private final MechthonPlugin plugin;
    private final Entity entity;

    public EntityState(MechthonPlugin plugin, Entity entity) {
        this.plugin = plugin;
        this.entity = entity;
    }

    public void save() {
    }
}
