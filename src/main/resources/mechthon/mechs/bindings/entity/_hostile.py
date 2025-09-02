from enum import Enum, auto

from ._mobs import Golem
from mechs.bindings.misc import Colorable, InventoryHolder
from mechs._internal import *
from mechs._internal.mirrors import *
from ._categories import *

class AbstractSkeleton(Monster, RangedEntity):
    @binding_constructor("org.bukkit.entity.AbstractSkeleton")
    def __init__(self, delegate: BukkitType):
        self._delegate = delegate

    should_burn_in_day = RWProp[bool]("shouldBurnInDay", "setShouldBurnInDay")

class Endermite(Monster):
    @binding_constructor("org.bukkit.entity.Endermite")
    def __init__(self, delegate: BukkitType):
        self._delegate = delegate

    lifetime_ticks = RWProp[int]("getLifetimeTicks", "setLifetimeTicks")

class Slime(Mob, Enemy):
    @binding_constructor("org.bukkit.entity.Slime")
    def __init__(self, delegate: BukkitType):
        self._delegate = delegate

    size = RWProp[int]("getSize", "setSize")
    can_wander = RWProp[bool]("canWander", "setWander")

class Shulker(Golem, Colorable, Enemy):
    @binding_constructor("org.bukkit.entity.Shulker")
    def __init__(self, delegate: BukkitType):
        self._delegate = delegate

    # TODO: property attached_face getAttachedFace setAttachedFace
    peek = RWProp[float]("getPeek", "setPeek")

class PiglinAbstract(Monster, Ageable):
    @binding_constructor("org.bukkit.entity.PiglinAbstract")
    def __init__(self, delegate: BukkitType):
        self._delegate = delegate

    conversion_time = RWProp[int]("getConversionTime", "setConversionTime")
    is_converting = ROProp[bool]("isConverting")
    is_immune_to_zombification = RWProp[bool]("isImmuneToZombification", "setImmuneToZombification")

class Breeze(Monster):
    @binding_constructor("org.bukkit.entity.Breeze")
    def __init__(self, delegate: BukkitType):
        self._delegate = delegate

class Creeper(Monster):
    @binding_constructor("org.bukkit.entity.Creeper")
    def __init__(self, delegate: BukkitType):
        self._delegate = delegate

    fuse_ticks = RWProp[int]("getFuseTicks", "setFuseTicks")
    is_ignited = RWProp[bool]("isIgnited", "setIgnited")
    max_fuse_ticks = RWProp[int]("getMaxFuseTicks", "setMaxFuseTicks")
    explosion_radius = RWProp[int]("getExplosionRadius", "setExplosionRadius")
    is_powered = RWProp[bool]("isPowered", "setPowered")
    def explode(self): self._delegate.explode()
    def ignite(self): self._delegate.ignite()

class Giant(Monster):
    @binding_constructor("org.bukkit.entity.Giant")
    def __init__(self, delegate: BukkitType):
        self._delegate = delegate

class Enderman(Monster):
    @binding_constructor("org.bukkit.entity.Enderman")
    def __init__(self, delegate: BukkitType):
        self._delegate = delegate

    has_been_stared_at = RWProp[bool]("hasBeenStaredAt", "setHasBeenStaredAt")
    # TODO: property carried_block getCarriedBlock setCarriedBlock
    is_screaming = RWProp[bool]("isScreaming", "setScreaming")
    # TODO: method public abstract boolean org.bukkit.entity.Enderman.teleportRandomly()
    # TODO: method public abstract boolean org.bukkit.entity.Enderman.teleportTowards(org.bukkit.entity.Entity)
    # TODO: method public abstract boolean org.bukkit.entity.Enderman.teleport()

class Warden(Monster):
    @binding_constructor("org.bukkit.entity.Warden")
    def __init__(self, delegate: BukkitType):
        self._delegate = delegate

    # TODO: property entity_angry_at getEntityAngryAt null
    anger = ROProp[int]("getAnger")
    highest_anger = ROProp[int]("getHighestAnger")
    class AngerLevel(Enum):
        CALM = auto()
        AGITATED = auto()
        ANGRY = auto()

    _anger_level_enum_mirror = EnumMirror(AngerLevel, "org.bukkit.entity.Warden$AngerLevel")
    anger_level = TransformedROProp[AngerLevel]("getAngerLevel", _anger_level_enum_mirror.from_native)
    # TODO: method public abstract void org.bukkit.entity.Warden.setAnger(org.bukkit.entity.Entity,int)
    # TODO: method public abstract void org.bukkit.entity.Warden.clearAnger(org.bukkit.entity.Entity)
    # TODO: method public abstract void org.bukkit.entity.Warden.increaseAnger(org.bukkit.entity.Entity,int)
    # TODO: method public abstract int org.bukkit.entity.Warden.getAnger(org.bukkit.entity.Entity)
    # TODO: method public abstract void org.bukkit.entity.Warden.setDisturbanceLocation(org.bukkit.Location)

