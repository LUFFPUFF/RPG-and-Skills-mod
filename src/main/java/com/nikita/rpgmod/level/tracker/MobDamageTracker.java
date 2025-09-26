package com.nikita.rpgmod.level.tracker;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class MobDamageTracker {

    private final Map<UUID, Float> damageMap = new HashMap<>();

    public void addDamage(Player player, float amount) {
        damageMap.merge(player.getUUID(), amount, Float::sum);
    }

    public Map<UUID, Float> getDamageMap() {
        return damageMap;
    }

    public void saveNBTData(CompoundTag nbt) {
        CompoundTag damages = new CompoundTag();
        damageMap.forEach((uuid, damage) -> damages.putFloat(uuid.toString(), damage));
        nbt.put("damageMap", damages);
    }

    public void loadNBTData(CompoundTag nbt) {
        if (nbt.contains("damageMap")) {
            CompoundTag damages = nbt.getCompound("damageMap");
            for (String key : damages.getAllKeys()) {
                damageMap.put(UUID.fromString(key), damages.getFloat(key));
            }
        }
    }
}
