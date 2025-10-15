package com.nikita.rpgmod.magic.spell.modificator.grimoure_hourglass_spell;

import com.nikita.rpgmod.RPGMod;
import com.nikita.rpgmod.capibility.PlayerStatsProvider;
import com.nikita.rpgmod.entity.custom.spell.SandBlastProjectile;
import com.nikita.rpgmod.magic.PlayerMagicProvider;
import com.nikita.rpgmod.magic.spell.ISpell;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Player;

/**
 * Эродирующий Выстрел
 * "Выпускает сгусток песка, который наносит магический урон и замедляет цель."
 */
public class ErodingShotSpell implements ISpell {

    private static final float BASE_DAMAGE = 5.0f;

    @Override
    public ResourceLocation getRegistryName() {
        return ResourceLocation.fromNamespaceAndPath(RPGMod.MOD_ID, "eroding_shot");
    }

    @Override
    public Component getDisplayName() {
        return Component.translatable("spell.rpgmod.eroding_shot");
    }

    @Override
    public ResourceLocation getIcon() {
        return ResourceLocation.fromNamespaceAndPath(RPGMod.MOD_ID, "textures/gui/spells/eroding_shot.png");
    }

    @Override
    public float getManaCost(Player player) {
        return 10.0f;
    }

    @Override
    public int getCooldownTicks(Player player) {
        return player.getCapability(PlayerMagicProvider.PLAYER_MAGIC)
                .map(magic -> (int) (20 * magic.getCooldownModifier()))
                .orElse(20);
    }

    @Override
    public String getAnimationName() {
        return "animation.player.cast_shot";
    }

    @Override
    public int getAnimationLengthTicks() {
        return 20;
    }


    @Override
    public void cast(ServerPlayer player) {
        player.level().playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.BLAZE_SHOOT, SoundSource.PLAYERS, 0.5F, 1.0F);

        player.getCapability(PlayerStatsProvider.PLAYER_STATS).ifPresent(stats -> {
            float finalDamage = BASE_DAMAGE + (stats.getIntelligence() * 0.2f);
            SandBlastProjectile projectile = new SandBlastProjectile(player.level(), player, finalDamage);
            projectile.shootFromRotation(player, player.getXRot(), player.getYRot(), 0.0F, 1.5F, 1.0F);
            player.level().addFreshEntity(projectile);
        });
    }
}
