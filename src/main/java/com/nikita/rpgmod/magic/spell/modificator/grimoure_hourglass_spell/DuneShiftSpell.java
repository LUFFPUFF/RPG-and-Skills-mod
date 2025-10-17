package com.nikita.rpgmod.magic.spell.modificator.grimoure_hourglass_spell;

import com.nikita.rpgmod.RPGMod;
import com.nikita.rpgmod.item.ModItems;
import com.nikita.rpgmod.magic.PlayerMagicProvider;
import com.nikita.rpgmod.magic.spell.ISpell;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

/**
 * Дюна-Призрак (Телепорт/Дэш)
 * "Игрок на короткое мгновение рассыпается в облако песка и стремительно перемещается на небольшое расстояние в
 * направлении взгляда, позволяя быстро сократить дистанцию до врага или уклониться от атаки."
 */
public class DuneShiftSpell implements ISpell {

    @Override
    public ResourceLocation getRegistryName() {
        return ResourceLocation.fromNamespaceAndPath(RPGMod.MOD_ID, "dune_shift");
    }

    @Override
    public Component getDisplayName() {
        return Component.translatable("spell.rpgmod.dune_shift");
    }

    @Override
    public ResourceLocation getIcon() {
        return ResourceLocation.fromNamespaceAndPath(RPGMod.MOD_ID, "textures/gui/spells/grimoire_hourglass/dune_shift.png");
    }

    @Override
    public float getManaCost(Player player) {
        return 25.0f;
    }

    @Override
    public int getCooldownTicks(Player player) {
        return player.getCapability(PlayerMagicProvider.PLAYER_MAGIC)
                .map(magic -> (int) (100 * magic.getCooldownModifier())) // База: 5 секунд
                .orElse(100);
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
        ServerLevel level = player.serverLevel();
        Vec3 startPos = player.getEyePosition();
        Vec3 lookVec = player.getLookAngle();
        double distance = 8.0;
        Vec3 endPos = startPos.add(lookVec.scale(distance));

        HitResult hitResult = level.clip(new ClipContext(startPos, endPos, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, player));
        if (hitResult.getType() != HitResult.Type.MISS) {
            endPos = hitResult.getLocation().subtract(lookVec.scale(0.1));
        }

        level.sendParticles(ParticleTypes.POOF, player.getX(), player.getY(), player.getZ(), 30, 0.5, 1.0, 0.5, 0.1);
        level.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.ENDERMAN_TELEPORT, SoundSource.PLAYERS, 1.0f, 1.2f);

        player.teleportTo(endPos.x, endPos.y - player.getEyeHeight(), endPos.z);

        level.sendParticles(ParticleTypes.POOF, endPos.x, endPos.y, endPos.z, 30, 0.5, 1.0, 0.5, 0.1);
        level.playSound(null, endPos.x, endPos.y, endPos.z, SoundEvents.ENDERMAN_TELEPORT, SoundSource.PLAYERS, 1.0f, 1.5f);
    }

    @Override
    public Item getCooldownItem() {
        return ModItems.CD_DUNE_SHIFT.get();
    }

    @Override
    public Component getDescription() {
        return Component.translatable("spell.rpgmod.dune_shift.desc");
    }
}
