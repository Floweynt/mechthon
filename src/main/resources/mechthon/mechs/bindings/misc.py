from mechs._internal.bukkit import MINI_MESSAGE_INST, PLAIN_TEXT_SERIALIZER_INST
from mechs._internal import *
from mechs._internal.mirrors import *

class Message(BukkitWrapper):
    @binding_constructor("net.kyori.adventure.text.Component")
    def __init__(self, delegate: BukkitType):
        self._delegate = delegate

    def raw(self) -> str:
        return PLAIN_TEXT_SERIALIZER_INST.serialize(self._delegate)

    @staticmethod
    def parse_mini(msg: str) -> 'Message':
        return Message(MINI_MESSAGE_INST.deserialize(msg))

class ConfigurationSerializable(BukkitWrapper):
    @binding_constructor("org.bukkit.configuration.serialization.ConfigurationSerializable")
    def __init__(self, delegate: BukkitType):
        self._delegate = delegate

    # TODO: method public abstract java.util.Map<java.lang.String, java.lang.Object> org.bukkit.configuration.serialization.ConfigurationSerializable.serialize()

class CommandBlockHolder(BukkitWrapper):
    @binding_constructor("io.papermc.paper.command.CommandBlockHolder")
    def __init__(self, delegate: BukkitType):
        self._delegate = delegate

    # TODO: property command getCommand setCommand
    success_count = RWProp[int]("getSuccessCount", "setSuccessCount")
    last_output = BukkitWrapperRWProp[Message]
    # TODO: method public abstract net.kyori.adventure.text.Component io.papermc.paper.command.CommandBlockHolder.lastOutput()
    # TODO: method public abstract void io.papermc.paper.command.CommandBlockHolder.lastOutput(net.kyori.adventure.text.Component)

class Attributable(BukkitWrapper):
    @binding_constructor("org.bukkit.attribute.Attributable")
    def __init__(self, delegate: BukkitType):
        self._delegate = delegate

    # TODO: method public abstract org.bukkit.attribute.AttributeInstance org.bukkit.attribute.Attributable.getAttribute(org.bukkit.attribute.Attribute)
    # TODO: method public abstract void org.bukkit.attribute.Attributable.registerAttribute(org.bukkit.attribute.Attribute)

class NetworkClient(BukkitWrapper):
    @binding_constructor("com.destroystokyo.paper.network.NetworkClient")
    def __init__(self, delegate: BukkitType):
        self._delegate = delegate

    # TODO: property address getAddress null
    # TODO: property virtual_host getVirtualHost null
    protocol_version = ROProp[int]("getProtocolVersion")

class HoverEventSource(BukkitWrapper):
    @binding_constructor("net.kyori.adventure.text.event.HoverEventSource")
    def __init__(self, delegate: BukkitType):
        self._delegate = delegate

    # TODO: method public static <V> net.kyori.adventure.text.event.HoverEvent<V> net.kyori.adventure.text.event.HoverEventSource.unbox(net.kyori.adventure.text.event.HoverEventSource<V>)
    # TODO: method public default net.kyori.adventure.text.event.HoverEvent<V> net.kyori.adventure.text.event.HoverEventSource.asHoverEvent()
    # TODO: method public abstract net.kyori.adventure.text.event.HoverEvent<V> net.kyori.adventure.text.event.HoverEventSource.asHoverEvent(java.util.function.UnaryOperator<V>)

class Pointered(BukkitWrapper):
    @binding_constructor("net.kyori.adventure.pointer.Pointered")
    def __init__(self, delegate: BukkitType):
        self._delegate = delegate

    # TODO: method public default net.kyori.adventure.pointer.Pointers net.kyori.adventure.pointer.Pointered.pointers()
    # TODO: method public default <T> T net.kyori.adventure.pointer.Pointered.getOrDefaultFrom(net.kyori.adventure.pointer.Pointer<T>,java.util.function.Supplier<? extends T>)
    # TODO: method public default <T> T net.kyori.adventure.pointer.Pointered.getOrDefault(net.kyori.adventure.pointer.Pointer<T>,T)
    # TODO: method public default <T> java.util.Optional<T> net.kyori.adventure.pointer.Pointered.get(net.kyori.adventure.pointer.Pointer<T>)

