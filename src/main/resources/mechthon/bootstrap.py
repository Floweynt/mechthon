import importlib.util
import sys
from typing import Any

__current_script_instance: Any
__api: Any
__library_dir: str

sys.path.append(__library_dir) # type:ignore

def exec_sandboxed(source_code, filename):
    globals = {
        "__name__": "__main__",
    }

    exec(
        compile(source_code, filename=filename, mode="exec"),
        globals,
        globals
    )

    return globals

def load_script(script_instance, source_code: str):
    global __current_script_instance
    __current_script_instance = script_instance
    exec_sandboxed(source_code, f"{script_instance.name()}.py")

def make_module_helper(package: str, name: str, is_package: bool):
    spec = importlib.util.spec_from_loader(name, loader=None, is_package=is_package)
    assert spec is not None
    module = importlib.util.module_from_spec(spec)
    module.__package__ = package
    sys.modules[name] = module
    return module.__dict__

def bootstrap():
    def current_script_instance():
        res = __current_script_instance
        if res is None:
            raise ValueError("current script instance is not set")
        return res

    internal_mod = make_module_helper("_mechthon_builtin", "_mechthon_builtin", False)
    internal_mod["current_script_instance"] = current_script_instance 
    internal_mod["get_api"] = lambda: __api

bootstrap()
