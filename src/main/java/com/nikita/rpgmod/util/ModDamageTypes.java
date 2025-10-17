//package com.nikita.rpgmod.util;
//
//import com.nikita.rpgmod.RPGMod;
//import net.minecraft.core.registries.Registries;
//import net.minecraft.world.damagesource.DamageType;
//import net.minecraftforge.eventbus.api.IEventBus;
//import net.minecraftforge.registries.DeferredRegister;
//import net.minecraftforge.registries.RegistryObject;
//
//
//public class ModDamageTypes {
//    public static final DeferredRegister<DamageType> DAMAGE_TYPES =
//            DeferredRegister.create(Registries.DAMAGE_TYPE, RPGMod.MOD_ID);
//
//    public static final RegistryObject<DamageType> SAND_BLAST =
//            DAMAGE_TYPES.register("sand_blast", () -> new DamageType("sand_blast", 0.1f));
//
//    public static void register(IEventBus eventBus) {
//        DAMAGE_TYPES.register(eventBus);
//    }
//}
