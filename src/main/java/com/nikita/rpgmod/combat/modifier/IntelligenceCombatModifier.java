package com.nikita.rpgmod.combat.modifier;

import com.nikita.rpgmod.capibility.PlayerStatsProvider;
import com.nikita.rpgmod.util.ModTags;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.Nullable;

public class IntelligenceCombatModifier implements ICombatModifier {

    private static final float MAGIC_DAMAGE_PER_POINT = 0.025f;

    @Override
    public float applyDamageModification(@Nullable LivingEntity attacker, LivingEntity target, DamageSource source, float originalDamage) {
        if (!(attacker instanceof Player player)) {
            return originalDamage;
        }

        if (!source.is(ModTags.DamageTypes.IS_MAGIC)) {
            return originalDamage;
        }

        return player.getCapability(PlayerStatsProvider.PLAYER_STATS)
                .map(stats -> {
                    int intelligence = stats.getIntelligence();
                    float multiplier = 1.0f + (intelligence * MAGIC_DAMAGE_PER_POINT);
                    return originalDamage * multiplier;
                })
                .orElse(originalDamage);
    }
}
