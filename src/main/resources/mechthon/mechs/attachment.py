from typing import Any, Callable, Coroutine
from _mechthon_builtin import current_script_instance 
from _internal import run_possibly_async
from mechs.entity import Entity

def ticker(func: Callable[[Entity], Any | Coroutine[Any, Any, Any]], delay: int = 1):
    current_script_instance().registerTicker(delay, lambda e: run_possibly_async(e, func))
    return func

def triggerable(func: Callable[[Entity], Any | Coroutine[Any, Any, Any]]):
    current_script_instance().registerTriggerable(func.__name__, lambda e: run_possibly_async(e, func))
    return func
