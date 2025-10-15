package com.nikita.rpgmod.network.cs2packet;

import com.nikita.rpgmod.capibility.PlayerAnimationProvider;
import com.nikita.rpgmod.item.custom.GrimoireHourglassItem;
import com.nikita.rpgmod.magic.PlayerMagicProvider;
import com.nikita.rpgmod.magic.spell.ISpell;
import com.nikita.rpgmod.magic.spell.capability.PlayerSpellsProvider;
import com.nikita.rpgmod.network.PacketHandler;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.Item;
import net.minecraftforge.network.NetworkEvent;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.PacketDistributor;

import java.util.function.Supplier;

public class CastSpellC2SPacket {

    public CastSpellC2SPacket() {}
    public static void encode(CastSpellC2SPacket pkt, FriendlyByteBuf buf) {}
    public static CastSpellC2SPacket decode(FriendlyByteBuf buf) { return new CastSpellC2SPacket(); }

    public static void handle(CastSpellC2SPacket pkt, Supplier<NetworkEvent.Context> context) {
        context.get().enqueueWork(() -> {
            ServerPlayer player = context.get().getSender();
            if (player == null) return;

            Item heldItem = player.getMainHandItem().getItem();
            if (!(heldItem instanceof GrimoireHourglassItem)) {
                player.sendSystemMessage(Component.literal("Нужно держать гримуар в руке!"), true);
                return;
            }

            if (player.getCooldowns().isOnCooldown(heldItem)) {
                player.sendSystemMessage(Component.literal("Заклинание еще не готово!"), true);
                return;
            }

            player.getCapability(PlayerSpellsProvider.PLAYER_SPELLS).ifPresent(spellsData -> {
                ISpell currentSpell = spellsData.getCurrentSpell();
                if (currentSpell == null) return;

                player.getCapability(PlayerMagicProvider.PLAYER_MAGIC).ifPresent(magic -> {
                    if (magic.consumeMana(currentSpell.getManaCost(player))) {
                        player.getCooldowns().addCooldown(heldItem, currentSpell.getCooldownTicks(player));
                        currentSpell.cast(player);

                        player.getCapability(PlayerAnimationProvider.PLAYER_ANIMATION).ifPresent(animData -> {
                            animData.setAnimation(currentSpell.getAnimationName(), currentSpell.getAnimationLengthTicks());
                            PacketHandler.INSTANCE.send(PacketDistributor.TRACKING_ENTITY_AND_SELF.with(() -> player),
                                    new SyncAnimationS2CPacket(player.getId(), currentSpell.getAnimationName(), currentSpell.getAnimationLengthTicks()));
                        });

                    } else {
                        player.sendSystemMessage(Component.literal("Недостаточно маны!"), true);
                    }
                });
            });
        });
        context.get().setPacketHandled(true);
    }
}
