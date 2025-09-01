from typing import TypeAlias
from mechs.types import Vec3, VecLike

Vector: TypeAlias = java.type("org.bukkit.util.Vector") # type:ignore
Bukkit = java.type("org.bukkit.Bukkit") #type:ignore 
MiniMessage = java.type("net.kyori.adventure.text.minimessage.MiniMessage") # type:ignore 
TagResolver = java.type("net.kyori.adventure.text.minimessage.tag.resolver.TagResolver") # type:ignore
PlainTextComponentSerializer = java.type("net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer") # type:ignore
EntityType = java.type("org.bukkit.entity.EntityType") # type:ignore
TriState = java.type("net.kyori.adventure.util.TriState") # type:ignore

UNWRAP_TRISTATE = {
    TriState.NOT_SET: None,
    TriState.FALSE: False,
    TriState.TRUE: True
}

WRAP_TRISTATE = { value: key for (key, value) in UNWRAP_TRISTATE } 

def unwrap_vector(v: Vector): return Vec3(v.getX(), v.getY(), v.getZ())
def wrap_vector(v: VecLike):
    (x, y, z) = v
    return Vector(x, y, z)

MINI_MESSAGE_INST = MiniMessage.builder().tags(TagResolver.standard()).build()
PLAIN_TEXT_SERIALIZER_INST = PlainTextComponentSerializer.plainText()
