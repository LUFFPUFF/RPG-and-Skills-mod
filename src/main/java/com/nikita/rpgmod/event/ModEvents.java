package com.nikita.rpgmod.event;


import com.nikita.rpgmod.RPGMod;
import com.nikita.rpgmod.capibility.PlayerStatsProvider;
import com.nikita.rpgmod.classes.PlayerClassData;
import com.nikita.rpgmod.classes.PlayerClassDataProvider;
import com.nikita.rpgmod.combat.CombatEngine;
import com.nikita.rpgmod.command.StatsCommand;
import com.nikita.rpgmod.level.mob.MobLevelProvider;
import com.nikita.rpgmod.level.stats.PlayerLevel;
import com.nikita.rpgmod.level.stats.PlayerLevelProvider;
import com.nikita.rpgmod.level.tracker.MobDamageTracker;
import com.nikita.rpgmod.level.tracker.MobDamageTrackerProvider;
import com.nikita.rpgmod.magic.stats.PlayerMagicProvider;
import com.nikita.rpgmod.network.PacketHandler;
import com.nikita.rpgmod.network.cs2packet.SyncDataS2CPacket;
import com.nikita.rpgmod.network.cs2packet.SyncMobLevelS2CPacket;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.boss.enderdragon.EnderDragon;
import net.minecraft.world.entity.boss.wither.WitherBoss;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.EntityJoinLevelEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.living.MobEffectEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraft.world.entity.Entity;

import javax.annotation.Nullable;
import java.util.*;

@Mod.EventBusSubscriber(modid = RPGMod.MOD_ID)
public class ModEvents {

