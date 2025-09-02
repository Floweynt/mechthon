from enum import Enum
from typing import Callable, Generic, Type, TypeVar
from .bukkit import TriState, Vector
from mechs.types import Vec3, VecLike
from . import BukkitType, BukkitWrapper
from uuid import UUID
import java

T = TypeVar('T')

class ROProp(Generic[T]):
    def __init__(self, getter: str):
        self._getter_name = getter

    def __get__(self, obj: BukkitWrapper, objtype: type | None = None) -> T:
        return getattr(obj._delegate, self._getter_name)()

class RWProp(ROProp[T]):
    def __init__(self, getter: str, setter: str):
        super().__init__(getter)
        self._setter_name = setter

    def __set__(self, obj: BukkitWrapper, value: T):
        return getattr(obj._delegate, self._setter_name)(value)

class TransformedROProp(Generic[T]):
    def __init__(self, getter: str, to_wrapped: Callable[[BukkitType], T]):
        self._getter_name = getter
        self._to_wrapped = to_wrapped

    def __get__(self, obj: BukkitWrapper, objtype: type | None = None) -> T:
        return self._to_wrapped(getattr(obj._delegate, self._getter_name)())

class TransformedRWProp(TransformedROProp[T]):
    def __init__(self, getter: str, setter: str, to_wrapped: Callable[[BukkitType], T], from_wrapped: Callable[[T], BukkitType]):
        super().__init__(getter, to_wrapped)
        self._setter_name = setter
        self._from_wrapped = from_wrapped

    def __set__(self, obj: BukkitWrapper, value: T):
        return getattr(obj._delegate, self._setter_name)(self._from_wrapped(value))

B = TypeVar('B', bound=BukkitWrapper)

class BukkitWrapperRWProp(TransformedRWProp[B]):
    def __init__(self, getter: str, setter: str, to_wrapped: Callable[[BukkitType], B]) -> None:
        super().__init__(getter ,setter, to_wrapped, lambda x: x._delegate)

E = TypeVar('E', bound=Enum)

class EnumMirror(Generic[E]):
    def __init__(self, enum_type: Type[E], native_type: str):
        native = java.type(native_type) # type:ignore
        self._to_native = { x: getattr(native, x.name) for x in enum_type }
        self._from_native = { b: a for (a, b) in self._to_native.items() }

    def to_native(self, wrapped: E) -> BukkitType:
        return self._to_native[wrapped]

    def from_native(self, native: BukkitType) -> E:
        return self._from_native[native]

_uuid_type = java.type("java.util.UUID") # type:ignore

def java_uuid_to_python(uuid: BukkitType):
    return UUID(int = (uuid.getMostSignificantBits() << 64) | uuid.getLeastSignificantBits())

def python_uuid_to_java(uuid: UUID):
    return _uuid_type(uuid.bytes)

UNWRAP_TRISTATE = {
    TriState.NOT_SET: None,
    TriState.FALSE: False,
    TriState.TRUE: True
}

WRAP_TRISTATE = { value: key for (key, value) in UNWRAP_TRISTATE.items() } 

def unwrap_vector(v: Vector): return Vec3(v.getX(), v.getY(), v.getZ())
def wrap_vector(v: VecLike):
    (x, y, z) = v
    return Vector(x, y, z)
