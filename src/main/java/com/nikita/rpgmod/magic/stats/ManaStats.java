package com.nikita.rpgmod.magic.stats;

public class ManaStats {

    private int maxMana = 100;
    private int currentMana = 100;

    public int getMaxMana() {
        return maxMana;
    }

    public void setMaxMana(int value) {
        this.maxMana = value;
        if (currentMana > maxMana) {
            currentMana = maxMana;
        }
    }

    public int getCurrentMana() {
        return currentMana;
    }

    public void setCurrentMana(int value) {
        this.currentMana = Math.max(0, Math.min(maxMana, value));
    }

    public void addMana(int value) {
        setCurrentMana(this.currentMana + value);
    }

    public void consumeMana(int value) {
        setCurrentMana(this.currentMana - value);
    }
}
