package com.floweytf.mechthon.engine;

import com.google.common.base.Preconditions;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import java.util.Collections;
import java.util.Map;
import net.kyori.adventure.key.Key;
import org.graalvm.polyglot.Value;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.UnmodifiableView;

public class ScriptInstance {
    public record Ticker(String name, Value callback, int delay) {
    }

    private final String name;

    @Nullable
    private Value main;
    private final Map<String, Value> triggerable = new Object2ObjectOpenHashMap<>(1);
    private final Map<String, Ticker> ticker = new Object2ObjectOpenHashMap<>(0);
    private boolean isFrozen;

    public ScriptInstance(String name) {
        this.name = name;
    }

    public String name() {
        return name;
    }

    public void registerMain(Value value) {
        Preconditions.checkArgument(value != null && value.canExecute());
        Preconditions.checkState(!isFrozen, "already frozen");
        Preconditions.checkState(
            main == null,
            "can't register multiple script entrypoints (only one method decorated with @main allowed!)"
        );
        main = value;
    }

    public void registerTicker(String name, int delay, Value value) {
        Preconditions.checkArgument(name != null && Key.parseableNamespace(name) && !name.isEmpty());
        Preconditions.checkArgument(delay > 0);
        Preconditions.checkArgument(value != null && value.canExecute());

        Preconditions.checkState(!isFrozen, "already frozen");
        Preconditions.checkArgument(
            ticker.putIfAbsent(name, new Ticker(name, value, delay)) == null,
            "can't register multiple tickers under the same name: '%s'", name
        );
    }

    public void registerTriggerable(String name, Value value) {
        Preconditions.checkArgument(name != null && Key.parseableNamespace(name) && !name.isEmpty());
        Preconditions.checkArgument(value != null && value.canExecute());

        Preconditions.checkState(!isFrozen, "already frozen");
        Preconditions.checkArgument(
            triggerable.putIfAbsent(name, value) == null,
            "can't register multiple triggerables under the same name: '%s'", name
        );
    }

    void freeze() {
        Preconditions.checkState(!isFrozen, "already frozen");
        isFrozen = true;
    }

    public @Nullable Value getMain() {
        return main;
    }

    @UnmodifiableView
    public Map<String, Ticker> getTickers() {
        return Collections.unmodifiableMap(ticker);
    }

    @UnmodifiableView
    public Map<String, Value> triggerables() {
        return Collections.unmodifiableMap(triggerable);
    }
}