class Spider(Monster):
    @binding_constructor("org.bukkit.entity.Spider")
    def __init__(self, delegate: BukkitType):
        self._delegate = delegate

class Blaze(Monster):
    @binding_constructor("org.bukkit.entity.Blaze")
    def __init__(self, delegate: BukkitType):
        self._delegate = delegate

class Phantom(Flying, Enemy):
    @binding_constructor("org.bukkit.entity.Phantom")
    def __init__(self, delegate: BukkitType):
        self._delegate = delegate

    size = RWProp[int]("getSize", "setSize")
    should_burn_in_day = RWProp[bool]("shouldBurnInDay", "setShouldBurnInDay")
    spawning_entity = TransformedROProp[Optional[UUID]]("getSpawningEntity", lambda x: None if x is None else java_uuid_to_python(x))
    # TODO: property anchor_location getAnchorLocation setAnchorLocation

class Zoglin(Monster, Ageable):
    @binding_constructor("org.bukkit.entity.Zoglin")
    def __init__(self, delegate: BukkitType):
        self._delegate = delegate


class Vex(Monster):
    @binding_constructor("org.bukkit.entity.Vex")
    def __init__(self, delegate: BukkitType):
        self._delegate = delegate

    # TODO: property summoner getSummoner setSummoner
    has_limited_lifetime = RWProp[bool]("hasLimitedLifetime", "setLimitedLifetime")
    is_charging = RWProp[bool]("isCharging", "setCharging")
    # TODO: property bound getBound setBound
    limited_lifetime_ticks = RWProp[int]("getLimitedLifetimeTicks", "setLimitedLifetimeTicks")

class Guardian(Monster):
    @binding_constructor("org.bukkit.entity.Guardian")
    def __init__(self, delegate: BukkitType):
        self._delegate = delegate

    is_moving = ROProp[bool]("isMoving")
    has_laser = ROProp[bool]("hasLaser")
    laser_ticks = RWProp[int]("getLaserTicks", "setLaserTicks")
    laser_duration = ROProp[int]("getLaserDuration")
    # TODO: method public abstract boolean org.bukkit.entity.Guardian.setLaser(boolean)

class Ghast(Flying, Enemy):
    @binding_constructor("org.bukkit.entity.Ghast")
    def __init__(self, delegate: BukkitType):
        self._delegate = delegate

    is_charging = RWProp[bool]("isCharging", "setCharging")
    explosion_power = RWProp[int]("getExplosionPower", "setExplosionPower")

class Wither(Monster, Boss, RangedEntity):
    @binding_constructor("org.bukkit.entity.Wither")
    def __init__(self, delegate: BukkitType):
        self._delegate = delegate

    can_travel_through_portals = RWProp[bool]("canTravelThroughPortals", "setCanTravelThroughPortals")
    is_charged = ROProp[bool]("isCharged")
    invulnerable_ticks = RWProp[int]("getInvulnerableTicks", "setInvulnerableTicks")
    # TODO: method public abstract org.bukkit.entity.LivingEntity org.bukkit.entity.Wither.getTarget(org.bukkit.entity.Wither$Head)
    # TODO: method public abstract void org.bukkit.entity.Wither.setTarget(org.bukkit.entity.Wither$Head,org.bukkit.entity.LivingEntity)
    def enter_invulnerability_phase(self): self._delegate.enterInvulnerabilityPhase()

class Zombie(Monster, Ageable):
    @binding_constructor("org.bukkit.entity.Zombie")
    def __init__(self, delegate: BukkitType):
        self._delegate = delegate

    conversion_time = RWProp[int]("getConversionTime", "setConversionTime")
    can_break_doors = RWProp[bool]("canBreakDoors", "setCanBreakDoors")
    is_converting = ROProp[bool]("isConverting")
    should_burn_in_day = RWProp[bool]("shouldBurnInDay", "setShouldBurnInDay")
    is_drowning = ROProp[bool]("isDrowning")
    # TODO: method public abstract boolean org.bukkit.entity.Zombie.supportsBreakingDoors()
    def stop_drowning(self): self._delegate.stopDrowning()

class ComplexLivingEntity(LivingEntity):
    @binding_constructor("org.bukkit.entity.ComplexLivingEntity")
    def __init__(self, delegate: BukkitType):
        self._delegate = delegate

    # TODO: property parts getParts null

