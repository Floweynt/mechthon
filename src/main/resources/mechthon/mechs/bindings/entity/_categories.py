from typing import Optional
from ._mappings import wrap_entity
from mechs.bindings.misc import Attributable, Frictional, Lootable, ProjectileSource
from ._entity import Entity
from mechs._internal import *
from mechs._internal.mirrors import *

class Bucketable(Entity):
    @binding_constructor("io.papermc.paper.entity.Bucketable")
    def __init__(self, delegate: BukkitType):
        self._delegate = delegate

    # TODO: property base_bucket_item getBaseBucketItem null
    is_from_bucket = RWProp[bool]("isFromBucket", "setFromBucket")
    # TODO: property pickup_sound getPickupSound null

class Boss(Entity):
    @binding_constructor("org.bukkit.entity.Boss")
    def __init__(self, delegate: BukkitType):
        self._delegate = delegate

    # TODO: property boss_bar getBossBar null

class Damageable(Entity):
    @binding_constructor("org.bukkit.entity.Damageable")
    def __init__(self, delegate: BukkitType):
        self._delegate = delegate

    health = RWProp[float]("getHealth", "setHealth")
    absorption_amount = RWProp[float]("getAbsorptionAmount", "setAbsorptionAmount")

    # TODO: method public abstract void org.bukkit.entity.Damageable.damage(double)
    # TODO: method public abstract void org.bukkit.entity.Damageable.damage(double,org.bukkit.damage.DamageSource)
    # TODO: method public abstract void org.bukkit.entity.Damageable.damage(double,org.bukkit.entity.Entity)

class Shearable(Entity):
    @binding_constructor("io.papermc.paper.entity.Shearable")
    def __init__(self, delegate: BukkitType):
        self._delegate = delegate

    is_ready_to_shear = ROProp[bool]("readyToBeSheared")
    def shear(self): self._delegate.shear()
    # TODO: method public abstract void io.papermc.paper.entity.Shearable.shear(net.kyori.adventure.sound.Sound$Source)

class Sittable(BukkitWrapper):
    @binding_constructor("org.bukkit.entity.Sittable")
    def __init__(self, delegate: BukkitType):
        self._delegate = delegate

    is_sitting = RWProp[bool]("isSitting", "setSitting")

class Vehicle(Entity):
    @binding_constructor("org.bukkit.entity.Vehicle")
    def __init__(self, delegate: BukkitType):
        self._delegate = delegate

class Explosive(Entity):
    @binding_constructor("org.bukkit.entity.Explosive")
    def __init__(self, delegate: BukkitType):
        self._delegate = delegate

    is_incendiary = RWProp[bool]("isIncendiary", "setIsIncendiary")
    explosive_yield = RWProp[float]("getYield", "setYield")

