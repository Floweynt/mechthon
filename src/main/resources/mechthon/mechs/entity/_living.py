from mechs._internal.mirrors import ROProp, RWProp
from mechs.entity._entity import Entity
from mechs._internal import BukkitType, binding_constructor

class LivingEntity(Entity):
    @binding_constructor("org.bukkit.entity.LivingEntity")
    def __init__(self, delegate: BukkitType):
        super().__init__(delegate)

    remaining_air = RWProp[int]("getRemainingAir", "setRemainingAir")
    maximum_air = RWProp[int]("getMaximumAir", "setMaximumAir")
    arrow_cooldown = RWProp[int]("getArrowCooldown", "setArrowCooldown")
    arrows_in_body = RWProp[int]("getArrowsInBody", "setArrowsInBody")
    next_arrow_removal = RWProp[int]("getNextArrowRemoval", "setNextArrowRemoval")
    bee_stinger_cooldown = RWProp[int]("getBeeStingerCooldown", "setBeeStingerCooldown")
    bee_stingers_in_body = RWProp[int]("getBeeStingersInBody", "setBeeStingersInBody")
    next_bee_stinger_removal = RWProp[int]("getNextBeeStingerRemoval", "setNextBeeStingerRemoval")
    maximum_no_damage_ticks = RWProp[int]("getMaximumNoDamageTicks", "setMaximumNoDamageTicks")
    last_damage = RWProp[float]("getLastDamage", "setLastDamage")
    no_damage_ticks = RWProp[int]("getNoDamageTicks", "setNoDamageTicks")
    no_action_ticks = RWProp[int]("getNoActionTicks", "setNoActionTicks")
    remove_when_far_away = RWProp[bool]("getRemoveWhenFarAway", "setRemoveWhenFarAway")
    can_pickup_items = RWProp[bool]("getCanPickupItems", "setCanPickupItems")
    gliding = RWProp[bool]("isGliding", "setGliding")
    swimming = ROProp[bool]("isSwimming") 
    riptiding = ROProp[bool]("isRiptiding")
    sleeping = ROProp[bool]("isSleeping")
    climbing = ROProp[bool]("isClimbing")
    ai = RWProp[bool]("hasAI", "setAI")
    collidable = RWProp[bool]("isCollidable", "setCollidable")
    invisible = RWProp[bool]("isInvisible", "setInvisible")
    shield_blocking_delay = RWProp[int]("getShieldBlockingDelay", "setShieldBlockingDelay")
    sideways_movement = ROProp[float]("getSidewaysMovement")
    upwards_movement = ROProp[float]("getUpwardsMovement")
    forwards_movement = ROProp[float]("getForwardsMovement")
    active_item_remaining_time = RWProp[int]("getActiveItemRemainingTime", "setActiveItemRemainingTime")
    has_active_item = ROProp[bool]("hasActiveItem")
    active_item_used_time = ROProp[int]("getActiveItemUsedTime")
    hurt_direction = RWProp[float]("getHurtDirection", "setHurtDirection")
    body_yaw = RWProp[float]("getBodyYaw", "setBodyYaw")

    # --- TODO: missing composite types ---
    # TODO: getEyeHeight / getEyeHeight(boolean)
    # TODO: getEyeLocation
    # TODO: getLineOfSight
    # TODO: getTargetBlock / getTargetBlockExact
    # TODO: getTargetBlockFace
    # TODO: getTargetBlockInfo
    # TODO: getTargetEntity / getTargetEntityInfo
    # TODO: rayTraceEntities / rayTraceBlocks
    # TODO: getLastTwoTargetBlocks
    # TODO: getItemInUse / setItemInUseTicks (deprecated overloads)
    # TODO: setArrowsInBody(count, fireEvent)
    # TODO: setNextArrowRemoval
    # TODO: setNextBeeStingerRemoval
    # TODO: getKiller / setKiller
    # TODO: addPotionEffect / addPotionEffects / removePotionEffect / getPotionEffect
    # TODO: getActivePotionEffects / clearActivePotionEffects
    # TODO: hasLineOfSight(Entity) / hasLineOfSight(Location)
    # TODO: getEquipment
    # TODO: isLeashed / getLeashHolder / setLeashHolder
    # TODO: attack(Entity) / swingMainHand / swingOffHand / swingHand
    # TODO: playHurtAnimation
    # TODO: getCollidableExemptions
    # TODO: getMemory / setMemory
    # TODO: getHurtSound / getDeathSound / getFallDamageSound / getFallDamageSoundSmall / getFallDamageSoundBig
    # TODO: getDrinkingSound / getEatingSound
    # TODO: canBreatheUnderwater
    # TODO: getCategory
    # TODO: startUsingItem / completeUsingActiveItem / getActiveItem / clearActiveItem / getActiveItemHand
    # TODO: isJumping / setJumping
    # TODO: playPickupItemAnimation
    # TODO: knockback
    # TODO: broadcastSlotBreak / damageItemStack
