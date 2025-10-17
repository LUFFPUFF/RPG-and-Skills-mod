package com.nikita.rpgmod.magic.spell.capability;

import com.nikita.rpgmod.magic.spell.ISpell;
import com.nikita.rpgmod.magic.spell.register.SpellRegistry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class PlayerSpellData {

    private final List<ResourceLocation> knownSpells = new ArrayList<>();
    private int currentSpellIndex = -1;

    public List<ResourceLocation> getKnownSpells() {
        return knownSpells;
    }

    public void learnSpell(ResourceLocation spellId) {
        if (!knownSpells.contains(spellId)) {
            knownSpells.add(spellId);
            if (currentSpellIndex == -1) {
                currentSpellIndex = 0;
            }
        }
    }

    public int getCurrentSpellIndex() {
        return this.currentSpellIndex;
    }

    public void forgetSpell(ResourceLocation spellId) {
        knownSpells.remove(spellId);
        if (currentSpellIndex >= knownSpells.size()) {
            currentSpellIndex = knownSpells.isEmpty() ? -1 : 0;
        }
    }

    @Nullable
    public ISpell getCurrentSpell() {
        if (currentSpellIndex == -1 || knownSpells.isEmpty()) {
            return null;
        }
        return SpellRegistry.getSpell(knownSpells.get(currentSpellIndex));
    }

    public void selectNextSpell() {
        if (!knownSpells.isEmpty()) {
            currentSpellIndex = (currentSpellIndex + 1) % knownSpells.size();
        }
    }

    public void selectPreviousSpell() {
        if (!knownSpells.isEmpty()) {
            currentSpellIndex--;
            if (currentSpellIndex < 0) {
                currentSpellIndex = knownSpells.size() - 1;
            }
        }
    }

    public void saveNBTData(CompoundTag nbt) {
        ListTag knownSpellsTag = new ListTag();
        for (ResourceLocation spellId : knownSpells) {
            knownSpellsTag.add(StringTag.valueOf(spellId.toString()));
        }
        nbt.put("knownSpells", knownSpellsTag);
        nbt.putInt("currentSpellIndex", currentSpellIndex);
    }

    public void loadNBTData(CompoundTag nbt) {
        knownSpells.clear();
        ListTag knownSpellsTag = nbt.getList("knownSpells", 8);
        for (int i = 0; i < knownSpellsTag.size(); i++) {
            knownSpells.add(ResourceLocation.parse(knownSpellsTag.getString(i)));
        }
        currentSpellIndex = nbt.getInt("currentSpellIndex");
    }
}
