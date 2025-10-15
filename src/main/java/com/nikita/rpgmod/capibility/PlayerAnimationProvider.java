package com.nikita.rpgmod.capibility;

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

public class PlayerAnimationProvider implements ICapabilityProvider, INBTSerializable<CompoundTag> {
    public static final Capability<PlayerAnimationData> PLAYER_ANIMATION = CapabilityManager.get(new CapabilityToken<>() {});

    private PlayerAnimationData animData = null;
    private final LazyOptional<PlayerAnimationData> optional = LazyOptional.of(this::createAnimData);

    private PlayerAnimationData createAnimData() {
        if (this.animData == null) {
            this.animData = new PlayerAnimationData();
        }
        return this.animData;
    }

    @NotNull
    @Override
    public <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        if (cap == PLAYER_ANIMATION) {
            return optional.cast();
        }
        return LazyOptional.empty();
    }

    @Override
    public CompoundTag serializeNBT() {
        CompoundTag nbt = new CompoundTag();
        createAnimData().saveNBT(nbt);
        return nbt;
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        createAnimData().loadNBT(nbt);
    }
}
