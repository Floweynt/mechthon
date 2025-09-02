from mechs.bindings.misc import CommandBlockHolder, InventoryHolder 
from ._categories import Vehicle
from ._misc import LootableEntityInventory
from mechs._internal import *
from mechs._internal.mirrors import *

class Minecart(Vehicle):
    @binding_constructor("org.bukkit.entity.Minecart")
    def __init__(self, delegate: BukkitType):
        self._delegate = delegate

    # TODO: property minecart_material getMinecartMaterial null
    max_speed = RWProp[float]("getMaxSpeed", "setMaxSpeed")
    display_block_offset = RWProp[int]("getDisplayBlockOffset", "setDisplayBlockOffset")
    # TODO: property display_block_data getDisplayBlockData setDisplayBlockData
    flying_velocity_mod = TransformedRWProp[Vector]("getFlyingVelocityMod", "setFlyingVelocityMod", wrap_vector, unwrap_vector)
    derailed_velocity_mod = TransformedRWProp[Vector]("getDerailedVelocityMod", "setDerailedVelocityMod", wrap_vector, unwrap_vector)
    damage = RWProp[float]("getDamage", "setDamage")
    is_slow_when_empty = RWProp[bool]("isSlowWhenEmpty", "setSlowWhenEmpty")

class CommandMinecart(Minecart, CommandBlockHolder):
    @binding_constructor("org.bukkit.entity.minecart.CommandMinecart")
    def __init__(self, delegate: BukkitType):
        self._delegate = delegate

class RideableMinecart(Minecart):
    @binding_constructor("org.bukkit.entity.minecart.RideableMinecart")
    def __init__(self, delegate: BukkitType):
        self._delegate = delegate

class StorageMinecart(Minecart, InventoryHolder, LootableEntityInventory):
    @binding_constructor("org.bukkit.entity.minecart.StorageMinecart")
    def __init__(self, delegate: BukkitType):
            self._delegate = delegate

class PoweredMinecart(Minecart):
    @binding_constructor("org.bukkit.entity.minecart.PoweredMinecart")
    def __init__(self, delegate: BukkitType):
        self._delegate = delegate

    push_x = RWProp[float]("getPushX", "setPushX")
    fuel = RWProp[int]("getFuel", "setFuel")
    push_z = RWProp[float]("getPushZ", "setPushZ")

class ExplosiveMinecart(Minecart):
    @binding_constructor("org.bukkit.entity.minecart.ExplosiveMinecart")
    def __init__(self, delegate: BukkitType):
        self._delegate = delegate

    fuse_ticks = RWProp[int]("getFuseTicks", "setFuseTicks")
    is_ignited = ROProp[bool]("isIgnited")
    def ignite(self): self._delegate.ignite()
    def explode(self): self._delegate.explode()
    # TODO: method public abstract void org.bukkit.entity.minecart.ExplosiveMinecart.explode(double)

class HopperMinecart(Minecart, InventoryHolder, LootableEntityInventory):
    @binding_constructor("org.bukkit.entity.minecart.HopperMinecart")
    def __init__(self, delegate: BukkitType):
        self._delegate = delegate

    is_enabled = RWProp[bool]("isEnabled", "setEnabled")

class SpawnerMinecart(Minecart):
    @binding_constructor("org.bukkit.entity.minecart.SpawnerMinecart")
    def __init__(self, delegate: BukkitType):
        self._delegate = delegate

