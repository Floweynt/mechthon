from mechs._internal import BukkitType, BukkitWrapper, binding_constructor

class World(BukkitWrapper):
    @binding_constructor("org.bukkit.World")
    def __init__(self, delegate: BukkitType) -> None:
        self._delegate = delegate
