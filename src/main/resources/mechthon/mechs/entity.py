from typing import Any, Callable, MutableMapping, NoReturn, cast
from mechs.scheduler import Scheduler
from mechs.types import Message, Pos, PosLike
from _mechthon_builtin import get_api 

class EntityScores:
    def __init__(self, delegate: NoReturn):
        self._delegate = delegate

    def __getitem__(self, name: str) -> int:
        return get_api().entityGetScore(self._delegate, name)

    def __setitem__(self, name: str, value: int):
        return get_api().entitySetScore(self._delegate, name, value)

    def __contains__(self, ent: object):
        if not isinstance(ent, str):
            return False
        return get_api().entityScoreExists(ent)

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

class Entity:
    def __init__(self, delegate: NoReturn):
        self._delegate = cast(Any, delegate)
        self._scores = EntityScores(delegate)
        self._scheduler = _EntityScheduler(delegate)

    @property
    def scores(self) -> EntityScores: return self._scores

    @property
    def tags(self) -> set[str]: return self._delegate.getTags()

    @property
    def scheduler(self) -> Scheduler:
        return self._scheduler

    # position getters

    def pos(self):
        loc = self._delegate.getLocation()
        return Pos(loc.getX(), loc.getY(), loc.getZ())

    def pos_local(self, x: float, y: float, z: float) -> Pos: ...

    def pos_rel(self, x: float, y: float, z: float) -> Pos:
        return self.pos().rel(x, y, z)

    # mutations
    def tp(self, pos: PosLike):
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
        return self._delegate.sendMessage(msg._delegate) # type: ignore

