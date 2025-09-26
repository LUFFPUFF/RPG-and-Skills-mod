package com.nikita.rpgmod.magic.stats;

import com.nikita.rpgmod.capibility.PlayerStats;
import com.nikita.rpgmod.capibility.PlayerStatsProvider;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class PlayerMagicProvider implements ICapabilityProvider, INBTSerializable<CompoundTag> {

    public static Capability<PlayerMagicStats> PLAYER_MAGIC = CapabilityManager.get(new CapabilityToken<>() {});

    private PlayerMagicStats magic = null;
    private final LazyOptional<PlayerMagicStats> optional;
    private final Player player;

    public PlayerMagicProvider(Player player) {
        this.player = player;
        this.optional = LazyOptional.of(this::createPlayerMagic);
    }

    private PlayerMagicStats createPlayerMagic() {
        if (this.magic == null) {
            LazyOptional<PlayerStats> statsLazyOptional = player.getCapability(PlayerStatsProvider.PLAYER_STATS);

            PlayerStats stats = statsLazyOptional.orElseThrow(() ->
                    new IllegalStateException("Cannot create PlayerMagicStats: PlayerStats capability is missing!"));

            this.magic = new PlayerMagicStats(stats);

        }
        return this.magic;
    }

    @NotNull
    @Override
    public <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        if (cap == PLAYER_MAGIC) {
            return optional.cast();
        }
        return LazyOptional.empty();
    }

    @Override
    public CompoundTag serializeNBT() {
        CompoundTag nbt = new CompoundTag();
        createPlayerMagic().saveNBTData(nbt);
        return nbt;
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        createPlayerMagic().loadNBTData(nbt);
    }
}
