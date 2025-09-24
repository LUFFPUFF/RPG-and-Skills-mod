package com.nikita.rpgmod;

import com.mojang.logging.LogUtils;
import com.nikita.rpgmod.event.ModEvents;
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

        MinecraftForge.EVENT_BUS.register(ModEvents.class);

        LOGGER.info("[RPGMod] Initialized!");

    }
}
