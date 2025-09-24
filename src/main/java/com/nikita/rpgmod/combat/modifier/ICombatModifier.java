package com.nikita.rpgmod.combat.modifier;

import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;

import javax.annotation.Nullable;

public interface ICombatModifier {

    /**
     * Рассчитывает итоговый урон, основываясь на логике конкретного модификатора.
     *
     * @param attacker       Сущность, наносящая урон. Может быть null (например, урон от падения).
     * @param target         Сущность, получающая урон.
     * @param source         Источник урона (например, ближний бой, стрела, магия).
     * @param originalDamage Количество урона до применения этого модификатора.
     * @return Новое, измененное значение урона.
     */
    float applyDamageModification(@Nullable LivingEntity attacker, LivingEntity target, DamageSource source, float originalDamage);
}
