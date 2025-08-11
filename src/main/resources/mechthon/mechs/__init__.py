from _mechthon_builtin import current_script_instance
from _internal import run_possibly_async 

def main(func):
    current_script_instance().registerMain(lambda e: run_possibly_async(e, func))
    return func
