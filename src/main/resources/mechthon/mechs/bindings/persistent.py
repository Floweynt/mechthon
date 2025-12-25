from typing import Generic, Protocol, TypeVar
from main.resources.mechthon._mechthon_builtin import current_script_instance, get_api

T = TypeVar('T')

class PersistentKey(Generic[T]):
    pass

class _ScriptKey(PersistentKey[T]):
    def __init__(self):
        self._script = current_script_instance(); 

class _GlobalKey(PersistentKey[T]):
    def __init__(self, name: str):
        self._metadata = get_api().getGlobalPersistentKey(name);
        if self._metadata is None: 
            raise ValueError(f"unknown global persistent key `{name}`")

def global_persistent_key(name: str) -> PersistentKey:
    return _GlobalKey(name)

def script_persistent_key():
    return _ScriptKey();

class PersistentContainer(Protocol):
    def __getitem__(self, name: PersistentKey[T]) -> T: ...
    def __setitem__(self, name: PersistentKey[T], value: T): ...
    def __contains__(self, name: PersistentKey[T]) -> bool: ...

def global_data_container() -> PersistentKey:
    