class PluginMessageRecipient(BukkitWrapper):
    @binding_constructor("org.bukkit.plugin.messaging.PluginMessageRecipient")
    def __init__(self, delegate: BukkitType):
        self._delegate = delegate

    # TODO: property listening_plugin_channels getListeningPluginChannels null
    # TODO: method public abstract void org.bukkit.plugin.messaging.PluginMessageRecipient.sendPluginMessage(org.bukkit.plugin.Plugin,java.lang.String,byte[])

class Emitter(BukkitWrapper):
    @binding_constructor("net.kyori.adventure.sound.Sound$Emitter")
    def __init__(self, delegate: BukkitType):
        self._delegate = delegate

    # TODO: method public static net.kyori.adventure.sound.Sound$Emitter net.kyori.adventure.sound.Sound$Emitter.self()

class Merchant(BukkitWrapper):
    @binding_constructor("org.bukkit.inventory.Merchant")
    def __init__(self, delegate: BukkitType):
        self._delegate = delegate

    recipe_count = ROProp[int]("getRecipeCount")
    # TODO: property trader getTrader null
    is_trading = ROProp[bool]("isTrading")
    # TODO: property recipes getRecipes setRecipes
    # TODO: method public abstract void org.bukkit.inventory.Merchant.setRecipe(int,org.bukkit.inventory.MerchantRecipe) throws java.lang.IndexOutOfBoundsException
    # TODO: method public abstract org.bukkit.inventory.MerchantRecipe org.bukkit.inventory.Merchant.getRecipe(int) throws java.lang.IndexOutOfBoundsException

class ServerOperator(BukkitWrapper):
    @binding_constructor("org.bukkit.permissions.ServerOperator")
    def __init__(self, delegate: BukkitType):
        self._delegate = delegate

    is_op = RWProp[bool]("isOp", "setOp")

class Metadatable(BukkitWrapper):
    @binding_constructor("org.bukkit.metadata.Metadatable")
    def __init__(self, delegate: BukkitType):
        self._delegate = delegate

    # TODO: method public abstract boolean org.bukkit.metadata.Metadatable.hasMetadata(java.lang.String)
    # TODO: method public abstract void org.bukkit.metadata.Metadatable.setMetadata(java.lang.String,org.bukkit.metadata.MetadataValue)
    # TODO: method public abstract java.util.List<org.bukkit.metadata.MetadataValue> org.bukkit.metadata.Metadatable.getMetadata(java.lang.String)
    # TODO: method public abstract void org.bukkit.metadata.Metadatable.removeMetadata(java.lang.String,org.bukkit.plugin.Plugin)

class Lootable(BukkitWrapper):
    @binding_constructor("org.bukkit.loot.Lootable")
    def __init__(self, delegate: BukkitType):
        self._delegate = delegate

    seed = RWProp[int]("getSeed", "setSeed")
    # TODO: method public abstract void org.bukkit.loot.Lootable.setLootTable(org.bukkit.loot.LootTable)
    # TODO: method public default void org.bukkit.loot.Lootable.setLootTable(org.bukkit.loot.LootTable,long)
    # TODO: method public abstract org.bukkit.loot.LootTable org.bukkit.loot.Lootable.getLootTable()
    def clear_loot_table(self): self._delegate.clearLootTable()
    # TODO: method public default boolean org.bukkit.loot.Lootable.hasLootTable()

class Identified(BukkitWrapper):
    @binding_constructor("net.kyori.adventure.identity.Identified")
    def __init__(self, delegate: BukkitType):
        self._delegate = delegate

    # TODO: method public abstract net.kyori.adventure.identity.Identity net.kyori.adventure.identity.Identified.identity()

class PersistentDataHolder(BukkitWrapper):
    @binding_constructor("org.bukkit.persistence.PersistentDataHolder")
    def __init__(self, delegate: BukkitType):
        self._delegate = delegate

    # TODO: property persistent_data_container getPersistentDataContainer null

