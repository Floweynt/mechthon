from abc import abstractmethod
from typing import Any, Callable, Protocol
from _internal import GenericAwaitable

class Scheduler(Protocol):
    async def await_tick(self, ticks: int): 
        await GenericAwaitable(lambda cont: self.schedule(lambda: cont(None), ticks))
    
    @abstractmethod
    def schedule(self, callback: Callable[[], Any], ticks: int): ...
