from typing import Any, Callable, Coroutine
from _mechthon_builtin import current_script_instance 
from mechs._internal.coro import run_possibly_async
from mechs.bindings.entity import Entity

def triggerable(func: Callable[[Entity], Any | Coroutine[Any, Any, Any]]):
    current_script_instance().registerTriggerable(func.__name__, lambda e: run_possibly_async(e, func))
    return func
