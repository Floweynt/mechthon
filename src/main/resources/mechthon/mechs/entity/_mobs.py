from mechs.entity._living import LivingEntity
from mechs._internal import BukkitType, binding_constructor
from mechs._internal .mirrors import ROProp, RWProp

class Mob(LivingEntity):
    @binding_constructor("org.bukkit.entity.Mob")
    def __init__(self, delegate: BukkitType):
        super().__init__(delegate)

    # --- Primitive-backed props ---
    head_rotation_speed = ROProp[int]("getHeadRotationSpeed")
    max_head_pitch = ROProp[int]("getMaxHeadPitch")
    aware = RWProp[bool]("isAware", "setAware")
    aggressive = RWProp[bool]("isAggressive", "setAggressive")
    left_handed = RWProp[bool]("isLeftHanded", "setLeftHanded")
    possible_experience_reward = ROProp[int]("getPossibleExperienceReward")
    in_daylight = ROProp[bool]("isInDaylight")

    # --- TODO: missing composite types ---
    # TODO: getEquipment
    # TODO: getPathfinder
    # TODO: lookAt(Location) / lookAt(Location,float,float)
    # TODO: lookAt(Entity) / lookAt(Entity,float,float)
    # TODO: lookAt(x,y,z) / lookAt(x,y,z,float,float)
    # TODO: setTarget / getTarget
    # TODO: getAmbientSound

