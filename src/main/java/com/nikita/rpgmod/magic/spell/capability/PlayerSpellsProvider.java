package com.nikita.rpgmod.magic.spell.capability;

import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class PlayerSpellsProvider implements ICapabilityProvider, INBTSerializable<CompoundTag> {
    public static Capability<PlayerSpellData> PLAYER_SPELLS = CapabilityManager.get(new CapabilityToken<>() {});

    private PlayerSpellData spellsData = null;
    private final LazyOptional<PlayerSpellData> optional = LazyOptional.of(this::createSpellsData);

    private PlayerSpellData createSpellsData() {
        if (this.spellsData == null) {
            this.spellsData = new PlayerSpellData();
        }
        return this.spellsData;
    }

    @NotNull
    @Override
    public <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        if (cap == PLAYER_SPELLS) {
            return optional.cast();
        }
        return LazyOptional.empty();
    }

    @Override
    public CompoundTag serializeNBT() {
        CompoundTag nbt = new CompoundTag();
        createSpellsData().saveNBTData(nbt);
        return nbt;
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        createSpellsData().loadNBTData(nbt);
    }
}
