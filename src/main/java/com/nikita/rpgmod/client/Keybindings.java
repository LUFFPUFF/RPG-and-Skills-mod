package com.nikita.rpgmod.client;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import net.minecraftforge.client.settings.KeyConflictContext;
import org.lwjgl.glfw.GLFW;

public class Keybindings {

    public static final KeyMapping OPEN_CHARACTER_SCREEN = new KeyMapping(
            "key.rpgmod.open_character_screen",
            KeyConflictContext.IN_GAME,
            InputConstants.getKey(InputConstants.KEY_P, -1),
            "key.category.rpgmod"
    );

    public static final KeyMapping USE_INSIGHT_SKILL = new KeyMapping(
            "key.rpgmod.use_insight_skill",
            KeyConflictContext.IN_GAME,
            InputConstants.getKey(InputConstants.KEY_V, -1),
            "key.category.rpgmod"
    );

    public static final KeyMapping CAST_SPELL = new KeyMapping(
            "key.rpgmod.cast_spell",
            KeyConflictContext.IN_GAME,
            InputConstants.getKey(GLFW.GLFW_KEY_R, -1),
            "key.category.rpgmod"
    );

    public static final KeyMapping NEXT_SPELL = new KeyMapping(
            "key.rpgmod.next_spell",
            KeyConflictContext.IN_GAME,
            InputConstants.getKey(GLFW.GLFW_KEY_X, -1),
            "key.category.rpgmod"
    );

    public static final KeyMapping PREVIOUS_SPELL = new KeyMapping(
            "key.rpgmod.previous_spell",
            KeyConflictContext.IN_GAME,
            InputConstants.getKey(GLFW.GLFW_KEY_Z, -1),
            "key.category.rpgmod"
    );
}
