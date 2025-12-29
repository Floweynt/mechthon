package com.floweytf.mechthon.state.world;

import com.floweytf.mechthon.MechthonPlugin;
import com.floweytf.mechthon.util.Util;
import com.google.gson.Gson;
import org.bukkit.NamespacedKey;
import org.bukkit.World;
import org.bukkit.persistence.PersistentDataType;

public class WorldState {
    private static final NamespacedKey PDC_KEY = Util.key("data/v1");
    private static final Gson GSON = new Gson();

    private final MechthonPlugin plugin;
    private final World world;

    public WorldState(MechthonPlugin plugin, World world) {
        this.plugin = plugin;
        this.world = world;
    }

    public void save() {
        world.getPersistentDataContainer().set(
            PDC_KEY,
            PersistentDataType.STRING,
            ""
        );
    }

    public boolean isDirty() {
        return false;
    }
}
