package com.nikita.rpgmod.magic.stats;

import com.nikita.rpgmod.capibility.PlayerStats;
import net.minecraft.nbt.CompoundTag;

/**
 * Хранит и управляет всеми магическими параметрами игрока,
 * такими как мана и модификаторы, зависящие от интеллекта.
 */
public class PlayerMagicStats {

    private float currentMana;
    private static final float BASE_MANA = 100.0f;
    private static final float MANA_PER_INTELLIGENCE = 5.0f;
    private static final double CDR_PER_INTELLIGENCE = 0.007;

    private final PlayerStats stats;

    public PlayerMagicStats(PlayerStats stats) {
        this.stats = stats;
        this.currentMana = getMaxMana();
    }

    public float getCurrentMana() {
        return currentMana;
    }

    public float getMaxMana() {
        return BASE_MANA + stats.getIntelligence() * MANA_PER_INTELLIGENCE;
    }

    public void setMana(float amount) {
        this.currentMana = Math.max(0, Math.min(amount, getMaxMana()));
    }

    public void addMana(float amount) {
        setMana(this.currentMana + amount);
    }

    public boolean consumeMana(float amount) {
        if (this.currentMana >= amount) {
            this.currentMana -= amount;
            return true;
        }
        return false;
    }

    /**
     * При изменении интеллекта нужно убедиться, что текущая мана не превышает новый максимум.
     * Этот метод должен вызываться из setIntelligence.
     */
    public void onIntelligenceChanged() {
        this.currentMana = Math.min(this.currentMana, this.getMaxMana());
    }

    public double getCooldownModifier() {
        return (1.0 / (1.0 + stats.getIntelligence() * CDR_PER_INTELLIGENCE));
    }

    public void saveNBTData(CompoundTag nbt) {
        nbt.putFloat("currentMana", currentMana);
    }

    public void loadNBTData(CompoundTag nbt) {
        currentMana = nbt.contains("currentMana") ? nbt.getFloat("currentMana") : getMaxMana();
    }

}
