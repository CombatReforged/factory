package com.combatreforged.factory.builder.extension.world.level.storage;

import net.minecraft.world.level.LevelSettings;

public interface PrimaryLevelDataExtension {
    LevelSettings getSettings();
    void setSettings(LevelSettings settings);
}
