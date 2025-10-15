package com.nikita.rpgmod.item.client;

import com.nikita.rpgmod.RPGMod;
import com.nikita.rpgmod.item.custom.GrimoireHourglassItem;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class GrimoireHourglassModel extends GeoModel<GrimoireHourglassItem> {

    @Override
    public ResourceLocation getModelResource(GrimoireHourglassItem animatable) {
        return ResourceLocation.fromNamespaceAndPath(RPGMod.MOD_ID, "geo/grimoire_hourglass_model.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(GrimoireHourglassItem animatable) {
        return ResourceLocation.fromNamespaceAndPath(RPGMod.MOD_ID, "textures/entity/grimoire_hourglass.png");
    }

    @Override
    public ResourceLocation getAnimationResource(GrimoireHourglassItem animatable) {
        return ResourceLocation.fromNamespaceAndPath(RPGMod.MOD_ID, "animations/grimoire_hourglass.animation.json");
    }
}
