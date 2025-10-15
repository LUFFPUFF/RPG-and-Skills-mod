package com.nikita.rpgmod.entity.custom.spell;

import com.nikita.rpgmod.entity.ModEntities;
import com.nikita.rpgmod.util.ModDamageTypes;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.ItemSupplier;
import net.minecraft.world.entity.projectile.ThrowableProjectile;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import org.jetbrains.annotations.NotNull;

public class SandBlastProjectile extends ThrowableProjectile implements ItemSupplier {

    private float damage;

    public SandBlastProjectile(EntityType<? extends ThrowableProjectile> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    public SandBlastProjectile(Level pLevel, LivingEntity pShooter, float damage) {
        super(ModEntities.SAND_BLAST_PROJECTILE.get(), pShooter, pLevel);
        this.damage = damage;
    }

    @Override
    protected void defineSynchedData() {
    }

    @Override
    public void tick() {
        super.tick();
        if (this.level().isClientSide) {
            this.level().addParticle(ParticleTypes.CRIT, this.getX(), this.getY(), this.getZ(), 0.0D, 0.0D, 0.0D);
            this.level().addParticle(ParticleTypes.SOUL_FIRE_FLAME, this.getX(), this.getY(), this.getZ(), 0.0D, 0.0D, 0.0D);
        }
    }

    @Override
    protected void onHitEntity(@NotNull EntityHitResult pResult) {
        super.onHitEntity(pResult);
        if (pResult.getEntity() instanceof LivingEntity target && getOwner() != null) {
            DamageSource damageSource = new DamageSource(
                    this.level().registryAccess().registryOrThrow(Registries.DAMAGE_TYPE).getHolderOrThrow(ModDamageTypes.SAND_BLAST),
                    this.getOwner()
            );
            target.hurt(damageSource, this.damage);
            target.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 60, 0));
        }
    }

    @Override
    protected void onHit(@NotNull HitResult pResult) {
        super.onHit(pResult);
        if (!this.level().isClientSide) {
            this.discard();
        }
    }

    @Override
    public @NotNull ItemStack getItem() {
        return new ItemStack(Items.AMETHYST_SHARD);
    }
}
