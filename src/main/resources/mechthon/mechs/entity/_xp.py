from enum import Enum, auto

from mechs.entity._entity import Entity
from mechs._internal.mirrors import EnumMirror, RWProp, TransformedROProp, make_uuid_mirror_ro

class ExperienceOrb(Entity):
    class SpawnReason(Enum):
        PLAYER_DEATH = auto()
        ENTITY_DEATH = auto()
        FURNACE = auto()
        BREED = auto()
        VILLAGER_TRADE = auto()
        FISHING = auto()
        BLOCK_BREAK = auto()
        CUSTOM = auto()
        EXP_BOTTLE = auto()
        GRINDSTONE = auto()
        UNKNOWN = auto()

    _spawn_reason_mirror = EnumMirror(SpawnReason, "org.bukkit.entity.ExperienceOrb.SpawnReason")

    experience = RWProp[int]("getExperience", "setExperience")
    trigger_entity_id = make_uuid_mirror_ro("getTriggerEntityId")
    source_entity_id = make_uuid_mirror_ro("getSourceEntityId")
    spawn_reason = TransformedROProp[SpawnReason]("getSpawnReason", _spawn_reason_mirror.from_native)