class ProjectileSource(BukkitWrapper):
    @binding_constructor("org.bukkit.projectiles.ProjectileSource")
    def __init__(self, delegate: BukkitType):
        self._delegate = delegate

    # TODO: method public abstract <T extends org.bukkit.entity.Projectile> T org.bukkit.projectiles.ProjectileSource.launchProjectile(java.lang.Class<? extends T>)
    # TODO: method public abstract <T extends org.bukkit.entity.Projectile> T org.bukkit.projectiles.ProjectileSource.launchProjectile(java.lang.Class<? extends T>,org.bukkit.util.Vector)
    # TODO: method public abstract <T extends org.bukkit.entity.Projectile> T org.bukkit.projectiles.ProjectileSource.launchProjectile(java.lang.Class<? extends T>,org.bukkit.util.Vector,java.util.function.Consumer<? super T>)

class AnimalTamer(BukkitWrapper):
    @binding_constructor("org.bukkit.entity.AnimalTamer")
    def __init__(self, delegate: BukkitType):
        self._delegate = delegate

    unique_id = TransformedROProp[UUID]("getUniqueId", java_uuid_to_python)
    # TODO: property name getName null

class Keyed(BukkitWrapper):
    @binding_constructor("net.kyori.adventure.key.Keyed")
    def __init__(self, delegate: BukkitType):
        self._delegate = delegate

    # TODO: method public abstract net.kyori.adventure.key.Key net.kyori.adventure.key.Keyed.key()

class Nameable(BukkitWrapper):
    @binding_constructor("org.bukkit.Nameable")
    def __init__(self, delegate: BukkitType):
        self._delegate = delegate

    # TODO: method public abstract net.kyori.adventure.text.Component org.bukkit.Nameable.customName()
    # TODO: method public abstract void org.bukkit.Nameable.customName(net.kyori.adventure.text.Component)

class Conversable(BukkitWrapper):
    @binding_constructor("org.bukkit.conversations.Conversable")
    def __init__(self, delegate: BukkitType):
        self._delegate = delegate

    is_conversing = ROProp[bool]("isConversing")
    # TODO: method public abstract void org.bukkit.conversations.Conversable.acceptConversationInput(java.lang.String)
    # TODO: method public abstract boolean org.bukkit.conversations.Conversable.beginConversation(org.bukkit.conversations.Conversation)
    # TODO: method public abstract void org.bukkit.conversations.Conversable.sendRawMessage(java.lang.String)
    # TODO: method public abstract void org.bukkit.conversations.Conversable.abandonConversation(org.bukkit.conversations.Conversation)
    # TODO: method public abstract void org.bukkit.conversations.Conversable.abandonConversation(org.bukkit.conversations.Conversation,org.bukkit.conversations.ConversationAbandonedEvent)

class InventoryHolder(BukkitWrapper):
    @binding_constructor("org.bukkit.inventory.InventoryHolder")
    def __init__(self, delegate: BukkitType):
        self._delegate = delegate

    # TODO: property inventory getInventory null

class Directional(BukkitWrapper):
    @binding_constructor("org.bukkit.material.Directional")
    def __init__(self, delegate: BukkitType):
        self._delegate = delegate

    # TODO: property facing getFacing null
    # TODO: method public abstract void org.bukkit.material.Directional.setFacingDirection(org.bukkit.block.BlockFace)

class Frictional(BukkitWrapper):
    @binding_constructor("io.papermc.paper.entity.Frictional")
    def __init__(self, delegate: BukkitType):
        self._delegate = delegate

    # TODO: property friction_state getFrictionState setFrictionState

class BossBarViewer(BukkitWrapper):
    @binding_constructor("net.kyori.adventure.bossbar.BossBarViewer")
    def __init__(self, delegate: BukkitType):
        self._delegate = delegate

    # TODO: method public abstract java.lang.Iterable<? extends net.kyori.adventure.bossbar.BossBar> net.kyori.adventure.bossbar.BossBarViewer.activeBossBars()

