package com.nikita.rpgmod.network;

import com.nikita.rpgmod.RPGMod;
import com.nikita.rpgmod.network.cs2packet.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.simple.SimpleChannel;

public class PacketHandler {

    private static final String PROTOCOL_VERSION = "1";
    public static final SimpleChannel INSTANCE = NetworkRegistry.newSimpleChannel(
            ResourceLocation.fromNamespaceAndPath(RPGMod.MOD_ID, "main"),
            () -> PROTOCOL_VERSION,
            PROTOCOL_VERSION::equals,
            PROTOCOL_VERSION::equals
    );

    public static void register() {
        int id = 0;
        INSTANCE.messageBuilder(InvestStatC2SPacket.class, id++, NetworkDirection.PLAY_TO_SERVER)
                .encoder(InvestStatC2SPacket::encode)
                .decoder(InvestStatC2SPacket::decode)
                .consumerMainThread(InvestStatC2SPacket::handle)
                .add();
        INSTANCE.messageBuilder(SyncDataS2CPacket.class, id++, NetworkDirection.PLAY_TO_CLIENT)
                .encoder(SyncDataS2CPacket::encode)
                .decoder(SyncDataS2CPacket::decode)
                .consumerMainThread(SyncDataS2CPacket::handle)
                .add();
        INSTANCE.messageBuilder(SyncMobLevelS2CPacket.class, id++, NetworkDirection.PLAY_TO_CLIENT)
                .encoder(SyncMobLevelS2CPacket::encode)
                .decoder(SyncMobLevelS2CPacket::decode)
                .consumerMainThread(SyncMobLevelS2CPacket::handle)
                .add();
        INSTANCE.messageBuilder(UseInsightSkillC2SPacket.class, id++, NetworkDirection.PLAY_TO_SERVER)
                .encoder(UseInsightSkillC2SPacket::encode)
                .decoder(UseInsightSkillC2SPacket::decode)
                .consumerMainThread(UseInsightSkillC2SPacket::handle)
                .add();
        INSTANCE.messageBuilder(HighlightEnemiesS2CPacket.class, id++, NetworkDirection.PLAY_TO_CLIENT)
                .encoder(HighlightEnemiesS2CPacket::encode)
                .decoder(HighlightEnemiesS2CPacket::decode)
                .consumerMainThread(HighlightEnemiesS2CPacket::handle)
                .add();
        INSTANCE.messageBuilder(CastSpellC2SPacket.class, id++, NetworkDirection.PLAY_TO_SERVER)
                .encoder(CastSpellC2SPacket::encode)
                .decoder(CastSpellC2SPacket::decode)
                .consumerMainThread(CastSpellC2SPacket::handle)
                .add();
        INSTANCE.messageBuilder(ChangeSpellC2SPacket.class, id++, NetworkDirection.PLAY_TO_SERVER)
                .encoder(ChangeSpellC2SPacket::encode)
                .decoder(ChangeSpellC2SPacket::decode)
                .consumerMainThread(ChangeSpellC2SPacket::handle)
                .add();
    }

    public static <MSG> void sendToServer(MSG message) {
        INSTANCE.sendToServer(message);
    }

    public static <MSG> void sendToPlayer(MSG message, ServerPlayer player) {
        INSTANCE.send(PacketDistributor.PLAYER.with(() -> player), message);
    }
}
