package com.nikita.rpgmod.combat.modifier;

import com.nikita.rpgmod.capibility.PlayerStatsProvider;
import com.nikita.rpgmod.util.ModTags;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;

public class AttributeCombatModifier implements ICombatModifier {

    private static final float STRENGTH_DAMAGE_MULTIPLIER_PER_POINT = 0.02f;

    @Override
    public float applyDamageModification(LivingEntity attacker, LivingEntity target, DamageSource source, float originalDamage) {

        if (!(attacker instanceof Player player)) {
            return originalDamage;
        }

        if (!source.is(ModTags.DamageTypes.IS_MELEE)) {
            return originalDamage;
        }

        return player.getCapability(PlayerStatsProvider.PLAYER_STATS)
                .map(stats -> {
                    int strength = stats.getStrength();
                    float strengthMultiplier = 1.0f + (strength * STRENGTH_DAMAGE_MULTIPLIER_PER_POINT);
                    return originalDamage *  strengthMultiplier;
                })
                .orElse(originalDamage);
    }
}
