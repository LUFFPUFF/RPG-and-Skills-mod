package com.nikita.rpgmod.client.gui;

import com.nikita.rpgmod.RPGMod;
import com.nikita.rpgmod.client.ClientData;
import com.nikita.rpgmod.network.PacketHandler;
import com.nikita.rpgmod.network.cs2packet.InvestStatC2SPacket;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.ImageButton;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

public class CharacterScreen extends Screen {

    private static final ResourceLocation BOOK_TEXTURE = ResourceLocation.fromNamespaceAndPath(RPGMod.MOD_ID, "textures/gui/ui/profile_book.png");
    private static final ResourceLocation HEALTH_BAR_TEXTURE  = ResourceLocation.fromNamespaceAndPath(RPGMod.MOD_ID, "textures/gui/ui/fill/ui_bar_fill_height.png");
    private static final ResourceLocation MANA_BAR_TEXTURE   = ResourceLocation.fromNamespaceAndPath(RPGMod.MOD_ID, "textures/gui/ui/fill/ui_bar_fill_mana.png");
    private static final ResourceLocation EXP_BAR_TEXTURE   = ResourceLocation.fromNamespaceAndPath(RPGMod.MOD_ID, "textures/gui/ui/fill/ui_bar_fill_xp.png");

    private static final ResourceLocation WARRIOR_ICON_TEXTURE = ResourceLocation.fromNamespaceAndPath(RPGMod.MOD_ID, "textures/gui/border_classes/tier1/border-warrior2.png");
    private static final ResourceLocation ARCHER_ICON_TEXTURE = ResourceLocation.fromNamespaceAndPath(RPGMod.MOD_ID, "textures/gui/border_classes/tier1/border-archer.png");
    private static final ResourceLocation MAGE_ICON_TEXTURE = ResourceLocation.fromNamespaceAndPath(RPGMod.MOD_ID, "textures/gui/border_classes/tier1/border-mage.png");
    private static final ResourceLocation PALADIN_ICON_TEXTURE = ResourceLocation.fromNamespaceAndPath(RPGMod.MOD_ID, "textures/gui/border_classes/tier1/border-paladin.png");
    private static final ResourceLocation ASSASIN_ICON_TEXTURE = ResourceLocation.fromNamespaceAndPath(RPGMod.MOD_ID, "textures/gui/border_classes/tier1/border-assasin.png");
    private static final ResourceLocation NECROMANSER_ICON_TEXTURE = ResourceLocation.fromNamespaceAndPath(RPGMod.MOD_ID, "textures/gui/border_classes/tier1/border-necromanser.png");

    private static final ResourceLocation PLUS_BUTTON_TEXTURE = ResourceLocation.fromNamespaceAndPath(RPGMod.MOD_ID, "textures/gui/ui/plus.png");

    private final int BOOK_WIDTH = 300;
    private final int BOOK_HEIGHT = 240;

    private int bookX;
    private int bookY;

    public CharacterScreen() {
        super(Component.literal("Profile"));
    }


    @Override
    protected void init() {
        super.init();
        this.bookX = (this.width - BOOK_WIDTH) / 2;
        this.bookY = (this.height - BOOK_HEIGHT) / 2;

        if (ClientData.attributePoints > 0) {
            int buttonX = this.bookX + 272 - 12;
            int buttonSize = 15;
            int yOffset = -5;

            addRenderableWidget(new ImageButton(buttonX, this.bookY + 65 + yOffset, buttonSize, buttonSize, 0, 0, buttonSize, PLUS_BUTTON_TEXTURE, 15, 15, button -> invest("strength")));
            addRenderableWidget(new ImageButton(buttonX, this.bookY + 90 + yOffset, buttonSize, buttonSize, 0, 0, buttonSize, PLUS_BUTTON_TEXTURE, 15, 15, button -> invest("dexterity")));
            addRenderableWidget(new ImageButton(buttonX, this.bookY + 113 + yOffset, buttonSize, buttonSize, 0, 0, buttonSize, PLUS_BUTTON_TEXTURE, 15, 15, button -> invest("intelligence")));
            addRenderableWidget(new ImageButton(buttonX, this.bookY + 135 + yOffset, buttonSize, buttonSize, 0, 0, buttonSize, PLUS_BUTTON_TEXTURE, 15, 15, button -> invest("vitality")));
            addRenderableWidget(new ImageButton(buttonX, this.bookY + 159 + yOffset, buttonSize, buttonSize, 0, 0, buttonSize, PLUS_BUTTON_TEXTURE, 15, 15, button -> invest("insight")));
        }
    }

    private void invest(String statName) {
        PacketHandler.sendToServer(new InvestStatC2SPacket(statName));
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        this.renderBackground(guiGraphics);

        guiGraphics.blit(BOOK_TEXTURE, bookX, bookY, 0, 0, BOOK_WIDTH, BOOK_HEIGHT, BOOK_WIDTH, BOOK_HEIGHT);

        drawProfilePage(guiGraphics);

        drawLevelingPage(guiGraphics);

        super.render(guiGraphics, mouseX, mouseY, partialTick);
    }