class Colorable(BukkitWrapper):
    @binding_constructor("org.bukkit.material.Colorable")
    def __init__(self, delegate: BukkitType):
        self._delegate = delegate

    # TODO: method public abstract org.bukkit.DyeColor org.bukkit.material.Colorable.getColor()
    # TODO: method public abstract void org.bukkit.material.Colorable.setColor(org.bukkit.DyeColor)

class Audience(Pointered):
    @binding_constructor("net.kyori.adventure.audience.Audience")
    def __init__(self, delegate: BukkitType):
        self._delegate = delegate

    def tell(self, msg: Message | str):
        if isinstance(msg, str):
            msg = Message.parse_mini(msg)
        return self._delegate.sendMessage(msg._delegate) # type: ignore

    # TODO: method public default void net.kyori.adventure.audience.Audience.showTitle(net.kyori.adventure.title.Title)
    # TODO: method public default void net.kyori.adventure.audience.Audience.openBook(net.kyori.adventure.inventory.Book)
    # TODO: method public default void net.kyori.adventure.audience.Audience.openBook(net.kyori.adventure.inventory.Book$Builder)
    # TODO: method private static java.lang.Iterable net.kyori.adventure.audience.Audience.lambda$audience$0(java.lang.Iterable)
    def reset_title(self): self._delegate.resetTitle()
    # TODO: method public default net.kyori.adventure.audience.Audience net.kyori.adventure.audience.Audience.filterAudience(java.util.function.Predicate<? super net.kyori.adventure.audience.Audience>)
    # TODO: method public default void net.kyori.adventure.audience.Audience.sendActionBar(net.kyori.adventure.text.ComponentLike)
    # TODO: method public default void net.kyori.adventure.audience.Audience.sendActionBar(net.kyori.adventure.text.Component)
    def clear_title(self): self._delegate.clearTitle()
    # TODO: method public default void net.kyori.adventure.audience.Audience.forEachAudience(java.util.function.Consumer<? super net.kyori.adventure.audience.Audience>)
    # TODO: method public static net.kyori.adventure.audience.Audience net.kyori.adventure.audience.Audience.audience(net.kyori.adventure.audience.Audience...)
    # TODO: method public static net.kyori.adventure.audience.ForwardingAudience net.kyori.adventure.audience.Audience.audience(java.lang.Iterable<? extends net.kyori.adventure.audience.Audience>)
    # TODO: method public default void net.kyori.adventure.audience.Audience.removeResourcePacks(net.kyori.adventure.resource.ResourcePackInfoLike,net.kyori.adventure.resource.ResourcePackInfoLike...)
    # TODO: method public default void net.kyori.adventure.audience.Audience.removeResourcePacks(java.lang.Iterable<java.util.UUID>)
    # TODO: method public default void net.kyori.adventure.audience.Audience.removeResourcePacks(java.util.UUID,java.util.UUID...)
    # TODO: method public default void net.kyori.adventure.audience.Audience.removeResourcePacks(net.kyori.adventure.resource.ResourcePackRequest)
    # TODO: method public default void net.kyori.adventure.audience.Audience.removeResourcePacks(net.kyori.adventure.resource.ResourcePackRequestLike)
    # TODO: method public default void net.kyori.adventure.audience.Audience.hideBossBar(net.kyori.adventure.bossbar.BossBar)
    # TODO: method public default void net.kyori.adventure.audience.Audience.sendPlayerListHeaderAndFooter(net.kyori.adventure.text.ComponentLike,net.kyori.adventure.text.ComponentLike)
    # TODO: method public default void net.kyori.adventure.audience.Audience.sendPlayerListHeaderAndFooter(net.kyori.adventure.text.Component,net.kyori.adventure.text.Component)
    # TODO: method public static java.util.stream.Collector<? super net.kyori.adventure.audience.Audience, ?, net.kyori.adventure.audience.ForwardingAudience> net.kyori.adventure.audience.Audience.toAudience()
    # TODO: method public default void net.kyori.adventure.audience.Audience.deleteMessage(net.kyori.adventure.chat.SignedMessage)
    # TODO: method public default void net.kyori.adventure.audience.Audience.deleteMessage(net.kyori.adventure.chat.SignedMessage$Signature)
    # TODO: method public default void net.kyori.adventure.audience.Audience.playSound(net.kyori.adventure.sound.Sound,net.kyori.adventure.sound.Sound$Emitter)
    # TODO: method public default void net.kyori.adventure.audience.Audience.playSound(net.kyori.adventure.sound.Sound,double,double,double)
    # TODO: method public default void net.kyori.adventure.audience.Audience.playSound(net.kyori.adventure.sound.Sound)
    def clear_resource_packs(self): self._delegate.clearResourcePacks()
    # TODO: method public default void net.kyori.adventure.audience.Audience.showBossBar(net.kyori.adventure.bossbar.BossBar)
    # TODO: method public default void net.kyori.adventure.audience.Audience.stopSound(net.kyori.adventure.sound.SoundStop)
    # TODO: method public default void net.kyori.adventure.audience.Audience.stopSound(net.kyori.adventure.sound.Sound)
    # TODO: method public default void net.kyori.adventure.audience.Audience.sendPlayerListFooter(net.kyori.adventure.text.Component)
    # TODO: method public default void net.kyori.adventure.audience.Audience.sendPlayerListFooter(net.kyori.adventure.text.ComponentLike)
    # TODO: method public default void net.kyori.adventure.audience.Audience.sendPlayerListHeader(net.kyori.adventure.text.Component)
    # TODO: method public default void net.kyori.adventure.audience.Audience.sendPlayerListHeader(net.kyori.adventure.text.ComponentLike)
    # TODO: method public static net.kyori.adventure.audience.Audience net.kyori.adventure.audience.Audience.empty()
    # TODO: method public default <T> void net.kyori.adventure.audience.Audience.sendTitlePart(net.kyori.adventure.title.TitlePart<T>,T)
    # TODO: method public default void net.kyori.adventure.audience.Audience.sendResourcePacks(net.kyori.adventure.resource.ResourcePackRequest)
    # TODO: method public default void net.kyori.adventure.audience.Audience.sendResourcePacks(net.kyori.adventure.resource.ResourcePackRequestLike)
    # TODO: method public default void net.kyori.adventure.audience.Audience.sendResourcePacks(net.kyori.adventure.resource.ResourcePackInfoLike,net.kyori.adventure.resource.ResourcePackInfoLike...)

