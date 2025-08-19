from typing import Any, Callable, Coroutine, Protocol, TypeVar 

class GenericAwaitable:
    def __init__(self, task: Callable[[Callable[[Any], None]], None]):
        self._task = task
    def __await__(self):
        yield self._task

def pump_coro(coro: Coroutine, last_result = None):
    try:
        task = coro.send(last_result)
    except StopIteration:
        return
    else:
        task(lambda result: pump_coro(coro, result))

T = TypeVar('T')

def run_possibly_async(arg: T, func: Callable[[T], Any | Coroutine[Any, Any, Any]]):
    res = func(arg)
    if isinstance(res, Coroutine):
        pump_coro(res)

