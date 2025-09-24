package com.nikita.rpgmod.attributes;

import com.nikita.rpgmod.capibility.PlayerStatsProvider;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;

import java.util.UUID;

/**
 * Сервисный утилитарный класс, отвечающий за синхронизацию ванильных атрибутов игрока
 * (таких как здоровье, скорость, сопротивление отбрасыванию) с кастомными статами из мода.
 */

public class AttributeUpdateService {

    /**
     * Уникальный идентификатор (UUID) для нашего модификатора сопротивления отбрасыванию.
     * UUID абсолютно необходим для того, чтобы мы могли найти и удалить наш предыдущий
     * модификатор перед добавлением нового. Если этого не делать, модификаторы будут
     * бесконечно "стакаться" (накладываться друг на друга) при каждом изменении стата.
     * Вы можете сгенерировать свой собственный UUID на любом онлайн-генераторе.
     */
    private static final UUID STRENGTH_KNOCKBACK_RESISTANCE_UUID = UUID.fromString("a8a0b5a0-5a1b-4a5f-8b9a-1b9b1c1d1e1f");

    /**
     * Коэффициент, определяющий, сколько сопротивления отбрасыванию дает одно очко Силы.
     * Использование константы делает код более читаемым и легким для балансировки.
     */
    private static final double KNOCKBACK_RESISTANCE_PER_STRENGTH = 0.005;

    private static final UUID DEXTERITY_ATTACK_SPEED_UUID = UUID.fromString("b1c7f8b0-44a2-11ee-be56-0242ac120002");
    private static final double ATTACK_SPEED_PER_DEXTERITY = 0.015;

    private AttributeUpdateService() {}

    /**
     * Обновляет ванильный атрибут сопротивления отбрасыванию игрока на основе
     * его текущего значения кастомного стата Силы.
     *
     * @param player Игрок, чьи атрибуты нужно обновить. Не может быть null.
     */
    public static void updateKnockbackResistance(Player player) {
        AttributeInstance knockbackResistanceInstance = player.getAttribute(Attributes.KNOCKBACK_RESISTANCE);

        if (knockbackResistanceInstance == null) {
            return;
        }

        knockbackResistanceInstance.removeModifier(STRENGTH_KNOCKBACK_RESISTANCE_UUID);

        player.getCapability(PlayerStatsProvider.PLAYER_STATS).ifPresent(stats -> {
            int strength = stats.getStrength();

            if (strength > 1) {
                double value = strength * KNOCKBACK_RESISTANCE_PER_STRENGTH;

                AttributeModifier modifier = new AttributeModifier(
                        STRENGTH_KNOCKBACK_RESISTANCE_UUID,
                        "RPGMod Strength Knockback Bonus",
                        value,
                        AttributeModifier.Operation.ADDITION
                );

                knockbackResistanceInstance.addPermanentModifier(modifier);
            }
        });
    }

    public static void updateAttackSpeed(Player player) {
        AttributeInstance attackSpeedInstance = player.getAttribute(Attributes.ATTACK_SPEED);

        if (attackSpeedInstance == null) {
            return;
        }

        attackSpeedInstance.removeModifier(DEXTERITY_ATTACK_SPEED_UUID);
        player.getCapability(PlayerStatsProvider.PLAYER_STATS).ifPresent(stats -> {
            int dexterity = stats.getDexterity();

            if (dexterity > 1) {

                double value = dexterity * ATTACK_SPEED_PER_DEXTERITY;

                AttributeModifier modifier = new AttributeModifier(
                        DEXTERITY_ATTACK_SPEED_UUID,
                        "RPGMod Dexterity Attack Speed Bonus",
                        value,
                        AttributeModifier.Operation.MULTIPLY_BASE
                );

                attackSpeedInstance.addPermanentModifier(modifier);
            }
        });
    }
}
