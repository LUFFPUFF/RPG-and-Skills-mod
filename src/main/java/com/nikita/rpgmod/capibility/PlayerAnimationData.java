package com.nikita.rpgmod.capibility;

import net.minecraft.nbt.CompoundTag;

public class PlayerAnimationData {
    private String currentAnimation = "";
    private int animationTicksLeft = 0;

    public String getCurrentAnimation() {
        return currentAnimation;
    }

    public void setAnimation(String animation, int durationTicks) {
        this.currentAnimation = animation;
        this.animationTicksLeft = durationTicks;
    }

    public boolean isAnimating() {
        return this.animationTicksLeft > 0;
    }

    public void tick() {
        if (this.animationTicksLeft > 0) {
            this.animationTicksLeft--;
        }
        if (this.animationTicksLeft == 0) {
            this.currentAnimation = "";
        }
    }

    public void saveNBT(CompoundTag nbt) {
        nbt.putString("currentAnimation", currentAnimation);
        nbt.putInt("animationTicksLeft", animationTicksLeft);
    }

    public void loadNBT(CompoundTag nbt) {
        this.currentAnimation = nbt.getString("currentAnimation");
        this.animationTicksLeft = nbt.getInt("animationTicksLeft");
    }
}
