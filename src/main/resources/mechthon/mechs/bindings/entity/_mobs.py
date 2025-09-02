from enum import Enum, auto
from ._categories import Ambient, Breedable, Bucketable, Creature, RangedEntity, Shearable
from mechs.bindings.misc import InventoryHolder, Merchant
from mechs._internal import *
from mechs._internal.mirrors import *

class Bat(Ambient):
    @binding_constructor("org.bukkit.entity.Bat")
    def __init__(self, delegate: BukkitType):
        self._delegate = delegate

    # TODO: property target_location getTargetLocation setTargetLocation
    is_awake = RWProp[bool]("isAwake", "setAwake")

class WaterMob(Creature):
    @binding_constructor("org.bukkit.entity.WaterMob")
    def __init__(self, delegate: BukkitType):
        self._delegate = delegate


class Allay(Creature, InventoryHolder):
    @binding_constructor("org.bukkit.entity.Allay")
    def __init__(self, delegate: BukkitType):
        self._delegate = delegate

    # TODO: property jukebox getJukebox null
    duplication_cooldown = RWProp[int]("getDuplicationCooldown", "setDuplicationCooldown")
    can_duplicate = RWProp[bool]("canDuplicate", "setCanDuplicate")
    is_dancing = ROProp[bool]("isDancing")
    # TODO: method public abstract org.bukkit.entity.Allay org.bukkit.entity.Allay.duplicateAllay()
    def reset_duplication_cooldown(self): self._delegate.resetDuplicationCooldown()
    # TODO: method public abstract void org.bukkit.entity.Allay.startDancing(org.bukkit.Location)
    def start_dancing(self): self._delegate.startDancing()
    def stop_dancing(self): self._delegate.stopDancing()

class Golem(Creature):
    @binding_constructor("org.bukkit.entity.Golem")
    def __init__(self, delegate: BukkitType):
        self._delegate = delegate


class NPC(Creature):
    @binding_constructor("org.bukkit.entity.NPC")
    def __init__(self, delegate: BukkitType):
        self._delegate = delegate


class Squid(WaterMob):
    @binding_constructor("org.bukkit.entity.Squid")
    def __init__(self, delegate: BukkitType):
        self._delegate = delegate


class Fish(WaterMob, Bucketable):
    @binding_constructor("org.bukkit.entity.Fish")
    def __init__(self, delegate: BukkitType):
        self._delegate = delegate


class Dolphin(WaterMob):
    @binding_constructor("org.bukkit.entity.Dolphin")
    def __init__(self, delegate: BukkitType):
        self._delegate = delegate

    moistness = RWProp[int]("getMoistness", "setMoistness")
    has_fish = RWProp[bool]("hasFish", "setHasFish")
    # TODO: property treasure_location getTreasureLocation setTreasureLocation

class Snowman(Golem, RangedEntity, Shearable):
    @binding_constructor("org.bukkit.entity.Snowman")
    def __init__(self, delegate: BukkitType):
        self._delegate = delegate

    is_derp = RWProp[bool]("isDerp", "setDerp")

class IronGolem(Golem):
    @binding_constructor("org.bukkit.entity.IronGolem")
    def __init__(self, delegate: BukkitType):
        self._delegate = delegate

    is_player_created = RWProp[bool]("isPlayerCreated", "setPlayerCreated")

class AbstractVillager(Breedable, NPC, InventoryHolder, Merchant):
    @binding_constructor("org.bukkit.entity.AbstractVillager")
    def __init__(self, delegate: BukkitType):
        self._delegate = delegate

    def reset_offers(self): self._delegate.resetOffers()

class GlowSquid(Squid):
    @binding_constructor("org.bukkit.entity.GlowSquid")
    def __init__(self, delegate: BukkitType):
        self._delegate = delegate

    dark_ticks_remaining = RWProp[int]("getDarkTicksRemaining", "setDarkTicksRemaining")

class SchoolableFish(Fish):
    @binding_constructor("io.papermc.paper.entity.SchoolableFish")
    def __init__(self, delegate: BukkitType):
        self._delegate = delegate

    # TODO: property school_leader getSchoolLeader null
    school_size = ROProp[int]("getSchoolSize")
    max_school_size = ROProp[int]("getMaxSchoolSize")
    def stop_following(self): self._delegate.stopFollowing()
    # TODO: method public abstract void io.papermc.paper.entity.SchoolableFish.startFollowing(io.papermc.paper.entity.SchoolableFish)

class PufferFish(Fish):
    @binding_constructor("org.bukkit.entity.PufferFish")
    def __init__(self, delegate: BukkitType):
        self._delegate = delegate

    puff_state = RWProp[int]("getPuffState", "setPuffState")

