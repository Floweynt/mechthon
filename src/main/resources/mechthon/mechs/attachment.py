from typing import Callable
from _mechthon_internal import current_script_instance # type: ignore 
from mechs.entity import Entity

def ticker(func: Callable[[Entity], None], delay: int = 1):
    current_script_instance().registerTicker(func, delay)
    return func

def triggerable(func: Callable[[Entity], None]):
    current_script_instance().registerTriggerable(func.__name__, func)
    return func
