package com.combatreforged.factory.builder.extension.server;

import net.minecraft.world.level.LevelSettings;
import net.minecraft.world.level.levelgen.WorldGenSettings;
import net.minecraft.world.level.storage.LevelStorageSource;
import net.minecraft.world.level.storage.WorldData;

public interface MinecraftServerExtension {
    boolean hasLevel(String name);

    void addLevel(LevelStorageSource.LevelStorageAccess access);
    void addLevel(WorldData worldData, LevelStorageSource.LevelStorageAccess access);
    void addLevel(LevelSettings settings, WorldGenSettings genSettings, LevelStorageSource.LevelStorageAccess access);

    void unloadLevel(String name);
}
