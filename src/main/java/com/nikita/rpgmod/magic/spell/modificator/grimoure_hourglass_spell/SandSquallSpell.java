package com.nikita.rpgmod.magic.spell.modificator.grimoure_hourglass_spell;

import com.nikita.rpgmod.RPGMod;
import com.nikita.rpgmod.capibility.PlayerStatsProvider;
import com.nikita.rpgmod.item.ModItems;
import com.nikita.rpgmod.magic.PlayerMagicProvider;
import com.nikita.rpgmod.magic.spell.ISpell;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import java.util.List;
import java.util.Objects;

/**
 * Песчаный Шквал
 * "Создает перед собой короткий, но широкий порыв острого песка, поражающий всех врагов в небольшом радиусе."
 */
public class SandSquallSpell implements ISpell {

    private static final float BASE_DAMAGE = 3.0f;

    @Override
    public ResourceLocation getRegistryName() {
        return ResourceLocation.fromNamespaceAndPath(RPGMod.MOD_ID, "sand_squall");
    }

    @Override
    public Component getDisplayName() {
        return Component.translatable("spell.rpgmod.sand_squall");
    }

    @Override
    public ResourceLocation getIcon() {
        return ResourceLocation.fromNamespaceAndPath(RPGMod.MOD_ID, "textures/gui/spells/grimoire_hourglass/sand_squall.png");
    }

    @Override
    public float getManaCost(Player player) {
        return 15.0f;
    }

    @Override
    public int getCooldownTicks(Player player) {
        return player.getCapability(PlayerMagicProvider.PLAYER_MAGIC)
                .map(magic -> (int) (60 * magic.getCooldownModifier()))
                .orElse(60);
    }

    @Override
    public String getAnimationName() {
        return "animation.grimoire.cast_cone";
    }

    @Override
    public int getAnimationLengthTicks() {
        return 0;
    }

    @Override
    public void cast(ServerPlayer player) {
        ServerLevel level = player.serverLevel();
        level.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.SAND_BREAK, SoundSource.PLAYERS, 1.0F, 1.0F);

        player.getCapability(PlayerStatsProvider.PLAYER_STATS).ifPresent(stats -> {
            float finalDamage = BASE_DAMAGE + (stats.getIntelligence() * 0.15f);
            double range = 4.0;
            Vec3 playerPos = player.position().add(0, player.getEyeHeight() / 2, 0);
            Vec3 lookVec = player.getLookAngle();

            for (int i = 0; i < 20; i++) {
                Vec3 particleVec = lookVec.scale(1 + level.random.nextDouble() * 2.0)
                        .add(new Vec3(level.random.nextGaussian() * 0.5, level.random.nextGaussian() * 0.5, level.random.nextGaussian() * 0.5));
                level.sendParticles(ParticleTypes.CRIT, playerPos.x + particleVec.x, playerPos.y + particleVec.y, playerPos.z + particleVec.z, 1, 0, 0, 0, 0);
            }

            AABB searchBox = player.getBoundingBox().inflate(range);
            List<LivingEntity> nearbyEntities = level.getEntitiesOfClass(LivingEntity.class, searchBox, e -> e instanceof Enemy && e.isAlive());

            for (LivingEntity target : nearbyEntities) {
                Vec3 toTarget = target.position().subtract(playerPos).normalize();
                if (lookVec.dot(toTarget) > 0.5) {
                    target.hurt(level.damageSources().magic(), finalDamage);
                }
            }
        });
    }

    @Override
    public Item getCooldownItem() {
        return ModItems.CD_SAND_SQUALL.get();
    }

    @Override
    public Component getDescription() {
        return Component.translatable("spell.rpgmod.sand_squall.desc");
    }
}
