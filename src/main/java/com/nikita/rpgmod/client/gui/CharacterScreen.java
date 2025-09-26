package com.nikita.rpgmod.client.gui;

import com.nikita.rpgmod.client.ClientData;
import com.nikita.rpgmod.network.PacketHandler;
import com.nikita.rpgmod.network.cs2packet.InvestStatC2SPacket;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

public class CharacterScreen extends Screen {

    private final int WINDOW_WIDTH = 200;
    private final int WINDOW_HEIGHT = 160;

    public CharacterScreen() {
        super(Component.literal("Character Stats"));
    }

    @Override
    protected void init() {
        int x = (this.width - WINDOW_WIDTH) / 2;
        int y = (this.height - WINDOW_HEIGHT) / 2;

        if (ClientData.attributePoints > 0) {
            addRenderableWidget(Button.builder(Component.literal("+"), button -> invest("strength")).bounds(x + 150, y + 58, 20, 20).build());
            addRenderableWidget(Button.builder(Component.literal("+"), button -> invest("dexterity")).bounds(x + 150, y + 78, 20, 20).build());
            addRenderableWidget(Button.builder(Component.literal("+"), button -> invest("intelligence")).bounds(x + 150, y + 98, 20, 20).build());
            addRenderableWidget(Button.builder(Component.literal("+"), button -> invest("vitality")).bounds(x + 150, y + 118, 20, 20).build());
            addRenderableWidget(Button.builder(Component.literal("+"), button -> invest("insight")).bounds(x + 150, y + 138, 20, 20).build());
        }
    }

    private void invest(String statName) {
        PacketHandler.sendToServer(new InvestStatC2SPacket(statName));
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        this.renderBackground(guiGraphics);

        int x = (this.width - WINDOW_WIDTH) / 2;
        int y = (this.height - WINDOW_HEIGHT) / 2;

        guiGraphics.fill(x, y, x + WINDOW_WIDTH, y + WINDOW_HEIGHT, 0xC0101010);

        super.render(guiGraphics, mouseX, mouseY, partialTick);

        int textColor = 0xFFFFFF;
        guiGraphics.drawString(this.font, "Персонаж", x + 10, y + 10, textColor);
        guiGraphics.drawString(this.font, "Уровень: " + ClientData.playerLevel, x + 10, y + 25, textColor);
        guiGraphics.drawString(this.font, "Очки: " + ClientData.attributePoints, x + 100, y + 25, textColor);

        guiGraphics.drawString(this.font, "Сила: " + ClientData.strength, x + 10, y + 63, textColor);
        guiGraphics.drawString(this.font, "Ловкость: " + ClientData.dexterity, x + 10, y + 83, textColor);
        guiGraphics.drawString(this.font, "Интеллект: " + ClientData.intelligence, x + 10, y + 103, textColor);
        guiGraphics.drawString(this.font, "Живучесть: " + ClientData.vitality, x + 10, y + 123, textColor);
        guiGraphics.drawString(this.font, "Проницательность: " + ClientData.insight, x + 10, y + 143, textColor);
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }
}
