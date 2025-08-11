from abc import abstractmethod
from typing import Any, Callable, Protocol

class Scheduler(Protocol):
    @abstractmethod
    async def await_tick(self, ticks: int): ...
    
    @abstractmethod
    def schedule(self, callback: Callable[[], Any], ticks: int): ...
