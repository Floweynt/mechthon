from enum import Enum, auto
from ._categories import * 
from mechs.bindings.misc import Colorable, InventoryHolder
from mechs._internal import *
from mechs._internal.mirrors import *
from ._mappings import wrap_entity

class Strider(Steerable, Vehicle):
    @binding_constructor("org.bukkit.entity.Strider")
    def __init__(self, delegate: BukkitType):
        self._delegate = delegate

    is_shivering = RWProp[bool]("isShivering", "setShivering")

class Fox(Animals, Sittable):
    @binding_constructor("org.bukkit.entity.Fox")
    def __init__(self, delegate: BukkitType):
        self._delegate = delegate

    class Type(Enum):
        RED = auto()
        SNOW = auto()
    _type_enum_mirror = EnumMirror(Type, "org.bukkit.entity.Fox$Type")

    fox_type = TransformedRWProp[Type]("getFoxType", "setFoxType", _type_enum_mirror.from_native, _type_enum_mirror.to_native)

    is_leaping = RWProp[bool]("isLeaping", "setLeaping")
    is_crouching = RWProp[bool]("isCrouching", "setCrouching")
    is_faceplanted = RWProp[bool]("isFaceplanted", "setFaceplanted")
    is_defending = RWProp[bool]("isDefending", "setDefending")
    is_interested = RWProp[bool]("isInterested", "setInterested")

    def set_sleeping(self, sleeping: bool = True): self._delegate.setSleeping(sleeping)

    # TODO: property first_trusted_player getFirstTrustedPlayer setFirstTrustedPlayer
    # TODO: property second_trusted_player getSecondTrustedPlayer setSecondTrustedPlayer

class Sniffer(Animals):
    @binding_constructor("org.bukkit.entity.Sniffer")
    def __init__(self, delegate: BukkitType):
        self._delegate = delegate

    can_dig = ROProp[bool]("canDig")
    class State(Enum):
        IDLING = auto()
        FEELING_HAPPY = auto()
        SCENTING = auto()
        SNIFFING = auto()
        SEARCHING = auto()
        DIGGING = auto()
        RISING = auto()

    _state_enum_mirror = EnumMirror(State, "org.bukkit.entity.Sniffer$State")
    state = TransformedRWProp[State]("getState", "setState", _state_enum_mirror.from_native, _state_enum_mirror.to_native)
    # TODO: property explored_locations getExploredLocations null
    # TODO: method public abstract void org.bukkit.entity.Sniffer.removeExploredLocation(org.bukkit.Location)
    # TODO: method public abstract void org.bukkit.entity.Sniffer.addExploredLocation(org.bukkit.Location)
    # TODO: method public abstract org.bukkit.Location org.bukkit.entity.Sniffer.findPossibleDigLocation()

class Rabbit(Animals):
    @binding_constructor("org.bukkit.entity.Rabbit")
    def __init__(self, delegate: BukkitType):
        self._delegate = delegate

    more_carrot_ticks = RWProp[int]("getMoreCarrotTicks", "setMoreCarrotTicks")
    class Type(Enum):
        BROWN = auto()
        WHITE = auto()
        BLACK = auto()
        BLACK_AND_WHITE = auto()
        GOLD = auto()
        SALT_AND_PEPPER = auto()
        THE_KILLER_BUNNY = auto()

    _type_enum_mirror = EnumMirror(Type, "org.bukkit.entity.Rabbit$Type")
    rabbit_type = TransformedRWProp[Type]("getRabbitType", "setRabbitType", _type_enum_mirror.from_native, _type_enum_mirror.to_native)

class Cat(Tameable, Sittable, CollarColorable):
    @binding_constructor("org.bukkit.entity.Cat")
    def __init__(self, delegate: BukkitType):
        self._delegate = delegate

    is_head_up = RWProp[bool]("isHeadUp", "setHeadUp")
    class Type(Enum):
        TABBY = auto()
        BLACK = auto()
        RED = auto()
        SIAMESE = auto()
        BRITISH_SHORTHAIR = auto()
        CALICO = auto()
        PERSIAN = auto()
        RAGDOLL = auto()
        WHITE = auto()
        JELLIE = auto()
        ALL_BLACK = auto()

    _type_enum_mirror = EnumMirror(Type, "org.bukkit.entity.Cat$Type")
    cat_type = TransformedRWProp[Type]("getCatType", "setCatType", _type_enum_mirror.from_native, _type_enum_mirror.to_native)
    is_lying_down = RWProp[bool]("isLyingDown", "setLyingDown")