    private void drawProfilePage(GuiGraphics guiGraphics) {
//        ResourceLocation classIcon = getClassIcon();
//        if (classIcon != null) {
//            guiGraphics.blit(classIcon, bookX + 26, bookY + 57, 0, 0, 48, 48, 48, 48);
//        }

        int maxBarWidth = 41;
        int barHeight = 3;

        int healthBarX = bookX + 87;
        int healthBarY = bookY + 66;

        int manaBarY = healthBarY + 12;
        int expBarY = manaBarY + 12;

        renderBar(guiGraphics, healthBarX, healthBarY, maxBarWidth, barHeight, ClientData.visualHealth, ClientData.maxHealth, HEALTH_BAR_TEXTURE);
        renderBar(guiGraphics, healthBarX, manaBarY, maxBarWidth, barHeight, ClientData.visualMana, ClientData.maxMana, MANA_BAR_TEXTURE);
        renderBar(guiGraphics, healthBarX, expBarY - 1, maxBarWidth, barHeight, ClientData.playerExperience, ClientData.experienceNeeded, EXP_BAR_TEXTURE);

        int textColorWhite = 0xFFFFFF;
        float textScale = 0.8f;
        int textX = healthBarX;

        String healthText = String.format("%.0f/%.0f", ClientData.visualHealth, ClientData.maxHealth);
        drawScaledString(guiGraphics, healthText, textX + 10, healthBarY - 2, textScale, textColorWhite);

        String manaText = String.format("%.0f/%.0f", ClientData.visualMana, ClientData.maxMana);
        drawScaledString(guiGraphics, manaText, textX + 8, manaBarY - 2, textScale, textColorWhite);

        String expText = String.format("%d/%d", ClientData.playerExperience, ClientData.experienceNeeded);
        drawScaledString(guiGraphics, expText, textX + 5, expBarY - 3, textScale, textColorWhite);

        int textColorBlack = 0x404040;
        String levelStr = String.valueOf(ClientData.playerLevel);
        int levelX = bookX + 52;
        guiGraphics.drawString(this.font, levelStr, levelX - (this.font.width(levelStr) / 2), bookY + 117, textColorBlack, false);

        String jobStr = ClientData.playerClassName;
        int jobX = bookX + 112;
        guiGraphics.drawString(this.font, jobStr, jobX - (this.font.width(jobStr) / 2), bookY + 117, textColorBlack, false);
    }

    private void drawLevelingPage(GuiGraphics guiGraphics) {
        int textColorBlack = 0x404040;

        if (ClientData.attributePoints > 0) {
            String pointsText = "Очки: " + ClientData.attributePoints;
            int pointsX = bookX + 225;
            guiGraphics.drawString(this.font, pointsText, pointsX - (this.font.width(pointsText) / 2) - 10, bookY + 45, textColorBlack, false);
        }

        int textX = bookX + 190;
        guiGraphics.drawString(this.font, "Сила: " + ClientData.strength, textX, bookY + 65, textColorBlack, false);
        guiGraphics.drawString(this.font, "Ловкость: " + ClientData.dexterity, textX, bookY + 90, textColorBlack, false);
        guiGraphics.drawString(this.font, "Интеллект: " + ClientData.intelligence, textX, bookY + 113, textColorBlack, false);
        guiGraphics.drawString(this.font, "Живучесть: " + ClientData.vitality, textX, bookY + 135, textColorBlack, false);
        guiGraphics.drawString(this.font, "Проницат: " + ClientData.insight, textX, bookY + 159, textColorBlack, false);
    }

    private void renderBar(GuiGraphics guiGraphics, int x, int y, int maxBarWidth, int barHeight, float currentValue, float maxValue, ResourceLocation texture) {
        if (maxValue <= 0) return;
        int fillWidth = (int) ((currentValue / maxValue) * maxBarWidth);
        if (fillWidth > maxBarWidth) fillWidth = maxBarWidth;

        if (fillWidth > 0) {
            guiGraphics.blit(texture, x, y, 0, 0, fillWidth, barHeight, 40, 3);
        }
    }

    private void drawScaledString(GuiGraphics guiGraphics, String text, int x, int y, float scale, int color) {
        var poseStack = guiGraphics.pose();
        poseStack.pushPose();
        poseStack.translate(x, y, 0);
        poseStack.scale(scale, scale, 1.0F);

        guiGraphics.drawString(this.font, text, 0, 0, color, true); // true = с тенью

        poseStack.popPose();
    }

    private ResourceLocation getClassIcon() {
        return switch (ClientData.playerClassName) {
            case "Воин" -> WARRIOR_ICON_TEXTURE;
            case "Лучник" -> ARCHER_ICON_TEXTURE;
            case "Ассасин" -> ASSASIN_ICON_TEXTURE;
            case "Маг" -> MAGE_ICON_TEXTURE;
            case "Паладин" -> PALADIN_ICON_TEXTURE;
            case "Некромант" -> NECROMANSER_ICON_TEXTURE;
            default -> null;
        };
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }
}
