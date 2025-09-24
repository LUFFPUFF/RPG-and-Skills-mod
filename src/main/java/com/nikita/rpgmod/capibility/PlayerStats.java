package com.nikita.rpgmod.capibility;

import com.nikita.rpgmod.attributes.AttributeUpdateService;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Player;

import javax.annotation.Nullable;

public class PlayerStats {

    private int strength; //сила
    private int dexterity; //ловкость
    private int intelligence; //интелект
    private int health; //здоровье
    private int insight; //проницательность

    private static final int MIN_LEVEL = 1;
    private static final int MAX_LEVEL = 100;

    public  PlayerStats() {
        this.strength = MIN_LEVEL;
        this.dexterity = MIN_LEVEL;
        this.intelligence = MIN_LEVEL;
        this.health = MIN_LEVEL;
        this.insight = MIN_LEVEL;
    }

    // ---Strength---
    public int getStrength() {
        return strength;
    }

    /**
     * Простой сеттер для внутреннего использования (например, при загрузке данных),
     * который не вызывает обновление атрибутов.
     * @param strength Новое значение силы.
     */
    public void setStrength(int strength) {
        setStrength(strength, null);
    }

    /**
     * "Умный" сеттер. Устанавливает новое значение Силы и немедленно обновляет
     * все связанные ванильные атрибуты игрока.
     *
     * @param strength Новое значение силы.
     * @param player   Объект игрока. Если он не null, будут обновлены его атрибуты.
     */
    public void setStrength(int strength, @Nullable Player player) {
        this.strength = Math.max(MIN_LEVEL, Math.min(MAX_LEVEL, strength));

        if (player != null) {
            AttributeUpdateService.updateKnockbackResistance(player);
        }
    }

    public void addStrength(int add, @Nullable Player player) {
        this.setStrength(this.strength + add, player);
    }

    //---Dexterity---
    public int getDexterity() {
        return dexterity;
    }

    public void setDexterity(int dexterity) {
        setDexterity(dexterity, null);
    }

    public void setDexterity(int dexterity, @Nullable Player player) {
        this.dexterity = Math.max(MIN_LEVEL, Math.min(MAX_LEVEL, dexterity));

        if (player != null) {
            AttributeUpdateService.updateAttackSpeed(player);
        }
    }

    public void addDexterity(int add, @Nullable Player player) {
        this.setDexterity(this.dexterity + add, player);
    }

    //---Intelligence---
    public int getIntelligence() {
        return intelligence;
    }

    public void setIntelligence(int intelligence) {
        this.intelligence = Math.max(MIN_LEVEL, Math.min(MAX_LEVEL, intelligence));
    }

    public void addIntelligence(int add) {
        setIntelligence(this.intelligence + add);
    }

    //--Health---
    public int getHealth() {
        return health;
    }

    public void setHealth(int health) {
        this.health = Math.max(MIN_LEVEL, Math.min(MAX_LEVEL, health));
    }

    public void addHealth(int add) {
        setHealth(this.health + add);
    }

    //--Insight--
    public int getInsight() {
        return insight;
    }

    public void setInsight(int insight) {
        this.insight = Math.max(MIN_LEVEL, Math.min(MAX_LEVEL, insight));
    }

    public void addInsight(int add) {
        setInsight(this.insight + add);
    }

    public void saveNBTData(CompoundTag nbt) {
        nbt.putInt("strength", strength);
        nbt.putInt("dexterity", dexterity);
        nbt.putInt("intelligence", intelligence);
        nbt.putInt("health", health);
        nbt.putInt("insight", insight);
    }

    public void loadNBTData(CompoundTag nbt) {
        strength = nbt.getInt("strength");
        dexterity = nbt.getInt("dexterity");
        intelligence = nbt.getInt("intelligence");
        health = nbt.getInt("health");
        insight = nbt.getInt("insight");
    }
}
