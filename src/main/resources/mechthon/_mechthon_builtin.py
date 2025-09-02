# exposition only
from abc import abstractmethod
from typing import Any, Callable, Protocol
from mechs.bindings.entity import Entity

class ScriptInstance(Protocol):
    @abstractmethod
    def registerMain(self, cb: Callable[[Entity], Any]): ...

    @abstractmethod
    def registerTicker(self, interval: int, cb: Callable[[Entity], Any]): ...

    @abstractmethod
    def registerTriggerable(self, name: str, cb: Callable[[Entity], Any]): ...

class APIAccess(Protocol):
    @abstractmethod
    def plugin(self): ...

    @abstractmethod
    def entityInvoke(self, entity, name: str) -> Any: ...

    @abstractmethod
    def entityTrigger(self, entity, scriptName: str, triggerName: str) -> Any: ...

    @abstractmethod
    def scheduleEntityTask(self, scheduler, callback: Callable[[], Any], delay: int) -> None: ...

def current_script_instance() -> ScriptInstance: ...
def get_api() -> APIAccess: ...
