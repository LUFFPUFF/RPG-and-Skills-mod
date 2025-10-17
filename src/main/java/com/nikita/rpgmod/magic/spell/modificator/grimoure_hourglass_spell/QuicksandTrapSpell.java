package com.nikita.rpgmod.magic.spell.modificator.grimoure_hourglass_spell;

import com.nikita.rpgmod.RPGMod;
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
import net.minecraft.world.entity.AreaEffectCloud;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;

/**
 * Зыбучие Пески
 * "Создает на земле ловушку из зыбучих песков. Первый враг, наступивший на нее, обездвиживается и получает
 * периодический урон, имитирующий погружение в песок."
 */
public class QuicksandTrapSpell implements ISpell {

    @Override
    public ResourceLocation getRegistryName() {
        return ResourceLocation.fromNamespaceAndPath(RPGMod.MOD_ID, "quicksand_trap");
    }

    @Override
    public Component getDisplayName() {
        return Component.translatable("spell.rpgmod.quicksand_trap");
    }

    @Override
    public ResourceLocation getIcon() {
        return ResourceLocation.fromNamespaceAndPath(RPGMod.MOD_ID, "textures/gui/spells/grimoire_hourglass/quicksand_trap.png");
    }

    @Override
    public float getManaCost(Player player) {
        return 40.0f;
    }

    @Override
    public int getCooldownTicks(Player player) {
        return player.getCapability(PlayerMagicProvider.PLAYER_MAGIC)
                .map(magic -> (int) (300 * magic.getCooldownModifier()))
                .orElse(300);
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
        BlockHitResult hitResult = player.level().clip(new ClipContext(player.getEyePosition(), player.getEyePosition().add(player.getLookAngle().scale(15)), ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, player));

        if (hitResult.getType() == HitResult.Type.BLOCK) {
            player.level().playSound(null, hitResult.getBlockPos(), SoundEvents.SAND_FALL, SoundSource.BLOCKS, 1.0f, 1.0f);

            AreaEffectCloud cloud = new AreaEffectCloud(player.level(), hitResult.getLocation().x, hitResult.getLocation().y + 0.1, hitResult.getLocation().z);
            cloud.setOwner(player);
            cloud.setRadius(2.0f);
            cloud.setDuration(200);
            cloud.setWaitTime(10);
            cloud.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 100, 10));
            cloud.addEffect(new MobEffectInstance(MobEffects.WEAKNESS, 100, 1));

            player.level().addFreshEntity(cloud);
        }
    }

    @Override
    public Item getCooldownItem() {
        return ModItems.CD_QUICKSAND_TRAP.get();
    }

    @Override
    public Component getDescription() {
        return Component.translatable("spell.rpgmod.quicksand_trap.desc");
    }
}
