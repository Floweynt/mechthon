from mechs.entity._aoe import AreaEffectCloud
from mechs.entity._xp import ExperienceOrb
from mechs.entity._item import ItemEntity
from mechs.entity._entity_type import EntityType

_MAPPINGS = {
    EntityType.DROPPED_ITEM: ItemEntity,
    EntityType.EXPERIENCE_ORB: ExperienceOrb,
    EntityType.AREA_EFFECT_CLOUD: AreaEffectCloud 
}
