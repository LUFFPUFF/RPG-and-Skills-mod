package com.nikita.rpgmod.level.stats;

import com.nikita.rpgmod.event.ModEvents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;

public class PlayerLevel {

    private int level;
    private int experience;
    private int attributePoints;

    private static final int STARTING_LEVEL = 1;
    private static final int POINTS_PER_LEVEL = 5;

    public PlayerLevel() {
        this.level = STARTING_LEVEL;
        this.experience = 0;
        this.attributePoints = 0;
    }

    public int getLevel() { return level; }
    public int getExperience() { return experience; }
    public int getAttributePoints() { return attributePoints; }

    public int getExperienceNeededForNextLevel() {
        if (level >= 100) return Integer.MAX_VALUE;
        return (int) Math.floor(100 * Math.pow(this.level, 1.5));
    }

    /**
     * Добавляет опыт и проверяет, не пора ли повышать уровень.
     */
    public void addExperience(int amount, ServerPlayer player) {
        if (level >= 100 ) return;

        this.experience += amount;

        while (this.experience >= getExperienceNeededForNextLevel()) {
            int neededXp = getExperienceNeededForNextLevel();
            this.experience -= neededXp;
            this.level++;
            this.attributePoints += POINTS_PER_LEVEL;
            //TODO звуковое оповещение
        }

        ModEvents.syncAllData(player);

    }

    /**
     * Тратит одно очко характеристики.
     * @return true, если очко было потрачено, false, если очков не было.
     */
    public boolean spendAttributePoint() {
        if (this.attributePoints > 0) {
            this.attributePoints--;
            return true;
        }
        return false;
    }

    public void saveNBTData(CompoundTag nbt) {
        nbt.putInt("level", level);
        nbt.putInt("experience", experience);
        nbt.putInt("attributePoints", attributePoints);
    }

    public void loadNBTData(CompoundTag nbt) {
        level = nbt.contains("level") ? nbt.getInt("level") : STARTING_LEVEL;
        experience = nbt.getInt("experience");
        attributePoints = nbt.getInt("attributePoints");
    }
}
