package com.floweytf.mechthon;

import com.floweytf.mechthon.engine.LoadHandler;
import com.floweytf.mechthon.util.Util;
import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.ArgumentSuggestions;
import dev.jorel.commandapi.arguments.StringArgument;
import dev.jorel.commandapi.executors.ExecutorType;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.entity.Entity;
import org.codehaus.plexus.util.ExceptionUtils;

import static net.kyori.adventure.text.Component.text;

public class Commands {
    private static StringArgument scriptArg(MechthonPlugin plugin) {
        final var arg = new StringArgument("script");
        arg.replaceSuggestions(ArgumentSuggestions.strings(c ->
            plugin.getEngine().getScriptManager().getScripts().keySet().toArray(String[]::new))
        );

        return arg;
    }

    public static StringArgument triggerArg(StringArgument scriptArg, MechthonPlugin plugin) {
        final var arg = new StringArgument("trigger");

        arg.replaceSuggestions(ArgumentSuggestions.strings(ctx -> {
            final var script = plugin.getEngine()
                .getScriptManager()
                .getScripts()
                .get(ctx.previousArgs().getByArgument(scriptArg));

            if (script == null) {
                return new String[0];
            }

            return script.getTriggerable().keySet().toArray(String[]::new);
        }));

        return arg;
    }

    private static CommandAPICommand reload(MechthonPlugin plugin) {
        return new CommandAPICommand("reload").executes((sender, args) -> {
            plugin.getSLF4JLogger().info("reloading scripts...");

            final var res = plugin.getEngine().getScriptManager().reloadScripts(
                LoadHandler.of(
                    LoadHandler.logging(plugin.getSLF4JLogger()),
                    LoadHandler.broadcasting(sender)
                ),
                () -> {
                    plugin.getSLF4JLogger().info("reload complete");

                    Util.sendMessage(sender, NamedTextColor.WHITE, "reload complete");
                },
                e -> {
                    plugin.getSLF4JLogger().error("while reloading: ", e);

                    Util.sendMessage(
                        sender,
                        text("uncaught exception while reloading, check logs")
                            .color(NamedTextColor.RED)
                            .hoverEvent(text(ExceptionUtils.getFullStackTrace(e)).color(NamedTextColor.RED))
                    );
                }
            );

            if (res) {
                Util.sendMessage(sender, NamedTextColor.WHITE, "starting reload...");
            } else {
                Util.sendMessage(sender, NamedTextColor.RED, "reload already in progress");
            }
        });
    }

    private static CommandAPICommand invoke(MechthonPlugin plugin) {
        final var script = scriptArg(plugin);

        return new CommandAPICommand("invoke").withArguments(script).executes((sender, args) -> {
            plugin.getEngine().invokeScript(
                (Entity) sender,
                args.getByArgument(script)
            );
        }, ExecutorType.PLAYER, ExecutorType.ENTITY);
    }

    private static CommandAPICommand trigger(MechthonPlugin plugin) {
        final var scriptArg = scriptArg(plugin);
        final var triggerArg = triggerArg(scriptArg, plugin);

        return new CommandAPICommand("trigger").withArguments(scriptArg, triggerArg).executes((sender, args) -> {
            plugin.getEngine().invokeTrigger(
                (Entity) sender,
                args.getByArgument(scriptArg),
                args.getByArgument(triggerArg)
            );
        }, ExecutorType.PLAYER, ExecutorType.ENTITY);
    }

    public static void register(MechthonPlugin plugin) {
        new CommandAPICommand("mechthon")
            .withSubcommands(
                reload(plugin),
                invoke(plugin),
                trigger(plugin)
            )
            .register(plugin);
    }
}
