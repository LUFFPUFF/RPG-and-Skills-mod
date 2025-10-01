package com.nikita.rpgmod.classes;

public enum PlayerClass {
    NONE("Без класса"),
    WARRIOR("Воин"),
    ARCHER("Лучник"),
    ASSASSIN("Ассасин"),
    MAGE("Маг"),
    PALADIN("Паладин"),
    NECROMANCER("Некромант");

    private final String displayName;

    PlayerClass(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
