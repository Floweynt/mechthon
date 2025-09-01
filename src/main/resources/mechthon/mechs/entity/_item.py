from mechs._internal.mirrors import RWProp, make_uuid_mirror_rw
from mechs.entity._entity import Entity
from mechs.entity._misc import Frictional

class ItemEntity(Entity, Frictional):
    # TODO getItemStack/setItemStack

    pickup_delay = RWProp[int]("getPickupDelay", "setPickupDelay")
    is_unlimited_lifetime = RWProp[bool]("isUnlimitedLifetime", "setUnlimitedLifetime")
    owner = make_uuid_mirror_rw("getOwner", "setOwner")
    thrower = make_uuid_mirror_rw("getThrower", "setThrower")
    can_mob_pickup = RWProp[bool]("canMobPickup", "setCanMobPickup")
    can_player_pickup = RWProp[bool]("canPlayerPickup", "setCanPlayerPickup")
    will_tick_despawn = RWProp[bool]("willAge", "setWillAge")
    health = RWProp[int]("getHealth", "setHealth")
