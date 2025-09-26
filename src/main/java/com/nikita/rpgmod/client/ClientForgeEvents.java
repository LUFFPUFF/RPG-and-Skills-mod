package com.nikita.rpgmod.client;

import com.nikita.rpgmod.RPGMod;
import com.nikita.rpgmod.client.gui.CharacterScreen;
import net.minecraft.client.Minecraft;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = RPGMod.MOD_ID, value = Dist.CLIENT)
public class ClientForgeEvents {

    @SubscribeEvent
    public static void onKeyInput(InputEvent.Key event) {
        if (Keybindings.OPEN_CHARACTER_SCREEN.consumeClick()) {
            Minecraft.getInstance().setScreen(new CharacterScreen());
        }
    }
}
