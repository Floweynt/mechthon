from enum import Enum, auto
from typing import Optional
from ._categories import Explosive
from mechs.bindings.misc import Attachable, Frictional, LootableInventory
from ._entity import Entity
from ._mappings import wrap_entity
from mechs._internal import *
from mechs._internal.mirrors import *

class ExperienceOrb(Entity):
    @binding_constructor("org.bukkit.entity.ExperienceOrb")
    def __init__(self, delegate: BukkitType):
        self._delegate = delegate

    experience = RWProp[int]("getExperience", "setExperience")
    class SpawnReason(Enum):
        PLAYER_DEATH = auto()
        ENTITY_DEATH = auto()
        FURNACE = auto()
        BREED = auto()
        VILLAGER_TRADE = auto()
        FISHING = auto()
        BLOCK_BREAK = auto()
        CUSTOM = auto()
        EXP_BOTTLE = auto()
        GRINDSTONE = auto()
        UNKNOWN = auto()

    _spawn_reason_enum_mirror = EnumMirror(SpawnReason, "org.bukkit.entity.ExperienceOrb$SpawnReason")
    spawn_reason = TransformedROProp[SpawnReason]("getSpawnReason", _spawn_reason_enum_mirror.from_native)
    trigger_entity_id = TransformedROProp[Optional[UUID]]("getTriggerEntityId", lambda x: None if x is None else java_uuid_to_python(x))
    source_entity_id = TransformedROProp[Optional[UUID]]("getSourceEntityId", lambda x: None if x is None else java_uuid_to_python(x))

class EnderCrystal(Entity):
    @binding_constructor("org.bukkit.entity.EnderCrystal")
    def __init__(self, delegate: BukkitType):
        self._delegate = delegate

    is_showing_bottom = RWProp[bool]("isShowingBottom", "setShowingBottom")
    # TODO: property beam_target getBeamTarget setBeamTarget

class FallingBlock(Entity):
    @binding_constructor("org.bukkit.entity.FallingBlock")
    def __init__(self, delegate: BukkitType):
        self._delegate = delegate

    can_hurt_entities = RWProp[bool]("canHurtEntities", "setHurtEntities")
    # TODO: property block_state getBlockState setBlockState
    max_damage = RWProp[int]("getMaxDamage", "setMaxDamage")
    damage_per_block = RWProp[float]("getDamagePerBlock", "setDamagePerBlock")
    # TODO: property block_data getBlockData setBlockData
    cancel_drop = RWProp[bool]("getCancelDrop", "setCancelDrop")
    drop_item = RWProp[bool]("getDropItem", "setDropItem")
    # TODO: method public abstract boolean org.bukkit.entity.FallingBlock.doesAutoExpire()
    # TODO: method public abstract void org.bukkit.entity.FallingBlock.shouldAutoExpire(boolean)

class EvokerFangs(Entity):
    @binding_constructor("org.bukkit.entity.EvokerFangs")
    def __init__(self, delegate: BukkitType):
        self._delegate = delegate

    attack_delay = RWProp[int]("getAttackDelay", "setAttackDelay")
    # TODO: property owner getOwner setOwner

class Hanging(Entity, Attachable):
    @binding_constructor("org.bukkit.entity.Hanging")
    def __init__(self, delegate: BukkitType):
        self._delegate = delegate

    # TODO: method public abstract boolean org.bukkit.entity.Hanging.setFacingDirection(org.bukkit.block.BlockFace,boolean)

class EnderSignal(Entity):
    @binding_constructor("org.bukkit.entity.EnderSignal")
    def __init__(self, delegate: BukkitType):
        self._delegate = delegate

    # TODO: property item getItem setItem
    # TODO: property target_location getTargetLocation setTargetLocation
    despawn_timer = RWProp[int]("getDespawnTimer", "setDespawnTimer")
    drop_item = RWProp[bool]("getDropItem", "setDropItem")
    # TODO: method public abstract void org.bukkit.entity.EnderSignal.setTargetLocation(org.bukkit.Location,boolean)

class Item(Entity, Frictional):
    @binding_constructor("org.bukkit.entity.Item")
    def __init__(self, delegate: BukkitType):
        self._delegate = delegate

    owner = TransformedRWProp[Optional[UUID]]("getOwner", "setOwner", lambda x: None if x is None else java_uuid_to_python(x), lambda x: None if x is None else python_uuid_to_java(x))
    health = RWProp[int]("getHealth", "setHealth")
    pickup_delay = RWProp[int]("getPickupDelay", "setPickupDelay")
    can_mob_pickup = RWProp[bool]("canMobPickup", "setCanMobPickup")
    # TODO: property item_stack getItemStack setItemStack
    thrower = TransformedRWProp[Optional[UUID]]("getThrower", "setThrower", lambda x: None if x is None else java_uuid_to_python(x), lambda x: None if x is None else python_uuid_to_java(x))
    will_age = RWProp[bool]("willAge", "setWillAge")
    is_unlimited_lifetime = RWProp[bool]("isUnlimitedLifetime", "setUnlimitedLifetime")
    can_player_pickup = RWProp[bool]("canPlayerPickup", "setCanPlayerPickup")

