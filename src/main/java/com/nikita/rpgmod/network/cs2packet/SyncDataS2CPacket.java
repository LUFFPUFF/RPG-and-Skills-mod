package com.nikita.rpgmod.network.cs2packet;

import com.nikita.rpgmod.client.ClientData;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class SyncDataS2CPacket {

    private final int level, experience, experienceNeeded, attributePoints;
    private final float currentMana, maxMana;
    private final float currentHealth, maxHealth;
    private final int currentHunger;
    private final int strength, dexterity, intelligence, vitality, insight;
    private final String playerClassName;

    public SyncDataS2CPacket(int level, int experience, int expNeeded, int points, float curMana, float maxMana, float curHealth, float maxHealth, int curHunger, int str, int dex, int intel, int vit, int ins, String playerClassName) {
        this.level = level; this.experience = experience; this.experienceNeeded = expNeeded; this.attributePoints = points;
        this.currentMana = curMana; this.maxMana = maxMana;
        this.currentHealth = curHealth; this.maxHealth = maxHealth;
        this.currentHunger = curHunger;
        this.strength = str; this.dexterity = dex; this.intelligence = intel; this.vitality = vit; this.insight = ins;
        this.playerClassName = playerClassName;
    }

    public static void encode(SyncDataS2CPacket pkt, FriendlyByteBuf buf) {
        buf.writeInt(pkt.level); buf.writeInt(pkt.experience); buf.writeInt(pkt.experienceNeeded); buf.writeInt(pkt.attributePoints);
        buf.writeFloat(pkt.currentMana); buf.writeFloat(pkt.maxMana);
        buf.writeFloat(pkt.currentHealth); buf.writeFloat(pkt.maxHealth);
        buf.writeInt(pkt.currentHunger);
        buf.writeInt(pkt.strength); buf.writeInt(pkt.dexterity); buf.writeInt(pkt.intelligence); buf.writeInt(pkt.vitality); buf.writeInt(pkt.insight);
        buf.writeUtf(pkt.playerClassName);
    }

    public static SyncDataS2CPacket decode(FriendlyByteBuf buf) {
        int level = buf.readInt();
        int experience = buf.readInt();
        int experienceNeeded = buf.readInt();
        int attributePoints = buf.readInt();
        float currentMana = buf.readFloat();
        float maxMana = buf.readFloat();
        float currentHealth = buf.readFloat();
        float maxHealth = buf.readFloat();
        int currentHunger = buf.readInt();
        int strength = buf.readInt();
        int dexterity = buf.readInt();
        int intelligence = buf.readInt();
        int vitality = buf.readInt();
        int insight = buf.readInt();
        String playerClassName = buf.readUtf();

        return new SyncDataS2CPacket(level, experience, experienceNeeded, attributePoints, currentMana, maxMana, currentHealth, maxHealth, currentHunger, strength, dexterity, intelligence, vitality, insight, playerClassName);
    }

    public static void handle(SyncDataS2CPacket pkt, Supplier<NetworkEvent.Context> context) {
        context.get().enqueueWork(() -> {
            ClientData.playerLevel = pkt.level; ClientData.playerExperience = pkt.experience; ClientData.experienceNeeded = pkt.experienceNeeded; ClientData.attributePoints = pkt.attributePoints;
            ClientData.currentMana = pkt.currentMana; ClientData.maxMana = pkt.maxMana;
            ClientData.currentHealth = pkt.currentHealth; ClientData.maxHealth = pkt.maxHealth;
            ClientData.currentHunger = pkt.currentHunger;
            ClientData.strength = pkt.strength; ClientData.dexterity = pkt.dexterity; ClientData.intelligence = pkt.intelligence; ClientData.vitality = pkt.vitality; ClientData.insight = pkt.insight;
            ClientData.playerClassName = pkt.playerClassName;
        });
        context.get().setPacketHandled(true);
    }
}
