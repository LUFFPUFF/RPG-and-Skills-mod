package com.nikita.rpgmod.client.hud;

import com.nikita.rpgmod.client.ClientData;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraftforge.client.gui.overlay.ForgeGui;
import net.minecraftforge.client.gui.overlay.IGuiOverlay;

public class HudOverlay implements IGuiOverlay {


    @Override
    public void render(ForgeGui gui, GuiGraphics guiGraphics, float partialTick, int screenWidth, int screenHeight) {
        Font font = Minecraft.getInstance().font;

        String healthText = String.format("%sHP: %.0f / %.0f", ChatFormatting.RED, ClientData.currentHealth, ClientData.maxHealth);
        String manaText = String.format("%sMana: %.0f / %.0f", ChatFormatting.AQUA, ClientData.currentMana, ClientData.maxMana);
        String xpText = String.format("%sXP: %d / %d", ChatFormatting.GREEN, ClientData.playerExperience, ClientData.experienceNeeded);

        int x = 10;
        int y_start = 10;
        int spacing = 12;

        guiGraphics.drawString(font, healthText, x, y_start, 0xFFFFFFFF);
        guiGraphics.drawString(font, manaText, x, y_start + spacing, 0xFFFFFFFF);
        guiGraphics.drawString(font, xpText, x, y_start + (spacing * 2), 0xFFFFFFFF);
    }
}