    @Mod.EventBusSubscriber(modid = RPGMod.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
    public static class ModEventBusEvents {
        @SubscribeEvent
        public static void registerCapabilities(RegisterCapabilitiesEvent event) {
            event.register(com.nikita.rpgmod.capibility.PlayerStats.class);
            event.register(com.nikita.rpgmod.magic.stats.PlayerMagicStats.class);
            event.register(PlayerLevel.class);
            event.register(MobDamageTracker.class);
            event.register(PlayerClassData.class);
        }
    }

    // Это событие "прикрепляет" наши статы к игроку при его создании
    @SubscribeEvent
    public static void onAttachCapabilitiesPlayer(AttachCapabilitiesEvent<Entity> event) {
        if (event.getObject() instanceof Player player) {
            if (!player.getCapability(PlayerStatsProvider.PLAYER_STATS).isPresent()) {
                event.addCapability(ResourceLocation.fromNamespaceAndPath(RPGMod.MOD_ID, "player_stats"), new PlayerStatsProvider());
            }
            if (!player.getCapability(PlayerMagicProvider.PLAYER_MAGIC).isPresent()) {
                event.addCapability(ResourceLocation.fromNamespaceAndPath(RPGMod.MOD_ID, "player_magic"), new PlayerMagicProvider(player));
            }
            if (!player.getCapability(PlayerLevelProvider.PLAYER_LEVEL).isPresent()) {
                event.addCapability(ResourceLocation.fromNamespaceAndPath(RPGMod.MOD_ID, "player_level"), new PlayerLevelProvider());
            }
            if (!player.getCapability(PlayerClassDataProvider.PLAYER_CLASS).isPresent()) {
                event.addCapability(ResourceLocation.fromNamespaceAndPath(RPGMod.MOD_ID, "player_class"), new PlayerClassDataProvider());
            }
        }
    }

    // Это событие реагирует на урон по враждебным мобам
    @SubscribeEvent
    public static void onAttachCapabilitiesEntity(AttachCapabilitiesEvent<Entity> event) {
        if (event.getObject() instanceof Enemy) {
            event.getObject().getCapability(MobDamageTrackerProvider.MOB_DAMAGE_TRACKER).ifPresent(tracker -> {});
            event.addCapability(ResourceLocation.fromNamespaceAndPath(RPGMod.MOD_ID, "mob_damage_tracker"), new MobDamageTrackerProvider());
            event.addCapability(ResourceLocation.fromNamespaceAndPath(RPGMod.MOD_ID, "mob_level"), new MobLevelProvider());
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
                    newStats.setVitality(oldStats.getVitality());
                    newStats.setInsight(oldStats.getInsight());
                    newStats.setIntelligence(oldStats.getIntelligence());
                });
            });
            event.getEntity().getCapability(PlayerMagicProvider.PLAYER_MAGIC).ifPresent(newMagic -> {
                newMagic.setMana(newMagic.getMaxMana());
            });
            event.getOriginal().getCapability(PlayerLevelProvider.PLAYER_LEVEL).ifPresent(oldLevel -> {
                event.getEntity().getCapability(PlayerLevelProvider.PLAYER_LEVEL).ifPresent(newLevel -> {
                    CompoundTag nbt = new CompoundTag();
                    oldLevel.saveNBTData(nbt);
                    newLevel.loadNBTData(nbt);
                });
            });

            event.getOriginal().getCapability(PlayerClassDataProvider.PLAYER_CLASS).ifPresent(oldClass -> {
                event.getEntity().getCapability(PlayerClassDataProvider.PLAYER_CLASS).ifPresent(newClass -> {
                    CompoundTag nbt = new CompoundTag();
                    oldClass.saveNBTData(nbt);
                    newClass.loadNBTData(nbt);
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
        LivingEntity target = event.getEntity();
        float originalDamage = event.getAmount();
        @Nullable LivingEntity attacker = event.getSource().getEntity() instanceof LivingEntity ? (LivingEntity) event.getSource().getEntity() : null;

        float finalDamage = CombatEngine.getInstance().calculateFinalDamage(attacker, target, event.getSource(), originalDamage);
        event.setAmount(finalDamage);

        if (attacker instanceof ServerPlayer player && target instanceof Enemy) {
            target.getCapability(MobDamageTrackerProvider.MOB_DAMAGE_TRACKER).ifPresent(tracker -> {
                tracker.addDamage(player, originalDamage);
            });
        }
    }

    @SubscribeEvent
    public static void onLivingDeath(LivingDeathEvent event) {
        LivingEntity entity = event.getEntity();
        if (entity.level().isClientSide || !(entity instanceof Enemy)) {
            return;
        }

        ServerPlayer killer = event.getSource().getEntity() instanceof ServerPlayer p ? p : null;

        entity.getCapability(MobDamageTrackerProvider.MOB_DAMAGE_TRACKER).ifPresent(tracker -> {
            var damageMap = tracker.getDamageMap();
            if (damageMap.isEmpty()) return;

            for (Map.Entry<UUID, Float> entry : damageMap.entrySet()) {
                UUID playerUUID = entry.getKey();
                ServerPlayer player = (ServerPlayer) entity.level().getPlayerByUUID(playerUUID);
                if (player == null) continue;

                player.getCapability(PlayerLevelProvider.PLAYER_LEVEL).ifPresent(playerLevel -> {
                    int baseExp = getBaseXpForMob(entity);

                    int mobLevel = (int) entity.getMaxHealth() / 2;
                    int levelDiff = playerLevel.getLevel() - mobLevel;
                    if (levelDiff > 5) {
                        baseExp *= (int) Math.max(0.1, 1.0 - (levelDiff - 5) * 0.1);
                    }

                    int finalExp;
                    if (killer != null && killer.getUUID().equals(playerUUID)) {
                        finalExp = baseExp;
                    } else {
                        finalExp = baseExp / 2;
                    }

                    if (finalExp > 0) {
                        playerLevel.addExperience(finalExp, player);
                    }
                });
            }
        });
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

    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
        if (event.phase == TickEvent.Phase.END && !event.player.level().isClientSide()) {
            ServerPlayer player = (ServerPlayer) event.player;

            if (player.tickCount % 20 == 0) {
                player.getCapability(PlayerStatsProvider.PLAYER_STATS).ifPresent(stats -> {
                    player.getCapability(PlayerMagicProvider.PLAYER_MAGIC).ifPresent(magic -> {
                        if (magic.getCurrentMana() < magic.getMaxMana()) {
                            float regenAmount = 1.0f + (stats.getIntelligence() * 0.2f);
                            magic.addMana(regenAmount);
                        }
                    });
                });
                syncAllData(player);
            }

            if (player.tickCount % 100 == 0) {
                player.getCapability(PlayerStatsProvider.PLAYER_STATS).ifPresent(stats -> {
                    float regenAmount = stats.getVitality() * 0.1f;
                    if (regenAmount > 0) {
                        player.heal(regenAmount);
                    }
                });
            }
        }
    }

    @SubscribeEvent
    public static void onMobSpawn(EntityJoinLevelEvent event) {
        Entity entity = event.getEntity();

        if (event.getLevel().isClientSide() || !(entity instanceof Enemy)) {
            return;
        }

        final double searchRadius = 64.0;

        List<Player> nearbyPlayers = event.getLevel().getEntitiesOfClass(
                Player.class,
                entity.getBoundingBox().inflate(searchRadius)
        );

        if (nearbyPlayers.isEmpty()) {
            entity.getCapability(MobLevelProvider.MOB_LEVEL).ifPresent(mobLevelCap -> {
                mobLevelCap.setLevel(1);
            });
            return;
        }

        OptionalInt maxPlayerLevelOpt = nearbyPlayers.stream()
                .mapToInt(player -> player.getCapability(PlayerLevelProvider.PLAYER_LEVEL)
                        .map(PlayerLevel::getLevel)
                        .orElse(1))
                .max();

        int maxPlayerLevel = maxPlayerLevelOpt.getAsInt();
        Random random = new Random();

        double dimensionMultiplier = 1.0;
        ResourceKey<Level> dimensionKey = event.getLevel().dimension();
        if (dimensionKey.equals(Level.NETHER)) {
            dimensionMultiplier = 1.5;
        } else if (dimensionKey.equals(Level.END)) {
            dimensionMultiplier = 2.0;
        }

        int levelVariance = random.nextInt(5) - 2;
        int calculatedLevel = (int) Math.round((maxPlayerLevel + levelVariance) * dimensionMultiplier);
        int finalLevel = Math.max(1, Math.min(100, calculatedLevel));

        entity.getCapability(MobLevelProvider.MOB_LEVEL).ifPresent(mobLevelCap -> {
            mobLevelCap.setLevel(finalLevel);
        });
    }

    @SubscribeEvent
    public static void onEffectApplied(MobEffectEvent.Added event) {
        if (event.getEntity() instanceof Player player) {
            MobEffectInstance effectInstance = event.getEffectInstance();

            if (effectInstance.getEffect().isBeneficial()) {
                player.getCapability(PlayerStatsProvider.PLAYER_STATS).ifPresent(stats -> {
                    int insight = stats.getInsight();
                    if (insight > 0) {
                        float durationMultiplier = 1.0f + (insight * 0.01f);
                        int newDuration = (int) (effectInstance.getDuration() * durationMultiplier);

                        MobEffectInstance newEffect = new MobEffectInstance(
                                effectInstance.getEffect(),
                                newDuration,
                                effectInstance.getAmplifier(),
                                effectInstance.isAmbient(),
                                effectInstance.isVisible(),
                                effectInstance.showIcon()
                        );

                        event.getEffectInstance().update(newEffect);
                    }
                });
            }
        }
    }

    @SubscribeEvent
    public static void onPlayerLoggedIn(PlayerEvent.PlayerLoggedInEvent event) {
        if (event.getEntity() instanceof ServerPlayer player) {
            syncAllData(player);
        }
    }

    @SubscribeEvent
    public static void onStartTracking(PlayerEvent.StartTracking event) {
        Entity target = event.getTarget();

        if (target instanceof Enemy && event.getEntity() instanceof ServerPlayer player) {

            target.getCapability(MobLevelProvider.MOB_LEVEL).ifPresent(mobLevelCap -> {
                PacketHandler.sendToPlayer(new SyncMobLevelS2CPacket(target.getId(), mobLevelCap.getLevel()), player);
            });
        }
    }

    private static int getBaseXpForMob(LivingEntity mob) {
        if (mob instanceof EnderDragon) return 12000;
        if (mob instanceof WitherBoss) return 1000;

        if (mob instanceof Enemy) {
            return 20;
        }
        return 15;
    }

    public static void syncAllData(ServerPlayer player) {
        player.getCapability(PlayerLevelProvider.PLAYER_LEVEL).ifPresent(level -> {
            player.getCapability(PlayerMagicProvider.PLAYER_MAGIC).ifPresent(magic -> {
                player.getCapability(PlayerStatsProvider.PLAYER_STATS).ifPresent(stats -> {
                    player.getCapability(PlayerClassDataProvider.PLAYER_CLASS).ifPresent(classData -> {

                        PacketHandler.sendToPlayer(new SyncDataS2CPacket(
                                level.getLevel(), level.getExperience(), level.getExperienceNeededForNextLevel(), level.getAttributePoints(),
                                magic.getCurrentMana(), magic.getMaxMana(),
                                player.getHealth(), player.getMaxHealth(),
                                player.getFoodData().getFoodLevel(),
                                stats.getStrength(), stats.getDexterity(), stats.getIntelligence(), stats.getVitality(), stats.getInsight(),
                                classData.getDisplayName()
                        ), player);

                    });
                });
            });
        });
    }
}
