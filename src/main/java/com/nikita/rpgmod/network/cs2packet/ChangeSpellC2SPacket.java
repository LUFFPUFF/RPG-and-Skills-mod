package com.nikita.rpgmod.network.cs2packet;

import com.nikita.rpgmod.magic.spell.capability.PlayerSpellsProvider;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class ChangeSpellC2SPacket {
    private final boolean next;
    public ChangeSpellC2SPacket(boolean next) { this.next = next; }
    public static void encode(ChangeSpellC2SPacket pkt, FriendlyByteBuf buf) { buf.writeBoolean(pkt.next); }
    public static ChangeSpellC2SPacket decode(FriendlyByteBuf buf) { return new ChangeSpellC2SPacket(buf.readBoolean()); }

    public static void handle(ChangeSpellC2SPacket pkt, Supplier<NetworkEvent.Context> context) {
        context.get().enqueueWork(() -> {
            ServerPlayer player = context.get().getSender();
            if (player == null) return;

            player.getCapability(PlayerSpellsProvider.PLAYER_SPELLS).ifPresent(spellsData -> {
                if (pkt.next) {
                    spellsData.selectNextSpell();
                } else {
                    spellsData.selectPreviousSpell();
                }
                // TODO: Отправить пакет обратно клиенту, чтобы обновить HUD
            });
        });
        context.get().setPacketHandled(true);
    }
}
