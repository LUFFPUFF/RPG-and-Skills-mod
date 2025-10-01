package com.nikita.rpgmod.classes;

import net.minecraft.nbt.CompoundTag;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class PlayerClassData {

    private PlayerClass primaryClass = PlayerClass.NONE;
    private PlayerClass secondaryClass = PlayerClass.NONE;

    public PlayerClass getPrimaryClass() {
        return primaryClass;
    }

    public void setPrimaryClass(PlayerClass primaryClass) {
        this.primaryClass = primaryClass;
    }

    public PlayerClass getSecondaryClass() {
        return secondaryClass;
    }

    public void setSecondaryClass(PlayerClass secondaryClass) {
        this.secondaryClass = secondaryClass;
    }

    public String getDisplayName() {
        if (primaryClass == PlayerClass.NONE) {
            return "Без класса";
        }
        if (secondaryClass == PlayerClass.NONE || primaryClass == secondaryClass) {
            return primaryClass.getDisplayName();
        }

        Set<PlayerClass> classes = new HashSet<>(Arrays.asList(primaryClass, secondaryClass));

        //Комбинации с воином
        if (classes.contains(PlayerClass.WARRIOR)) {
            if (classes.contains(PlayerClass.MAGE)) return "Клинок Чар";
            if (classes.contains(PlayerClass.ARCHER)) return "Военачальник";
            if (classes.contains(PlayerClass.ASSASSIN)) return "Рубака";
            if (classes.contains(PlayerClass.PALADIN)) return "Страж";
            if (classes.contains(PlayerClass.NECROMANCER)) return "Рыцарь Смерти";
        }

        // Комбинации с Магом
        if (classes.contains(PlayerClass.MAGE)) {
            if (classes.contains(PlayerClass.ARCHER)) return "Чародейский Лучник";
            if (classes.contains(PlayerClass.ASSASSIN)) return "Ночной Клинок";
            if (classes.contains(PlayerClass.PALADIN)) return "Клерик";
            if (classes.contains(PlayerClass.NECROMANCER)) return "Лич";
        }
        // Комбинации с Лучником
        if (classes.contains(PlayerClass.ARCHER)) {
            if (classes.contains(PlayerClass.ASSASSIN)) return "Охотник";
            if (classes.contains(PlayerClass.PALADIN)) return "Инквизитор";
            if (classes.contains(PlayerClass.NECROMANCER)) return "Чумной Странник";
        }
        // Комбинации с Ассасином
        if (classes.contains(PlayerClass.ASSASSIN)) {
            if (classes.contains(PlayerClass.PALADIN)) return "Мститель";
            if (classes.contains(PlayerClass.NECROMANCER)) return "Жнец";
        }
        // Уникальная комбинация
        if (classes.contains(PlayerClass.PALADIN) && classes.contains(PlayerClass.NECROMANCER)) {
            return "Хранитель Равновесия";
        }

        return primaryClass.getDisplayName() + "/" + secondaryClass.getDisplayName();
    }

    public void saveNBTData(CompoundTag nbt) {
        nbt.putString("primaryClass", primaryClass.name());
        nbt.putString("secondaryClass", secondaryClass.name());
    }

    public void loadNBTData(CompoundTag nbt) {
        try {
            primaryClass = PlayerClass.valueOf(nbt.getString("primaryClass"));
        } catch (IllegalArgumentException e) {
            primaryClass = PlayerClass.NONE;
        }
        try {
            secondaryClass = PlayerClass.valueOf(nbt.getString("secondaryClass"));
        } catch (IllegalArgumentException e) {
            secondaryClass = PlayerClass.NONE;
        }
    }
}
