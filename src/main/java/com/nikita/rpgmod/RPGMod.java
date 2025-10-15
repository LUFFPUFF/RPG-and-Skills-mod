package com.nikita.rpgmod;

import com.mojang.logging.LogUtils;
import com.nikita.rpgmod.client.ClientForgeEvents;
import com.nikita.rpgmod.client.ClientModEvents;
import com.nikita.rpgmod.entity.ModEntities;
import com.nikita.rpgmod.event.ModEvents;
import com.nikita.rpgmod.item.ModItems;
import com.nikita.rpgmod.magic.spell.ModSpells;
import com.nikita.rpgmod.network.PacketHandler;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;

@Mod(RPGMod.MOD_ID)
public class RPGMod {

    public static final String MOD_ID = "rpgmod";
    public static final Logger LOGGER = LogUtils.getLogger();

    public RPGMod(FMLJavaModLoadingContext context) {

        IEventBus modEventBus = context.getModEventBus();

        modEventBus.register(ModEvents.ModEventBusEvents.class);
        modEventBus.register(ClientModEvents.class);

        MinecraftForge.EVENT_BUS.register(ModEvents.class);
        MinecraftForge.EVENT_BUS.register(ClientForgeEvents.class);

        ModItems.register(modEventBus);
        ModEntities.register(modEventBus);
        ModSpells.register();

        PacketHandler.register();

        LOGGER.info("[RPGMod] Initialized!");

    }
}
