from enum import Enum, auto
from ._entity import Entity
from mechs._internal import *
from mechs._internal.mirrors import *

class Interaction(Entity):
    @binding_constructor("org.bukkit.entity.Interaction")
    def __init__(self, delegate: BukkitType):
        self._delegate = delegate

    # TODO: property last_interaction getLastInteraction null
    is_responsive = RWProp[bool]("isResponsive", "setResponsive")
    interaction_height = RWProp[float]("getInteractionHeight", "setInteractionHeight")
    # TODO: property last_attack getLastAttack null
    interaction_width = RWProp[float]("getInteractionWidth", "setInteractionWidth")

class Display(Entity):
    @binding_constructor("org.bukkit.entity.Display")
    def __init__(self, delegate: BukkitType):
        self._delegate = delegate

    display_height = RWProp[float]("getDisplayHeight", "setDisplayHeight")
    # TODO: property transformation getTransformation setTransformation
    # TODO: property glow_color_override getGlowColorOverride setGlowColorOverride
    # TODO: property brightness getBrightness setBrightness
    teleport_duration = RWProp[int]("getTeleportDuration", "setTeleportDuration")
    display_width = RWProp[float]("getDisplayWidth", "setDisplayWidth")
    view_range = RWProp[float]("getViewRange", "setViewRange")
    shadow_radius = RWProp[float]("getShadowRadius", "setShadowRadius")
    class Billboard(Enum):
        FIXED = auto()
        VERTICAL = auto()
        HORIZONTAL = auto()
        CENTER = auto()

    _billboard_enum_mirror = EnumMirror(Billboard, "org.bukkit.entity.Display$Billboard")
    billboard = TransformedRWProp[Billboard]("getBillboard", "setBillboard", _billboard_enum_mirror.from_native, _billboard_enum_mirror.to_native)
    interpolation_duration = RWProp[int]("getInterpolationDuration", "setInterpolationDuration")
    interpolation_delay = RWProp[int]("getInterpolationDelay", "setInterpolationDelay")
    shadow_strength = RWProp[float]("getShadowStrength", "setShadowStrength")
    # TODO: method public abstract void org.bukkit.entity.Display.setTransformationMatrix(org.joml.Matrix4f)

class BlockDisplay(Display):
    @binding_constructor("org.bukkit.entity.BlockDisplay")
    def __init__(self, delegate: BukkitType):
        self._delegate = delegate

    # TODO: property block getBlock setBlock

class ItemDisplay(Display):
    @binding_constructor("org.bukkit.entity.ItemDisplay")
    def __init__(self, delegate: BukkitType):
        self._delegate = delegate

    class ItemDisplayTransform(Enum):
        NONE = auto()
        THIRDPERSON_LEFTHAND = auto()
        THIRDPERSON_RIGHTHAND = auto()
        FIRSTPERSON_LEFTHAND = auto()
        FIRSTPERSON_RIGHTHAND = auto()
        HEAD = auto()
        GUI = auto()
        GROUND = auto()
        FIXED = auto()

    _item_display_transform_enum_mirror = EnumMirror(ItemDisplayTransform, "org.bukkit.entity.ItemDisplay$ItemDisplayTransform")
    item_display_transform = TransformedRWProp[ItemDisplayTransform]("getItemDisplayTransform", "setItemDisplayTransform", _item_display_transform_enum_mirror.from_native, _item_display_transform_enum_mirror.to_native)
    # TODO: property item_stack getItemStack setItemStack

class TextDisplay(Display):
    @binding_constructor("org.bukkit.entity.TextDisplay")
    def __init__(self, delegate: BukkitType):
        self._delegate = delegate

    class TextAlignment(Enum):
        CENTER = auto()
        LEFT = auto()
        RIGHT = auto()

    _text_alignment_enum_mirror = EnumMirror(TextAlignment, "org.bukkit.entity.TextDisplay$TextAlignment")
    alignment = TransformedRWProp[TextAlignment]("getAlignment", "setAlignment", _text_alignment_enum_mirror.from_native, _text_alignment_enum_mirror.to_native)
    is_see_through = RWProp[bool]("isSeeThrough", "setSeeThrough")
    is_shadowed = RWProp[bool]("isShadowed", "setShadowed")
    line_width = RWProp[int]("getLineWidth", "setLineWidth")
    is_default_background = RWProp[bool]("isDefaultBackground", "setDefaultBackground")
    # TODO: property background_color getBackgroundColor setBackgroundColor
    text_opacity = RWProp[int]("getTextOpacity", "setTextOpacity")
    # TODO: method public abstract void org.bukkit.entity.TextDisplay.text(net.kyori.adventure.text.Component)
    # TODO: method public abstract net.kyori.adventure.text.Component org.bukkit.entity.TextDisplay.text()
