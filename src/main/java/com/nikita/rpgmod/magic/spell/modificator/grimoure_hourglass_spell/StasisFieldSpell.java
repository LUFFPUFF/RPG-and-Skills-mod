package com.nikita.rpgmod.magic.spell.modificator.grimoure_hourglass_spell;

import com.nikita.rpgmod.RPGMod;
import com.nikita.rpgmod.capibility.PlayerStatsProvider;
import com.nikita.rpgmod.item.ModItems;
import com.nikita.rpgmod.magic.PlayerMagicProvider;
import com.nikita.rpgmod.magic.spell.ISpell;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.phys.AABB;

import java.util.List;

/**
 * Поле Временного Стазиса
 * "Создает вокруг игрока сферическую область искаженного времени. Все враги, попавшие в эту область, становятся чрезвычайно медленными,
 * их движения практически замирают."
 */
public class StasisFieldSpell implements ISpell {

    @Override
    public ResourceLocation getRegistryName() {
        return ResourceLocation.fromNamespaceAndPath(RPGMod.MOD_ID, "stasis_field");
    }

    @Override
    public Component getDisplayName() {
        return Component.translatable("spell.rpgmod.stasis_field");
    }

    @Override
    public ResourceLocation getIcon() {
        return ResourceLocation.fromNamespaceAndPath(RPGMod.MOD_ID, "textures/gui/spells/grimoire_hourglass/stasis_field.png");
    }

    @Override
    public float getManaCost(Player player) {
        return 60.0f;
    }

    @Override
    public int getCooldownTicks(Player player) {
        return player.getCapability(PlayerMagicProvider.PLAYER_MAGIC)
                .map(magic -> (int) (400 * magic.getCooldownModifier()))
                .orElse(400);
    }

    @Override
    public String getAnimationName() {
        return "";
    }

    @Override
    public int getAnimationLengthTicks() {
        return 0;
    }

    @Override
    public void cast(ServerPlayer player) {
        player.level().playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.BEACON_ACTIVATE, SoundSource.PLAYERS, 0.7F, 1.5F);

        player.getCapability(PlayerStatsProvider.PLAYER_STATS).ifPresent(stats -> {
            double radius = 5.0;
            int durationTicks = 100 + (stats.getIntelligence() * 2);

            AABB area = player.getBoundingBox().inflate(radius);
            List<LivingEntity> targets = player.level().getEntitiesOfClass(LivingEntity.class, area, e -> e instanceof Enemy && e.isAlive());

            for (LivingEntity target : targets) {
                target.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, durationTicks, 4));
            }
        });
    }

    @Override
    public Item getCooldownItem() {
        return ModItems.CD_STASIS_FIELD.get();
    }

    @Override
    public Component getDescription() {
        return Component.translatable("spell.rpgmod.stasis_field.desc");
    }
}
