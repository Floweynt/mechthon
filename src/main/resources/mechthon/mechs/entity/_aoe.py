from typing import Optional
from uuid import UUID

from mechs._internal.mirrors import ROProp, RWProp, TransformedRWProp, java_uuid_to_py, python_uuid_to_java
from mechs.entity._entity import Entity
from mechs._internal import BukkitType, binding_constructor

class AreaEffectCloud(Entity):
    @binding_constructor("org.bukkit.entity.AreaEffectCloud")
    def __init__(self, delegate: BukkitType):
        super().__init__(delegate)

    duration = RWProp[int]("getDuration", "setDuration")
    wait_time = RWProp[int]("getWaitTime", "setWaitTime")
    reapplication_delay = RWProp[int]("getReapplicationDelay", "setReapplicationDelay")
    duration_on_use = RWProp[int]("getDurationOnUse", "setDurationOnUse")

    radius = RWProp[float]("getRadius", "setRadius")
    radius_on_use = RWProp[float]("getRadiusOnUse", "setRadiusOnUse")
    radius_per_tick = RWProp[float]("getRadiusPerTick", "setRadiusPerTick")

    has_custom_effects = ROProp[bool]("hasCustomEffects")

    owner_unique_id = TransformedRWProp[Optional[UUID]](
        "getOwnerUniqueId",
        "setOwnerUniqueId",
        lambda n: None if n is None else java_uuid_to_py(n),
        lambda p: None if p is None else python_uuid_to_java(p)
    )

    # --- TODO: missing composite types ---
    # TODO: getParticle / setParticle / setParticle(Particle, data)
    # TODO: getBasePotionData / setBasePotionData
    # TODO: getBasePotionType / setBasePotionType
    # TODO: getCustomEffects / addCustomEffect / removeCustomEffect / hasCustomEffect / clearCustomEffects
    # TODO: getColor / setColor
    # TODO: getSource / setSource
