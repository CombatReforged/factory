package com.combatreforged.factory.builder.implementation.dynamicworld;

import com.google.common.collect.ImmutableList;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.storage.LevelStorageSource;
import net.minecraft.world.level.storage.WorldData;
import org.jetbrains.annotations.ApiStatus;

import java.util.List;

@ApiStatus.Experimental
public class DynamicWorld {
    private boolean loaded;
    private final String levelName;
    private final ServerLevel overworld;
    private final ImmutableList<ServerLevel> dimensions;
    private final LevelStorageSource.LevelStorageAccess source;
    private final WorldData worldData;

    public DynamicWorld(String levelName, ServerLevel overworld, List<ServerLevel> dimensions, LevelStorageSource.LevelStorageAccess source, WorldData worldData) {
        this.levelName = levelName;
        this.overworld = overworld;
        this.dimensions = ImmutableList.copyOf(dimensions);
        this.source = source;
        this.worldData = worldData;

        this.loaded = true;
    }

    public String getLevelName() {
        return levelName;
    }

    public ServerLevel getOverworld() {
        return overworld;
    }

    public ImmutableList<ServerLevel> getDimensions() {
        return dimensions;
    }

    public LevelStorageSource.LevelStorageAccess getSource() {
        return source;
    }

    public WorldData getWorldData() {
        return worldData;
    }

    public boolean isLoaded() {
        return loaded;
    }

    public void unloaded() {
        this.loaded = false;
    }
}
