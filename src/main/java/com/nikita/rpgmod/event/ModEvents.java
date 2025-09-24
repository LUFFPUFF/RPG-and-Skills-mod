package com.nikita.rpgmod.event;


import com.nikita.rpgmod.RPGMod;
import com.nikita.rpgmod.capibility.PlayerStatsProvider;
import com.nikita.rpgmod.combat.CombatEngine;
import com.nikita.rpgmod.command.StatsCommand;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraft.world.entity.Entity;

import javax.annotation.Nullable;

@Mod.EventBusSubscriber(modid = RPGMod.MOD_ID)
public class ModEvents {

    @Mod.EventBusSubscriber(modid = RPGMod.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
    public static class ModEventBusEvents {
        @SubscribeEvent
        public static void registerCapabilities(RegisterCapabilitiesEvent event) {
            event.register(com.nikita.rpgmod.capibility.PlayerStats.class);
        }
    }

    // Это событие "прикрепляет" наши статы к игроку при его создании
    @SubscribeEvent
    public static void onAttachCapabilitiesPlayer(AttachCapabilitiesEvent<Entity> event) {
        if (event.getObject() instanceof Player) {
            if (!event.getObject().getCapability(PlayerStatsProvider.PLAYER_STATS).isPresent()) {
                event.addCapability(ResourceLocation.fromNamespaceAndPath(RPGMod.MOD_ID, "player_stats"), new PlayerStatsProvider());
            }
        }
    }

    // Это событие копирует статы со старого "тела" игрока на новое после смерти
    @SubscribeEvent
    public static void onPlayerCloned(PlayerEvent.Clone event) {
        if (event.isWasDeath()) {
            event.getOriginal().getCapability(PlayerStatsProvider.PLAYER_STATS).ifPresent(oldStats -> {
                event.getEntity().getCapability(PlayerStatsProvider.PLAYER_STATS).ifPresent(newStats -> {
                    newStats.setStrength(oldStats.getStrength());
                    newStats.setDexterity(oldStats.getDexterity());
                    newStats.setHealth(oldStats.getHealth());
                    newStats.setInsight(oldStats.getInsight());
                    newStats.setIntelligence(oldStats.getIntelligence());
                });
            });
        }
    }

    //Регистрация команд stats
    @SubscribeEvent
    public static void onCommandsRegister(RegisterCommandsEvent event) {
        new StatsCommand(event.getDispatcher());
    }

    @SubscribeEvent
    public static void onLivingHurt(LivingHurtEvent event) {
        @Nullable LivingEntity attacker = event.getSource().getEntity() instanceof LivingEntity ? (LivingEntity) event.getSource().getEntity() : null;

        LivingEntity target = event.getEntity();
        float originalDamage = event.getAmount();

        float finalDamage = CombatEngine.getInstance().calculateFinalDamage(
                attacker,
                target,
                event.getSource(),
                originalDamage
        );

        event.setAmount(finalDamage);
    }

    @SubscribeEvent
    public static void onPlayerBreakSpeed(PlayerEvent.BreakSpeed event) {
        Player player = event.getEntity();
        player.getCapability(PlayerStatsProvider.PLAYER_STATS).ifPresent(stats -> {
            int strength = stats.getStrength();
            if (strength > 1) {
                float speedMultiplier = 1.0f + (strength * 0.01f);
                event.setNewSpeed(event.getOriginalSpeed() * speedMultiplier);
            }
        });
    }
}
