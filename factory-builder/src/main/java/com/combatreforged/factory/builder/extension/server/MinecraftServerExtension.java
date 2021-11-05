package com.combatreforged.factory.builder.extension.server;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.storage.LevelStorageSource;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface MinecraftServerExtension {
    ServerLevel getOverworldForLevel(ServerLevel serverLevel);
    List<ServerLevel> getRelatedLevels(ServerLevel serverLevel);

    void loadDynamicWorldSync(@Nullable String name, LevelStorageSource.LevelStorageAccess access);
    CompletableFuture<Void> loadDynamicWorldAsync(@Nullable String name, LevelStorageSource.LevelStorageAccess access);

    boolean hasDynamicWorld(String name);

    void saveDynamicWorld(String name);

    void unloadDynamicWorld(String name, boolean save) throws IOException;
}
