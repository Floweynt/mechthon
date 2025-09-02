from ._categories import LivingEntity
from mechs.bindings.misc import AnimalTamer, InventoryHolder
from mechs._internal import *
from mechs._internal.mirrors import *

class ArmorStand(LivingEntity):
    @binding_constructor("org.bukkit.entity.ArmorStand")
    def __init__(self, delegate: BukkitType):
        self._delegate = delegate

    is_small = RWProp[bool]("isSmall", "setSmall")
    # TODO: property right_arm_rotations getRightArmRotations setRightArmRotations
    # TODO: property head_pose getHeadPose setHeadPose
    # TODO: property body_pose getBodyPose setBodyPose
    # TODO: property body_rotations getBodyRotations setBodyRotations
    has_arms = RWProp[bool]("hasArms", "setArms")
    # TODO: property right_arm_pose getRightArmPose setRightArmPose
    # TODO: property left_arm_pose getLeftArmPose setLeftArmPose
    is_marker = RWProp[bool]("isMarker", "setMarker")
    # TODO: property left_arm_rotations getLeftArmRotations setLeftArmRotations
    has_base_plate = RWProp[bool]("hasBasePlate", "setBasePlate")
    can_move = RWProp[bool]("canMove", "setCanMove")
    # TODO: property left_leg_rotations getLeftLegRotations setLeftLegRotations
    # TODO: property left_leg_pose getLeftLegPose setLeftLegPose
    # TODO: property head_rotations getHeadRotations setHeadRotations
    is_visible = RWProp[bool]("isVisible", "setVisible")
    # TODO: property right_leg_pose getRightLegPose setRightLegPose
    can_tick = RWProp[bool]("canTick", "setCanTick")
    # TODO: property right_leg_rotations getRightLegRotations setRightLegRotations
    # TODO: method public abstract void org.bukkit.entity.ArmorStand.removeDisabledSlots(org.bukkit.inventory.EquipmentSlot...)
    # TODO: method public abstract void org.bukkit.entity.ArmorStand.addDisabledSlots(org.bukkit.inventory.EquipmentSlot...)
    # TODO: method public abstract boolean org.bukkit.entity.ArmorStand.isSlotDisabled(org.bukkit.inventory.EquipmentSlot)
    # TODO: method public abstract void org.bukkit.entity.ArmorStand.addEquipmentLock(org.bukkit.inventory.EquipmentSlot,org.bukkit.entity.ArmorStand$LockType)
    # TODO: method public abstract void org.bukkit.entity.ArmorStand.setItem(org.bukkit.inventory.EquipmentSlot,org.bukkit.inventory.ItemStack)
    # TODO: method public abstract void org.bukkit.entity.ArmorStand.removeEquipmentLock(org.bukkit.inventory.EquipmentSlot,org.bukkit.entity.ArmorStand$LockType)
    # TODO: method public abstract void org.bukkit.entity.ArmorStand.setDisabledSlots(org.bukkit.inventory.EquipmentSlot...)
    # TODO: method public abstract boolean org.bukkit.entity.ArmorStand.hasEquipmentLock(org.bukkit.inventory.EquipmentSlot,org.bukkit.entity.ArmorStand$LockType)
    # TODO: method public abstract java.util.Set<org.bukkit.inventory.EquipmentSlot> org.bukkit.entity.ArmorStand.getDisabledSlots()
    # TODO: method public abstract org.bukkit.inventory.ItemStack org.bukkit.entity.ArmorStand.getItem(org.bukkit.inventory.EquipmentSlot)

class ComplexLivingEntity(LivingEntity):
    @binding_constructor("org.bukkit.entity.ComplexLivingEntity")
    def __init__(self, delegate: BukkitType):
        self._delegate = delegate

    # TODO: property parts getParts null

