package com.nikita.rpgmod.client;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import net.minecraftforge.client.settings.KeyConflictContext;

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
}
