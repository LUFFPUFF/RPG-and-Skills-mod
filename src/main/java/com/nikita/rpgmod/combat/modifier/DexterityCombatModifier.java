package com.nikita.rpgmod.combat.modifier;

import com.nikita.rpgmod.capibility.PlayerStatsProvider;
import com.nikita.rpgmod.util.ModTags;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.Nullable;

public class DexterityCombatModifier implements ICombatModifier {

    private static final float DEXTERITY_RANGED_DAMAGE_PER_POINT = 0.02f;
    private static final float CRIT_MULTIPLIER = 1.5f;

    @Override
    public float applyDamageModification(@Nullable LivingEntity attacker, LivingEntity target, DamageSource source, float originalDamage) {
        if (!(attacker instanceof Player player)) {
            return originalDamage;
        }

        if (source.is(ModTags.DamageTypes.IS_PROJECTILE)) {
            return player.getCapability(PlayerStatsProvider.PLAYER_STATS)
                    .map(stats -> {
                        int dexterity = stats.getDexterity();

                        float damageMultiplier = 1.0f + (dexterity * DEXTERITY_RANGED_DAMAGE_PER_POINT);
                        float modifiedDamage = originalDamage * damageMultiplier;

                        float critChance = 1f - (1f / (1f + dexterity * 0.01f));
                        if (player.level().getRandom().nextFloat() < critChance) {
                            modifiedDamage *= CRIT_MULTIPLIER;
                        }

                        return  modifiedDamage;
                    })
                    .orElse(originalDamage);
        }
        return originalDamage;
    }
}
