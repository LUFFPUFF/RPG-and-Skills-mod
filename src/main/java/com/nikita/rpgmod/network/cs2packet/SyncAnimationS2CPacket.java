package com.nikita.rpgmod.network.cs2packet;

import com.nikita.rpgmod.capibility.PlayerAnimationProvider;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class SyncAnimationS2CPacket {
    private final int playerId;
    private final String animationName;
    private final int duration;

    public SyncAnimationS2CPacket(int playerId, String animationName, int duration) {
        this.playerId = playerId;
        this.animationName = animationName;
        this.duration = duration;
    }

    public static void encode(SyncAnimationS2CPacket pkt, FriendlyByteBuf buf) {
        buf.writeInt(pkt.playerId);
        buf.writeUtf(pkt.animationName);
        buf.writeInt(pkt.duration);
    }

    public static SyncAnimationS2CPacket decode(FriendlyByteBuf buf) {
        return new SyncAnimationS2CPacket(buf.readInt(), buf.readUtf(), buf.readInt());
    }

    public static void handle(SyncAnimationS2CPacket pkt, Supplier<NetworkEvent.Context> context) {
        context.get().enqueueWork(() -> {
            Minecraft mc = Minecraft.getInstance();
            if (mc.level == null) return;
            Entity entity = mc.level.getEntity(pkt.playerId);
            if (entity instanceof Player player) {
                player.getCapability(PlayerAnimationProvider.PLAYER_ANIMATION).ifPresent(animData -> {
                    animData.setAnimation(pkt.animationName, pkt.duration);
                });
            }
        });
        context.get().setPacketHandled(true);
    }
}
