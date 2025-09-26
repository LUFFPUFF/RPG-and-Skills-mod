package com.nikita.rpgmod.combat;

import com.nikita.rpgmod.combat.modifier.*;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

/**
 * Сервисный класс-синглтон, который управляет всеми расчетами, связанными с боем.
 * Он обрабатывает события урона, пропуская их через цепочку зарегистрированных
 * модификаторов (ICombatModifier).
 */
public class CombatEngine {

    private static final CombatEngine INSTANCE = new CombatEngine();
    private final List<ICombatModifier> modifiers = new ArrayList<>();

    private CombatEngine() {
        registerModifiers();
    }

    public static CombatEngine getInstance() {
        return INSTANCE;
    }

    /**
     * Регистрирует все боевые механики в движке.
     */
    private void registerModifiers() {
        this.modifiers.add(new AttributeCombatModifier());
        this.modifiers.add(new DexterityCombatModifier());
        this.modifiers.add(new IntelligenceCombatModifier());
        this.modifiers.add(new VitalityCombatModifier());
    }

    /**
     * Обрабатывает событие урона, последовательно применяя все зарегистрированные модификаторы.
     *
     * @param attacker      Сущность, наносящая урон.
     * @param target        Сущность, получающая урон.
     * @param source        Источник урона.
     * @param initialDamage Изначальное количество урона из события.
     * @return Финальное количество урона после всех модификаций.
     */
    public float calculateFinalDamage(@Nullable LivingEntity attacker, LivingEntity target, DamageSource source, float initialDamage) {
        float modifiedDamage = initialDamage;
        for (ICombatModifier modifier : this.modifiers) {
            modifiedDamage = modifier.applyDamageModification(attacker, target, source, modifiedDamage);
        }
        return modifiedDamage;
    }
}
