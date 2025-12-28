from mechs.bindings.persistent import PersistentContainer, PersistentKey
from mechs.types import SimpleMap, Vec3
from mechs.bindings.misc import CommandSender, Emitter, HoverEventSource, Metadatable, Nameable, PersistentDataHolder
from mechs._internal import *
from mechs._internal.mirrors import *
import mechs._internal.bukkit as bukkit
from mechs.scheduler import Scheduler
from _mechthon_builtin import get_api

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
        get_api().scheduleEntityTask(self._delegate.getScheduler(), callback, ticks)

class Entity(Metadatable, CommandSender, Nameable, PersistentDataHolder, HoverEventSource, Emitter):
    @binding_constructor("org.bukkit.entity.Entity")
    def __init__(self, delegate: BukkitType):
        self._delegate = delegate

    # wrappers
    @property
    def scores(self) -> SimpleMap[str, int]:
        if not hasattr(self, '_scores'):
            self._scores = _EntityScoresImpl(self._delegate)
        return self._scores

    @property
    def tags(self) -> set[str]: return self._delegate.getTags()

    @property 
    def scheduler(self) -> Scheduler:     
        if not hasattr(self, '_scheduler'):
            self._scheduler = _EntityScheduler(self._delegate)
        return self._scheduler

    @property
    def persistent_data(self) -> PersistentContainer: ...

    # basic getters 
    @property
    def pos(self):
        loc = self._delegate.getLocation()
        return Vec3(loc.getX(), loc.getY(), loc.getZ())

    def pos_local(self, x: float, y: float, z: float) -> Vec3: ...

    def pos_rel(self, x: float, y: float, z: float) -> Vec3:
        return self.pos.rel(x, y, z)

    # basic flags 
    is_in_lava = ROProp[bool]("isInLava")
    is_under_water = ROProp[bool]("isUnderWater")
    is_in_water = ROProp[bool]("isInWater")
    is_in_water_or_bubble_column = ROProp[bool]("isInWaterOrBubbleColumn")
    is_in_water_or_rain_or_bubble_column = ROProp[bool]("isInWaterOrRainOrBubbleColumn")
    is_in_rain = ROProp[bool]("isInRain")
    is_in_bubble_column = ROProp[bool]("isInBubbleColumn")
    is_inside_vehicle = ROProp[bool]("isInsideVehicle")
    is_in_water_or_rain = ROProp[bool]("isInWaterOrRain")
    is_on_ground = ROProp[bool]("isOnGround")
   
    # fire 
    is_visual_fire = RWProp[bool]("isVisualFire", "setVisualFire")
    fire_ticks = RWProp[int]("getFireTicks", "setFireTicks")
    
    # freezing
    is_frozen = ROProp[bool]("isFrozen")
    freeze_ticks = RWProp[int]("getFreezeTicks", "setFreezeTicks")
    max_freeze_ticks = ROProp[int]("getMaxFreezeTicks")
    is_freeze_ticking_locked = RWProp[bool]("isFreezeTickingLocked", "lockFreezeTicks")
   
    # state flags
    is_in_world = ROProp[bool]("isInWorld")
    is_dead = ROProp[bool]("isDead")
    is_valid = ROProp[bool]("isValid")

    is_invulnerable = RWProp[bool]("isInvulnerable", "setInvulnerable")
    is_visible_by_default = RWProp[bool]("isVisibleByDefault", "setVisibleByDefault")
    is_invisible = RWProp[bool]("isInvisible", "setInvisible")
    is_custom_name_visible = RWProp[bool]("isCustomNameVisible", "setCustomNameVisible")
    is_silent = RWProp[bool]("isSilent", "setSilent")
    is_persistent = RWProp[bool]("isPersistent", "setPersistent")
    is_glowing = RWProp[bool]("isGlowing", "setGlowing")
    is_sneaking = RWProp[bool]("isSneaking", "setSneaking") 
    has_gravity = RWProp[bool]("hasGravity", "setGravity")
    has_no_physics = RWProp[bool]("hasNoPhysics", "setNoPhysics")

    @property
    def rotation(self) -> tuple[float, float]:
        return (self._delegate.getYaw(), self._delegate.getPitch())

    @rotation.setter
    def rotation(self, rot: tuple[float, float]):
        self._delegate.setRotation(rot[0], rot[1])

    # misc
    max_fire_ticks = ROProp[int]("getMaxFireTicks")
    portal_cooldown = RWProp[int]("getPortalCooldown", "setPortalCooldown")
    is_passengerless = ROProp[bool]("isEmpty")
    ticks_lived = RWProp[int]("getTicksLived", "setTicksLived")
    is_in_powdered_snow = ROProp[bool]("isInPowderedSnow")
    is_ticking = ROProp[bool]("isTicking") 
    scoreboard_entry_name = ROProp[str]("getScoreboardEntryName")
    fall_distance = RWProp[float]("getFallDistance", "setFallDistance")
    
    height = ROProp[float]("getHeight")
    width = ROProp[float]("getWidth")

    velocity = TransformedRWProp[Vec3]("getVelocity", "setVelocity", wrap_vector, unwrap_vector)

    unique_id = TransformedROProp[UUID]("getUniqueId", java_uuid_to_python)
    network_id = ROProp[int]("getEntityId")
    
    has_fixed_pose = ROProp[bool]("hasFixedPose")
    
    # TODO: property type getType null
    # TODO: property tracked_by getTrackedBy null
    # TODO: property last_damage_cause getLastDamageCause null
    # TODO: property bounding_box getBoundingBox null
    # TODO: property swim_sound getSwimSound null
    # TODO: property swim_high_speed_splash_sound getSwimHighSpeedSplashSound null
    # TODO: property world getWorld null
    # TODO: property vehicle getVehicle null
    # TODO: property piston_move_reaction getPistonMoveReaction null
    # TODO: property pose getPose setPose
    # TODO: property swim_splash_sound getSwimSplashSound null
    # TODO: property chunk getChunk null
    # TODO: property passengers getPassengers null
    # TODO: property entity_spawn_reason getEntitySpawnReason null
    # TODO: property spawn_category getSpawnCategory null
    # TODO: property origin getOrigin null
    # TODO: property facing getFacing null

    # TODO: method public abstract org.bukkit.Location org.bukkit.entity.Entity.getLocation(org.bukkit.Location)
    # TODO: method public abstract boolean org.bukkit.entity.Entity.spawnAt(org.bukkit.Location,org.bukkit.event.entity.CreatureSpawnEvent$SpawnReason)
    # TODO: method public default boolean org.bukkit.entity.Entity.spawnAt(org.bukkit.Location)
    # TODO: method public abstract boolean org.bukkit.entity.Entity.eject()
    # TODO: method public abstract boolean org.bukkit.entity.Entity.wouldCollideUsing(org.bukkit.util.BoundingBox)
    # TODO: method public abstract org.bukkit.entity.EntitySnapshot org.bukkit.entity.Entity.createSnapshot()
    # TODO: method public abstract boolean org.bukkit.entity.Entity.collidesAt(org.bukkit.Location)
    # TODO: method public abstract net.kyori.adventure.text.Component org.bukkit.entity.Entity.teamDisplayName()
    # TODO: method public abstract void org.bukkit.entity.Entity.playEffect(org.bukkit.EntityEffect)
    # TODO: method public abstract void org.bukkit.entity.Entity.setPose(org.bukkit.entity.Pose,boolean)
    # TODO: method public abstract org.bukkit.entity.Entity org.bukkit.entity.Entity.copy()
    # TODO: method public abstract org.bukkit.entity.Entity org.bukkit.entity.Entity.copy(org.bukkit.Location)
    # TODO: method public abstract boolean org.bukkit.entity.Entity.addPassenger(org.bukkit.entity.Entity)
    # TODO: method public abstract boolean org.bukkit.entity.Entity.leaveVehicle()
    # TODO: method public abstract java.util.List<org.bukkit.entity.Entity> org.bukkit.entity.Entity.getNearbyEntities(double,double,double)
    # TODO: method public abstract boolean org.bukkit.entity.Entity.fromMobSpawner()
    # TODO: method public abstract boolean org.bukkit.entity.Entity.removePassenger(org.bukkit.entity.Entity)

    # TODO: more robust api for tp
    def tp(self, pos: VecLike):
        (x, y, z) = pos
        loc = self._delegate.getLocation()
        loc.set(x, y, z)
        self._delegate.teleport(loc)

    def remove(self): self._delegate.remove()

    # our api
    def invoke(self, script: str) -> Any:
        return get_api().entityInvoke(self._delegate, script)

    def trigger(self, script: str, trigger: str) -> Any:
        return get_api().entityTrigger(self._delegate, script, trigger)
