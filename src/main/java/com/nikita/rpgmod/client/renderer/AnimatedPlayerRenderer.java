package com.nikita.rpgmod.client.renderer;

import com.nikita.rpgmod.client.model.AnimatedPlayerModel;
import software.bernie.geckolib.renderer.GeoObjectRenderer;


public class AnimatedPlayerRenderer extends GeoObjectRenderer<PlayerAnimatable> {
    public AnimatedPlayerRenderer() {
        super(new AnimatedPlayerModel());
    }
}

