package com.nikita.rpgmod.network.cs2packet;

import com.nikita.rpgmod.client.ClientData;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class SyncDataS2CPacket {

    private final int level, experience, experienceNeeded, attributePoints;
    private final float currentMana, maxMana;
    private final float currentHealth, maxHealth;
    private final int strength, dexterity, intelligence, vitality, insight;

    public SyncDataS2CPacket(int level, int experience, int expNeeded, int points, float curMana, float maxMana, float curHealth, float maxHealth, int str, int dex, int intel, int vit, int ins) {
        this.level = level; this.experience = experience; this.experienceNeeded = expNeeded; this.attributePoints = points;
        this.currentMana = curMana; this.maxMana = maxMana;
        this.currentHealth = curHealth; this.maxHealth = maxHealth;
        this.strength = str; this.dexterity = dex; this.intelligence = intel; this.vitality = vit; this.insight = ins;
    }

    public static void encode(SyncDataS2CPacket pkt, FriendlyByteBuf buf) {
        buf.writeInt(pkt.level); buf.writeInt(pkt.experience); buf.writeInt(pkt.experienceNeeded); buf.writeInt(pkt.attributePoints);
        buf.writeFloat(pkt.currentMana); buf.writeFloat(pkt.maxMana);
        buf.writeFloat(pkt.currentHealth); buf.writeFloat(pkt.maxHealth);
        buf.writeInt(pkt.strength); buf.writeInt(pkt.dexterity); buf.writeInt(pkt.intelligence); buf.writeInt(pkt.vitality); buf.writeInt(pkt.insight);
    }

    public static SyncDataS2CPacket decode(FriendlyByteBuf buf) {
        return new SyncDataS2CPacket(buf.readInt(), buf.readInt(), buf.readInt(), buf.readInt(), buf.readFloat(), buf.readFloat(), buf.readFloat(), buf.readFloat(), buf.readInt(), buf.readInt(), buf.readInt(), buf.readInt(), buf.readInt());
    }

    public static void handle(SyncDataS2CPacket pkt, Supplier<NetworkEvent.Context> context) {
        ClientData.playerLevel = pkt.level; ClientData.playerExperience = pkt.experience; ClientData.experienceNeeded = pkt.experienceNeeded; ClientData.attributePoints = pkt.attributePoints;
        ClientData.currentMana = pkt.currentMana; ClientData.maxMana = pkt.maxMana;
        ClientData.currentHealth = pkt.currentHealth; ClientData.maxHealth = pkt.maxHealth;
        ClientData.strength = pkt.strength; ClientData.dexterity = pkt.dexterity; ClientData.intelligence = pkt.intelligence; ClientData.vitality = pkt.vitality; ClientData.insight = pkt.insight;
    }
}
