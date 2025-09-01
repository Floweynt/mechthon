from typing import Any, Callable 
from mechs.entity._entity_type import ENTITY_TYPE_ENUM, EntityType
from mechs._internal.mirrors import ROProp, RWProp, TransformedROProp
from mechs._internal import BukkitType, binding_constructor 
from mechs.world import World
from mechs.scheduler import Scheduler
from mechs.types import AABB, Message, Nameable, Vec3, VecLike, SimpleMap
from _mechthon_builtin import get_api 
import mechs._internal.bukkit as bukkit

class _EntityScoresImpl:
    def __init__(self, delegate: BukkitType):
        self._delegate = delegate

    def __getitem__(self, name: str) -> int:
        objective = bukkit.Bukkit.getScoreboardManager().getMainScoreboard().getObjective(name);
        if objective is None:
            raise ValueError(f"score {name} does not exist!")
        return objective.getScoreFor(self._delegate).getScore();

    def __setitem__(self, name: str, value: int):
        objective = bukkit.Bukkit.getScoreboardManager().getMainScoreboard().getObjective(name);
        if objective is None:
            raise ValueError(f"score {name} does not exist!")
        return objective.getScoreFor(self._delegate).setScore(value);

    def __contains__(self, name: str):
        return bukkit.Bukkit.getScoreboardManager().getMainScoreboard().getObjective(name) is not None

class _EntityScheduler(Scheduler):
    def __init__(self, delegate):
        self._delegate = delegate

    def schedule(self, callback: Callable[[], Any], ticks: int):
        self._delegate.getScheduler().execute(
            get_api().plugin(),
            callback,
            None,
            ticks
        )

