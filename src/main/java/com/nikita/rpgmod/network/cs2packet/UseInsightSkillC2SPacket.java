package com.nikita.rpgmod.network.cs2packet;

import com.nikita.rpgmod.capibility.PlayerStatsProvider;
import com.nikita.rpgmod.magic.stats.PlayerMagicProvider;
import com.nikita.rpgmod.network.PacketHandler;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.boss.enderdragon.EnderDragon;
import net.minecraft.world.entity.boss.wither.WitherBoss;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.item.Items;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.network.NetworkEvent;

import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class UseInsightSkillC2SPacket {

    public UseInsightSkillC2SPacket() {}

    public static void encode(UseInsightSkillC2SPacket pkt, FriendlyByteBuf buf) {}

    public static UseInsightSkillC2SPacket decode(FriendlyByteBuf buf) {
        return new UseInsightSkillC2SPacket();
    }

    public static void handle(UseInsightSkillC2SPacket pkt, Supplier<NetworkEvent.Context> context) {
        ServerPlayer player = context.get().getSender();
        if (player == null) return;

        // Получаем сразу все необходимые capabilities
        player.getCapability(PlayerStatsProvider.PLAYER_STATS).ifPresent(stats -> {
            player.getCapability(PlayerMagicProvider.PLAYER_MAGIC).ifPresent(magic -> {

                float manaCost = 25.0f;
                int cooldownTicks = 200;

                if (player.getCooldowns().isOnCooldown(Items.SPYGLASS)) {
                    player.sendSystemMessage(Component.literal("Способность еще не готова!"), true);
                    return;
                }

                if (magic.consumeMana(manaCost)) {

                    player.getCooldowns().addCooldown(Items.SPYGLASS, cooldownTicks);

                    int insight = stats.getInsight();
                    if (insight <= 0) return;

                    double radius = 5.0 + (insight * 0.5);

                    AABB searchBox = new AABB(player.blockPosition()).inflate(radius);
                    List<LivingEntity> enemies = player.level().getEntitiesOfClass(LivingEntity.class, searchBox, e -> e instanceof Enemy && e.isAlive());

                    List<Integer> enemyIds;
                    if (insight < 50) {
                        enemyIds = enemies.stream()
                                .filter(e -> !(e instanceof EnderDragon) && !(e instanceof WitherBoss))
                                .map(Entity::getId)
                                .collect(Collectors.toList());
                    } else {
                        enemyIds = enemies.stream().map(Entity::getId).collect(Collectors.toList());
                    }

                    if (!enemyIds.isEmpty()) {
                        PacketHandler.sendToPlayer(new HighlightEnemiesS2CPacket(enemyIds), player);
                    }

                } else {
                    player.sendSystemMessage(Component.literal("Недостаточно маны!"), true);
                }
            });
        });
    }
}
