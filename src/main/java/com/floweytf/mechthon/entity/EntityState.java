package com.floweytf.mechthon.entity;

import com.floweytf.mechthon.MechthonPlugin;
import com.floweytf.mechthon.engine.ScriptEngine;
import com.floweytf.mechthon.engine.ScriptInstance;
import com.google.gson.Gson;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.PriorityQueue;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Entity;
import org.bukkit.persistence.PersistentDataType;

public class EntityState {
    private static final NamespacedKey PDC_KEY = new NamespacedKey("mecthon", "data/v1");
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
