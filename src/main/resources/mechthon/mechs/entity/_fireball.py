from mechs._internal import BukkitType, binding_constructor
from mechs._internal.bukkit import unwrap_vector, wrap_vector
from mechs._internal.mirrors import RWProp, TransformedRWProp
from mechs.entity._misc import Explosive, Projectile

class Fireball(Projectile, Explosive):
    @binding_constructor("org.bukkit.entity.Fireball")
    def __init__(self, delegate: BukkitType):
        super().__init__(delegate)

    direction = TransformedRWProp("getDirection", "setDirection", wrap_vector, unwrap_vector)
    power = TransformedRWProp("getPower", "setPower", wrap_vector, unwrap_vector)

class DragonFireball(Fireball):
    @binding_constructor("org.bukkit.entity.DragonFireball")
    def __init__(self, delegate: BukkitType):
        super().__init__(delegate)

class SizedFireball(Fireball):
    @binding_constructor("org.bukkit.entity.SizedFireball")
    def __init__(self, delegate: BukkitType):
        super().__init__(delegate)

    # TODO: getDisplayItem/setDisplayItem

class LargeFireball(SizedFireball):
    @binding_constructor("org.bukkit.entity.LargeFireball")
    def __init__(self, delegate: BukkitType):
        super().__init__(delegate)

class SmallFireball(SizedFireball):
    @binding_constructor("org.bukkit.entity.SmallFireball")
    def __init__(self, delegate: BukkitType):
        super().__init__(delegate)

class WindCharge(Fireball):
    @binding_constructor("org.bukkit.entity.WindCharge")
    def __init__(self, delegate: BukkitType):
        super().__init__(delegate)

    def explode(self): self._delegate.explode()

class WitherSkull(Fireball):
    @binding_constructor("org.bukkit.entity.Fireball")
    def __init__(self, delegate: BukkitType):
        super().__init__(delegate)

    charged = RWProp[bool]("isCharged", "setCharged")

