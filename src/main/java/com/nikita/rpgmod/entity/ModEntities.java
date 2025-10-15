package com.nikita.rpgmod.entity;

import com.nikita.rpgmod.RPGMod;
import com.nikita.rpgmod.entity.custom.spell.SandBlastProjectile;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

/**
 * Класс, отвечающий за регистрацию всех кастомных сущностей.
 */
public class ModEntities {

    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES =
            DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, RPGMod.MOD_ID);

    public static final RegistryObject<EntityType<SandBlastProjectile>> SAND_BLAST_PROJECTILE =
            ENTITY_TYPES.register("sand_blast_projectile",
                    () -> EntityType.Builder.<SandBlastProjectile>of(SandBlastProjectile::new, MobCategory.MISC)
                            .sized(0.25F, 0.25F)
                            .clientTrackingRange(4)
                            .updateInterval(10)
                            .build("sand_blast_projectile"));

    public static void register(IEventBus eventBus) {
        ENTITY_TYPES.register(eventBus);
    }
}
