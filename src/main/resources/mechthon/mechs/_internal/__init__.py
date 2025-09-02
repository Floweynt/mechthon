from collections.abc import Callable
from typing import Any, TypeVar
import java

BukkitType = Any

class BukkitWrapper:
    def __init__(self, delegate: BukkitType) -> None:
        self._delegate = delegate

T = TypeVar('T', bound=BukkitWrapper)

def binding_constructor(native_name: str):
    native_type = java.type(native_name)  # type: ignore
    def decorator(ctor: Callable[[T, BukkitType], None]):
        def resulting_ctor(self: T, native: BukkitType):
            assert java.instanceof(native, native_type) # type: ignore
            ctor(self, native)
        return resulting_ctor
    return decorator