class Entity(Nameable):
    @binding_constructor("org.bukkit.entity.Entity")
    def __init__(self, delegate: BukkitType):
        super().__init__(delegate)
        self._scores = _EntityScoresImpl(delegate)
        self._scheduler = _EntityScheduler(delegate)

    # wrappers
    @property
    def scores(self) -> SimpleMap[str, int]: return self._scores

    @property
    def tags(self) -> set[str]: return self._delegate.getTags()

    @property
    def scheduler(self) -> Scheduler:
        return self._scheduler

    # simple mirrors
    @property
    def velocity(self) -> Vec3:
        vel = self._delegate.getVelocity()
        return Vec3(vel.getX(), vel.getY(), vel.getZ())

    @velocity.setter
    def velocity(self, vel: VecLike):
        (x, y, z) = vel
        self._delegate.setVelocity(bukkit.Vector(x, y, z))

    height = ROProp[float]("getHeight")
    width = ROProp[float]("getWidth")

    @property
    def bounding_box(self) -> AABB:
        bb = self._delegate.getBoundingBox()
        return AABB(Vec3(bb.getMinX(), bb.getMinY(), bb.getMinZ()), Vec3(bb.getMaxX(), bb.getMaxY(), bb.getMaxZ()))

    is_on_ground = ROProp[bool]("isOnGround")
    is_in_water = ROProp[bool]("isInWater")
    world = TransformedROProp[World]("getWorld", World)

    @property
    def rotation(self) -> tuple[float, float]: return (self._delegate.getYaw(), self._delegate.getPitch())

    @rotation.setter
    def rotation(self, rot: tuple[float, float]): self._delegate.setRotation(rot[0], rot[1])

    @property
    def uuid(self) -> str: return self._delegate.getUniqueId().toString()

    # TODO: getNearbyEntities

    network_id = ROProp[int]("getEntityId")

    fire_ticks = RWProp[int]("getFireTicks", "setFireTicks")
    max_fire_ticks = ROProp[int]("getMaxFireTicks")
    is_visual_fire = RWProp[bool]("isVisualFire", "setVisualFire")

    powdered_snow_ticks = RWProp[int]("getFreezeTicks", "setFreezeTicks")
    max_powdered_snow_ticks = ROProp[int]("getMaxFreezeTicks")
    lock_powered_snow_ticks = RWProp[bool]("isFreezeTickingLocked", "lockFreezeTicks")
    is_frozen = ROProp[bool]("isFrozen")
    is_in_powdered_snow = ROProp[bool]("isInPowderedSnow")

    is_invisible = RWProp[bool]("isInvisible", "setInvisible")
    has_no_physics = RWProp[bool]("hasNoPhysics", "setNoPhysics")

    def remove(self): self._delegate.remove()

    is_dead = ROProp[bool]("isDead")
    is_valid = ROProp[bool]("isValid")
    is_persistent = RWProp[bool]("isPersistent", "setPersistent")

    # TODO: getPassengers
    
    def add_passenger(self, entity: 'Entity') -> bool: return self._delegate.addPassenger(entity._delegate)
    def remove_passenger(self, entity: 'Entity') -> bool: return self._delegate.removePassenger(entity._delegate)

    has_passengers = ROProp[bool]("isEmpty")

    def eject_passengers(self) -> bool: return self._delegate.eject()

    fall_distance = RWProp[float]("getFallDistance", "setFallDistance")
    ticks_lived = RWProp[int]("getTicksLived", "setTicksLived")
    
    # TODO: playEffect

    type = TransformedROProp[EntityType]("getType", ENTITY_TYPE_ENUM.from_native)

    # TODO: getSwimSound, getSwimSplashSound, getSwimHighSpeedSplashSound

    is_inside_vehicle = ROProp[bool]("isInsideVehicle")

    def leave_vehicle(self) -> bool: return self._delegate.leaveVehicle()

    vehicle = TransformedROProp['Entity | None']("getVehicle", lambda x: None if x is None else Entity(x))

    is_custom_name_visible = RWProp[bool]("isCustomNameVisible", "setCustomNameVisible")
    is_visible_by_default = RWProp[bool]("isVisibleByDefault", "setVisibleByDefault")
    is_glowing = RWProp[bool]("isGlowing", "setGlowing")
    is_invulnerable = RWProp[bool]("isInvulnerable", "setInvulnerable") 
    is_silent = RWProp[bool]("isSilent", "setSilent")
    has_gravity = RWProp[bool]("hasGravity", "setGravity")
    portal_cooldown = RWProp[int]("getPortalCooldown", "setPortalCooldown")

    # TODO: getPistonMoveReaction, getFacing, getPose, getSpawnCategory

    # TODO: createSnapshot, copy, spawnAt

    is_sneaking = RWProp[bool]("isSneaking", "setSneaking")
    is_from_mob_spawner = ROProp[bool]("fromMobSpawner")
    has_fixed_pose = ROProp[bool]("hasFixedPose")
    is_in_world = ROProp[bool]("isInWorld")
    is_under_water = ROProp[bool]("isUnderWater")
    is_in_rain = ROProp[bool]("isInRain")
    is_in_bubble_column = ROProp[bool]("isInBubbleColumn")
    is_in_water_or_rain = ROProp[bool]("isInWaterOrRain")
    is_in_water_or_bubble_column = ROProp[bool]("isInWaterOrBubbleColumn")
    is_in_water_or_rain_or_bubble_column = ROProp[bool]("isInWaterOrRainOrBubbleColumn")
    is_in_lava = ROProp[bool]("isInLava")
    is_ticking = ROProp[bool]("isTicking")
    scoreboard_entry_name = ROProp[str]("getScoreboardEntryName")
 
    # position getters
    def pos(self):
        return Vec3(self._delegate.getX(), self._delegate.getY(), self._delegate.getZ())

    def pos_local(self, x: float, y: float, z: float) -> Vec3: ...

    def pos_rel(self, x: float, y: float, z: float) -> Vec3:
        return self.pos().rel(x, y, z)

    # mutations
    def tp(self, pos: VecLike):
        (x, y, z) = pos
        loc = self._delegate.getLocation()
        loc.set(x, y, z)
        self._delegate.teleport(loc)

    def invoke(self, script: str) -> Any:
        return get_api().entityInvoke(self._delegate, script)

    def trigger(self, script: str, trigger: str) -> Any:
        return get_api().entityTrigger(self._delegate, script, trigger)

    def tell(self, msg: Message | str):
        if isinstance(msg, str):
            msg = Message.parse_mini(msg)
        return self._delegate.sendMessage(msg._delegate)
