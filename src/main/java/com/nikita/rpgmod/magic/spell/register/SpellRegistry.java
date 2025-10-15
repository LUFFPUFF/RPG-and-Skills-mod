package com.nikita.rpgmod.magic.spell.register;

import com.nikita.rpgmod.magic.spell.ISpell;
import net.minecraft.resources.ResourceLocation;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Статический реестр для всех заклинаний в моде.
 * Позволяет получать доступ к объекту заклинания по его ID.
 */
public class SpellRegistry {

    private static final Map<ResourceLocation, ISpell> SPELLS = new HashMap<>();

    public static void register(ISpell spell) {
        if (SPELLS.containsKey(spell.getRegistryName())) {
            throw new IllegalArgumentException("Duplicate spell registration for " + spell.getRegistryName());
        }
        SPELLS.put(spell.getRegistryName(), spell);
    }

    public static ISpell getSpell(ResourceLocation id) {
        return SPELLS.get(id);
    }

    public static Collection<ISpell> getAllSpells() {
        return Collections.unmodifiableCollection(SPELLS.values());
    }
}
