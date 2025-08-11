from mechs.types import Message
from _mechthon_internal import get_api # type: ignore 

class MessageImpl(Message):
    def __init__(self, delegate):
        self._delegate = delegate

    def raw(self):
        return get_api().componentToRaw(self._delegate)