class EnderDragon(ComplexLivingEntity, Boss, Mob, Enemy):
    @binding_constructor("org.bukkit.entity.EnderDragon")
    def __init__(self, delegate: BukkitType):
        self._delegate = delegate

    # TODO: property podium getPodium setPodium
    class Phase(Enum):
        CIRCLING = auto()
        STRAFING = auto()
        FLY_TO_PORTAL = auto()
        LAND_ON_PORTAL = auto()
        LEAVE_PORTAL = auto()
        BREATH_ATTACK = auto()
        SEARCH_FOR_BREATH_ATTACK_TARGET = auto()
        ROAR_BEFORE_ATTACK = auto()
        CHARGE_PLAYER = auto()
        DYING = auto()
        HOVER = auto()

    _phase_enum_mirror = EnumMirror(Phase, "org.bukkit.entity.EnderDragon$Phase")
    phase = TransformedRWProp[Phase]("getPhase", "setPhase", _phase_enum_mirror.from_native, _phase_enum_mirror.to_native)
    death_animation_ticks = ROProp[int]("getDeathAnimationTicks")
    # TODO: property dragon_battle getDragonBattle null

class Hoglin(Animals, Enemy):
    @binding_constructor("org.bukkit.entity.Hoglin")
    def __init__(self, delegate: BukkitType):
        self._delegate = delegate

    conversion_time = RWProp[int]("getConversionTime", "setConversionTime")
    is_converting = ROProp[bool]("isConverting")
    is_able_to_be_hunted = RWProp[bool]("isAbleToBeHunted", "setIsAbleToBeHunted")
    is_immune_to_zombification = RWProp[bool]("isImmuneToZombification", "setImmuneToZombification")

class Silverfish(Monster):
    @binding_constructor("org.bukkit.entity.Silverfish")
    def __init__(self, delegate: BukkitType):
        self._delegate = delegate

class Raider(Monster):
    @binding_constructor("org.bukkit.entity.Raider")
    def __init__(self, delegate: BukkitType):
        self._delegate = delegate

    wave = RWProp[int]("getWave", "setWave")
    is_celebrating = RWProp[bool]("isCelebrating", "setCelebrating")
    is_can_join_raid = ROProp[bool]("isCanJoinRaid")
    is_patrol_leader = RWProp[bool]("isPatrolLeader", "setPatrolLeader")
    # TODO: property celebration_sound getCelebrationSound null
    # TODO: property raid getRaid setRaid
    ticks_outside_raid = RWProp[int]("getTicksOutsideRaid", "setTicksOutsideRaid")
    # TODO: property patrol_target getPatrolTarget setPatrolTarget
    # TODO: method public abstract void org.bukkit.entity.Raider.setCanJoinRaid(boolean)

class WitherSkeleton(AbstractSkeleton):
    @binding_constructor("org.bukkit.entity.WitherSkeleton")
    def __init__(self, delegate: BukkitType):
        self._delegate = delegate

class Stray(AbstractSkeleton):
    @binding_constructor("org.bukkit.entity.Stray")
    def __init__(self, delegate: BukkitType):
        self._delegate = delegate

class Skeleton(AbstractSkeleton):
    @binding_constructor("org.bukkit.entity.Skeleton")
    def __init__(self, delegate: BukkitType):
        self._delegate = delegate

    conversion_time = RWProp[int]("getConversionTime", "setConversionTime")
    is_converting = ROProp[bool]("isConverting")
    # TODO: method public abstract int org.bukkit.entity.Skeleton.inPowderedSnowTime()

class MagmaCube(Slime):
    @binding_constructor("org.bukkit.entity.MagmaCube")
    def __init__(self, delegate: BukkitType):
        self._delegate = delegate

class Piglin(PiglinAbstract, InventoryHolder, RangedEntity):
    @binding_constructor("org.bukkit.entity.Piglin")
    def __init__(self, delegate: BukkitType):
        self._delegate = delegate

    is_charging_crossbow = RWProp[bool]("isChargingCrossbow", "setChargingCrossbow")
    # TODO: property barter_list getBarterList null
    is_dancing = ROProp[bool]("isDancing")
    is_able_to_hunt = RWProp[bool]("isAbleToHunt", "setIsAbleToHunt")
    # TODO: property interest_list getInterestList null
    # TODO: method public abstract boolean org.bukkit.entity.Piglin.addBarterMaterial(org.bukkit.Material)
    # TODO: method public abstract boolean org.bukkit.entity.Piglin.removeMaterialOfInterest(org.bukkit.Material)
    # TODO: method public abstract boolean org.bukkit.entity.Piglin.addMaterialOfInterest(org.bukkit.Material)
    # TODO: method public abstract void org.bukkit.entity.Piglin.setDancing(long)
    # TODO: method public abstract void org.bukkit.entity.Piglin.setDancing(boolean)
    # TODO: method public abstract boolean org.bukkit.entity.Piglin.removeBarterMaterial(org.bukkit.Material)

