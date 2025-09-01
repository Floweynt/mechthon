from mechs.entity._living import LivingEntity
from mechs.entity._mobs import Mob
from mechs._internal.bukkit import UNWRAP_TRISTATE, WRAP_TRISTATE, Vector
from mechs.types import VecLike
from mechs._internal.mirrors import RWProp
from mechs._internal import BukkitType, BukkitWrapper, binding_constructor
from mechs.entity._entity import Entity

class Explosive(Entity):
    @binding_constructor("org.bukkit.entity.Explosive")
    def __init__(self, delegate: BukkitType):
        super().__init__(delegate)

    explosive_yield = RWProp[float]("getYield", "setYield")
    is_incendiary = RWProp[bool]("isIncendiary", "setIsIncendiary")

class Projectile(Entity):
    @binding_constructor("org.bukkit.entity.Projectile")
    def __init__(self, delegate: BukkitType):
        super().__init__(delegate)

    # TODO: getShooter
    has_left_shooter = RWProp[bool]("hasLeftShooter", "setHasLeftShooter")
    has_been_shot = RWProp[bool]("hasBeenShot", "setHasBeenShot")

    @property
    def owner_uuid(self) -> str | None: 
        res = self._delegate.getOwnerUniqueId()
        return None if res is None else res.toString()

    def can_hit(self, entity: Entity) -> bool:
        return self._delegate.canHitEntity(entity._delegate)

    def hit(self, entity: Entity):
        self._delegate.hitEntity(entity._delegate)

    def hit_from(self, entity: Entity, vec: VecLike):
        (x, y, z) = vec
        self._delegate.hitEntity(entity._delegate, Vector(x, y, z))

class Frictional(BukkitWrapper):
    @binding_constructor("io.papermc.paper.entity.Frictional")
    def __init__(self, delegate: BukkitType) -> None:
        super().__init__(delegate)

    @property
    def friction_state(self) -> bool | None:
        return UNWRAP_TRISTATE[self._delegate.getFrictionState()]

    @friction_state.setter
    def friction_state(self, value: bool | None):
        self._delegate.setFrictionState(WRAP_TRISTATE[value])

class Creature(Mob):
    @binding_constructor("org.bukkit.entity.Creature")
    def __init__(self, delegate: BukkitType):
        super().__init__(delegate)

class Enemy(LivingEntity):
    @binding_constructor("org.bukkit.entity.Enemy")
    def __init__(self, delegate: BukkitType):
        super().__init__(delegate)


