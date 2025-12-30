from typing import TypeAlias
import java

Vector: TypeAlias = java.type("org.bukkit.util.Vector") # type:ignore
Bukkit = java.type("org.bukkit.Bukkit") #type:ignore 
MiniMessage = java.type("net.kyori.adventure.text.minimessage.MiniMessage") # type:ignore 
TagResolver = java.type("net.kyori.adventure.text.minimessage.tag.resolver.TagResolver") # type:ignore
PlainTextComponentSerializer = java.type("net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer") # type:ignore
EntityType = java.type("org.bukkit.entity.EntityType") # type:ignore
TriState = java.type("net.kyori.adventure.util.TriState") # type:ignore
Location = java.type("org.bukkit.Location") # type:ignore

MINI_MESSAGE_INST = MiniMessage.builder().tags(TagResolver.standard()).build()
PLAIN_TEXT_SERIALIZER_INST = PlainTextComponentSerializer.plainText()