class Turtle(Animals):
    @binding_constructor("org.bukkit.entity.Turtle")
    def __init__(self, delegate: BukkitType):
        self._delegate = delegate

    has_egg = RWProp[bool]("hasEgg", "setHasEgg")
    is_going_home = ROProp[bool]("isGoingHome")
    is_laying_egg = ROProp[bool]("isLayingEgg")
    is_digging = ROProp[bool]("isDigging")
    # TODO: property home getHome setHome

class Frog(Animals):
    @binding_constructor("org.bukkit.entity.Frog")
    def __init__(self, delegate: BukkitType):
        self._delegate = delegate

    class Variant(Enum):
        TEMPERATE = auto()
        WARM = auto()
        COLD = auto()

    _variant_enum_mirror = EnumMirror(Variant, "org.bukkit.entity.Frog$Variant")
    variant = TransformedRWProp[Variant]("getVariant", "setVariant", _variant_enum_mirror.from_native, _variant_enum_mirror.to_native)
    tongue_target = TransformedRWProp[Optional[Entity]](
        "getTongueTarget", "setTongueTarget",
        lambda x: None if x is None else wrap_entity(x),
        lambda x: None if x is None else x._delegate 
    )

class Pig(Steerable, Vehicle):
    @binding_constructor("org.bukkit.entity.Pig")
    def __init__(self, delegate: BukkitType):
        self._delegate = delegate

class Bee(Animals):
    @binding_constructor("org.bukkit.entity.Bee")
    def __init__(self, delegate: BukkitType):
        self._delegate = delegate

    anger = RWProp[int]("getAnger", "setAnger")
    rolling_override = TransformedRWProp[Optional[bool]](
        "getRollingOverride",
        "setRollingOverride",
        lambda x: UNWRAP_TRISTATE[x],
        lambda x: WRAP_TRISTATE[x]
    )

    has_nectar = RWProp[bool]("hasNectar", "setHasNectar")
    # TODO: property flower getFlower setFlower
    # TODO: property hive getHive setHive
    has_stung = RWProp[bool]("hasStung", "setHasStung")
    is_rolling = ROProp[bool]("isRolling")
    crops_grown_since_pollination = RWProp[int]("getCropsGrownSincePollination", "setCropsGrownSincePollination")
    ticks_since_pollination = RWProp[int]("getTicksSincePollination", "setTicksSincePollination")
    cannot_enter_hive_ticks = RWProp[int]("getCannotEnterHiveTicks", "setCannotEnterHiveTicks")

class AbstractHorse(Vehicle, InventoryHolder, Tameable):
    @binding_constructor("org.bukkit.entity.AbstractHorse")
    def __init__(self, delegate: BukkitType):
        self._delegate = delegate

    is_eating = RWProp[bool]("isEating", "setEating")
    domestication = RWProp[int]("getDomestication", "setDomestication")
    is_eating_grass = RWProp[bool]("isEatingGrass", "setEatingGrass")
    max_domestication = RWProp[int]("getMaxDomestication", "setMaxDomestication")
    is_rearing = RWProp[bool]("isRearing", "setRearing")
    jump_strength = RWProp[float]("getJumpStrength", "setJumpStrength")

class Sheep(Animals, Colorable, Shearable):
    @binding_constructor("org.bukkit.entity.Sheep")
    def __init__(self, delegate: BukkitType):
        self._delegate = delegate

    is_sheared = RWProp[bool]("isSheared", "setSheared")

class Cow(Animals):
    @binding_constructor("org.bukkit.entity.Cow")
    def __init__(self, delegate: BukkitType):
        self._delegate = delegate


class Goat(Animals):
    @binding_constructor("org.bukkit.entity.Goat")
    def __init__(self, delegate: BukkitType):
        self._delegate = delegate

    has_left_horn = RWProp[bool]("hasLeftHorn", "setLeftHorn")
    has_right_horn = RWProp[bool]("hasRightHorn", "setRightHorn")
    is_screaming = RWProp[bool]("isScreaming", "setScreaming")

    def ram(self, target: LivingEntity): self._delegate.ram(target._delegate)

class Ocelot(Animals):
    @binding_constructor("org.bukkit.entity.Ocelot")
    def __init__(self, delegate: BukkitType):
        self._delegate = delegate

    is_trusting = RWProp[bool]("isTrusting", "setTrusting")

