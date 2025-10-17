package com.nikita.rpgmod.item;

import com.nikita.rpgmod.RPGMod;
import com.nikita.rpgmod.item.custom.GrimoireHourglassItem;
import com.nikita.rpgmod.item.custom.SpellCooldownItem;
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

    public static final RegistryObject<Item> CD_ERODING_SHOT = ITEMS.register("cd_eroding_shot", SpellCooldownItem::new);
    public static final RegistryObject<Item> CD_SAND_SQUALL = ITEMS.register("cd_sand_squall", SpellCooldownItem::new);
    public static final RegistryObject<Item> CD_STASIS_FIELD = ITEMS.register("cd_stasis_field", SpellCooldownItem::new);
    public static final RegistryObject<Item> CD_QUICKSAND_TRAP = ITEMS.register("cd_quicksand_trap", SpellCooldownItem::new);
    public static final RegistryObject<Item> CD_DUNE_SHIFT = ITEMS.register("cd_dune_shift", SpellCooldownItem::new);
    public static final RegistryObject<Item> CD_SANDSTORM_AEGIS = ITEMS.register("cd_sandstorm_aegis", SpellCooldownItem::new);

    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}