class Permissible(ServerOperator):
    @binding_constructor("org.bukkit.permissions.Permissible")
    def __init__(self, delegate: BukkitType):
        self._delegate = delegate

    # TODO: property effective_permissions getEffectivePermissions null
    # TODO: method public abstract boolean org.bukkit.permissions.Permissible.isPermissionSet(java.lang.String)
    # TODO: method public abstract boolean org.bukkit.permissions.Permissible.isPermissionSet(org.bukkit.permissions.Permission)
    # TODO: method public abstract org.bukkit.permissions.PermissionAttachment org.bukkit.permissions.Permissible.addAttachment(org.bukkit.plugin.Plugin,int)
    # TODO: method public abstract org.bukkit.permissions.PermissionAttachment org.bukkit.permissions.Permissible.addAttachment(org.bukkit.plugin.Plugin,java.lang.String,boolean,int)
    # TODO: method public abstract org.bukkit.permissions.PermissionAttachment org.bukkit.permissions.Permissible.addAttachment(org.bukkit.plugin.Plugin,java.lang.String,boolean)
    # TODO: method public abstract org.bukkit.permissions.PermissionAttachment org.bukkit.permissions.Permissible.addAttachment(org.bukkit.plugin.Plugin)
    # TODO: method public abstract void org.bukkit.permissions.Permissible.removeAttachment(org.bukkit.permissions.PermissionAttachment)
    def recalculate_permissions(self): self._delegate.recalculatePermissions()
    # TODO: method public abstract boolean org.bukkit.permissions.Permissible.hasPermission(org.bukkit.permissions.Permission)
    # TODO: method public abstract boolean org.bukkit.permissions.Permissible.hasPermission(java.lang.String)
    # TODO: method public default net.kyori.adventure.util.TriState org.bukkit.permissions.Permissible.permissionValue(org.bukkit.permissions.Permission)
    # TODO: method public default net.kyori.adventure.util.TriState org.bukkit.permissions.Permissible.permissionValue(java.lang.String)

