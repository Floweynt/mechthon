from typing import Any
from _mechthon_internal import get_api # type: ignore
from mechs.types import Message, Pos, PosLike
from mechs.entity import Entity, EntityScores

class EntityScoresImpl(EntityScores):
    def __init__(self, delegate):
        self._delegate = delegate

    def __getitem__(self, name: str) -> int:
        return get_api().entityGetScore(self._delegate, name)

    def __setitem__(self, name: str, value: int):
        return get_api().entitySetScore(self._delegate, name, value)

    def __contains__(self, ent: str):
        return get_api().entityScoreExists(ent)

class EntityImpl(Entity):
    def __init__(self, delegate):
        self._delegate = delegate
        self._scores = EntityScoresImpl(delegate)

    @property
    def scores(self) -> EntityScores: return self._scores

    @property
    def tags(self) -> set[str]: return self._delegate.getTags()

    def pos(self):
        loc = self._delegate.getLocation()
        return Pos(loc.getX(), loc.getY(), loc.getZ())

    def pos_local(self, x: float, y: float, z: float) -> Pos: ...

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
            msg = Message.from_string(msg)
        return self._delegate.sendMessage(msg._delegate) # type: ignore
