package com.nikita.rpgmod.client.model;

import com.nikita.rpgmod.RPGMod;
import com.nikita.rpgmod.client.renderer.PlayerAnimatable;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

import java.util.Objects;

public class AnimatedPlayerModel extends GeoModel<PlayerAnimatable> {

    @Override
    public ResourceLocation getModelResource(PlayerAnimatable animatable) {
        return ResourceLocation.fromNamespaceAndPath(RPGMod.MOD_ID, "geo/detailed_player.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(PlayerAnimatable animatable) {
        return Objects.requireNonNull(Minecraft.getInstance().player).getSkinTextureLocation();
    }

    @Override
    public ResourceLocation getAnimationResource(PlayerAnimatable animatable) {
        return ResourceLocation.fromNamespaceAndPath(RPGMod.MOD_ID, "animations/eroding_shot_player.animation.json");
    }
}
