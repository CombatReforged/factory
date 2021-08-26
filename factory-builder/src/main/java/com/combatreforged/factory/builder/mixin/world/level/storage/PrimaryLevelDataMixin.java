package com.combatreforged.factory.builder.mixin.world.level.storage;

import com.combatreforged.factory.builder.extension.world.level.storage.PrimaryLevelDataExtension;
import net.minecraft.world.level.LevelSettings;
import net.minecraft.world.level.storage.PrimaryLevelData;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(PrimaryLevelData.class)
public abstract class PrimaryLevelDataMixin implements PrimaryLevelDataExtension {
    @Shadow private LevelSettings settings;

    @Override
    public LevelSettings getSettings() {
        return settings;
    }

    @Override
    public void setSettings(LevelSettings settings) {
        this.settings = settings;
    }
}
