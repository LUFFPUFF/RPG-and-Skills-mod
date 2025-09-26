package com.nikita.rpgmod.combat.modifier;

import com.nikita.rpgmod.capibility.PlayerStatsProvider;
import com.nikita.rpgmod.util.ModTags;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.Nullable;

public class VitalityCombatModifier implements ICombatModifier {

    private static final double PROTECTION_PER_CONSTITUTION = 0.002;

    @Override
    public float applyDamageModification(@Nullable LivingEntity attacker, LivingEntity target, DamageSource source, float originalDamage) {
        if (!(target instanceof Player player)) {
            return originalDamage;
        }

        if (source.is(ModTags.VanillaDamageTypeTags.BYPASSES_ARMOR)) {
            return originalDamage;
        }

        return player.getCapability(PlayerStatsProvider.PLAYER_STATS)
                .map(stats -> {
                    int vitality = stats.getVitality();

                    double reductionRatio = (vitality * PROTECTION_PER_CONSTITUTION) / (1.0 + vitality * PROTECTION_PER_CONSTITUTION);

                    return originalDamage * (1.0f - (float)reductionRatio);
                })
                .orElse(originalDamage);
    }
}
