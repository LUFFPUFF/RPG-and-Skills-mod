package com.nikita.rpgmod.magic.spell;

import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;

/**
 * Интерфейс, представляющий одно магическое заклинание.
 * Каждый новый спелл должен реализовывать этот интерфейс.
 */
public interface ISpell {

    /**
     * Уникальный идентификатор заклинания. Используется для регистрации и сохранения.
     * @return ResourceLocation, например, "rpgmod:eroding_shot"
     */
    ResourceLocation getRegistryName();

    /**
     * Отображаемое имя заклинания. Будет показано на HUD.
     * @return Component с названием.
     */
    Component getDisplayName();

    /**
     * Иконка заклинания. Будет показана на HUD.
     * @return ResourceLocation, указывающий на текстуру (например, в /textures/gui/spells/spell.png).
     */
    ResourceLocation getIcon();

    /**
     * Стоимость произнесения заклинания в мане.
     * @param player Игрок, который кастует заклинание.
     * @return Количество маны.
     */
    float getManaCost(Player player);

    /**
     * Время перезарядки заклинания в тиках (20 тиков = 1 секунда).
     * @param player Игрок, который кастует заклинание.
     * @return Количество тиков.
     */
    int getCooldownTicks(Player player);

    /**
     * Возвращает название анимации для этого заклинания.
     * @return Строка с названием анимации, например, "animation.grimoire.cast_shot".
     */
    String getAnimationName();

    /**
     * Возвращает длительность анимации каста в тиках (20 тиков = 1 секунда).
     * Это время, на которое будет надета "анимационная броня".
     * @return Количество тиков.
     */
    int getAnimationLengthTicks();

    /**
     * Основной метод, который "произносит" заклинание.
     * Вся логика заклинания (создание снарядов, наложение эффектов) находится здесь.
     * @param player Игрок, который кастует заклинание.
     */
    void cast(ServerPlayer player);

    /**
     * Возвращает предмет-заглушку, используемый для отслеживания перезарядки этого заклинания.
     * @return Предмет для кулдауна.
     */
    Item getCooldownItem();

    /**
     * Возвращает подробное описание заклинания.
     * Будет показано при наведении на иконку.
     * @return Component с описанием.
     */
    Component getDescription();

}
