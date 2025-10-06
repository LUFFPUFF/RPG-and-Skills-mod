package com.nikita.rpgmod.level.mob;

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

public class MobLevelProvider implements ICapabilityProvider, INBTSerializable<CompoundTag> {
    public static Capability<MobLevel> MOB_LEVEL = CapabilityManager.get(new CapabilityToken<>() {});

    private MobLevel level = null;
    private final LazyOptional<MobLevel> optional = LazyOptional.of(this::createMobLevel);

    private MobLevel createMobLevel() {
        if (this.level == null) {
            this.level = new MobLevel();
        }
        return this.level;
    }

    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        if (cap == MOB_LEVEL) {
            return optional.cast();
        }

        return LazyOptional.empty();
    }

    @Override
    public CompoundTag serializeNBT() {
        CompoundTag nbt = new CompoundTag();
        createMobLevel().saveNBTData(nbt);
        return nbt;
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        createMobLevel().loadNBTData(nbt);
    }

}