class Axolotl(Animals, Bucketable):
    @binding_constructor("org.bukkit.entity.Axolotl")
    def __init__(self, delegate: BukkitType):
        self._delegate = delegate

    class Variant(Enum):
        LUCY = auto()
        WILD = auto()
        GOLD = auto()
        CYAN = auto()
        BLUE = auto()

    _variant_enum_mirror = EnumMirror(Variant, "org.bukkit.entity.Axolotl$Variant")
    variant = TransformedRWProp[Variant]("getVariant", "setVariant", _variant_enum_mirror.from_native, _variant_enum_mirror.to_native)
    is_playing_dead = RWProp[bool]("isPlayingDead", "setPlayingDead")

class Chicken(Animals):
    @binding_constructor("org.bukkit.entity.Chicken")
    def __init__(self, delegate: BukkitType):
        self._delegate = delegate

    egg_lay_time = RWProp[int]("getEggLayTime", "setEggLayTime")
    is_chicken_jockey = RWProp[bool]("isChickenJockey", "setIsChickenJockey")

class Panda(Animals, Sittable):
    @binding_constructor("org.bukkit.entity.Panda")
    def __init__(self, delegate: BukkitType):
        self._delegate = delegate

    is_eating = RWProp[bool]("isEating", "setEating")
    unhappy_ticks = RWProp[int]("getUnhappyTicks", "setUnhappyTicks")
    is_scared = ROProp[bool]("isScared")
    is_sneezing = RWProp[bool]("isSneezing", "setSneezing")
    is_rolling = RWProp[bool]("isRolling", "setRolling")
    class Gene(Enum):
        NORMAL = auto()
        LAZY = auto()
        WORRIED = auto()
        PLAYFUL = auto()
        BROWN = auto()
        WEAK = auto()
        AGGRESSIVE = auto()

    _gene_enum_mirror = EnumMirror(Gene, "org.bukkit.entity.Panda$Gene")
    hidden_gene = TransformedRWProp[Gene]("getHiddenGene", "setHiddenGene", _gene_enum_mirror.from_native, _gene_enum_mirror.to_native)
    is_on_back = RWProp[bool]("isOnBack", "setOnBack")
    eating_ticks = RWProp[int]("getEatingTicks", "setEatingTicks")
    main_gene = TransformedRWProp[Gene]("getMainGene", "setMainGene", _gene_enum_mirror.from_native, _gene_enum_mirror.to_native)
    sneeze_ticks = RWProp[int]("getSneezeTicks", "setSneezeTicks")
    combined_gene = TransformedROProp[Gene]("getCombinedGene", _gene_enum_mirror.from_native)

class Wolf(Tameable, Sittable, CollarColorable):
    @binding_constructor("org.bukkit.entity.Wolf")
    def __init__(self, delegate: BukkitType):
        self._delegate = delegate

    tail_angle = ROProp[float]("getTailAngle")
    is_angry = RWProp[bool]("isAngry", "setAngry")
    is_wet = ROProp[bool]("isWet")
    is_interested = RWProp[bool]("isInterested", "setInterested")

class PolarBear(Animals):
    @binding_constructor("org.bukkit.entity.PolarBear")
    def __init__(self, delegate: BukkitType):
        self._delegate = delegate

    is_standing = RWProp[bool]("isStanding", "setStanding")

class Parrot(Tameable, Sittable):
    @binding_constructor("org.bukkit.entity.Parrot")
    def __init__(self, delegate: BukkitType):
        self._delegate = delegate

    class Variant(Enum):
        RED = auto()
        BLUE = auto()
        GREEN = auto()
        CYAN = auto()
        GRAY = auto()

    _variant_enum_mirror = EnumMirror(Variant, "org.bukkit.entity.Parrot$Variant")
    variant = TransformedRWProp[Variant]("getVariant", "setVariant", _variant_enum_mirror.from_native, _variant_enum_mirror.to_native)
    is_dancing = ROProp[bool]("isDancing")

class SkeletonHorse(AbstractHorse):
    @binding_constructor("org.bukkit.entity.SkeletonHorse")
    def __init__(self, delegate: BukkitType):
        self._delegate = delegate

    trap_time = RWProp[int]("getTrapTime", "setTrapTime")
    is_trapped = RWProp[bool]("isTrapped", "setTrapped")

class ZombieHorse(AbstractHorse):
    @binding_constructor("org.bukkit.entity.ZombieHorse")
    def __init__(self, delegate: BukkitType):
        self._delegate = delegate

class ChestedHorse(AbstractHorse):
    @binding_constructor("org.bukkit.entity.ChestedHorse")
    def __init__(self, delegate: BukkitType):
        self._delegate = delegate

    is_carrying_chest = RWProp[bool]("isCarryingChest", "setCarryingChest")

