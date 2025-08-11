from abc import abstractmethod
from typing import NamedTuple, Protocol
from _mechthon_internal import get_api # type: ignore

class Message(Protocol):
    @abstractmethod
    def raw(self) -> str: ...

    @staticmethod
    def parse_mini(msg: str) -> 'Message':
        return get_api().componentFromMini(msg)

    @staticmethod
    def from_string(msg: str) -> 'Message':
        return get_api().componentFromString(msg)

class Pos(NamedTuple):
    x: float
    y: float
    z: float

    def rel(self, x: float, y: float, z: float): 
        return Pos(self.x + x, self.y + y, self.z + z)

PosLike = Pos | tuple[int, int, int]