class HumanEntity(LivingEntity, AnimalTamer, InventoryHolder):
    @binding_constructor("org.bukkit.entity.HumanEntity")
    def __init__(self, delegate: BukkitType):
        self._delegate = delegate

    # TODO: property open_inventory getOpenInventory null
    # TODO: property fish_hook getFishHook null
    # TODO: property item_on_cursor getItemOnCursor setItemOnCursor
    # TODO: property bed_location getBedLocation null
    # TODO: property discovered_recipes getDiscoveredRecipes null
    saturation = RWProp[float]("getSaturation", "setSaturation")
    saturated_regen_rate = RWProp[int]("getSaturatedRegenRate", "setSaturatedRegenRate")
    is_blocking = ROProp[bool]("isBlocking")
    food_level = RWProp[int]("getFoodLevel", "setFoodLevel")
    # TODO: property game_mode getGameMode setGameMode
    sleep_ticks = ROProp[int]("getSleepTicks")
    exhaustion = RWProp[float]("getExhaustion", "setExhaustion")
    attack_cooldown = ROProp[float]("getAttackCooldown")
    is_deeply_sleeping = ROProp[bool]("isDeeplySleeping")
    # TODO: property potential_bed_location getPotentialBedLocation null
    exp_to_level = ROProp[int]("getExpToLevel")
    enchantment_seed = RWProp[int]("getEnchantmentSeed", "setEnchantmentSeed")
    unsaturated_regen_rate = RWProp[int]("getUnsaturatedRegenRate", "setUnsaturatedRegenRate")
    # TODO: property main_hand getMainHand null
    starvation_rate = RWProp[int]("getStarvationRate", "setStarvationRate")
    # TODO: property ender_chest getEnderChest null
    # TODO: property last_death_location getLastDeathLocation setLastDeathLocation
    # TODO: method public abstract org.bukkit.inventory.InventoryView org.bukkit.entity.HumanEntity.openInventory(org.bukkit.inventory.Inventory)
    # TODO: method public abstract void org.bukkit.entity.HumanEntity.openInventory(org.bukkit.inventory.InventoryView)
    # TODO: method public abstract org.bukkit.inventory.InventoryView org.bukkit.entity.HumanEntity.openStonecutter(org.bukkit.Location,boolean)
    # TODO: method public abstract boolean org.bukkit.entity.HumanEntity.hasCooldown(org.bukkit.Material)
    # TODO: method public abstract org.bukkit.inventory.InventoryView org.bukkit.entity.HumanEntity.openLoom(org.bukkit.Location,boolean)
    # TODO: method public abstract void org.bukkit.entity.HumanEntity.setCooldown(org.bukkit.Material,int)
    # TODO: method public abstract org.bukkit.inventory.InventoryView org.bukkit.entity.HumanEntity.openCartographyTable(org.bukkit.Location,boolean)
    # TODO: method public abstract org.bukkit.inventory.InventoryView org.bukkit.entity.HumanEntity.openWorkbench(org.bukkit.Location,boolean)
    # TODO: method public abstract org.bukkit.inventory.InventoryView org.bukkit.entity.HumanEntity.openSmithingTable(org.bukkit.Location,boolean)
    # TODO: method public abstract void org.bukkit.entity.HumanEntity.openSign(org.bukkit.block.Sign,org.bukkit.block.sign.Side)
    # TODO: method public abstract boolean org.bukkit.entity.HumanEntity.undiscoverRecipe(org.bukkit.NamespacedKey)
    # TODO: method public abstract boolean org.bukkit.entity.HumanEntity.sleep(org.bukkit.Location,boolean)
    # TODO: method public abstract int org.bukkit.entity.HumanEntity.undiscoverRecipes(java.util.Collection<org.bukkit.NamespacedKey>)
    # TODO: method public abstract org.bukkit.entity.Entity org.bukkit.entity.HumanEntity.releaseRightShoulderEntity()
    # TODO: method public abstract org.bukkit.entity.Firework org.bukkit.entity.HumanEntity.fireworkBoost(org.bukkit.inventory.ItemStack)
    # TODO: method public abstract boolean org.bukkit.entity.HumanEntity.discoverRecipe(org.bukkit.NamespacedKey)
    # TODO: method public abstract org.bukkit.inventory.InventoryView org.bukkit.entity.HumanEntity.openEnchanting(org.bukkit.Location,boolean)
    # TODO: method public abstract org.bukkit.inventory.InventoryView org.bukkit.entity.HumanEntity.openAnvil(org.bukkit.Location,boolean)
    # TODO: method public abstract org.bukkit.entity.Entity org.bukkit.entity.HumanEntity.releaseLeftShoulderEntity()
    # TODO: method public abstract void org.bukkit.entity.HumanEntity.wakeup(boolean)
    # TODO: method public abstract int org.bukkit.entity.HumanEntity.getCooldown(org.bukkit.Material)
    # TODO: method public abstract boolean org.bukkit.entity.HumanEntity.setWindowProperty(org.bukkit.inventory.InventoryView$Property,int)
    # TODO: method public abstract int org.bukkit.entity.HumanEntity.discoverRecipes(java.util.Collection<org.bukkit.NamespacedKey>)
    # TODO: method public abstract org.bukkit.inventory.InventoryView org.bukkit.entity.HumanEntity.openMerchant(org.bukkit.entity.Villager,boolean)
    # TODO: method public abstract org.bukkit.inventory.InventoryView org.bukkit.entity.HumanEntity.openMerchant(org.bukkit.inventory.Merchant,boolean)
    def close_inventory(self): self._delegate.closeInventory()
    # TODO: method public abstract void org.bukkit.entity.HumanEntity.closeInventory(org.bukkit.event.inventory.InventoryCloseEvent$Reason)
    # TODO: method public abstract boolean org.bukkit.entity.HumanEntity.dropItem(boolean)
    # TODO: method public abstract org.bukkit.inventory.InventoryView org.bukkit.entity.HumanEntity.openGrindstone(org.bukkit.Location,boolean)
    # TODO: method public abstract boolean org.bukkit.entity.HumanEntity.hasDiscoveredRecipe(org.bukkit.NamespacedKey)
