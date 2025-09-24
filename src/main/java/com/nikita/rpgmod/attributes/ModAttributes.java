package com.nikita.rpgmod.attributes;

import com.nikita.rpgmod.RPGMod;
import com.nikita.rpgmod.util.ModTags;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.RangedAttribute;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModAttributes {

    public static final DeferredRegister<Attribute> ATTRIBUTES =
            DeferredRegister.create(ForgeRegistries.ATTRIBUTES, RPGMod.MOD_ID);

    public static final RegistryObject<Attribute> INTELLIGENCE =
            ATTRIBUTES.register("intelligence", () ->
                    new RangedAttribute("attribute.name.rpgmod.intelligence", 0.0D, 0.0D, 1024.0D).setSyncable(true));

    public static void register() {
        MinecraftForge.EVENT_BUS.register(IntelligenceHandler.class);
    }

    /**
     * Обработчик интеллекта
     */
    public static class IntelligenceHandler {

        @SubscribeEvent
        public static void onMagicDamage(LivingHurtEvent event) {
            DamageSource source = event.getSource();
            if (source.getEntity() instanceof LivingEntity attacker) {
                if (source.is(ModTags.DamageTypes.IS_MAGIC)) {
                    double intellect = attacker.getAttributeValue(INTELLIGENCE.get());
                    double multiplier = 1 + intellect * 0.025;
                    event.setAmount((float) (event.getAmount() * multiplier));
                }
            }
        }

        @SubscribeEvent
        public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
            if (event.phase != TickEvent.Phase.END) return;

            Player player = event.player;
            if (!(player instanceof ServerPlayer)) return;

            double intellect = player.getAttributeValue(INTELLIGENCE.get());

            int maxMana = (int) (100 + intellect * 5);
            PlayerMana.get(player).setMaxMana(maxMana);

            double cooldownReduction = 1 - (1.0 / (1 + intellect * 0.007));
            PlayerCooldowns.get(player).setCooldownReduction(cooldownReduction);
        }
    }
}
