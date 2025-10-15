package com.nikita.rpgmod.util;

import com.nikita.rpgmod.RPGMod;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.damagesource.DamageType;

public class ModDamageTypes {

    public static final ResourceKey<DamageType> SAND_BLAST = ResourceKey.create(
            Registries.DAMAGE_TYPE,
            ResourceLocation.fromNamespaceAndPath(RPGMod.MOD_ID, "sand_blast")
    );
}