class Tadpole(Fish):
    @binding_constructor("org.bukkit.entity.Tadpole")
    def __init__(self, delegate: BukkitType):
        self._delegate = delegate

    age_lock = RWProp[bool]("getAgeLock", "setAgeLock")
    age = RWProp[int]("getAge", "setAge")

class Villager(AbstractVillager):
    @binding_constructor("org.bukkit.entity.Villager")
    def __init__(self, delegate: BukkitType):
        self._delegate = delegate

    class Type(Enum):
        DESERT = auto()
        JUNGLE = auto()
        PLAINS = auto()
        SAVANNA = auto()
        SNOW = auto()
        SWAMP = auto()
        TAIGA = auto()

    _type_enum_mirror = EnumMirror(Type, "org.bukkit.entity.Villager$Type")
    villager_type = TransformedRWProp[Type]("getVillagerType", "setVillagerType", _type_enum_mirror.from_native, _type_enum_mirror.to_native)
    class Profession(Enum):
        NONE = auto()
        ARMORER = auto()
        BUTCHER = auto()
        CARTOGRAPHER = auto()
        CLERIC = auto()
        FARMER = auto()
        FISHERMAN = auto()
        FLETCHER = auto()
        LEATHERWORKER = auto()
        LIBRARIAN = auto()
        MASON = auto()
        NITWIT = auto()
        SHEPHERD = auto()
        TOOLSMITH = auto()
        WEAPONSMITH = auto()

    _profession_enum_mirror = EnumMirror(Profession, "org.bukkit.entity.Villager$Profession")
    profession = TransformedRWProp[Profession]("getProfession", "setProfession", _profession_enum_mirror.from_native, _profession_enum_mirror.to_native)
    villager_level = RWProp[int]("getVillagerLevel", "setVillagerLevel")
    # TODO: property reputations getReputations setReputations
    villager_experience = RWProp[int]("getVillagerExperience", "setVillagerExperience")
    restocks_today = RWProp[int]("getRestocksToday", "setRestocksToday")
    # TODO: method public abstract boolean org.bukkit.entity.Villager.sleep(org.bukkit.Location)
    def clear_reputations(self): self._delegate.clearReputations()
    def shake_head(self): self._delegate.shakeHead()
    # TODO: method public abstract com.destroystokyo.paper.entity.villager.Reputation org.bukkit.entity.Villager.getReputation(java.util.UUID)
    # TODO: method public abstract org.bukkit.entity.ZombieVillager org.bukkit.entity.Villager.zombify()
    def wakeup(self): self._delegate.wakeup()
    # TODO: method public abstract boolean org.bukkit.entity.Villager.increaseLevel(int)
    # TODO: method public abstract boolean org.bukkit.entity.Villager.addTrades(int)
    # TODO: method public abstract void org.bukkit.entity.Villager.setReputation(java.util.UUID,com.destroystokyo.paper.entity.villager.Reputation)

class WanderingTrader(AbstractVillager):
    @binding_constructor("org.bukkit.entity.WanderingTrader")
    def __init__(self, delegate: BukkitType):
        self._delegate = delegate

    # TODO: property wandering_towards getWanderingTowards setWanderingTowards
    despawn_delay = RWProp[int]("getDespawnDelay", "setDespawnDelay")
    can_drink_milk = RWProp[bool]("canDrinkMilk", "setCanDrinkMilk")
    can_drink_potion = RWProp[bool]("canDrinkPotion", "setCanDrinkPotion")

class Cod(SchoolableFish):
    @binding_constructor("org.bukkit.entity.Cod")
    def __init__(self, delegate: BukkitType):
        self._delegate = delegate


class Salmon(SchoolableFish):
    @binding_constructor("org.bukkit.entity.Salmon")
    def __init__(self, delegate: BukkitType):
        self._delegate = delegate


class TropicalFish(SchoolableFish):
    @binding_constructor("org.bukkit.entity.TropicalFish")
    def __init__(self, delegate: BukkitType):
        self._delegate = delegate

    class Pattern(Enum):
        KOB = auto()
        SUNSTREAK = auto()
        SNOOPER = auto()
        DASHER = auto()
        BRINELY = auto()
        SPOTTY = auto()
        FLOPPER = auto()
        STRIPEY = auto()
        GLITTER = auto()
        BLOCKFISH = auto()
        BETTY = auto()
        CLAYFISH = auto()

    _pattern_enum_mirror = EnumMirror(Pattern, "org.bukkit.entity.TropicalFish$Pattern")
    pattern = TransformedRWProp[Pattern]("getPattern", "setPattern", _pattern_enum_mirror.from_native, _pattern_enum_mirror.to_native)
    # TODO: property body_color getBodyColor setBodyColor
    # TODO: property pattern_color getPatternColor setPatternColor
