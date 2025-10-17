package com.nikita.rpgmod.client;

import net.minecraft.resources.ResourceLocation;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

/**
 * Статический класс-хранилище для всех данных, полученных от сервера.
 * GUI и HUD обращаются к этому классу, чтобы получить актуальную информацию для отрисовки.
 */
public class ClientData {

    // Уровень и опыт
    public static int playerLevel = 1;
    public static int playerExperience = 0;
    public static int experienceNeeded = 100;
    public static int attributePoints = 0;

    // Здоровье
    public static float currentHealth = 20;
    public static float visualHealth;
    public static float maxHealth = 20;

    // Мана
    public static float currentMana = 100;
    public static float visualMana;
    public static float maxMana = 100;

    // Голод
    public static int currentHunger = 20;
    public static final int MAX_HUNGER = 20;

    // Характеристики
    public static int strength = 1;
    public static int dexterity = 1;
    public static int intelligence = 1;
    public static int vitality = 1;
    public static int insight = 1;

    // Класс игрока
    public static String playerClassName = "Без класса";

    //спелы
    @Nullable
    public static ResourceLocation currentSpellId = null;
    public static List<ResourceLocation> knownSpells = new ArrayList<>();
    public static int currentSpellIndex = -1;
}