class LootableInventory(Lootable):
    @binding_constructor("com.destroystokyo.paper.loottable.LootableInventory")
    def __init__(self, delegate: BukkitType):
        self._delegate = delegate

    has_pending_refill = ROProp[bool]("hasPendingRefill")
    next_refill = ROProp[int]("getNextRefill")
    is_refill_enabled = ROProp[bool]("isRefillEnabled")
    last_filled = ROProp[int]("getLastFilled")
    has_been_filled = ROProp[bool]("hasBeenFilled")
    # TODO: method public default boolean com.destroystokyo.paper.loottable.LootableInventory.setHasPlayerLooted(org.bukkit.entity.Player,boolean)
    # TODO: method public abstract boolean com.destroystokyo.paper.loottable.LootableInventory.setHasPlayerLooted(java.util.UUID,boolean)
    # TODO: method public abstract boolean com.destroystokyo.paper.loottable.LootableInventory.canPlayerLoot(java.util.UUID)
    # TODO: method public abstract boolean com.destroystokyo.paper.loottable.LootableInventory.hasPlayerLooted(java.util.UUID)
    # TODO: method public default boolean com.destroystokyo.paper.loottable.LootableInventory.hasPlayerLooted(org.bukkit.entity.Player)
    # TODO: method public default java.lang.Long com.destroystokyo.paper.loottable.LootableInventory.getLastLooted(org.bukkit.entity.Player)
    # TODO: method public abstract java.lang.Long com.destroystokyo.paper.loottable.LootableInventory.getLastLooted(java.util.UUID)
    # TODO: method public abstract long com.destroystokyo.paper.loottable.LootableInventory.setNextRefill(long)

