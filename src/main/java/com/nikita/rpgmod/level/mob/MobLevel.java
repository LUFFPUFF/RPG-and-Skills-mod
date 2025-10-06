package com.nikita.rpgmod.level.mob;

import net.minecraft.nbt.CompoundTag;

public class MobLevel {

    private int level = 1;

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public void saveNBTData(CompoundTag nbt) {
        nbt.putInt("mobLevel", this.level);
    }

    public void loadNBTData(CompoundTag nbt) {
        if (nbt.contains("mobLevel")) {
            this.level = nbt.getInt("mobLevel");
        }
    }
}
