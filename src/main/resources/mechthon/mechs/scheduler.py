from abc import abstractmethod
from typing import Any, Callable
from ._internal.coro import GenericAwaitable

class Scheduler:
    async def await_tick(self, ticks: int): 
        await GenericAwaitable(lambda cont: self.schedule(lambda: cont(None), ticks))

    @abstractmethod
    def schedule(self, callback: Callable[[], Any], ticks: int): ...
