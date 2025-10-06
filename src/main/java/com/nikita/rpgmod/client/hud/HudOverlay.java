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

import java.util.ArrayList;
import java.util.List;

public class HudOverlay implements IGuiOverlay {

    private static final ResourceLocation WARRIOR_ICON_TEXTURE = ResourceLocation.fromNamespaceAndPath(RPGMod.MOD_ID, "textures/gui/border_classes/tier1/border-warrior2.png");
    private static final ResourceLocation ARCHER_ICON_TEXTURE = ResourceLocation.fromNamespaceAndPath(RPGMod.MOD_ID, "textures/gui/border_classes/tier1/border-archer.png");
    private static final ResourceLocation MAGE_ICON_TEXTURE = ResourceLocation.fromNamespaceAndPath(RPGMod.MOD_ID, "textures/gui/border_classes/tier1/border-mage.png");
    private static final ResourceLocation PALADIN_ICON_TEXTURE = ResourceLocation.fromNamespaceAndPath(RPGMod.MOD_ID, "textures/gui/border_classes/tier1/border-paladin.png");
    private static final ResourceLocation ASSASIN_ICON_TEXTURE = ResourceLocation.fromNamespaceAndPath(RPGMod.MOD_ID, "textures/gui/border_classes/tier1/border-assasin.png");
    private static final ResourceLocation NECROMANSER_ICON_TEXTURE = ResourceLocation.fromNamespaceAndPath(RPGMod.MOD_ID, "textures/gui/border_classes/tier1/border-necromanser.png");

    private static final ResourceLocation HEALTH_BAR_BG = ResourceLocation.fromNamespaceAndPath(RPGMod.MOD_ID, "textures/gui/hud/health_bar_bg.png");
    private static final ResourceLocation HEALTH_BAR_FILL = ResourceLocation.fromNamespaceAndPath(RPGMod.MOD_ID, "textures/gui/hud/health_bar_fill.png");
    private static final ResourceLocation MANA_BAR_BG = ResourceLocation.fromNamespaceAndPath(RPGMod.MOD_ID, "textures/gui/hud/mana_bar_bg.png");
    private static final ResourceLocation MANA_BAR_FILL = ResourceLocation.fromNamespaceAndPath(RPGMod.MOD_ID, "textures/gui/hud/mana_bar_fill.png");

    private static final ResourceLocation EFFS_BORDER_TEXTURE = ResourceLocation.fromNamespaceAndPath(RPGMod.MOD_ID, "textures/gui/border_classes/effs/effs_border.png");

    private static final int LEVELUP_TOTAL_FRAMES = 17;
    private static final int LEVELUP_TICKS_PER_FRAME = 10;
    private static final int INITIAL_HOLD_DURATION = 200;

    private static final List<ResourceLocation> LEVELUP_FRAMES = new ArrayList<>();
    private static final List<ResourceLocation> DIGIT_TEXTURES = new ArrayList<>();
    private static final ResourceLocation ARROW_TEXTURE = ResourceLocation.fromNamespaceAndPath(RPGMod.MOD_ID, "textures/gui/level/arrow.png");

    private int borderAnimationTickCounter = 0;
    private int borderCurrentFrame = 0;
    private static final int BORDER_TOTAL_FRAMES = 18;
    private static final int BORDER_TICKS_PER_FRAME = 8;
    private boolean isBorderAnimationPlaying = false;
    private int borderAnimationCooldown = 0;
    private static final int BORDER_ANIMATION_INTERVAL = 20 * 20;

    private enum LevelUpState { INACTIVE, INITIAL_HOLD, PLAYING }
    private LevelUpState levelUpState = LevelUpState.INACTIVE;
    private int levelUpPhaseTimer = 0;
    private int previousLevel = -1;
    private int levelUpFrom;
    private int levelUpTo;