class PiglinBrute(PiglinAbstract):
    @binding_constructor("org.bukkit.entity.PiglinBrute")
    def __init__(self, delegate: BukkitType):
        self._delegate = delegate

class CaveSpider(Spider):
    @binding_constructor("org.bukkit.entity.CaveSpider")
    def __init__(self, delegate: BukkitType):
        self._delegate = delegate

class ElderGuardian(Guardian):
    @binding_constructor("org.bukkit.entity.ElderGuardian")
    def __init__(self, delegate: BukkitType):
        self._delegate = delegate

class Husk(Zombie):
    @binding_constructor("org.bukkit.entity.Husk")
    def __init__(self, delegate: BukkitType):
        self._delegate = delegate

class ZombieVillager(Zombie):
    @binding_constructor("org.bukkit.entity.ZombieVillager")
    def __init__(self, delegate: BukkitType):
        self._delegate = delegate

    # TODO: property villager_type getVillagerType setVillagerType
    # TODO: property conversion_player getConversionPlayer setConversionPlayer
    # TODO: method public abstract void org.bukkit.entity.ZombieVillager.setConversionTime(int,boolean)

class PigZombie(Zombie):
    @binding_constructor("org.bukkit.entity.PigZombie")
    def __init__(self, delegate: BukkitType):
        self._delegate = delegate

    anger = RWProp[int]("getAnger", "setAnger")
    is_angry = RWProp[bool]("isAngry", "setAngry")

class Drowned(Zombie, RangedEntity):
    @binding_constructor("org.bukkit.entity.Drowned")
    def __init__(self, delegate: BukkitType):
        self._delegate = delegate

class Illager(Raider):
    @binding_constructor("org.bukkit.entity.Illager")
    def __init__(self, delegate: BukkitType):
        self._delegate = delegate

class Witch(Raider, RangedEntity):
    @binding_constructor("org.bukkit.entity.Witch")
    def __init__(self, delegate: BukkitType):
        self._delegate = delegate

    potion_use_time_left = RWProp[int]("getPotionUseTimeLeft", "setPotionUseTimeLeft")
    # TODO: method public abstract void org.bukkit.entity.Witch.setDrinkingPotion(org.bukkit.inventory.ItemStack)
    # TODO: method public abstract boolean org.bukkit.entity.Witch.isDrinkingPotion()
    # TODO: method public abstract org.bukkit.inventory.ItemStack org.bukkit.entity.Witch.getDrinkingPotion()

class Ravager(Raider):
    @binding_constructor("org.bukkit.entity.Ravager")
    def __init__(self, delegate: BukkitType):
        self._delegate = delegate

    attack_ticks = RWProp[int]("getAttackTicks", "setAttackTicks")
    roar_ticks = RWProp[int]("getRoarTicks", "setRoarTicks")
    stunned_ticks = RWProp[int]("getStunnedTicks", "setStunnedTicks")

class Spellcaster(Illager):
    @binding_constructor("org.bukkit.entity.Spellcaster")
    def __init__(self, delegate: BukkitType):
        self._delegate = delegate

    class Spell(Enum):
        NONE = auto()
        SUMMON_VEX = auto()
        FANGS = auto()
        WOLOLO = auto()
        DISAPPEAR = auto()
        BLINDNESS = auto()

    _spell_enum_mirror = EnumMirror(Spell, "org.bukkit.entity.Spellcaster$Spell")
    spell = TransformedRWProp[Spell]("getSpell", "setSpell", _spell_enum_mirror.from_native, _spell_enum_mirror.to_native)

class Vindicator(Illager):
    @binding_constructor("org.bukkit.entity.Vindicator")
    def __init__(self, delegate: BukkitType):
        self._delegate = delegate

    is_johnny = RWProp[bool]("isJohnny", "setJohnny")

class Pillager(Illager, InventoryHolder, RangedEntity):
    @binding_constructor("org.bukkit.entity.Pillager")
    def __init__(self, delegate: BukkitType):
        self._delegate = delegate

class Evoker(Spellcaster):
    @binding_constructor("org.bukkit.entity.Evoker")
    def __init__(self, delegate: BukkitType):
        self._delegate = delegate

    # TODO: property wololo_target getWololoTarget setWololoTarget

class Illusioner(Spellcaster, RangedEntity):
    @binding_constructor("org.bukkit.entity.Illusioner")
    def __init__(self, delegate: BukkitType):
        self._delegate = delegate