class AreaEffectCloud(Entity):
    @binding_constructor("org.bukkit.entity.AreaEffectCloud")
    def __init__(self, delegate: BukkitType):
        self._delegate = delegate

    # TODO: property particle getParticle setParticle
    radius = RWProp[float]("getRadius", "setRadius")
    reapplication_delay = RWProp[int]("getReapplicationDelay", "setReapplicationDelay")
    # TODO: property color getColor setColor
    owner_unique_id = TransformedRWProp[Optional[UUID]]("getOwnerUniqueId", "setOwnerUniqueId", lambda x: None if x is None else java_uuid_to_python(x), lambda x: None if x is None else python_uuid_to_java(x))
    duration_on_use = RWProp[int]("getDurationOnUse", "setDurationOnUse")
    radius_on_use = RWProp[float]("getRadiusOnUse", "setRadiusOnUse")
    duration = RWProp[int]("getDuration", "setDuration")
    wait_time = RWProp[int]("getWaitTime", "setWaitTime")
    radius_per_tick = RWProp[float]("getRadiusPerTick", "setRadiusPerTick")
    # TODO: property source getSource setSource
    # TODO: property base_potion_type getBasePotionType setBasePotionType
    # TODO: method public abstract boolean org.bukkit.entity.AreaEffectCloud.removeCustomEffect(org.bukkit.potion.PotionEffectType)
    # TODO: method public abstract boolean org.bukkit.entity.AreaEffectCloud.hasCustomEffect(org.bukkit.potion.PotionEffectType)
    # TODO: method public abstract boolean org.bukkit.entity.AreaEffectCloud.addCustomEffect(org.bukkit.potion.PotionEffect,boolean)
    def clear_custom_effects(self): self._delegate.clearCustomEffects()
    # TODO: method public abstract <T> void org.bukkit.entity.AreaEffectCloud.setParticle(org.bukkit.Particle,T)
    # TODO: method public abstract boolean org.bukkit.entity.AreaEffectCloud.hasCustomEffects()
    # TODO: method public abstract java.util.List<org.bukkit.potion.PotionEffect> org.bukkit.entity.AreaEffectCloud.getCustomEffects()

class LightningStrike(Entity):
    @binding_constructor("org.bukkit.entity.LightningStrike")
    def __init__(self, delegate: BukkitType):
        self._delegate = delegate

    flash_count = RWProp[int]("getFlashCount", "setFlashCount")
    # TODO: property causing_player getCausingPlayer setCausingPlayer
    life_ticks = RWProp[int]("getLifeTicks", "setLifeTicks")
    is_effect = ROProp[bool]("isEffect")
    # TODO: property causing_entity getCausingEntity null

class TNTPrimed(Explosive):
    @binding_constructor("org.bukkit.entity.TNTPrimed")
    def __init__(self, delegate: BukkitType):
        self._delegate = delegate

    fuse_ticks = RWProp[int]("getFuseTicks", "setFuseTicks")
    # TODO: property block_data getBlockData setBlockData
    # TODO: property source getSource setSource

class Marker(Entity):
    @binding_constructor("org.bukkit.entity.Marker")
    def __init__(self, delegate: BukkitType):
        self._delegate = delegate


class LeashHitch(Hanging):
    @binding_constructor("org.bukkit.entity.LeashHitch")
    def __init__(self, delegate: BukkitType):
        self._delegate = delegate


class Painting(Hanging):
    @binding_constructor("org.bukkit.entity.Painting")
    def __init__(self, delegate: BukkitType):
        self._delegate = delegate

    # TODO: property art getArt null
    # TODO: method public abstract boolean org.bukkit.entity.Painting.setArt(org.bukkit.Art)
    # TODO: method public abstract boolean org.bukkit.entity.Painting.setArt(org.bukkit.Art,boolean)

class ItemFrame(Hanging):
    @binding_constructor("org.bukkit.entity.ItemFrame")
    def __init__(self, delegate: BukkitType):
        self._delegate = delegate

    # TODO: property item getItem setItem
    item_drop_chance = RWProp[float]("getItemDropChance", "setItemDropChance")
    is_visible = RWProp[bool]("isVisible", "setVisible")
    # TODO: property rotation getRotation setRotation
    is_fixed = RWProp[bool]("isFixed", "setFixed")
    # TODO: method public abstract void org.bukkit.entity.ItemFrame.setItem(org.bukkit.inventory.ItemStack,boolean)

class GlowItemFrame(ItemFrame):
    @binding_constructor("org.bukkit.entity.GlowItemFrame")
    def __init__(self, delegate: BukkitType):
        self._delegate = delegate

class LootableEntityInventory(LootableInventory):
    @binding_constructor("com.destroystokyo.paper.loottable.LootableEntityInventory")
    def __init__(self, delegate: BukkitType):
        self._delegate = delegate

    entity = TransformedROProp[Entity]("getEntity", wrap_entity)
