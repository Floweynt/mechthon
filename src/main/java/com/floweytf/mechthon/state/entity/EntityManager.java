package com.floweytf.mechthon.state.entity;

import com.destroystokyo.paper.event.entity.EntityAddToWorldEvent;
import com.destroystokyo.paper.event.entity.EntityRemoveFromWorldEvent;
import com.floweytf.mechthon.MechthonPlugin;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import java.util.HashMap;
import java.util.Map;
import org.bukkit.entity.Entity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class EntityManager implements Listener {
    private final MechthonPlugin plugin;
    private final Map<Entity, EntityState> entities = new Object2ObjectOpenHashMap<>();

    public EntityManager(MechthonPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onLoad(EntityAddToWorldEvent event) {
        entities.put(event.getEntity(), new EntityState(plugin, event.getEntity()));
    }

    @EventHandler
    public void onRemove(EntityRemoveFromWorldEvent event) {
        final var state = entities.remove(event.getEntity());
        if (state == null) {
            plugin.getSLF4JLogger().warn("entity removed but was never added: {}", event.getEntity().getEntityId());
        } else {
            state.save();
        }
    }
}
