package com.nikita.rpgmod.client.hud;

import com.nikita.rpgmod.RPGMod;
import com.nikita.rpgmod.client.ClientData;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.client.gui.overlay.ForgeGui;
import net.minecraftforge.client.gui.overlay.IGuiOverlay;

public class HudOverlay implements IGuiOverlay {

    private static final ResourceLocation WARRIOR_ICON_TEXTURE = ResourceLocation.fromNamespaceAndPath(RPGMod.MOD_ID, "textures/gui/border_classes/tier1/border-warrior2.png");
    private static final ResourceLocation ARCHER_ICON_TEXTURE = ResourceLocation.fromNamespaceAndPath(RPGMod.MOD_ID, "textures/gui/border_classes/tier1/border-archer.png");
    private static final ResourceLocation MAGE_ICON_TEXTURE = ResourceLocation.fromNamespaceAndPath(RPGMod.MOD_ID, "textures/gui/border_classes/tier1/border-mage.png");
    private static final ResourceLocation PALADIN_ICON_TEXTURE = ResourceLocation.fromNamespaceAndPath(RPGMod.MOD_ID, "textures/gui/border_classes/tier1/border-paladin.png");
    private static final ResourceLocation ASSASIN_ICON_TEXTURE = ResourceLocation.fromNamespaceAndPath(RPGMod.MOD_ID, "textures/gui/border_classes/tier1/border-assasin.png");
    private static final ResourceLocation NECROMANSER_ICON_TEXTURE = ResourceLocation.fromNamespaceAndPath(RPGMod.MOD_ID, "textures/gui/border_classes/tier1/border-necromanser.png");

    private static final ResourceLocation HEALTH_BAR_TEXTURE = ResourceLocation.fromNamespaceAndPath(RPGMod.MOD_ID, "textures/gui/hud/health_bar3.png");
    private static final ResourceLocation MANA_BAR_TEXTURE = ResourceLocation.fromNamespaceAndPath(RPGMod.MOD_ID, "textures/gui/hud/mana_bar2.png");


    @Override
    public void render(ForgeGui gui, GuiGraphics guiGraphics, float partialTick, int screenWidth, int screenHeight) {

        drawClassIcon(guiGraphics);

        int iconSize = 50;
        int barWidth = 120;
        int barHeight = 20;

        int barsX = 10 + iconSize + 6;
        int barsY = 10 + (iconSize - (barHeight * 2 + 4)) / 2;
        int spacing = 4;

//        guiGraphics.fill(barsX, barsY, barsX + barWidth, barsY + barHeight * 2 + spacing, 0x80000000);
        renderClippedBar(guiGraphics, barsX, barsY, barWidth, barHeight, ClientData.currentHealth, ClientData.maxHealth, HEALTH_BAR_TEXTURE);
        renderText(guiGraphics, barsX, barsY, barWidth, barHeight, String.format("%.0f/%.0f", ClientData.currentHealth, ClientData.maxHealth));

        barsY += barHeight + spacing;

        renderClippedBar(guiGraphics, barsX, barsY, barWidth, barHeight, ClientData.currentMana, ClientData.maxMana, MANA_BAR_TEXTURE);
        renderText(guiGraphics, barsX, barsY, barWidth, barHeight, String.format("%.0f/%.0f", ClientData.currentMana, ClientData.maxMana));

    }

    private void drawClassIcon(GuiGraphics gui) {
        ResourceLocation iconToRender = switch (ClientData.playerClassName) {
            case "Воин" -> WARRIOR_ICON_TEXTURE;
            case "Лучник" -> ARCHER_ICON_TEXTURE;
            case "Ассасин" -> ASSASIN_ICON_TEXTURE;
            case "Маг" -> MAGE_ICON_TEXTURE;
            case "Паладин" -> PALADIN_ICON_TEXTURE;
            case "Некромант" -> NECROMANSER_ICON_TEXTURE;
            default -> null;
        };

        if (iconToRender != null) {
            int x = 10;
            int y = 10;
            int iconSize = 50;

            gui.blit(iconToRender, x, y, 0, 0, iconSize, iconSize, iconSize, iconSize);
        }
    }

    private void renderClippedBar(GuiGraphics gui, int x, int y, int width, int height, float currentValue, float maxValue, ResourceLocation texture) {
        int drawWidth = (int) ((currentValue / maxValue) * width);

        if (drawWidth > 0) {
            gui.blit(texture, x, y, 0, 0, drawWidth, height, width, height);
        }
    }

    private void renderText(GuiGraphics gui, int x, int y, int barWidth, int barHeight, String text) {
        Font font = Minecraft.getInstance().font;
        int textWidth = font.width(text);
        gui.drawString(font, text, x + (barWidth - textWidth) / 2, y + (barHeight - 8) / 2, 0xFFFFFF, true);
    }
}
