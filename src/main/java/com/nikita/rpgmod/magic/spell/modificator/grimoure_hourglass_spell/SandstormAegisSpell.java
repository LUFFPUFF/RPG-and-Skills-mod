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
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;

/**
 * Эгида Песчаной Бури
 * "Игрок на несколько секунд создает вокруг себя вихрь из песка. Этот вихрь дает мощную защиту,
 * значительно снижая входящий урон, а также наносит небольшой урон всем врагам, подошедшим вплотную."
 */
public class SandstormAegisSpell implements ISpell {

    @Override
    public ResourceLocation getRegistryName() {
        return ResourceLocation.fromNamespaceAndPath(RPGMod.MOD_ID, "sandstorm_aegis");
    }

    @Override
    public Component getDisplayName() {
        return Component.translatable("spell.rpgmod.sandstorm_aegis");
    }

    @Override
    public ResourceLocation getIcon() {
        return ResourceLocation.fromNamespaceAndPath(RPGMod.MOD_ID, "textures/gui/spells/grimoire_hourglass/sandstorm_aegis.png");
    }

    @Override
    public float getManaCost(Player player) {
        return 100.0f;
    }

    @Override
    public int getCooldownTicks(Player player) {
        return player.getCapability(PlayerMagicProvider.PLAYER_MAGIC)
                .map(magic -> (int) (1200 * magic.getCooldownModifier())) // База: 60 секунд
                .orElse(1200);
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
        player.level().playSound(null, player.blockPosition(), SoundEvents.BEACON_DEACTIVATE, SoundSource.PLAYERS, 1.0f, 0.5f);

        player.getCapability(PlayerStatsProvider.PLAYER_STATS).ifPresent(stats -> {
            int durationTicks = 100 + (stats.getIntelligence() * 3);

            player.addEffect(new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, durationTicks, 99, false, true));
        });
    }

    @Override
    public Item getCooldownItem() {
        return ModItems.CD_SANDSTORM_AEGIS.get();
    }

    @Override
    public Component getDescription() {
        return Component.translatable("spell.rpgmod.sandstorm_aegis.desc");
    }
}
