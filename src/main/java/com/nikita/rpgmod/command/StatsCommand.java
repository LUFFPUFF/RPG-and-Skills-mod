package com.nikita.rpgmod.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.FloatArgumentType;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.nikita.rpgmod.capibility.PlayerStats;
import com.nikita.rpgmod.capibility.PlayerStatsProvider;
import com.nikita.rpgmod.classes.PlayerClass;
import com.nikita.rpgmod.classes.PlayerClassDataProvider;
import com.nikita.rpgmod.event.ModEvents;
import com.nikita.rpgmod.level.stats.PlayerLevelProvider;
import com.nikita.rpgmod.magic.stats.PlayerMagicProvider;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.server.TickTask;
import net.minecraft.server.level.ServerPlayer;

import java.util.Objects;

public class StatsCommand {

    public StatsCommand(CommandDispatcher<CommandSourceStack> dispatcher) {
        //основная команда "stats"
        dispatcher.register(Commands.literal("stats")
                //подкоманда "get"
                .then(Commands.literal("get")
                                .then(Commands.literal("strength").executes(context -> getStat(context.getSource(), "strength")))
                                .then(Commands.literal("dexterity").executes(context -> getStat(context.getSource(), "dexterity")))
                                .then(Commands.literal("intelligence").executes(context -> getStat(context.getSource(), "intelligence")))
                                .then(Commands.literal("vitality").executes(context -> getStat(context.getSource(), "health")))
                                .then(Commands.literal("insight").executes(context -> getStat(context.getSource(), "insight")))
                                .then(Commands.literal("mana").executes(context -> getMana(context.getSource())))
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
                                .then(Commands.literal("vitality")
                                        .then(Commands.argument("value", IntegerArgumentType.integer(1, 100))
                                                .executes(context -> setStat(context.getSource(), "vitality", IntegerArgumentType.getInteger(context, "value"))))
                                )
                                .then(Commands.literal("insight")
                                        .then(Commands.argument("value", IntegerArgumentType.integer(1, 100))
                                                .executes(context -> setStat(context.getSource(), "insight", IntegerArgumentType.getInteger(context, "value"))))
                                )
                )
        );

        //Команда magic
        dispatcher.register(Commands.literal("magic")
                //Подкоманда set
                .then(Commands.literal("set")
                        .then(Commands.literal("mana")
                                .then(Commands.argument("value", FloatArgumentType.floatArg(0))
                                        .executes(context -> setMana(context.getSource(), FloatArgumentType.getFloat(context, "value"))))
                        )
                )
                //Подкоманда add
                .then(Commands.literal("add")
                        .then(Commands.literal("mana")
                                .then(Commands.argument("value", FloatArgumentType.floatArg())
                                        .executes(context -> addMana(context.getSource(), FloatArgumentType.getFloat(context, "value"))))
                        )
                )
        );

        dispatcher.register(Commands.literal("level")
                .then(Commands.literal("get")
                        .executes(context -> getLevelInfo(context.getSource()))
                )
                .then(Commands.literal("add")
                        .then(Commands.literal("xp")
                                .then(Commands.argument("amount", IntegerArgumentType.integer(1))
                                        .executes(context -> addExperience(
                                                context.getSource(),
                                                IntegerArgumentType.getInteger(context, "amount")
                                        ))
                                )
                        )
                )
        );

        dispatcher.register(Commands.literal("invest")
                .then(Commands.literal("strength").executes(context -> investPoint(context.getSource(), "strength")))
                .then(Commands.literal("dexterity").executes(context -> investPoint(context.getSource(), "dexterity")))
                .then(Commands.literal("intelligence").executes(context -> investPoint(context.getSource(), "intelligence")))
                .then(Commands.literal("vitality").executes(context -> investPoint(context.getSource(), "vitality")))
                .then(Commands.literal("insight").executes(context -> investPoint(context.getSource(), "insight")))
        );

        dispatcher.register(Commands.literal("class")
                .then(Commands.literal("set")
                        .then(Commands.literal("primary")
                                .then(Commands.argument("classname", StringArgumentType.word())
                                        .executes(context -> setClass(context.getSource(), "primary", StringArgumentType.getString(context, "classname"))))
                        )
                        .then(Commands.literal("secondary")
                                .then(Commands.argument("classname", StringArgumentType.word())
                                        .executes(context -> setClass(context.getSource(), "secondary", StringArgumentType.getString(context, "classname"))))
                        )
                )
        );

        dispatcher.register(Commands.literal("rpgtest")
                .then(Commands.literal("damage")
                        .then(Commands.argument("amount", FloatArgumentType.floatArg(0))
                                .executes(context -> dealDamage(context.getSource(), FloatArgumentType.getFloat(context, "amount"))))
                )
                .then(Commands.literal("heal")
                        .then(Commands.argument("amount", FloatArgumentType.floatArg(0))
                                .executes(context -> healPlayer(context.getSource(), FloatArgumentType.getFloat(context, "amount"))))
                )
                .then(Commands.literal("spendmana")
                        .then(Commands.argument("amount", FloatArgumentType.floatArg(0))
                                .executes(context -> spendMana(context.getSource(), FloatArgumentType.getFloat(context, "amount"))))
                )
                .then(Commands.literal("regenmana")
                        .then(Commands.argument("amount", FloatArgumentType.floatArg(0))
                                .executes(context -> regenMana(context.getSource(), FloatArgumentType.getFloat(context, "amount"))))
                )
        );
    }