class LivingEntity(Attributable, Damageable, ProjectileSource, Frictional):
    @binding_constructor("org.bukkit.entity.LivingEntity")
    def __init__(self, delegate: BukkitType):
        self._delegate = delegate

    # TODO: property fall_damage_sound_big getFallDamageSoundBig null
    # TODO: property category getCategory null
    maximum_no_damage_ticks = RWProp[int]("getMaximumNoDamageTicks", "setMaximumNoDamageTicks")
    is_riptiding = ROProp[bool]("isRiptiding")
    # TODO: property active_potion_effects getActivePotionEffects null
    is_climbing = ROProp[bool]("isClimbing")
    # TODO: property hurt_sound getHurtSound null
    maximum_air = RWProp[int]("getMaximumAir", "setMaximumAir")
    eye_height = ROProp[float]("getEyeHeight")
    last_damage = RWProp[float]("getLastDamage", "setLastDamage")
    # TODO: property active_item_hand getActiveItemHand null
    shield_blocking_delay = RWProp[int]("getShieldBlockingDelay", "setShieldBlockingDelay")
    forwards_movement = ROProp[float]("getForwardsMovement")
    next_bee_stinger_removal = RWProp[int]("getNextBeeStingerRemoval", "setNextBeeStingerRemoval")
    sideways_movement = ROProp[float]("getSidewaysMovement")
    arrow_cooldown = RWProp[int]("getArrowCooldown", "setArrowCooldown")
    arrows_in_body = RWProp[int]("getArrowsInBody", "setArrowsInBody")
    is_swimming = ROProp[bool]("isSwimming")
    remaining_air = RWProp[int]("getRemainingAir", "setRemainingAir")
    no_action_ticks = RWProp[int]("getNoActionTicks", "setNoActionTicks")
    # TODO: property killer getKiller setKiller
    is_gliding = RWProp[bool]("isGliding", "setGliding")
    active_item_remaining_time = RWProp[int]("getActiveItemRemainingTime", "setActiveItemRemainingTime")
    is_collidable = RWProp[bool]("isCollidable", "setCollidable")
    no_damage_ticks = RWProp[int]("getNoDamageTicks", "setNoDamageTicks")
    # TODO: property eye_location getEyeLocation null
    is_leashed = ROProp[bool]("isLeashed")
    # TODO: property collidable_exemptions getCollidableExemptions null
    upwards_movement = ROProp[float]("getUpwardsMovement")
    item_use_remaining_time = ROProp[int]("getItemUseRemainingTime")
    has_a_i = RWProp[bool]("hasAI", "setAI")
    # TODO: property fall_damage_sound_small getFallDamageSoundSmall null
    hurt_direction = ROProp[float]("getHurtDirection")
    leash_holder = TransformedROProp[Entity]("getLeashHolder", wrap_entity)
    remove_when_far_away = RWProp[bool]("getRemoveWhenFarAway", "setRemoveWhenFarAway")
    bee_stinger_cooldown = RWProp[int]("getBeeStingerCooldown", "setBeeStingerCooldown")
    body_yaw = RWProp[float]("getBodyYaw", "setBodyYaw")
    can_breathe_underwater = ROProp[bool]("canBreatheUnderwater")
    # TODO: property equipment getEquipment null
    is_sleeping = ROProp[bool]("isSleeping")
    active_item_used_time = ROProp[int]("getActiveItemUsedTime")
    next_arrow_removal = RWProp[int]("getNextArrowRemoval", "setNextArrowRemoval")
    can_pickup_items = RWProp[bool]("getCanPickupItems", "setCanPickupItems")
    hand_raised_time = ROProp[int]("getHandRaisedTime")
    is_jumping = RWProp[bool]("isJumping", "setJumping")
    bee_stingers_in_body = RWProp[int]("getBeeStingersInBody", "setBeeStingersInBody")
    # TODO: property death_sound getDeathSound null
    # TODO: method public abstract void org.bukkit.entity.LivingEntity.broadcastSlotBreak(org.bukkit.inventory.EquipmentSlot,java.util.Collection<org.bukkit.entity.Player>)
    # TODO: method public abstract void org.bukkit.entity.LivingEntity.broadcastSlotBreak(org.bukkit.inventory.EquipmentSlot)
    # TODO: method public abstract org.bukkit.block.Block org.bukkit.entity.LivingEntity.getTargetBlock(java.util.Set<org.bukkit.Material>,int)
    # TODO: method public abstract org.bukkit.util.RayTraceResult org.bukkit.entity.LivingEntity.rayTraceEntities(int,boolean)
    # TODO: method public default org.bukkit.util.RayTraceResult org.bukkit.entity.LivingEntity.rayTraceEntities(int)
    def swing_off_hand(self): self._delegate.swingOffHand()
    # TODO: method public abstract java.util.List<org.bukkit.block.Block> org.bukkit.entity.LivingEntity.getLineOfSight(java.util.Set<org.bukkit.Material>,int)
    # TODO: method public abstract void org.bukkit.entity.LivingEntity.setArrowsInBody(int,boolean)
    # TODO: method public abstract void org.bukkit.entity.LivingEntity.knockback(double,double,double)
    # TODO: method public abstract boolean org.bukkit.entity.LivingEntity.clearActivePotionEffects()
    def clear_active_item(self): self._delegate.clearActiveItem()
    # TODO: method public abstract boolean org.bukkit.entity.LivingEntity.hasActiveItem()
    # TODO: method public abstract boolean org.bukkit.entity.LivingEntity.hasPotionEffect(org.bukkit.potion.PotionEffectType)
    # TODO: method public abstract void org.bukkit.entity.LivingEntity.removePotionEffect(org.bukkit.potion.PotionEffectType)
    # TODO: method public abstract org.bukkit.Sound org.bukkit.entity.LivingEntity.getFallDamageSound(int)
    # TODO: method public abstract java.util.List<org.bukkit.block.Block> org.bukkit.entity.LivingEntity.getLastTwoTargetBlocks(java.util.Set<org.bukkit.Material>,int)
    # TODO: method public abstract org.bukkit.entity.Entity org.bukkit.entity.LivingEntity.getTargetEntity(int,boolean)
    # TODO: method public default org.bukkit.entity.Entity org.bukkit.entity.LivingEntity.getTargetEntity(int)
    # TODO: method public abstract boolean org.bukkit.entity.LivingEntity.setLeashHolder(org.bukkit.entity.Entity)
    # TODO: method public abstract boolean org.bukkit.entity.LivingEntity.hasLineOfSight(org.bukkit.entity.Entity)
    # TODO: method public abstract boolean org.bukkit.entity.LivingEntity.hasLineOfSight(org.bukkit.Location)
    # TODO: method public abstract void org.bukkit.entity.LivingEntity.playPickupItemAnimation(org.bukkit.entity.Item,int)
    # TODO: method public default void org.bukkit.entity.LivingEntity.playPickupItemAnimation(org.bukkit.entity.Item)
    # TODO: method public abstract org.bukkit.Sound org.bukkit.entity.LivingEntity.getEatingSound(org.bukkit.inventory.ItemStack)
    # TODO: method public default void org.bukkit.entity.LivingEntity.swingHand(org.bukkit.inventory.EquipmentSlot)
    # TODO: method public abstract <T> T org.bukkit.entity.LivingEntity.getMemory(org.bukkit.entity.memory.MemoryKey<T>)
    def complete_using_active_item(self): self._delegate.completeUsingActiveItem()
    # TODO: method public abstract org.bukkit.inventory.ItemStack org.bukkit.entity.LivingEntity.getActiveItem()
    # TODO: method public abstract void org.bukkit.entity.LivingEntity.startUsingItem(org.bukkit.inventory.EquipmentSlot)
    # TODO: method public abstract org.bukkit.potion.PotionEffect org.bukkit.entity.LivingEntity.getPotionEffect(org.bukkit.potion.PotionEffectType)
    # TODO: method public abstract <T> void org.bukkit.entity.LivingEntity.setMemory(org.bukkit.entity.memory.MemoryKey<T>,T)
    # TODO: method public abstract org.bukkit.block.Block org.bukkit.entity.LivingEntity.getTargetBlockExact(int,org.bukkit.FluidCollisionMode)
    # TODO: method public abstract org.bukkit.block.Block org.bukkit.entity.LivingEntity.getTargetBlockExact(int)
    # TODO: method public abstract org.bukkit.util.RayTraceResult org.bukkit.entity.LivingEntity.rayTraceBlocks(double)
    # TODO: method public abstract org.bukkit.util.RayTraceResult org.bukkit.entity.LivingEntity.rayTraceBlocks(double,org.bukkit.FluidCollisionMode)
    # TODO: method public default org.bukkit.inventory.EquipmentSlot org.bukkit.entity.LivingEntity.getHandRaised()
    # TODO: method public abstract boolean org.bukkit.entity.LivingEntity.addPotionEffects(java.util.Collection<org.bukkit.potion.PotionEffect>)
    # TODO: method public abstract void org.bukkit.entity.LivingEntity.playHurtAnimation(float)
    # TODO: method public abstract void org.bukkit.entity.LivingEntity.damageItemStack(org.bukkit.inventory.EquipmentSlot,int)
    # TODO: method public abstract org.bukkit.inventory.ItemStack org.bukkit.entity.LivingEntity.damageItemStack(org.bukkit.inventory.ItemStack,int)
    # TODO: method public abstract double org.bukkit.entity.LivingEntity.getEyeHeight(boolean)
    def swing_main_hand(self): self._delegate.swingMainHand()
    # TODO: method public default boolean org.bukkit.entity.LivingEntity.isHandRaised()
    # TODO: method public abstract org.bukkit.Sound org.bukkit.entity.LivingEntity.getDrinkingSound(org.bukkit.inventory.ItemStack)
    # TODO: method public abstract org.bukkit.block.BlockFace org.bukkit.entity.LivingEntity.getTargetBlockFace(int,org.bukkit.FluidCollisionMode)
    # TODO: method public default org.bukkit.block.BlockFace org.bukkit.entity.LivingEntity.getTargetBlockFace(int)
    # TODO: method public abstract void org.bukkit.entity.LivingEntity.attack(org.bukkit.entity.Entity)
    # TODO: method public abstract boolean org.bukkit.entity.LivingEntity.addPotionEffect(org.bukkit.potion.PotionEffect)

