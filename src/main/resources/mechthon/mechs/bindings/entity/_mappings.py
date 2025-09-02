from typing import Type

from ._entity import Entity
from mechs._internal import BukkitType

_is_init = False
_entity_type_to_constructor: dict[BukkitType, Type]

def _init():
    global _is_init
    global _entity_type_to_constructor

    if _is_init:
        return

    _is_init = True

    from ._display import ItemDisplay, BlockDisplay, Interaction, TextDisplay
    from ._player import Player
    from ._hostile import (
        Husk, ZombieVillager, Blaze, Breeze, CaveSpider, Creeper, Drowned, EnderDragon, Enderman, Endermite, Evoker, Ghast,
        Giant, Guardian, Hoglin, Illusioner, MagmaCube, Phantom, PigZombie, Piglin, PiglinBrute, Pillager, Ravager, Shulker, 
        Silverfish, Skeleton, Slime, Spider, Vex, Vindicator, Warden, Witch, Wither, Zoglin, Zombie, ElderGuardian, WitherSkeleton,
        Stray
    )
    from ._misc import (
        Item, ExperienceOrb, AreaEffectCloud, EnderSignal, EvokerFangs, FallingBlock, ItemFrame, LeashHitch, Painting, TNTPrimed,
        EnderCrystal, GlowItemFrame, LightningStrike, Marker
    )
    from ._projectiles import (
        Egg, Arrow, DragonFireball, EnderPearl, Firework, LargeFireball, ShulkerBullet, SmallFireball, Snowball, SpectralArrow, 
        ThrownExpBottle, ThrownPotion, WitherSkull, FishHook, LlamaSpit, Trident, WindCharge
    )
    from ._living import ArmorStand
    from ._entity_type import EntityType, _ENTITY_TYPE_ENUM_MIRROR
    from ._animal import (
        SkeletonHorse, ZombieHorse, Donkey, Mule, Axolotl, Bee, Camel, Cat, Chicken, Cow, Fox, Frog, Goat, Horse, Llama, MushroomCow,
        Ocelot, Panda, Parrot, Pig, PolarBear, Rabbit, Sheep, Sniffer, Strider, TraderLlama, Turtle, Wolf
    )
    from ._minecart import (
        CommandMinecart, RideableMinecart, StorageMinecart, PoweredMinecart, ExplosiveMinecart, HopperMinecart, SpawnerMinecart
    )
    from ._mobs import (
        Allay, Bat, Cod, Dolphin, GlowSquid, IronGolem, PufferFish, Salmon, Snowman, Squid, Tadpole, TropicalFish, Villager, WanderingTrader
    )
    from ._boat import Boat, ChestBoat
    from ._entity import Entity

    _entity_type_to_constructor = {
        _ENTITY_TYPE_ENUM_MIRROR.to_native(EntityType.DROPPED_ITEM): Item,
        _ENTITY_TYPE_ENUM_MIRROR.to_native(EntityType.EXPERIENCE_ORB): ExperienceOrb,
        _ENTITY_TYPE_ENUM_MIRROR.to_native(EntityType.AREA_EFFECT_CLOUD): AreaEffectCloud,
        _ENTITY_TYPE_ENUM_MIRROR.to_native(EntityType.ELDER_GUARDIAN): ElderGuardian,
        _ENTITY_TYPE_ENUM_MIRROR.to_native(EntityType.WITHER_SKELETON): WitherSkeleton,
        _ENTITY_TYPE_ENUM_MIRROR.to_native(EntityType.STRAY): Stray,
        _ENTITY_TYPE_ENUM_MIRROR.to_native(EntityType.EGG): Egg,
        _ENTITY_TYPE_ENUM_MIRROR.to_native(EntityType.LEASH_HITCH): LeashHitch,
        _ENTITY_TYPE_ENUM_MIRROR.to_native(EntityType.PAINTING): Painting,
        _ENTITY_TYPE_ENUM_MIRROR.to_native(EntityType.ARROW): Arrow,
        _ENTITY_TYPE_ENUM_MIRROR.to_native(EntityType.SNOWBALL): Snowball,
        _ENTITY_TYPE_ENUM_MIRROR.to_native(EntityType.FIREBALL): LargeFireball,
        _ENTITY_TYPE_ENUM_MIRROR.to_native(EntityType.SMALL_FIREBALL): SmallFireball,
        _ENTITY_TYPE_ENUM_MIRROR.to_native(EntityType.ENDER_PEARL): EnderPearl,
        _ENTITY_TYPE_ENUM_MIRROR.to_native(EntityType.ENDER_SIGNAL): EnderSignal,
        _ENTITY_TYPE_ENUM_MIRROR.to_native(EntityType.SPLASH_POTION): ThrownPotion,
        _ENTITY_TYPE_ENUM_MIRROR.to_native(EntityType.THROWN_EXP_BOTTLE): ThrownExpBottle,
        _ENTITY_TYPE_ENUM_MIRROR.to_native(EntityType.ITEM_FRAME): ItemFrame,
        _ENTITY_TYPE_ENUM_MIRROR.to_native(EntityType.WITHER_SKULL): WitherSkull,
        _ENTITY_TYPE_ENUM_MIRROR.to_native(EntityType.PRIMED_TNT): TNTPrimed,
        _ENTITY_TYPE_ENUM_MIRROR.to_native(EntityType.FALLING_BLOCK): FallingBlock,
        _ENTITY_TYPE_ENUM_MIRROR.to_native(EntityType.FIREWORK): Firework,
        _ENTITY_TYPE_ENUM_MIRROR.to_native(EntityType.HUSK): Husk,
        _ENTITY_TYPE_ENUM_MIRROR.to_native(EntityType.SPECTRAL_ARROW): SpectralArrow,
        _ENTITY_TYPE_ENUM_MIRROR.to_native(EntityType.SHULKER_BULLET): ShulkerBullet,
        _ENTITY_TYPE_ENUM_MIRROR.to_native(EntityType.DRAGON_FIREBALL): DragonFireball,
        _ENTITY_TYPE_ENUM_MIRROR.to_native(EntityType.ZOMBIE_VILLAGER): ZombieVillager,
        _ENTITY_TYPE_ENUM_MIRROR.to_native(EntityType.SKELETON_HORSE): SkeletonHorse,
        _ENTITY_TYPE_ENUM_MIRROR.to_native(EntityType.ZOMBIE_HORSE): ZombieHorse,
        _ENTITY_TYPE_ENUM_MIRROR.to_native(EntityType.ARMOR_STAND): ArmorStand,
        _ENTITY_TYPE_ENUM_MIRROR.to_native(EntityType.DONKEY): Donkey,
        _ENTITY_TYPE_ENUM_MIRROR.to_native(EntityType.MULE): Mule,
        _ENTITY_TYPE_ENUM_MIRROR.to_native(EntityType.EVOKER_FANGS): EvokerFangs,
        _ENTITY_TYPE_ENUM_MIRROR.to_native(EntityType.EVOKER): Evoker,
        _ENTITY_TYPE_ENUM_MIRROR.to_native(EntityType.VEX): Vex,
        _ENTITY_TYPE_ENUM_MIRROR.to_native(EntityType.VINDICATOR): Vindicator,
        _ENTITY_TYPE_ENUM_MIRROR.to_native(EntityType.ILLUSIONER): Illusioner,
        _ENTITY_TYPE_ENUM_MIRROR.to_native(EntityType.MINECART_COMMAND): CommandMinecart,
        _ENTITY_TYPE_ENUM_MIRROR.to_native(EntityType.BOAT): Boat,
        _ENTITY_TYPE_ENUM_MIRROR.to_native(EntityType.MINECART): RideableMinecart,
        _ENTITY_TYPE_ENUM_MIRROR.to_native(EntityType.MINECART_CHEST): StorageMinecart,
        _ENTITY_TYPE_ENUM_MIRROR.to_native(EntityType.MINECART_FURNACE): PoweredMinecart,
        _ENTITY_TYPE_ENUM_MIRROR.to_native(EntityType.MINECART_TNT): ExplosiveMinecart,
        _ENTITY_TYPE_ENUM_MIRROR.to_native(EntityType.MINECART_HOPPER): HopperMinecart,
        _ENTITY_TYPE_ENUM_MIRROR.to_native(EntityType.MINECART_MOB_SPAWNER): SpawnerMinecart,
        _ENTITY_TYPE_ENUM_MIRROR.to_native(EntityType.CREEPER): Creeper,
        _ENTITY_TYPE_ENUM_MIRROR.to_native(EntityType.SKELETON): Skeleton,
        _ENTITY_TYPE_ENUM_MIRROR.to_native(EntityType.SPIDER): Spider,
        _ENTITY_TYPE_ENUM_MIRROR.to_native(EntityType.GIANT): Giant,
        _ENTITY_TYPE_ENUM_MIRROR.to_native(EntityType.ZOMBIE): Zombie,
        _ENTITY_TYPE_ENUM_MIRROR.to_native(EntityType.SLIME): Slime,
        _ENTITY_TYPE_ENUM_MIRROR.to_native(EntityType.GHAST): Ghast,
        _ENTITY_TYPE_ENUM_MIRROR.to_native(EntityType.ZOMBIFIED_PIGLIN): PigZombie,
        _ENTITY_TYPE_ENUM_MIRROR.to_native(EntityType.ENDERMAN): Enderman,
        _ENTITY_TYPE_ENUM_MIRROR.to_native(EntityType.CAVE_SPIDER): CaveSpider,
        _ENTITY_TYPE_ENUM_MIRROR.to_native(EntityType.SILVERFISH): Silverfish,
        _ENTITY_TYPE_ENUM_MIRROR.to_native(EntityType.BLAZE): Blaze,
        _ENTITY_TYPE_ENUM_MIRROR.to_native(EntityType.MAGMA_CUBE): MagmaCube,
        _ENTITY_TYPE_ENUM_MIRROR.to_native(EntityType.ENDER_DRAGON): EnderDragon,
        _ENTITY_TYPE_ENUM_MIRROR.to_native(EntityType.WITHER): Wither,
        _ENTITY_TYPE_ENUM_MIRROR.to_native(EntityType.BAT): Bat,
        _ENTITY_TYPE_ENUM_MIRROR.to_native(EntityType.WITCH): Witch,
        _ENTITY_TYPE_ENUM_MIRROR.to_native(EntityType.ENDERMITE): Endermite,
        _ENTITY_TYPE_ENUM_MIRROR.to_native(EntityType.GUARDIAN): Guardian,
        _ENTITY_TYPE_ENUM_MIRROR.to_native(EntityType.SHULKER): Shulker,
        _ENTITY_TYPE_ENUM_MIRROR.to_native(EntityType.PIG): Pig,
        _ENTITY_TYPE_ENUM_MIRROR.to_native(EntityType.SHEEP): Sheep,
        _ENTITY_TYPE_ENUM_MIRROR.to_native(EntityType.COW): Cow,
        _ENTITY_TYPE_ENUM_MIRROR.to_native(EntityType.CHICKEN): Chicken,
        _ENTITY_TYPE_ENUM_MIRROR.to_native(EntityType.SQUID): Squid,
        _ENTITY_TYPE_ENUM_MIRROR.to_native(EntityType.WOLF): Wolf,
        _ENTITY_TYPE_ENUM_MIRROR.to_native(EntityType.MUSHROOM_COW): MushroomCow,
        _ENTITY_TYPE_ENUM_MIRROR.to_native(EntityType.SNOWMAN): Snowman,
        _ENTITY_TYPE_ENUM_MIRROR.to_native(EntityType.OCELOT): Ocelot,
        _ENTITY_TYPE_ENUM_MIRROR.to_native(EntityType.IRON_GOLEM): IronGolem,
        _ENTITY_TYPE_ENUM_MIRROR.to_native(EntityType.HORSE): Horse,
        _ENTITY_TYPE_ENUM_MIRROR.to_native(EntityType.RABBIT): Rabbit,
        _ENTITY_TYPE_ENUM_MIRROR.to_native(EntityType.POLAR_BEAR): PolarBear,
        _ENTITY_TYPE_ENUM_MIRROR.to_native(EntityType.LLAMA): Llama,
        _ENTITY_TYPE_ENUM_MIRROR.to_native(EntityType.LLAMA_SPIT): LlamaSpit,
        _ENTITY_TYPE_ENUM_MIRROR.to_native(EntityType.PARROT): Parrot,
        _ENTITY_TYPE_ENUM_MIRROR.to_native(EntityType.VILLAGER): Villager,
        _ENTITY_TYPE_ENUM_MIRROR.to_native(EntityType.ENDER_CRYSTAL): EnderCrystal,
        _ENTITY_TYPE_ENUM_MIRROR.to_native(EntityType.TURTLE): Turtle,
        _ENTITY_TYPE_ENUM_MIRROR.to_native(EntityType.PHANTOM): Phantom,
        _ENTITY_TYPE_ENUM_MIRROR.to_native(EntityType.TRIDENT): Trident,
        _ENTITY_TYPE_ENUM_MIRROR.to_native(EntityType.COD): Cod,
        _ENTITY_TYPE_ENUM_MIRROR.to_native(EntityType.SALMON): Salmon,
        _ENTITY_TYPE_ENUM_MIRROR.to_native(EntityType.PUFFERFISH): PufferFish,
        _ENTITY_TYPE_ENUM_MIRROR.to_native(EntityType.TROPICAL_FISH): TropicalFish,
        _ENTITY_TYPE_ENUM_MIRROR.to_native(EntityType.DROWNED): Drowned,
        _ENTITY_TYPE_ENUM_MIRROR.to_native(EntityType.DOLPHIN): Dolphin,
        _ENTITY_TYPE_ENUM_MIRROR.to_native(EntityType.CAT): Cat,
        _ENTITY_TYPE_ENUM_MIRROR.to_native(EntityType.PANDA): Panda,
        _ENTITY_TYPE_ENUM_MIRROR.to_native(EntityType.PILLAGER): Pillager,
        _ENTITY_TYPE_ENUM_MIRROR.to_native(EntityType.RAVAGER): Ravager,
        _ENTITY_TYPE_ENUM_MIRROR.to_native(EntityType.TRADER_LLAMA): TraderLlama,
        _ENTITY_TYPE_ENUM_MIRROR.to_native(EntityType.WANDERING_TRADER): WanderingTrader,
        _ENTITY_TYPE_ENUM_MIRROR.to_native(EntityType.FOX): Fox,
        _ENTITY_TYPE_ENUM_MIRROR.to_native(EntityType.BEE): Bee,
        _ENTITY_TYPE_ENUM_MIRROR.to_native(EntityType.HOGLIN): Hoglin,
        _ENTITY_TYPE_ENUM_MIRROR.to_native(EntityType.PIGLIN): Piglin,
        _ENTITY_TYPE_ENUM_MIRROR.to_native(EntityType.STRIDER): Strider,
        _ENTITY_TYPE_ENUM_MIRROR.to_native(EntityType.ZOGLIN): Zoglin,
        _ENTITY_TYPE_ENUM_MIRROR.to_native(EntityType.PIGLIN_BRUTE): PiglinBrute,
        _ENTITY_TYPE_ENUM_MIRROR.to_native(EntityType.AXOLOTL): Axolotl,
        _ENTITY_TYPE_ENUM_MIRROR.to_native(EntityType.GLOW_ITEM_FRAME): GlowItemFrame,
        _ENTITY_TYPE_ENUM_MIRROR.to_native(EntityType.GLOW_SQUID): GlowSquid,
        _ENTITY_TYPE_ENUM_MIRROR.to_native(EntityType.GOAT): Goat,
        _ENTITY_TYPE_ENUM_MIRROR.to_native(EntityType.MARKER): Marker,
        _ENTITY_TYPE_ENUM_MIRROR.to_native(EntityType.ALLAY): Allay,
        _ENTITY_TYPE_ENUM_MIRROR.to_native(EntityType.CHEST_BOAT): ChestBoat,
        _ENTITY_TYPE_ENUM_MIRROR.to_native(EntityType.FROG): Frog,
        _ENTITY_TYPE_ENUM_MIRROR.to_native(EntityType.TADPOLE): Tadpole,
        _ENTITY_TYPE_ENUM_MIRROR.to_native(EntityType.WARDEN): Warden,
        _ENTITY_TYPE_ENUM_MIRROR.to_native(EntityType.CAMEL): Camel,
        _ENTITY_TYPE_ENUM_MIRROR.to_native(EntityType.BLOCK_DISPLAY): BlockDisplay,
        _ENTITY_TYPE_ENUM_MIRROR.to_native(EntityType.INTERACTION): Interaction,
        _ENTITY_TYPE_ENUM_MIRROR.to_native(EntityType.ITEM_DISPLAY): ItemDisplay,
        _ENTITY_TYPE_ENUM_MIRROR.to_native(EntityType.SNIFFER): Sniffer,
        _ENTITY_TYPE_ENUM_MIRROR.to_native(EntityType.TEXT_DISPLAY): TextDisplay,
        _ENTITY_TYPE_ENUM_MIRROR.to_native(EntityType.BREEZE): Breeze,
        _ENTITY_TYPE_ENUM_MIRROR.to_native(EntityType.WIND_CHARGE): WindCharge,
        _ENTITY_TYPE_ENUM_MIRROR.to_native(EntityType.FISHING_HOOK): FishHook,
        _ENTITY_TYPE_ENUM_MIRROR.to_native(EntityType.LIGHTNING): LightningStrike,
        _ENTITY_TYPE_ENUM_MIRROR.to_native(EntityType.PLAYER): Player,
        _ENTITY_TYPE_ENUM_MIRROR.to_native(EntityType.UNKNOWN): Entity,
    }

def wrap_entity(ty: BukkitType) -> Entity:
    _init()
    return _entity_type_to_constructor[ty.getType()](ty)

