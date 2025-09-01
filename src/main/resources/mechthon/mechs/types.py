from typing import  NamedTuple, Protocol 
from mechs._internal.mirrors import BukkitWrapperRWProp
from mechs._internal import BukkitType, BukkitWrapper, binding_constructor
from mechs._internal.bukkit import MINI_MESSAGE_INST, PLAIN_TEXT_SERIALIZER_INST

class Message(BukkitWrapper):
    @binding_constructor("net.kyori.adventure.text.Component")
    def __init__(self, delegate: BukkitType):
        super().__init__(delegate)

    def raw(self):
        return PLAIN_TEXT_SERIALIZER_INST.serialize(self._delegate)

    @staticmethod
    def parse_mini(msg: str) -> 'Message':
        return Message(MINI_MESSAGE_INST.deserialize(msg))

class Nameable(BukkitWrapper):
    custom_name = BukkitWrapperRWProp[Message]("customName", "customName", Message)

class Vec3(NamedTuple):
    x: float
    y: float
    z: float

    def rel(self, x: float, y: float, z: float): 
        return Vec3(self.x + x, self.y + y, self.z + z)

class AABB(NamedTuple):
    min: Vec3
    max: Vec3

VecLike = Vec3 | tuple[float, float, float]

class SimpleMap[K, V](Protocol):
    def __getitem__(self, name: K) -> int: ...
    def __setitem__(self, name: K, value: V): ...
    def __contains__(self, name: K) -> bool: ...
