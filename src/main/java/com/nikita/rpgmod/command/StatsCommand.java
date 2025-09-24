package com.nikita.rpgmod.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.nikita.rpgmod.capibility.PlayerStatsProvider;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;

public class StatsCommand {

    public StatsCommand(CommandDispatcher<CommandSourceStack> dispatcher) {
        //основная команда "stats"
        dispatcher.register(Commands.literal("stats")
                //подкоманда "get"
                .then(Commands.literal("get")
                                .then(Commands.literal("strength").executes(context -> getStat(context.getSource(), "strength")))
                                .then(Commands.literal("dexterity").executes(context -> getStat(context.getSource(), "dexterity")))
                                .then(Commands.literal("intelligence").executes(context -> getStat(context.getSource(), "intelligence")))
                                .then(Commands.literal("health").executes(context -> getStat(context.getSource(), "health")))
                                .then(Commands.literal("insight").executes(context -> getStat(context.getSource(), "insight")))
                )
                //подкоманда "set"
                .then(Commands.literal("set")
                                .then(Commands.literal("strength")
                                        .then(Commands.argument("value", IntegerArgumentType.integer(1, 100))
                                                .executes(context -> setStat(context.getSource(), "strength", IntegerArgumentType.getInteger(context, "value"))))
                                )
                                .then(Commands.literal("dexterity")
                                        .then(Commands.argument("value", IntegerArgumentType.integer(1, 100))
                                                .executes(context -> setStat(context.getSource(), "dexterity", IntegerArgumentType.getInteger(context, "value"))))
                                )
                                .then(Commands.literal("intelligence")
                                        .then(Commands.argument("value", IntegerArgumentType.integer(1, 100))
                                                .executes(context -> setStat(context.getSource(), "intelligence", IntegerArgumentType.getInteger(context, "value"))))
                                )
                                .then(Commands.literal("health")
                                        .then(Commands.argument("value", IntegerArgumentType.integer(1, 100))
                                                .executes(context -> setStat(context.getSource(), "health", IntegerArgumentType.getInteger(context, "value"))))
                                )
                                .then(Commands.literal("insight")
                                        .then(Commands.argument("value", IntegerArgumentType.integer(1, 100))
                                                .executes(context -> setStat(context.getSource(), "insight", IntegerArgumentType.getInteger(context, "value"))))
                                )
                )
        );
    }

    private int getStat(CommandSourceStack source, String statName) throws CommandSyntaxException {
        ServerPlayer player = source.getPlayerOrException();
        player.getCapability(PlayerStatsProvider.PLAYER_STATS).ifPresent(stats -> {
            int value = 0;
            switch (statName) {
                case "strength" -> value = stats.getStrength();
                case "dexterity" -> value = stats.getDexterity();
                case "intelligence" -> value = stats.getIntelligence();
                case "health" -> value = stats.getHealth();
                case "insight" -> value = stats.getInsight();
            }
            int finalValue = value;
            source.sendSuccess(() -> Component.literal(statName + " is: " + finalValue), false);
        });
        return 1;
    }

    private int setStat(CommandSourceStack source, String statName, int value) throws CommandSyntaxException {
        ServerPlayer player = source.getPlayerOrException();

        player.getCapability(PlayerStatsProvider.PLAYER_STATS).ifPresent(stats -> {
            switch (statName) {
                case "strength" -> stats.setStrength(value, player);
                case "dexterity" -> stats.setDexterity(value);
                case "intelligence" -> stats.setIntelligence(value);
                case "health" -> stats.setHealth(value);
                case "insight" -> stats.setInsight(value);
            }
            source.sendSuccess(() -> Component.literal("Set " + statName + " to " + value), false);
        });

        return 1;
    }
}
