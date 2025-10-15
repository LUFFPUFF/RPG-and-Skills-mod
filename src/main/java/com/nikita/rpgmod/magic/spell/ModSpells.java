package com.nikita.rpgmod.magic.spell;

import com.nikita.rpgmod.magic.spell.modificator.grimoure_hourglass_spell.ErodingShotSpell;
import com.nikita.rpgmod.magic.spell.modificator.grimoure_hourglass_spell.SandSquallSpell;
import com.nikita.rpgmod.magic.spell.register.SpellRegistry;

public class ModSpells {

    public static final ISpell ERODING_SHOT = new ErodingShotSpell();
    public static final ISpell SAND_SQUALL =  new SandSquallSpell();

    public static void register() {
        SpellRegistry.register(ERODING_SHOT);
        SpellRegistry.register(SAND_SQUALL);
    }
}
