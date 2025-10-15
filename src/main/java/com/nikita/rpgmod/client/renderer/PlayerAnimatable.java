package com.nikita.rpgmod.client.renderer;

import com.nikita.rpgmod.capibility.PlayerAnimationProvider;
import net.minecraft.world.entity.player.Player;
import software.bernie.geckolib.core.animatable.GeoAnimatable;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.RawAnimation;
import software.bernie.geckolib.core.object.PlayState;
import software.bernie.geckolib.util.GeckoLibUtil;

public class PlayerAnimatable implements GeoAnimatable {

    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);
    private final Player player;

    public PlayerAnimatable(Player player) {
        this.player = player;
    }

    public Player getPlayer() {
        return player;
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>(this, "controller", 0, state ->
                player.getCapability(PlayerAnimationProvider.PLAYER_ANIMATION).map(animData -> {
                    if (animData.isAnimating()) {
                        state.getController().setAnimation(RawAnimation.begin().thenPlay(animData.getCurrentAnimation()));
                    } else {
                        return PlayState.STOP;
                    }
                    return PlayState.CONTINUE;
                }).orElse(PlayState.STOP)
        ));
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }

    @Override
    public double getTick(Object object) {
        return 0;
    }
}
