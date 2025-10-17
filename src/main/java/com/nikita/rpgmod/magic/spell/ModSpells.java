package com.nikita.rpgmod.magic.spell;

import com.nikita.rpgmod.magic.spell.modificator.grimoure_hourglass_spell.*;
import com.nikita.rpgmod.magic.spell.register.SpellRegistry;

public class ModSpells {

    // --- Гримуар Песочных Часов ---
    public static final ISpell ERODING_SHOT = new ErodingShotSpell();
    public static final ISpell SAND_SQUALL = new SandSquallSpell();
    public static final ISpell STASIS_FIELD = new StasisFieldSpell();
    public static final ISpell QUICKSAND_TRAP = new QuicksandTrapSpell();
    public static final ISpell DUNE_SHIFT = new DuneShiftSpell();
    public static final ISpell SANDSTORM_AEGIS = new SandstormAegisSpell();


    public static void register() {
        SpellRegistry.register(ERODING_SHOT);
        SpellRegistry.register(SAND_SQUALL);
        SpellRegistry.register(STASIS_FIELD);
        SpellRegistry.register(QUICKSAND_TRAP);
        SpellRegistry.register(DUNE_SHIFT);
        SpellRegistry.register(SANDSTORM_AEGIS);
    }
}