class Horse(AbstractHorse):
    @binding_constructor("org.bukkit.entity.Horse")
    def __init__(self, delegate: BukkitType):
        self._delegate = delegate

    class Color(Enum):
        WHITE = auto()
        CREAMY = auto()
        CHESTNUT = auto()
        BROWN = auto()
        BLACK = auto()
        GRAY = auto()
        DARK_BROWN = auto()

    _color_enum_mirror = EnumMirror(Color, "org.bukkit.entity.Horse$Color")
    color = TransformedRWProp[Color]("getColor", "setColor", _color_enum_mirror.from_native, _color_enum_mirror.to_native)
    class Style(Enum):
        NONE = auto()
        WHITE = auto()
        WHITEFIELD = auto()
        WHITE_DOTS = auto()
        BLACK_DOTS = auto()

    _style_enum_mirror = EnumMirror(Style, "org.bukkit.entity.Horse$Style")
    style = TransformedRWProp[Style]("getStyle", "setStyle", _style_enum_mirror.from_native, _style_enum_mirror.to_native)

class Camel(AbstractHorse, Sittable):
    @binding_constructor("org.bukkit.entity.Camel")
    def __init__(self, delegate: BukkitType):
        self._delegate = delegate

    is_dashing = RWProp[bool]("isDashing", "setDashing")

class MushroomCow(Cow, Shearable):
    @binding_constructor("org.bukkit.entity.MushroomCow")
    def __init__(self, delegate: BukkitType):
        self._delegate = delegate

    class Variant(Enum):
        RED = auto()
        BROWN = auto()

    _variant_enum_mirror = EnumMirror(Variant, "org.bukkit.entity.MushroomCow$Variant")
    variant = TransformedRWProp[Variant]("getVariant", "setVariant", _variant_enum_mirror.from_native, _variant_enum_mirror.to_native)
    # TODO: property stew_effects getStewEffects setStewEffects
    # TODO: method public abstract boolean org.bukkit.entity.MushroomCow.addEffectToNextStew(io.papermc.paper.potion.SuspiciousEffectEntry,boolean)
    # TODO: method public abstract boolean org.bukkit.entity.MushroomCow.hasEffectsForNextStew()
    # TODO: method public abstract java.util.List<org.bukkit.potion.PotionEffect> org.bukkit.entity.MushroomCow.getEffectsForNextStew()
    # TODO: method public abstract boolean org.bukkit.entity.MushroomCow.removeEffectFromNextStew(org.bukkit.potion.PotionEffectType)
    # TODO: method public abstract boolean org.bukkit.entity.MushroomCow.hasEffectForNextStew(org.bukkit.potion.PotionEffectType)
    def clear_effects_for_next_stew(self): self._delegate.clearEffectsForNextStew()

class Donkey(ChestedHorse):
    @binding_constructor("org.bukkit.entity.Donkey")
    def __init__(self, delegate: BukkitType):
        self._delegate = delegate

class Mule(ChestedHorse):
    @binding_constructor("org.bukkit.entity.Mule")
    def __init__(self, delegate: BukkitType):
        self._delegate = delegate

class Llama(ChestedHorse, RangedEntity):
    @binding_constructor("org.bukkit.entity.Llama")
    def __init__(self, delegate: BukkitType):
        self._delegate = delegate

    class Color(Enum):
        CREAMY = auto()
        WHITE = auto()
        BROWN = auto()
        GRAY = auto()

    _color_enum_mirror = EnumMirror(Color, "org.bukkit.entity.Llama$Color")
    color = TransformedRWProp[Color]("getColor", "setColor", _color_enum_mirror.from_native, _color_enum_mirror.to_native)
    strength = RWProp[int]("getStrength", "setStrength")

    def leave_caravan(self): self._delegate.leaveCaravan()
    # TODO: property caravan_head getCaravanHead null
    # TODO: method public abstract org.bukkit.entity.Llama org.bukkit.entity.Llama.getCaravanTail()
    # TODO: method public abstract void org.bukkit.entity.Llama.joinCaravan(org.bukkit.entity.Llama)
    # TODO: method public abstract boolean org.bukkit.entity.Llama.inCaravan()
    # TODO: method public abstract boolean org.bukkit.entity.Llama.hasCaravanTail()

class TraderLlama(Llama):
    @binding_constructor("org.bukkit.entity.TraderLlama")
    def __init__(self, delegate: BukkitType):
        self._delegate = delegate

