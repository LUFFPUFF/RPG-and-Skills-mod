package com.nikita.rpgmod.item.custom;

import net.minecraft.world.item.Item;

/**
 * Простой предмет-заглушка, используемый исключительно для отслеживания
 * перезарядок заклинаний в ванильной системе ItemCooldowns.
 * Эти предметы не должны быть доступны в игре.
 */
public class SpellCooldownItem extends Item {
    public SpellCooldownItem() {
        super(new Item.Properties());
    }
}