    static {
        for (int i = 0; i < LEVELUP_TOTAL_FRAMES; i++) {
            LEVELUP_FRAMES.add(ResourceLocation.fromNamespaceAndPath(RPGMod.MOD_ID, "textures/gui/level/animated_level/" + i + ".png"));
        }
        for (int i = 0; i < 10; i++) {
            DIGIT_TEXTURES.add(ResourceLocation.fromNamespaceAndPath(RPGMod.MOD_ID, "textures/gui/level/numbers/" + i + ".png"));
        }
    }

    @Override
    public void render(ForgeGui gui, GuiGraphics guiGraphics, float partialTick, int screenWidth, int screenHeight) {
        updateBorderAnimation();
        checkForLevelUp();
        updateLevelUpAnimation();

        drawAnimatedClassIcon(guiGraphics);
        drawHudBars(guiGraphics);

        renderLevelUpAnimation(guiGraphics, screenWidth, screenHeight);
    }

    private void checkForLevelUp() {
        if (previousLevel == -1) {
            previousLevel = ClientData.playerLevel;
            return;
        }

        if (ClientData.playerLevel > previousLevel) {
            levelUpState = LevelUpState.INITIAL_HOLD;
            levelUpPhaseTimer = 0;
            levelUpFrom = previousLevel;
            levelUpTo = ClientData.playerLevel;
        }
        previousLevel = ClientData.playerLevel;
    }

    private void updateLevelUpAnimation() {
        if (levelUpState == LevelUpState.INACTIVE) return;
        levelUpPhaseTimer++;

        switch (levelUpState) {
            case INITIAL_HOLD -> {
                if (levelUpPhaseTimer >= INITIAL_HOLD_DURATION) {
                    levelUpState = LevelUpState.PLAYING;
                    levelUpPhaseTimer = 0;
                }
            }
            case PLAYING -> {
                int totalAnimationDuration = LEVELUP_TOTAL_FRAMES * LEVELUP_TICKS_PER_FRAME;
                if (levelUpPhaseTimer >= totalAnimationDuration) {
                    levelUpState = LevelUpState.INACTIVE;
                }
            }
        }
    }

    private void renderLevelUpAnimation(GuiGraphics guiGraphics, int screenWidth, int screenHeight) {
        if (levelUpState == LevelUpState.INACTIVE) return;

        int currentFrameIndex;
        if (levelUpState == LevelUpState.INITIAL_HOLD) {
            currentFrameIndex = 0;
        } else {
            currentFrameIndex = levelUpPhaseTimer / LEVELUP_TICKS_PER_FRAME;
            if (currentFrameIndex >= LEVELUP_TOTAL_FRAMES) {
                currentFrameIndex = LEVELUP_TOTAL_FRAMES - 1;
            }
        }

        ResourceLocation levelUpTexture = LEVELUP_FRAMES.get(currentFrameIndex);
        int textureWidth = 200;
        int textureHeight = 50;
        int x = (screenWidth - textureWidth) / 2;
        int y = (screenHeight / 2) - textureHeight + 10;

        guiGraphics.blit(levelUpTexture, x, y, 0, 0, textureWidth, textureHeight, textureWidth, textureHeight);

        if (levelUpState == LevelUpState.INITIAL_HOLD) {
            drawLevelTransition(guiGraphics, screenWidth, y + textureHeight - 5);
        }
    }

    private void drawLevelTransition(GuiGraphics guiGraphics, int screenWidth, int y) {
        int digitWidth = 15; int digitHeight = 15; int arrowWidth = 15; int spacing = 2;
        int fromWidth = String.valueOf(levelUpFrom).length() * (digitWidth + spacing);
        int toWidth = String.valueOf(levelUpTo).length() * (digitWidth + spacing);
        int totalWidth = fromWidth + arrowWidth + spacing + toWidth;
        int currentX = (screenWidth - totalWidth) / 2;
        currentX = drawNumber(guiGraphics, levelUpFrom, currentX, y, digitWidth, digitHeight, spacing);
        guiGraphics.blit(ARROW_TEXTURE, currentX, y, 0, 0, arrowWidth, digitHeight, arrowWidth, digitHeight);
        currentX += arrowWidth + spacing;
        drawNumber(guiGraphics, levelUpTo, currentX, y, digitWidth, digitHeight, spacing);
    }

