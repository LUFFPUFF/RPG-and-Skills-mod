package com.nikita.rpgmod.item.client;

import com.nikita.rpgmod.item.custom.GrimoireHourglassItem;
import software.bernie.geckolib.renderer.GeoItemRenderer;

public class GrimoireHourglassRenderer extends GeoItemRenderer<GrimoireHourglassItem> {

    public GrimoireHourglassRenderer() {
        super(new GrimoireHourglassModel());
    }
}
