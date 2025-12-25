package com.floweytf.mechthon.entity;

import com.floweytf.mechthon.MechthonPlugin;
import com.floweytf.mechthon.engine.ScriptEngine;
import com.floweytf.mechthon.engine.ScriptInstance;
import com.google.gson.Gson;
import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Entity;
import org.bukkit.persistence.PersistentDataType;

public class EntityState {
    private record TickTaskEntry(ScriptInstance script, ScriptInstance.Ticker ticker, int nextRunTime) {
        public TickTaskEntry doTask(Entity entity, ScriptEngine engine) {
            if (!engine.isClosed()) {
                engine.invokeTicker(entity, ticker);
            }
            return new TickTaskEntry(script, ticker, nextRunTime + ticker.delay());
        }

        public String serializedKey() {
            return "%s:%s".formatted(script.name(), ticker.name());
        }
    }

    private static final NamespacedKey PDC_KEY = new NamespacedKey("mecthon", "data/v1");
    private static final Gson GSON = new Gson();

    private static final class EntityData {
        private final Map<String, Integer> delays = new HashMap<>();
    }

    private final MechthonPlugin plugin;
    private final Entity entity;
    private final PriorityQueue<TickTaskEntry> queue = new PriorityQueue<>();
    private int currTime = 0;

    private void loadPersistentTickers(EntityData data) {
        final var scripts = plugin.engine().scriptManager().scripts();

        for (final var entry : data.delays.entrySet()) {
            final var name = entry.getKey();
            final var time = entry.getValue();

            final var parts = name.split(":");

            if (parts.length != 2) {
                plugin.getSLF4JLogger().warn(
                    "illegal key `{}` in  entity ticker metadata for entity {}",
                    name, entity.getUniqueId()
                );

                continue;
            }

            final var scriptName = parts[0];
            final var tickerName = parts[1];
            final var script = scripts.get(scriptName);

            if (script == null) {
                plugin.getSLF4JLogger().warn(
                    "entity {} references script `{}` for a persistent ticker, but the script was not found!",
                    entity.getUniqueId(), scriptName
                );

                continue;
            }

            final var ticker = script.getTickers().get(tickerName);

            if (ticker == null) {
                plugin.getSLF4JLogger().warn(
                    "entity {} references persistent ticker `{}` in script `{}`, but it was not found!",
                    entity.getUniqueId(), tickerName, scriptName
                );
            }

            this.queue.add(new TickTaskEntry(script, ticker, time));
        }
    }

    private void loadData() {
        final var pdc = entity.getPersistentDataContainer();

        if (pdc.has(PDC_KEY)) {
            // deserialize from json, if the data key is present
            final var data = GSON.fromJson(
                pdc.get(PDC_KEY, PersistentDataType.STRING),
                EntityData.class
            );

            loadPersistentTickers(data);
        }
    }

    public EntityState(MechthonPlugin plugin, Entity entity) {
        this.plugin = plugin;
        this.entity = entity;

        // schedule tasks
        entity.getScheduler().runAtFixedRate(plugin, x -> onTick(), () -> {
        }, 1, 1);

        // load stuff
        loadData();
    }

    private void onTick() {
        while (!queue.isEmpty() && queue.peek().nextRunTime() == currTime) {
            queue.add(queue.poll().doTask(entity, plugin.engine()));
        }

        currTime++;
    }

    public void save() {
        final var data = new EntityData();

        for (final var tickTaskEntry : queue) {
            data.delays.put(tickTaskEntry.serializedKey(), tickTaskEntry.nextRunTime() - currTime);
        }

        entity.getPersistentDataContainer().set(
            PDC_KEY,
            PersistentDataType.STRING,
            GSON.toJson(data)
        );
    }
}
