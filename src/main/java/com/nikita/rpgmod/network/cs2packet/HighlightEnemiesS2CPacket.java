package com.nikita.rpgmod.network.cs2packet;

import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraftforge.fml.util.ObfuscationReflectionHelper;
import net.minecraftforge.network.NetworkEvent;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class HighlightEnemiesS2CPacket {
    private final List<Integer> enemyIds;
    private static final Method setSharedFlagMethod = ObfuscationReflectionHelper.findMethod(Entity.class, "m_20115_", int.class, boolean.class);

    public HighlightEnemiesS2CPacket(List<Integer> enemyIds) { this.enemyIds = enemyIds; }
    public static void encode(HighlightEnemiesS2CPacket pkt, FriendlyByteBuf buf) { buf.writeVarIntArray(pkt.enemyIds.stream().mapToInt(i->i).toArray()); }
    public static HighlightEnemiesS2CPacket decode(FriendlyByteBuf buf) {
        return new HighlightEnemiesS2CPacket(Arrays.stream(buf.readVarIntArray()).boxed().collect(Collectors.toList()));
    }

    public static void handle(HighlightEnemiesS2CPacket pkt, Supplier<NetworkEvent.Context> context) {
        context.get().enqueueWork(() -> {
            Level level = Minecraft.getInstance().level;
            if (level == null) return;

            for (int id : pkt.enemyIds) {
                Entity entity = level.getEntity(id);
                if (entity != null) {
                    try {
                        setSharedFlagMethod.invoke(entity, 6, true);

                        new Thread(() -> {
                            try {
                                Thread.sleep(5000);
                                Minecraft.getInstance().execute(() -> {
                                    Entity stillExistingEntity = level.getEntity(id);
                                    if (stillExistingEntity != null) {
                                        try {
                                            setSharedFlagMethod.invoke(stillExistingEntity, 6, false);
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                    }
                                });
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }).start();

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        context.get().setPacketHandled(true);
    }
}
