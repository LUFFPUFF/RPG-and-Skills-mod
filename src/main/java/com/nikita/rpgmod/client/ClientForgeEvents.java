package com.nikita.rpgmod.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.nikita.rpgmod.RPGMod;
import com.nikita.rpgmod.capibility.PlayerAnimationProvider;
import com.nikita.rpgmod.client.gui.CharacterScreen;
import com.nikita.rpgmod.client.renderer.AnimatedPlayerRenderer;
import com.nikita.rpgmod.client.renderer.PlayerAnimatable;
import com.nikita.rpgmod.level.mob.MobLevelProvider;
import com.nikita.rpgmod.network.PacketHandler;
import com.nikita.rpgmod.network.cs2packet.CastSpellC2SPacket;
import com.nikita.rpgmod.network.cs2packet.ChangeSpellC2SPacket;
import com.nikita.rpgmod.network.cs2packet.UseInsightSkillC2SPacket;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.player.PlayerRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.client.event.RenderLivingEvent;
import net.minecraftforge.client.event.RenderPlayerEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.jetbrains.annotations.NotNull;
import org.joml.Matrix4f;

import java.util.Map;
import java.util.WeakHashMap;

@Mod.EventBusSubscriber(modid = RPGMod.MOD_ID, value = Dist.CLIENT)
public class ClientForgeEvents {

    private static final AnimatedPlayerRenderer RENDERER = new AnimatedPlayerRenderer();
    private static final Map<Player, PlayerAnimatable> ANIMATABLES = new WeakHashMap<>();

    @SubscribeEvent
    public static void onKeyInput(InputEvent.Key event) {
        if (Keybindings.OPEN_CHARACTER_SCREEN.consumeClick()) {
            Minecraft.getInstance().setScreen(new CharacterScreen());
        }
        if (Keybindings.USE_INSIGHT_SKILL.consumeClick()) {
            PacketHandler.sendToServer(new UseInsightSkillC2SPacket());
        }
        if (Keybindings.CAST_SPELL.consumeClick()) {
            PacketHandler.sendToServer(new CastSpellC2SPacket());
        }
        if (Keybindings.NEXT_SPELL.consumeClick()) {
            PacketHandler.sendToServer(new ChangeSpellC2SPacket(true));
        }
        if (Keybindings.PREVIOUS_SPELL.consumeClick()) {
            PacketHandler.sendToServer(new ChangeSpellC2SPacket(false));
        }
    }

    @SubscribeEvent
    public static void onClientTick(TickEvent.ClientTickEvent event) {
        if (event.phase == TickEvent.Phase.END) {
            float animationSpeed = 0.1f;

            if (ClientData.visualHealth != ClientData.currentHealth) {
                ClientData.visualHealth += (ClientData.currentHealth - ClientData.visualHealth) * animationSpeed;
                if (Math.abs(ClientData.currentHealth - ClientData.visualHealth) < 0.1f) {
                    ClientData.visualHealth = ClientData.currentHealth;
                }
            }

            if (ClientData.visualMana != ClientData.currentMana) {
                ClientData.visualMana += (ClientData.currentMana - ClientData.visualMana) * animationSpeed;
                if (Math.abs(ClientData.currentMana - ClientData.visualMana) < 0.1f) {
                    ClientData.visualMana = ClientData.currentMana;
                }
            }
        }
    }

    @SubscribeEvent
    public static void onRenderMob(RenderLivingEvent.Post<LivingEntity, ?> event) {
        LivingEntity entity = event.getEntity();
        if (!(entity instanceof Enemy)) return;

        entity.getCapability(MobLevelProvider.MOB_LEVEL).ifPresent(mobLevelCap -> {
            int mobLevel = mobLevelCap.getLevel();
            if (mobLevel <= 0) return;

            int playerLevel = ClientData.playerLevel;
            int levelDiff = mobLevel - playerLevel;

            int color;
            if (levelDiff > 5) {
                color = 0xFF5555;
            } else if (levelDiff < -5) {
                color = 0x55FF55;
            } else {
                color = 0xFFFF55;
            }

            String text = "Lvl " + mobLevel;

            PoseStack poseStack = event.getPoseStack();
            poseStack.pushPose();

            float entityHeight = entity.getDimensions(entity.getPose()).height;
            poseStack.translate(0, entityHeight + 0.5f, 0);

            poseStack.mulPose(Minecraft.getInstance().getEntityRenderDispatcher().cameraOrientation());
            poseStack.scale(-0.025f, -0.025f, 0.025f);

            Font font = Minecraft.getInstance().font;
            float textWidth = -font.width(text) / 2.0f;

            MultiBufferSource buffer = event.getMultiBufferSource();
            Matrix4f matrix4f = poseStack.last().pose();
            font.drawInBatch(text, textWidth, 0, color, false, matrix4f, buffer, Font.DisplayMode.NORMAL, 0, 15728880);

            poseStack.popPose();
        });
    }

    @SubscribeEvent
    public static void onRenderPlayer(RenderPlayerEvent.Pre event) {
        Player player = event.getEntity();
        player.getCapability(PlayerAnimationProvider.PLAYER_ANIMATION).ifPresent(animData -> {
            if (animData.isAnimating()) {
                event.setCanceled(true);

                PlayerAnimatable animatable = ANIMATABLES.computeIfAbsent(player, PlayerAnimatable::new);
                PoseStack poseStack = event.getPoseStack();
                MultiBufferSource bufferSource = event.getMultiBufferSource();
                int packedLight = event.getPackedLight();

                poseStack.pushPose();

                event.getRenderer().getModel().setAllVisible(false);

                RENDERER.render(poseStack, animatable, bufferSource, null, null, packedLight);

                event.getRenderer().getModel().setAllVisible(true);

                poseStack.popPose();
            }
        });
    }
}
