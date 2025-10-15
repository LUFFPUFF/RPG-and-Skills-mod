package com.nikita.rpgmod.item;

import com.nikita.rpgmod.RPGMod;
import com.nikita.rpgmod.item.custom.GrimoireHourglassItem;
import net.minecraft.world.item.Item;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModItems {

    /**
     * Магические предметы - предметы на мага
     */

    public static final DeferredRegister<Item> ITEMS =
            DeferredRegister.create(ForgeRegistries.ITEMS, RPGMod.MOD_ID);

    /**
     * ГРИМУАР ПЕСОЧНЫЕ ЧАСЫ
     */
    public static final RegistryObject<Item> GRIMOIRE_HOURGLASS = ITEMS.register("grimoire_hourglass",() ->
            new GrimoireHourglassItem(new Item.Properties().stacksTo(1)));

    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}
