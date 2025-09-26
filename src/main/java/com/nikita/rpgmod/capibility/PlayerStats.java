package com.nikita.rpgmod.capibility;

import com.nikita.rpgmod.attributes.AttributeUpdateService;
import com.nikita.rpgmod.magic.stats.PlayerMagicProvider;
import com.nikita.rpgmod.magic.stats.PlayerMagicStats;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Player;

import javax.annotation.Nullable;

public class PlayerStats {

    private int strength; //сила
    private int dexterity; //ловкость
    private int intelligence; //интелект
    private int vitality; //здоровье
    private int insight; //проницательность

    private boolean dangerSenseEnabled = true;

    private static final int MIN_LEVEL = 1;
    private static final int MAX_LEVEL = 100;

    public  PlayerStats() {
        this.strength = MIN_LEVEL;
        this.dexterity = MIN_LEVEL;
        this.intelligence = MIN_LEVEL;
        this.vitality = MIN_LEVEL;
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

    public void  setIntelligence(int intelligence) {
        setIntelligence(intelligence, null);
    }

    public void setIntelligence(int intelligence, @Nullable Player player) {
        this.intelligence = Math.max(MIN_LEVEL, Math.min(MAX_LEVEL, intelligence));

        if (player != null) {
            player.getCapability(PlayerMagicProvider.PLAYER_MAGIC).ifPresent(PlayerMagicStats::onIntelligenceChanged);
            //TODO Обновление GUI
        }
    }

    public void addIntelligence(int add, @Nullable Player player) {
        setIntelligence((this.intelligence + add),  player);
    }

    //--Vitality---
    public int getVitality() {
        return vitality;
    }

    public void setVitality(int vitality) {
        setVitality(vitality, null);
    }

    public void setVitality(int constitution, @Nullable Player player) { // <-- ИЗМЕНЕНО
        this.vitality = Math.max(MIN_LEVEL, Math.min(MAX_LEVEL, constitution));

        if (player != null) {
            AttributeUpdateService.updateMaxHealth(player);
        }
    }

    public void addVitality(int add, @Nullable Player player) {
        setVitality((this.vitality + add), player);
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

    public boolean isDangerSenseEnabled() {
        return dangerSenseEnabled;
    }

    public boolean toggleDangerSense() {
        this.dangerSenseEnabled = !this.dangerSenseEnabled;
        return this.dangerSenseEnabled;
    }


    public void saveNBTData(CompoundTag nbt) {
        nbt.putInt("strength", strength);
        nbt.putInt("dexterity", dexterity);
        nbt.putInt("intelligence", intelligence);
        nbt.putInt("vitality", vitality);
        nbt.putInt("insight", insight);
        nbt.putBoolean("dangerSenseEnabled", dangerSenseEnabled);
    }

    public void loadNBTData(CompoundTag nbt) {
        strength = nbt.getInt("strength");
        dexterity = nbt.getInt("dexterity");
        intelligence = nbt.getInt("intelligence");
        if (nbt.contains("vitality")) {
            vitality = nbt.getInt("constitution");
        } else if (nbt.contains("health")) {
            vitality = nbt.getInt("health");
        }
        insight = nbt.getInt("insight");
        if (nbt.contains("dangerSenseEnabled")) {
            dangerSenseEnabled = nbt.getBoolean("dangerSenseEnabled");
        }
    }
}
