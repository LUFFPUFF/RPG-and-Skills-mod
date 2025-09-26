package com.nikita.rpgmod.level.tracker;

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

public class MobDamageTrackerProvider implements ICapabilityProvider, INBTSerializable<CompoundTag> {

    public static Capability<MobDamageTracker> MOB_DAMAGE_TRACKER = CapabilityManager.get(new CapabilityToken<>() {});

    private MobDamageTracker tracker = null;

    private final LazyOptional<MobDamageTracker> optional = LazyOptional.of(this::createTracker);

    private MobDamageTracker createTracker() {
        if (this.tracker == null) {
            this.tracker = new MobDamageTracker();
        }
        return this.tracker;
    }


    @NotNull
    @Override
    public <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        if (cap == MOB_DAMAGE_TRACKER) {
            return optional.cast();
        }
        return LazyOptional.empty();
    }

    @Override
    public CompoundTag serializeNBT() {
        CompoundTag nbt = new CompoundTag();
        createTracker().saveNBTData(nbt);
        return nbt;
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        createTracker().loadNBTData(nbt);
    }
}
