from typing import Generic, NamedTuple, Protocol, TypeVar 

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

V = TypeVar('V')
K = TypeVar('K', contravariant=True)

class SimpleMap(Protocol, Generic[K, V]):
    def __getitem__(self, name: K) -> V: ...
    def __setitem__(self, name: K, value: V): ...
    def __contains__(self, name: K) -> bool: ...
