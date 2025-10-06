package com.nikita.rpgmod.network.cs2packet;

import com.nikita.rpgmod.level.mob.MobLevelProvider;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class SyncMobLevelS2CPacket {

    private final int entityId;
    private final int level;

    public SyncMobLevelS2CPacket(int entityId, int level) { this.entityId = entityId; this.level = level; }
    public static void encode(SyncMobLevelS2CPacket pkt, FriendlyByteBuf buf) { buf.writeInt(pkt.entityId); buf.writeInt(pkt.level); }
    public static SyncMobLevelS2CPacket decode(FriendlyByteBuf buf) { return new SyncMobLevelS2CPacket(buf.readInt(), buf.readInt()); }

    public static void handle(SyncMobLevelS2CPacket pkt, Supplier<NetworkEvent.Context> context) {
        context.get().enqueueWork(() -> {
            assert Minecraft.getInstance().level != null;
            Entity entity = Minecraft.getInstance().level.getEntity(pkt.entityId);
            if (entity != null) {
                entity.getCapability(MobLevelProvider.MOB_LEVEL).ifPresent(cap -> cap.setLevel(pkt.level));
            }
        });
        context.get().setPacketHandled(true);
    }
}
