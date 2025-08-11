from typing import Any, NamedTuple, NoReturn, cast
from _mechthon_builtin import get_api

class Message:
    def __init__(self, delegate: NoReturn):
        self._delegate = cast(Any, delegate)

    def raw(self):
        return get_api().componentToRaw(self._delegate)

    @staticmethod
    def parse_mini(msg: str) -> 'Message':
        return get_api().componentFromMini(msg)

class Pos(NamedTuple):
    x: float
    y: float
    z: float

    def rel(self, x: float, y: float, z: float): 
        return Pos(self.x + x, self.y + y, self.z + z)

PosLike = Pos | tuple[int, int, int]