class Mob(LivingEntity, Lootable):
    @binding_constructor("org.bukkit.entity.Mob")
    def __init__(self, delegate: BukkitType):
        self._delegate = delegate

    head_rotation_speed = ROProp[int]("getHeadRotationSpeed")
    # TODO: property pathfinder getPathfinder null
    # TODO: property ambient_sound getAmbientSound null
    # TODO: property target getTarget setTarget
    possible_experience_reward = ROProp[int]("getPossibleExperienceReward")
    is_aware = RWProp[bool]("isAware", "setAware")
    is_aggressive = RWProp[bool]("isAggressive", "setAggressive")
    is_left_handed = RWProp[bool]("isLeftHanded", "setLeftHanded")
    is_in_daylight = ROProp[bool]("isInDaylight")
    max_head_pitch = ROProp[int]("getMaxHeadPitch")
    # TODO: method public abstract void org.bukkit.entity.Mob.lookAt(double,double,double)
    # TODO: method public abstract void org.bukkit.entity.Mob.lookAt(double,double,double,float,float)
    # TODO: method public abstract void org.bukkit.entity.Mob.lookAt(org.bukkit.entity.Entity,float,float)
    # TODO: method public abstract void org.bukkit.entity.Mob.lookAt(org.bukkit.Location)
    # TODO: method public abstract void org.bukkit.entity.Mob.lookAt(org.bukkit.Location,float,float)
    # TODO: method public abstract void org.bukkit.entity.Mob.lookAt(org.bukkit.entity.Entity)

