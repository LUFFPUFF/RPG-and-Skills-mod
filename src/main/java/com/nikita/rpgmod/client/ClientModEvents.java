package com.nikita.rpgmod.client;

import com.nikita.rpgmod.RPGMod;
import com.nikita.rpgmod.client.hud.HudOverlay;
import com.nikita.rpgmod.entity.ModEntities;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.client.event.RegisterGuiOverlaysEvent;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraft.client.renderer.entity.ThrownItemRenderer;

@Mod.EventBusSubscriber(modid = RPGMod.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ClientModEvents {

    @SubscribeEvent
    public static void onKeyRegister(RegisterKeyMappingsEvent event) {
        event.register(Keybindings.OPEN_CHARACTER_SCREEN);
        event.register(Keybindings.USE_INSIGHT_SKILL);
        event.register(Keybindings.CAST_SPELL);
        event.register(Keybindings.NEXT_SPELL);
        event.register(Keybindings.PREVIOUS_SPELL);
    }

    @SubscribeEvent
    public static void registerGuiOverlays(RegisterGuiOverlaysEvent event) {
        event.registerAboveAll("rpg_hud", new HudOverlay());
    }

    @SubscribeEvent
    public static void registerEntityRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(ModEntities.SAND_BLAST_PROJECTILE.get(), ThrownItemRenderer::new);
    }
}