    private int getLevelInfo(CommandSourceStack source) throws CommandSyntaxException {
        ServerPlayer player = source.getPlayerOrException();

        player.getCapability(PlayerLevelProvider.PLAYER_LEVEL).ifPresent(level -> {
            String message = String.format(
                    "Уровень: %d | Опыт: %d / %d | Очки: %d",
                    level.getLevel(),
                    level.getExperience(),
                    level.getExperienceNeededForNextLevel(),
                    level.getAttributePoints()
            );
            source.sendSuccess(() -> Component.literal(message), false);
        });
        return 1;
    }

    private int addExperience(CommandSourceStack source, int amount) throws CommandSyntaxException {
        ServerPlayer player = source.getPlayerOrException();
        player.getCapability(PlayerLevelProvider.PLAYER_LEVEL).ifPresent(level -> {
            level.addExperience(amount, player);
        });
        return 1;
    }

    private int investPoint(CommandSourceStack source, String statName) throws CommandSyntaxException {
        ServerPlayer player = source.getPlayerOrException();

        player.getCapability(PlayerLevelProvider.PLAYER_LEVEL).ifPresent(level -> {
            if (level.getAttributePoints() > 0) {
                player.getCapability(PlayerStatsProvider.PLAYER_STATS).ifPresent(stats -> {
                    if (isStatMaxed(stats, statName)) {
                        source.sendFailure(Component.literal("Характеристика " + statName + " уже достигла максимума."));
                        return;
                    }

                    if (level.spendAttributePoint()) {
                        switch (statName) {
                            case "strength" -> stats.addStrength(1, player);
                            case "dexterity" -> stats.addDexterity(1, player);
                            case "intelligence" -> stats.addIntelligence(1, player);
                            case "vitality" -> stats.addVitality(1, player);
                            case "insight" -> stats.addInsight(1);
                        }
                    }
                });
            }
        });
        return 1;
    }

    private boolean isStatMaxed(PlayerStats stats, String statName) {
        int maxLevel = 100;
        return switch (statName) {
            case "strength" -> stats.getStrength() >= maxLevel;
            case "dexterity" -> stats.getDexterity() >= maxLevel;
            case "intelligence" -> stats.getIntelligence() >= maxLevel;
            case "vitality" -> stats.getVitality() >= maxLevel;
            case "insight" -> stats.getInsight() >= maxLevel;
            default -> true;
        };
    }

    private int getMana(CommandSourceStack source) throws CommandSyntaxException {
        ServerPlayer player = source.getPlayerOrException();

        player.getCapability(PlayerMagicProvider.PLAYER_MAGIC).ifPresent(magic -> {
            float currentMana = magic.getCurrentMana();
            float maxMana = magic.getMaxMana();

            String message = String.format("Mana: %.1f / %.1f", currentMana, maxMana);

            source.sendSuccess(() -> Component.literal(message), false);
        });
        return 1;
    }

