from abc import abstractmethod
from typing import Any, Callable, Protocol
from mechs.types import Message, Pos, PosLike

class EntityScores(Protocol):
    @abstractmethod
    def __getitem__(self, name: str) -> int: ...
    
    @abstractmethod
    def __setitem__(self, name: str, value: int): ...
    
    @abstractmethod
    def __contains__(self, ent: str): ...

class Entity(Protocol): 
    @property
    def scores(self) -> EntityScores: ...

    @property
    def tags(self) -> set[str]: ...

    @abstractmethod
    def pos(self) -> Pos: ...

    def pos_rel(self, x: float, y: float, z: float) -> Pos:
        return self.pos().rel(x, y, z)

    @abstractmethod
    def pos_local(self, x: float, y: float, z: float) -> Pos: ...

    @abstractmethod
    def tp(self, pos: PosLike): ...

    @abstractmethod
    def tell(self, msg: Message | str): ...

    @abstractmethod
    def invoke(self, script: str) -> Any: ...

    @abstractmethod
    def trigger(self, script: str, trigger: str) -> Any: ...

    def tell_mini(self, minimessage: str): 
        self.tell(Message.parse_mini(minimessage))
