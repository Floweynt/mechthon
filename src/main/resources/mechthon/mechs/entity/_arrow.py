from mechs.entity._misc import Projectile
from mechs._internal import BukkitType, binding_constructor
from mechs._internal.mirrors import ROProp, RWProp

class AbstractArrow(Projectile):
    @binding_constructor("org.bukkit.entity.AbstractArrow")
    def __init__(self, delegate: BukkitType):
        super().__init__(delegate)

    kb_strength = RWProp[int]("getKnockbackStrength", "setKnockbackStrength")
    damage = RWProp[float]("getDamage", "setDamage")
    pierce_level = RWProp[int]("getPierceLevel", "setPierceLevel")
    is_crit = RWProp[bool]("isCritical", "setCritical")

    is_in_block = ROProp[bool]("isInBlock")

    # TODO: getAttachedBlock, getPickupStatus, getItemStack, get/set HitSound

    shot_from_crossbow = RWProp[bool]("isShotFromCrossbow", "setShotFromCrossbow")
    lifetime_ticks = RWProp[int]("getLifetimeTicks", "setLifetimeTicks")

class Arrow(AbstractArrow):
    @binding_constructor("org.bukkit.entity.Arrow")
    def __init__(self, delegate: BukkitType):
        super().__init__(delegate)

    # TODO: setBasePotionType, getBasePotionType, 

    # TODO: getColor, setColor

    # TODO: EFFECT SLOP!!!
    pass

class SpectralArrow(AbstractArrow):
    @binding_constructor("org.bukkit.entity.SpectralArrow")
    def __init__(self, delegate: BukkitType):
        super().__init__(delegate)

    glowing_ticks = RWProp[int]("getGlowingTicks", "setGlowingTicks")

class Trident(AbstractArrow):
    @binding_constructor("org.bukkit.entity.Trident")
    def __init__(self, delegate: BukkitType):
        super().__init__(delegate)

    has_glint = RWProp[bool]("hasGlint", "setGlint")
    loyalty_level = RWProp[int]("getLoyaltyLevel", "setLoyaltyLevel")
    has_dealt_damage = RWProp[bool]("hasDealtDamage", "setHasDealtDamage")