    private int drawNumber(GuiGraphics guiGraphics, int number, int x, int y, int digitWidth, int digitHeight, int spacing) {
        String s = String.valueOf(number);
        int currentX = x;
        for (char c : s.toCharArray()) {
            int digit = Character.getNumericValue(c);
            if (digit >= 0 && digit < 10) {
                guiGraphics.blit(DIGIT_TEXTURES.get(digit), currentX, y, 0, 0, digitWidth, digitHeight, digitWidth, digitHeight);
                currentX += digitWidth + spacing;
            }
        }
        return currentX;
    }

    private void drawHudBars(GuiGraphics guiGraphics) {
        int iconSize = 50; int barsX = 8 + iconSize;
        int healthBarWidth = 120; int healthBarHeight = 20; int healthBarY = 10;
        int manaBarWidth = 120; int manaBarHeight = 8; int manaBarY = healthBarY + healthBarHeight + 5;
        renderBar(guiGraphics, barsX, healthBarY, healthBarWidth, healthBarHeight, ClientData.visualHealth, ClientData.maxHealth, HEALTH_BAR_BG, HEALTH_BAR_FILL);
        renderBar(guiGraphics, barsX, manaBarY, manaBarWidth, manaBarHeight, ClientData.visualMana, ClientData.maxMana, MANA_BAR_BG, MANA_BAR_FILL);
    }

    private void updateBorderAnimation() {
        if (isBorderAnimationPlaying) {
            borderAnimationTickCounter++;
            if (borderAnimationTickCounter >= BORDER_TICKS_PER_FRAME) {
                borderAnimationTickCounter = 0;
                borderCurrentFrame++;
                if (borderCurrentFrame >= BORDER_TOTAL_FRAMES) {
                    borderCurrentFrame = 0;
                    isBorderAnimationPlaying = false;
                    borderAnimationCooldown = BORDER_ANIMATION_INTERVAL;
                }
            }
        } else {
            if (borderAnimationCooldown > 0) {
                borderAnimationCooldown--;
            } else {
                isBorderAnimationPlaying = true;
            }
        }
    }

    private void drawAnimatedClassIcon(GuiGraphics gui) {
        ResourceLocation classIcon = getClassIcon();
        if (classIcon == null) return;
        int x = 10;
        int y = 10;
        int iconSize = 50;
        gui.blit(classIcon, x, y, 0, 0, iconSize, iconSize, iconSize, iconSize);
        if (isBorderAnimationPlaying) {
            int frameU = borderCurrentFrame * iconSize;
            int frameV = 0;
            gui.blit(EFFS_BORDER_TEXTURE, x, y, frameU, frameV, iconSize, iconSize, 900, 50);
        }
    }

    private ResourceLocation getClassIcon() {
        return switch (ClientData.playerClassName) {
            case "Воин" -> WARRIOR_ICON_TEXTURE; case "Лучник" -> ARCHER_ICON_TEXTURE; case "Ассасин" -> ASSASIN_ICON_TEXTURE;
            case "Маг" -> MAGE_ICON_TEXTURE; case "Паладин" -> PALADIN_ICON_TEXTURE; case "Некромант" -> NECROMANSER_ICON_TEXTURE;
            default -> null;
        };
    }

    private void renderBar(GuiGraphics gui, int x, int y, int width, int height, float currentValue, float maxValue, ResourceLocation background, ResourceLocation fill) {
        if (maxValue <= 0) return;
        int fillWidth = (int) ((currentValue / maxValue) * width);
        gui.blit(background, x, y, 0, 0, width, height, width, height);
        if (fillWidth > 0) {
            gui.blit(fill, x, y, 0, 0, fillWidth, height, width, height);
        }
    }
}