class Enemy(LivingEntity):
    @binding_constructor("org.bukkit.entity.Enemy")
    def __init__(self, delegate: BukkitType):
        self._delegate = delegate


class CollarColorable(LivingEntity):
    @binding_constructor("io.papermc.paper.entity.CollarColorable")
    def __init__(self, delegate: BukkitType):
        self._delegate = delegate

    # TODO: property collar_color getCollarColor setCollarColor

class Creature(Mob):
    @binding_constructor("org.bukkit.entity.Creature")
    def __init__(self, delegate: BukkitType):
        self._delegate = delegate

class RangedEntity(Mob):
    @binding_constructor("com.destroystokyo.paper.entity.RangedEntity")
    def __init__(self, delegate: BukkitType):
        self._delegate = delegate

    # TODO: method public abstract void com.destroystokyo.paper.entity.RangedEntity.rangedAttack(org.bukkit.entity.LivingEntity,float)

class Flying(Mob):
    @binding_constructor("org.bukkit.entity.Flying")
    def __init__(self, delegate: BukkitType):
        self._delegate = delegate

class Ambient(Mob):
    @binding_constructor("org.bukkit.entity.Ambient")
    def __init__(self, delegate: BukkitType):
        self._delegate = delegate

class Monster(Creature, Enemy):
    @binding_constructor("org.bukkit.entity.Monster")
    def __init__(self, delegate: BukkitType):
        self._delegate = delegate

class Ageable(Creature):
    @binding_constructor("org.bukkit.entity.Ageable")
    def __init__(self, delegate: BukkitType):
        self._delegate = delegate

    is_adult = ROProp[bool]("isAdult")
    age = RWProp[int]("getAge", "setAge")
    def set_adult(self): self._delegate.setAdult()
    def set_baby(self): self._delegate.setBaby()

class Breedable(Ageable):
    @binding_constructor("org.bukkit.entity.Breedable")
    def __init__(self, delegate: BukkitType):
        self._delegate = delegate


class Animals(Breedable):
    @binding_constructor("org.bukkit.entity.Animals")
    def __init__(self, delegate: BukkitType):
        self._delegate = delegate

    breed_cause = TransformedRWProp[Optional[UUID]]("getBreedCause", "setBreedCause", lambda x: None if x is None else java_uuid_to_python(x), lambda x: None if x is None else python_uuid_to_java(x))
    love_mode_ticks = RWProp[int]("getLoveModeTicks", "setLoveModeTicks")
    is_love_mode = ROProp[bool]("isLoveMode")
    # TODO: method public abstract boolean org.bukkit.entity.Animals.isBreedItem(org.bukkit.inventory.ItemStack)
    # TODO: method public abstract boolean org.bukkit.entity.Animals.isBreedItem(org.bukkit.Material)

class Tameable(Animals):
    @binding_constructor("org.bukkit.entity.Tameable")
    def __init__(self, delegate: BukkitType):
        self._delegate = delegate

    is_tamed = RWProp[bool]("isTamed", "setTamed")
    # TODO: property owner getOwner setOwner
    owner_unique_id = TransformedROProp[Optional[UUID]]("getOwnerUniqueId", lambda x: None if x is None else java_uuid_to_python(x))

class Steerable(Animals):
    @binding_constructor("org.bukkit.entity.Steerable")
    def __init__(self, delegate: BukkitType):
        self._delegate = delegate

    current_boost_ticks = RWProp[int]("getCurrentBoostTicks", "setCurrentBoostTicks")
    # TODO: property steer_material getSteerMaterial null
    boost_ticks = RWProp[int]("getBoostTicks", "setBoostTicks")
    has_saddle = RWProp[bool]("hasSaddle", "setSaddle")

