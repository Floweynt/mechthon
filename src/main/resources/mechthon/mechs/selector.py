from mechs.types import PosLike

class Selector:
    def __init__(self, pos: PosLike):
        self._pos = pos
        self._tag_whitelist = None
        self._range = None
        self._tag_blacklist = []

    def tagged(self, *tags: str):
        whitelist = self._tag_whitelist or []
        whitelist.extend(tags)
        self._tag_whitelist = whitelist       
        return self

    def notTagged(self, *tags: str):
        self._tag_blacklist.extend(tags)
        return self

    def distance(self, min: float | None = None, max: float | None = None):
        if min is None and max is None:
            raise ValueError("min and max cannot both be none")
        self._range = (min, max)
    
