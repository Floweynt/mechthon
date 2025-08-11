from _mechthon_internal import current_script_instance # type: ignore

def main(func):
    current_script_instance().registerMain(func)
    return func