class OfflinePlayer(ServerOperator, AnimalTamer, ConfigurationSerializable):
    @binding_constructor("org.bukkit.OfflinePlayer")
    def __init__(self, delegate: BukkitType):
        self._delegate = delegate

    is_whitelisted = RWProp[bool]("isWhitelisted", "setWhitelisted")
    # TODO: property player getPlayer null
    has_played_before = ROProp[bool]("hasPlayedBefore")
    # TODO: property respawn_location getRespawnLocation null
    is_connected = ROProp[bool]("isConnected")
    # TODO: property player_profile getPlayerProfile null
    last_seen = ROProp[int]("getLastSeen")
    is_banned = ROProp[bool]("isBanned")
    last_login = ROProp[int]("getLastLogin")
    first_played = ROProp[int]("getFirstPlayed")
    is_online = ROProp[bool]("isOnline")
    # TODO: property last_death_location getLastDeathLocation null
    # TODO: property location getLocation null
    # TODO: method public abstract void org.bukkit.OfflinePlayer.incrementStatistic(org.bukkit.Statistic,org.bukkit.Material) throws java.lang.IllegalArgumentException
    # TODO: method public abstract void org.bukkit.OfflinePlayer.incrementStatistic(org.bukkit.Statistic,org.bukkit.Material,int) throws java.lang.IllegalArgumentException
    # TODO: method public abstract void org.bukkit.OfflinePlayer.incrementStatistic(org.bukkit.Statistic,org.bukkit.entity.EntityType) throws java.lang.IllegalArgumentException
    # TODO: method public abstract void org.bukkit.OfflinePlayer.incrementStatistic(org.bukkit.Statistic,org.bukkit.entity.EntityType,int) throws java.lang.IllegalArgumentException
    # TODO: method public abstract void org.bukkit.OfflinePlayer.incrementStatistic(org.bukkit.Statistic) throws java.lang.IllegalArgumentException
    # TODO: method public abstract void org.bukkit.OfflinePlayer.incrementStatistic(org.bukkit.Statistic,int) throws java.lang.IllegalArgumentException
    # TODO: method public abstract void org.bukkit.OfflinePlayer.setStatistic(org.bukkit.Statistic,org.bukkit.entity.EntityType,int)
    # TODO: method public abstract void org.bukkit.OfflinePlayer.setStatistic(org.bukkit.Statistic,org.bukkit.Material,int) throws java.lang.IllegalArgumentException
    # TODO: method public abstract void org.bukkit.OfflinePlayer.setStatistic(org.bukkit.Statistic,int) throws java.lang.IllegalArgumentException
    # TODO: method public abstract <E extends org.bukkit.BanEntry<? super com.destroystokyo.paper.profile.PlayerProfile>> E org.bukkit.OfflinePlayer.ban(java.lang.String,java.time.Duration,java.lang.String)
    # TODO: method public abstract <E extends org.bukkit.BanEntry<? super com.destroystokyo.paper.profile.PlayerProfile>> E org.bukkit.OfflinePlayer.ban(java.lang.String,java.time.Instant,java.lang.String)
    # TODO: method public abstract <E extends org.bukkit.BanEntry<? super com.destroystokyo.paper.profile.PlayerProfile>> E org.bukkit.OfflinePlayer.ban(java.lang.String,java.util.Date,java.lang.String)
    # TODO: method public abstract void org.bukkit.OfflinePlayer.decrementStatistic(org.bukkit.Statistic,org.bukkit.entity.EntityType,int)
    # TODO: method public abstract void org.bukkit.OfflinePlayer.decrementStatistic(org.bukkit.Statistic,org.bukkit.Material,int) throws java.lang.IllegalArgumentException
    # TODO: method public abstract void org.bukkit.OfflinePlayer.decrementStatistic(org.bukkit.Statistic,org.bukkit.entity.EntityType) throws java.lang.IllegalArgumentException
    # TODO: method public abstract void org.bukkit.OfflinePlayer.decrementStatistic(org.bukkit.Statistic) throws java.lang.IllegalArgumentException
    # TODO: method public abstract void org.bukkit.OfflinePlayer.decrementStatistic(org.bukkit.Statistic,int) throws java.lang.IllegalArgumentException
    # TODO: method public abstract void org.bukkit.OfflinePlayer.decrementStatistic(org.bukkit.Statistic,org.bukkit.Material) throws java.lang.IllegalArgumentException
    # TODO: method public abstract int org.bukkit.OfflinePlayer.getStatistic(org.bukkit.Statistic) throws java.lang.IllegalArgumentException
    # TODO: method public abstract int org.bukkit.OfflinePlayer.getStatistic(org.bukkit.Statistic,org.bukkit.Material) throws java.lang.IllegalArgumentException
    # TODO: method public abstract int org.bukkit.OfflinePlayer.getStatistic(org.bukkit.Statistic,org.bukkit.entity.EntityType) throws java.lang.IllegalArgumentException

class Attachable(Directional):
    @binding_constructor("org.bukkit.material.Attachable")
    def __init__(self, delegate: BukkitType):
        self._delegate = delegate

    # TODO: property attached_face getAttachedFace null

class ForwardingAudience(Audience):
    @binding_constructor("net.kyori.adventure.audience.ForwardingAudience")
    def __init__(self, delegate: BukkitType):
        self._delegate = delegate

    # TODO: method public abstract java.lang.Iterable<? extends net.kyori.adventure.audience.Audience> net.kyori.adventure.audience.ForwardingAudience.audiences()

class CommandSender(Audience, Permissible):
    @binding_constructor("org.bukkit.command.CommandSender")
    def __init__(self, delegate: BukkitType):
        self._delegate = delegate

    # TODO: property server getServer null
    # TODO: property name getName null
    # TODO: method public default void org.bukkit.command.CommandSender.sendRichMessage(java.lang.String,net.kyori.adventure.text.minimessage.tag.resolver.TagResolver...)
    # TODO: method public default void org.bukkit.command.CommandSender.sendRichMessage(java.lang.String)
    # TODO: method public abstract net.kyori.adventure.text.Component org.bukkit.command.CommandSender.name()
    # TODO: method public abstract void org.bukkit.command.CommandSender.sendMessage(java.lang.String)
    # TODO: method public abstract void org.bukkit.command.CommandSender.sendMessage(java.lang.String...)
    # TODO: method public default void org.bukkit.command.CommandSender.sendPlainMessage(java.lang.String)

