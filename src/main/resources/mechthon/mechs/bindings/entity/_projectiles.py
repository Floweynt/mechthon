from enum import Enum, auto
from typing import Optional
from ._categories import Explosive
from ._entity import Entity
from mechs._internal import *
from mechs._internal.mirrors import *

class Projectile(Entity):
    @binding_constructor("org.bukkit.entity.Projectile")
    def __init__(self, delegate: BukkitType):
        self._delegate = delegate

    # TODO: property shooter getShooter setShooter
    owner_unique_id = TransformedROProp[Optional[UUID]]("getOwnerUniqueId", lambda x: None if x is None else java_uuid_to_python(x))
    has_been_shot = RWProp[bool]("hasBeenShot", "setHasBeenShot")
    has_left_shooter = RWProp[bool]("hasLeftShooter", "setHasLeftShooter")
    # TODO: method public abstract void org.bukkit.entity.Projectile.hitEntity(org.bukkit.entity.Entity,org.bukkit.util.Vector)
    # TODO: method public abstract void org.bukkit.entity.Projectile.hitEntity(org.bukkit.entity.Entity)
    # TODO: method public abstract boolean org.bukkit.entity.Projectile.canHitEntity(org.bukkit.entity.Entity)

class ThrowableProjectile(Projectile):
    @binding_constructor("org.bukkit.entity.ThrowableProjectile")
    def __init__(self, delegate: BukkitType):
        self._delegate = delegate

    # TODO: property item getItem setItem

class AbstractArrow(Projectile):
    @binding_constructor("org.bukkit.entity.AbstractArrow")
    def __init__(self, delegate: BukkitType):
        self._delegate = delegate

    # TODO: property attached_block getAttachedBlock null
    pierce_level = RWProp[int]("getPierceLevel", "setPierceLevel")
    # TODO: property hit_sound getHitSound setHitSound
    class PickupStatus(Enum):
        DISALLOWED = auto()
        ALLOWED = auto()
        CREATIVE_ONLY = auto()

    _pickup_status_enum_mirror = EnumMirror(PickupStatus, "org.bukkit.entity.AbstractArrow$PickupStatus")
    pickup_status = TransformedRWProp[PickupStatus]("getPickupStatus", "setPickupStatus", _pickup_status_enum_mirror.from_native, _pickup_status_enum_mirror.to_native)
    lifetime_ticks = RWProp[int]("getLifetimeTicks", "setLifetimeTicks")
    is_shot_from_crossbow = RWProp[bool]("isShotFromCrossbow", "setShotFromCrossbow")
    is_in_block = ROProp[bool]("isInBlock")
    is_critical = RWProp[bool]("isCritical", "setCritical")
    knockback_strength = RWProp[int]("getKnockbackStrength", "setKnockbackStrength")
    damage = RWProp[float]("getDamage", "setDamage")
    # TODO: property item_stack getItemStack null

class Fireball(Projectile, Explosive):
    @binding_constructor("org.bukkit.entity.Fireball")
    def __init__(self, delegate: BukkitType):
        self._delegate = delegate

    direction = TransformedRWProp[Vector]("getDirection", "setDirection", wrap_vector, unwrap_vector)
    power = TransformedRWProp[Vector]("getPower", "setPower", wrap_vector, unwrap_vector)

class Firework(Projectile):
    @binding_constructor("org.bukkit.entity.Firework")
    def __init__(self, delegate: BukkitType):
        self._delegate = delegate

    # TODO: property item getItem setItem
    is_detonated = ROProp[bool]("isDetonated")
    is_shot_at_angle = RWProp[bool]("isShotAtAngle", "setShotAtAngle")
    # TODO: property firework_meta getFireworkMeta setFireworkMeta
    spawning_entity = TransformedROProp[Optional[UUID]]("getSpawningEntity", lambda x: None if x is None else java_uuid_to_python(x))
    # TODO: property attached_to getAttachedTo null
    ticks_to_detonate = RWProp[int]("getTicksToDetonate", "setTicksToDetonate")
    ticks_flown = RWProp[int]("getTicksFlown", "setTicksFlown")
    def detonate(self): self._delegate.detonate()
    # TODO: method public abstract boolean org.bukkit.entity.Firework.setAttachedTo(org.bukkit.entity.LivingEntity)

class ShulkerBullet(Projectile):
    @binding_constructor("org.bukkit.entity.ShulkerBullet")
    def __init__(self, delegate: BukkitType):
        self._delegate = delegate

    target_delta = TransformedRWProp[Vector]("getTargetDelta", "setTargetDelta", wrap_vector, unwrap_vector)
    # TODO: property target getTarget setTarget
    flight_steps = RWProp[int]("getFlightSteps", "setFlightSteps")
    # TODO: property current_movement_direction getCurrentMovementDirection setCurrentMovementDirection

class LlamaSpit(Projectile):
    @binding_constructor("org.bukkit.entity.LlamaSpit")
    def __init__(self, delegate: BukkitType):
        self._delegate = delegate

