package com.floweytf.mechthon.codegen;

import com.destroystokyo.paper.entity.RangedEntity;
import io.papermc.paper.entity.Shearable;
import java.util.Set;
import java.util.function.Predicate;
import org.bukkit.RegionAccessor;
import org.bukkit.World;
import org.bukkit.entity.Ageable;
import org.bukkit.entity.Ambient;
import org.bukkit.entity.Animals;
import org.bukkit.entity.Boat;
import org.bukkit.entity.Boss;
import org.bukkit.entity.Breedable;
import org.bukkit.entity.Creature;
import org.bukkit.entity.Damageable;
import org.bukkit.entity.Display;
import org.bukkit.entity.Enemy;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Explosive;
import org.bukkit.entity.Flying;
import org.bukkit.entity.Interaction;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Minecart;
import org.bukkit.entity.Mob;
import org.bukkit.entity.Monster;
import org.bukkit.entity.Projectile;
import org.bukkit.entity.Steerable;
import org.bukkit.entity.Tameable;
import org.bukkit.generator.WorldInfo;

public enum Packaging {
    CATEGORIES(
        "entity/_categories.py",
        Set.of(
            Ageable.class, Creature.class, Breedable.class,
            Ambient.class, RangedEntity.class, Boss.class,
            Explosive.class, Flying.class, Tameable.class,
            Animals.class, Steerable.class, Enemy.class, Monster.class,
            Shearable.class
        )::contains
    ),
    BOAT("entity/_boat.py", Boat.class::isAssignableFrom),
    MINECART("entity/_minecart.py", Minecart.class::isAssignableFrom),
    HOSTILE("entity/_hostile.py", Enemy.class::isAssignableFrom),
    DISPLAY("entity/_display.py", x -> x == Interaction.class || Display.class.isAssignableFrom(x)),
    PROJECTILES("entity/_projectiles.py", Projectile.class::isAssignableFrom),
    ANIMALS("entity/_animal.py", Animals.class::isAssignableFrom),
    MOB("entity/_mobs.py", Mob.class::isAssignableFrom),
    LIVING_BASE("entity/_living_base.py", x -> x == LivingEntity.class || x == Damageable.class),
    LIVING("entity/_living.py", LivingEntity.class::isAssignableFrom),
    ENTITY("entity/_entity.py", Entity.class::equals),
    ENTITY_MISC("entity/_misc.py", Entity.class::isAssignableFrom),
    WORLD("world.py", Set.of(
        World.class, WorldInfo.class, RegionAccessor.class
    )::contains),
    MISC("misc.py", misc -> true);

    public final String path;
    public final Predicate<Class<?>> matcher;

    Packaging(String path, Predicate<Class<?>> matcher) {
        this.path = path;
        this.matcher = matcher;
    }

    public static Packaging match(Class<?> clazz) {
        for (final var value : values()) {
            if (value.matcher.test(clazz)) {
                return value;
            }
        }

        return MISC;
    }
}
