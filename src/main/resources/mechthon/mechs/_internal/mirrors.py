from enum import Enum
from typing import Callable, Type
from mechs._internal import BukkitType, BukkitWrapper
from uuid import UUID

class ROProp[T]:
    def __init__(self, getter: str):
        self._getter_name = getter

    def __get__(self, obj: BukkitWrapper, objtype: type | None = None) -> T:
        return getattr(obj._delegate, self._getter_name)()

class RWProp[T](ROProp[T]):
    def __init__(self, getter: str, setter: str):
        super().__init__(getter)
        self._setter_name = setter

    def __set__(self, obj: BukkitWrapper, value: T):
        return getattr(obj._delegate, self._setter_name)(value)

class TransformedROProp[T]:
    def __init__(self, getter: str, to_wrapped: Callable[[BukkitType], T]):
        self._getter_name = getter
        self._to_wrapped = to_wrapped

    def __get__(self, obj: BukkitWrapper, objtype: type | None = None) -> T:
        return self._to_wrapped(getattr(obj._delegate, self._getter_name)())

class TransformedRWProp[T](TransformedROProp[T]):
    def __init__(self, getter: str, setter: str, to_wrapped: Callable[[BukkitType], T], from_wrapped: Callable[[T], BukkitType]):
        super().__init__(getter, to_wrapped)
        self._setter_name = setter
        self._from_wrapped = from_wrapped

    def __set__(self, obj: BukkitWrapper, value: T):
        return getattr(obj._delegate, self._setter_name)(self._from_wrapped(value))

class BukkitWrapperRWProp[T : BukkitWrapper](TransformedRWProp[T]):
    def __init__(self, getter: str, setter: str, to_wrapped: Callable[[BukkitType], T]) -> None:
        super().__init__(getter ,setter, to_wrapped, lambda x: x._delegate)

class EnumMirror[T : Enum]:
    def __init__(self, enum_type: Type[T], native_type: str):
        native = java.type(native_type) # type:ignore
        self._to_native = { x: getattr(native, x.name) for x in enum_type }
        self._from_native = { b: a for (a, b) in self._to_native.items() }

    def from_native(self, native: BukkitType) -> T:
        return self._from_native[native]

    def to_native(self, wrapped: T) -> BukkitType:
        return self._from_native[wrapped]

_uuid_type = java.type("java.util.UUID") # type:ignore

def java_uuid_to_python(uuid: BukkitType):
    return UUID(int = (uuid.getMostSignificantBits() << 64) | uuid.getLeastSignificantBits())

def python_uuid_to_java(uuid: UUID):
    return _uuid_type(uuid.bytes)

