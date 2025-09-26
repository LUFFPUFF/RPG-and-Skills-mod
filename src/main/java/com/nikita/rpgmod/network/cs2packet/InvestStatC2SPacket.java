package com.nikita.rpgmod.network.cs2packet;

import com.nikita.rpgmod.capibility.PlayerStatsProvider;
import com.nikita.rpgmod.event.ModEvents;
import com.nikita.rpgmod.level.stats.PlayerLevelProvider;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class InvestStatC2SPacket {
    private final String statName;

    public InvestStatC2SPacket(String statName) {
        this.statName = statName;
    }

    public static void encode(InvestStatC2SPacket pkt, FriendlyByteBuf buf) {
        buf.writeUtf(pkt.statName);
    }

    public static InvestStatC2SPacket decode(FriendlyByteBuf buf) {
        return new InvestStatC2SPacket(buf.readUtf());
    }

    public static void handle(InvestStatC2SPacket pkt, Supplier<NetworkEvent.Context> context) {
        ServerPlayer player = context.get().getSender();
        if (player == null) return;

        player.getCapability(PlayerLevelProvider.PLAYER_LEVEL).ifPresent(level -> {
            if (level.spendAttributePoint()) {
                player.getCapability(PlayerStatsProvider.PLAYER_STATS).ifPresent(stats -> {
                    switch (pkt.statName) {
                        case "strength" -> stats.addStrength(1, player);
                        case "dexterity" -> stats.addDexterity(1, player);
                        case "intelligence" -> stats.addIntelligence(1, player);
                        case "vitality" -> stats.addVitality(1, player);
                        case "insight" -> stats.addInsight(1);
                    }
                    ModEvents.syncAllData(player);
                    player.sendSystemMessage(Component.literal("Вы вложили очко в " + pkt.statName));
                });
            } else {
                player.sendSystemMessage(Component.literal("У вас нет очков для вложения!"));
            }
        });
    }
}
