package com.combatreforged.factory.builder.extension.server;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.LevelSettings;
import net.minecraft.world.level.levelgen.WorldGenSettings;
import net.minecraft.world.level.storage.LevelStorageSource;
import net.minecraft.world.level.storage.WorldData;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.util.List;

public interface MinecraftServerExtension {
    ServerLevel getOverworldForLevel(ServerLevel serverLevel);
    List<ServerLevel> getRelatedLevels(ServerLevel serverLevel);

    boolean hasLevel(String name);

    void addLevel(LevelStorageSource.LevelStorageAccess access);
    void addLevel(WorldData worldData, LevelStorageSource.LevelStorageAccess access);

    @ApiStatus.Experimental
    void addLevel(LevelStorageSource.LevelStorageAccess access, @Nullable String name);

    void addLevel(LevelSettings settings, WorldGenSettings genSettings, LevelStorageSource.LevelStorageAccess access);

    void saveLevel(String name);

    void unloadLevel(String name, boolean save) throws IOException;
}
