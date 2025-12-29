package com.floweytf.mechthon.state.world;

import com.destroystokyo.paper.event.entity.EntityAddToWorldEvent;
import com.floweytf.mechthon.MechthonPlugin;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import java.util.Map;
import org.bukkit.World;
import org.bukkit.event.EventHandler;
import org.bukkit.event.world.WorldSaveEvent;
import org.bukkit.event.world.WorldUnloadEvent;

public class WorldManager {
    private final MechthonPlugin plugin;
    private final Map<World, WorldState> worlds = new Object2ObjectOpenHashMap<>();

    public WorldManager(MechthonPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onLoad(EntityAddToWorldEvent event) {
        worlds.put(event.getWorld(), new WorldState(plugin, event.getWorld()));
    }

    @EventHandler
    public void onSave(WorldSaveEvent event) {
        final var state = worlds.get(event.getWorld());
        if (state == null) {
            plugin.getSLF4JLogger().warn(
                "world saved called on world that was either never added or removed: {}",
                event.getWorld().getUID()
            );
        }
    }

    @EventHandler
    public void onRemove(WorldUnloadEvent event) {
        final var state = worlds.remove(event.getWorld());
        if (state.isDirty()) {
            throw new IllegalStateException();
        }
    }
}