class FishHook(Projectile):
    @binding_constructor("org.bukkit.entity.FishHook")
    def __init__(self, delegate: BukkitType):
        self._delegate = delegate

    is_in_open_water = ROProp[bool]("isInOpenWater")
    min_lure_angle = RWProp[float]("getMinLureAngle", "setMinLureAngle")
    max_wait_time = RWProp[int]("getMaxWaitTime", "setMaxWaitTime")
    max_lure_time = RWProp[int]("getMaxLureTime", "setMaxLureTime")
    is_rain_influenced = RWProp[bool]("isRainInfluenced", "setRainInfluenced")
    max_lure_angle = RWProp[float]("getMaxLureAngle", "setMaxLureAngle")
    class HookState(Enum):
        UNHOOKED = auto()
        HOOKED_ENTITY = auto()
        BOBBING = auto()

    _hook_state_enum_mirror = EnumMirror(HookState, "org.bukkit.entity.FishHook$HookState")
    state = TransformedROProp[HookState]("getState", _hook_state_enum_mirror.from_native)
    min_lure_time = RWProp[int]("getMinLureTime", "setMinLureTime")
    wait_time = RWProp[int]("getWaitTime", "setWaitTime")
    apply_lure = RWProp[bool]("getApplyLure", "setApplyLure")
    min_wait_time = RWProp[int]("getMinWaitTime", "setMinWaitTime")
    # TODO: property hooked_entity getHookedEntity setHookedEntity
    is_sky_influenced = RWProp[bool]("isSkyInfluenced", "setSkyInfluenced")
    # TODO: method public abstract void org.bukkit.entity.FishHook.setLureTime(int,int)
    # TODO: method public abstract boolean org.bukkit.entity.FishHook.pullHookedEntity()
    # TODO: method public abstract void org.bukkit.entity.FishHook.setWaitTime(int,int)
    # TODO: method public abstract void org.bukkit.entity.FishHook.setLureAngle(float,float)

class Egg(ThrowableProjectile):
    @binding_constructor("org.bukkit.entity.Egg")
    def __init__(self, delegate: BukkitType):
        self._delegate = delegate

class Snowball(ThrowableProjectile):
    @binding_constructor("org.bukkit.entity.Snowball")
    def __init__(self, delegate: BukkitType):
        self._delegate = delegate

class EnderPearl(ThrowableProjectile):
    @binding_constructor("org.bukkit.entity.EnderPearl")
    def __init__(self, delegate: BukkitType):
        self._delegate = delegate

class ThrownPotion(ThrowableProjectile):
    @binding_constructor("org.bukkit.entity.ThrownPotion")
    def __init__(self, delegate: BukkitType):
        self._delegate = delegate

    # TODO: property potion_meta getPotionMeta setPotionMeta
    # TODO: property effects getEffects null
    def splash(self): self._delegate.splash()

class ThrownExpBottle(ThrowableProjectile):
    @binding_constructor("org.bukkit.entity.ThrownExpBottle")
    def __init__(self, delegate: BukkitType):
        self._delegate = delegate

class Arrow(AbstractArrow):
    @binding_constructor("org.bukkit.entity.Arrow")
    def __init__(self, delegate: BukkitType):
        self._delegate = delegate

    # TODO: property color getColor setColor
    # TODO: property base_potion_type getBasePotionType setBasePotionType
    # TODO: method public abstract boolean org.bukkit.entity.Arrow.addCustomEffect(org.bukkit.potion.PotionEffect,boolean)
    # TODO: method public abstract java.util.List<org.bukkit.potion.PotionEffect> org.bukkit.entity.Arrow.getCustomEffects()
    def clear_custom_effects(self): self._delegate.clearCustomEffects()
    # TODO: method public abstract boolean org.bukkit.entity.Arrow.hasCustomEffects()
    # TODO: method public abstract boolean org.bukkit.entity.Arrow.hasCustomEffect(org.bukkit.potion.PotionEffectType)
    # TODO: method public abstract boolean org.bukkit.entity.Arrow.removeCustomEffect(org.bukkit.potion.PotionEffectType)

class SpectralArrow(AbstractArrow):
    @binding_constructor("org.bukkit.entity.SpectralArrow")
    def __init__(self, delegate: BukkitType):
        self._delegate = delegate

    glowing_ticks = RWProp[int]("getGlowingTicks", "setGlowingTicks")

class Trident(AbstractArrow, ThrowableProjectile):
    @binding_constructor("org.bukkit.entity.Trident")
    def __init__(self, delegate: BukkitType):
        self._delegate = delegate

    has_glint = RWProp[bool]("hasGlint", "setGlint")
    has_dealt_damage = RWProp[bool]("hasDealtDamage", "setHasDealtDamage")
    loyalty_level = RWProp[int]("getLoyaltyLevel", "setLoyaltyLevel")

class SizedFireball(Fireball):
    @binding_constructor("org.bukkit.entity.SizedFireball")
    def __init__(self, delegate: BukkitType):
        self._delegate = delegate

    # TODO: property display_item getDisplayItem setDisplayItem

class WitherSkull(Fireball):
    @binding_constructor("org.bukkit.entity.WitherSkull")
    def __init__(self, delegate: BukkitType):
        self._delegate = delegate

    is_charged = RWProp[bool]("isCharged", "setCharged")

class DragonFireball(Fireball):
    @binding_constructor("org.bukkit.entity.DragonFireball")
    def __init__(self, delegate: BukkitType):
        self._delegate = delegate

class WindCharge(Fireball):
    @binding_constructor("org.bukkit.entity.WindCharge")
    def __init__(self, delegate: BukkitType):
        self._delegate = delegate

    def explode(self): self._delegate.explode()

class LargeFireball(SizedFireball):
    @binding_constructor("org.bukkit.entity.LargeFireball")
    def __init__(self, delegate: BukkitType):
        self._delegate = delegate

class SmallFireball(SizedFireball):
    @binding_constructor("org.bukkit.entity.SmallFireball")
    def __init__(self, delegate: BukkitType):
        self._delegate = delegate

