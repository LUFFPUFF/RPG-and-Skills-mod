package com.nikita.rpgmod.util;

import com.nikita.rpgmod.RPGMod;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.item.Item;

/**
 * Хранение кастомных тегов для источников урона.
 * Эти теги позволяют удобно проверять тип атаки (например, ближний бой или дальний бой).
 */
public class ModTags {

    public static final class DamageTypes {

        /**
         * Тег для всех типов урона, связанных с ближним боем (мечи, топоры и т.п.).
         */
        public static final TagKey<DamageType> IS_MELEE = create("is_melee");

        /**
         * Тег для всех типов урона, связанных с дальними атаками (стрелы, арбалетные болты и т.п.).
         */
        public static final TagKey<DamageType> IS_PROJECTILE = create("is_projectile");

        public static final TagKey<DamageType> IS_MAGIC = create("is_magic");

        private static TagKey<DamageType> create(String name) {
            return TagKey.create(
                    Registries.DAMAGE_TYPE,
                    ResourceLocation.fromNamespaceAndPath(RPGMod.MOD_ID, name)
            );
        }
    }

    public static final class Items {

        /**
         * Тег для предметов, которые считаются магическими (жезлы, посохи, артефакты).
         * Используется для проверки, нужно ли применять бонус интеллекта.
         */
        public static final TagKey<Item> MAGIC_ITEMS = TagKey.create(
                Registries.ITEM,
                ResourceLocation.fromNamespaceAndPath(RPGMod.MOD_ID, "magic_items")
        );

    }

    public static class VanillaDamageTypeTags {

        /**
         * Тег для урона, который игнорирует броню (урон от падения, голода, пустоты, /kill и т.д.).
         */
        public static final TagKey<DamageType> BYPASSES_ARMOR = createVanilla("bypasses_armor");

        private static TagKey<DamageType> createVanilla(String name) {
            return TagKey.create(Registries.DAMAGE_TYPE, ResourceLocation.fromNamespaceAndPath("minecraft", name));
        }

    }

}