    private int getStat(CommandSourceStack source, String statName) throws CommandSyntaxException {
        ServerPlayer player = source.getPlayerOrException();
        player.getCapability(PlayerStatsProvider.PLAYER_STATS).ifPresent(stats -> {
            int value = 0;
            switch (statName) {
                case "strength" -> value = stats.getStrength();
                case "dexterity" -> value = stats.getDexterity();
                case "intelligence" -> value = stats.getIntelligence();
                case "vitality" -> value = stats.getVitality();
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
                case "dexterity" -> stats.setDexterity(value, player);
                case "intelligence" -> stats.setIntelligence(value, player);
                case "vitality" -> stats.setVitality(value);
                case "insight" -> stats.setInsight(value);
            }
            source.sendSuccess(() -> Component.literal("Set " + statName + " to " + value), false);
        });

        return 1;
    }

    private int setMana(CommandSourceStack source, float value) throws CommandSyntaxException {
        ServerPlayer player = source.getPlayerOrException();
        player.getCapability(PlayerMagicProvider.PLAYER_MAGIC).ifPresent(magic -> {
            magic.setMana(value);
        });
        return 1;
    }

    private int addMana(CommandSourceStack source, float value) throws CommandSyntaxException {
        ServerPlayer player = source.getPlayerOrException();
        player.getCapability(PlayerMagicProvider.PLAYER_MAGIC).ifPresent(magic -> {
            magic.addMana(value);
        });
        return 1;
    }

    private int setClass(CommandSourceStack source, String type, String className) throws CommandSyntaxException {
        ServerPlayer player = source.getPlayerOrException();
        try {
            PlayerClass selectedClass = PlayerClass.valueOf(className.toUpperCase());

            player.getCapability(PlayerClassDataProvider.PLAYER_CLASS).ifPresent(classData -> {
                if (type.equals("primary")) {
                    classData.setPrimaryClass(selectedClass);
                } else {
                    classData.setSecondaryClass(selectedClass);
                }

                source.sendSuccess(() -> Component.literal("Установлен " + type + " класс: " + selectedClass.getDisplayName()), false);

                ModEvents.syncAllData(player);
            });

        } catch (IllegalArgumentException e) {
            source.sendFailure(Component.literal("Неизвестный класс: " + className + ". Доступные: warrior, archer, assassin, mage, paladin, necromancer"));
        }
        return 1;
    }

    private int dealDamage(CommandSourceStack source, float amount) throws CommandSyntaxException {
        ServerPlayer player = source.getPlayerOrException();
        player.hurt(player.damageSources().generic(), amount);

        Objects.requireNonNull(player.getServer()).tell(new TickTask(1, () -> ModEvents.syncAllData(player)));

        source.sendSuccess(() -> Component.literal("Вы получили " + amount + " урона"), false);
        return 1;
    }

    private int healPlayer(CommandSourceStack source, float amount) throws CommandSyntaxException {
        ServerPlayer player = source.getPlayerOrException();
        player.heal(amount);

        Objects.requireNonNull(player.getServer()).tell(new TickTask(1, () -> ModEvents.syncAllData(player)));

        source.sendSuccess(() -> Component.literal("Вы исцелились на " + amount), false);
        return 1;
    }

    private int spendMana(CommandSourceStack source, float amount) throws CommandSyntaxException {
        ServerPlayer player = source.getPlayerOrException();
        player.getCapability(PlayerMagicProvider.PLAYER_MAGIC).ifPresent(magic -> {
            magic.consumeMana(amount);
            ModEvents.syncAllData(player);
            source.sendSuccess(() -> Component.literal("Вы потратили " + amount + " маны"), false);
        });
        return 1;
    }

    private int regenMana(CommandSourceStack source, float amount) throws CommandSyntaxException {
        ServerPlayer player = source.getPlayerOrException();
        player.getCapability(PlayerMagicProvider.PLAYER_MAGIC).ifPresent(magic -> {
            magic.addMana(amount);
            ModEvents.syncAllData(player);
            source.sendSuccess(() -> Component.literal("Вы восстановили " + amount + " маны"), false);
        });
        return 1;
    }
}
