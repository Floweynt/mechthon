from enum import Enum, auto
from ._categories import Vehicle
from mechs.bindings.misc import InventoryHolder
from ._misc import LootableEntityInventory
from mechs._internal import *
from mechs._internal.mirrors import *

class Boat(Vehicle):
    @binding_constructor("org.bukkit.entity.Boat")
    def __init__(self, delegate: BukkitType):
        self._delegate = delegate

    class Status(Enum):
        NOT_IN_WORLD = auto()
        IN_WATER = auto()
        UNDER_WATER = auto()
        UNDER_FLOWING_WATER = auto()
        ON_LAND = auto()
        IN_AIR = auto()

    class Type(Enum):
        OAK = auto()
        SPRUCE = auto()
        BIRCH = auto()
        JUNGLE = auto()
        ACACIA = auto()
        CHERRY = auto()
        DARK_OAK = auto()
        MANGROVE = auto()
        BAMBOO = auto()

    _status_enum_mirror = EnumMirror(Status, "org.bukkit.entity.Boat$Status")
    _type_enum_mirror = EnumMirror(Type, "org.bukkit.entity.Boat$Type")

    status = TransformedROProp[Status]("getStatus", _status_enum_mirror.from_native)
    boat_type = TransformedRWProp[Type]("getBoatType", "setBoatType", _type_enum_mirror.from_native, _type_enum_mirror.to_native)

class ChestBoat(Boat, InventoryHolder, LootableEntityInventory):
    @binding_constructor("org.bukkit.entity.ChestBoat")
    def __init__(self, delegate: BukkitType):
        self._delegate = delegate

