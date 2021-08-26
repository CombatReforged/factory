package com.combatreforged.factory.builder.mixin.world.level.storage;

import com.combatreforged.factory.builder.extension.world.level.storage.LevelStorageAccessExtension;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.LevelStorageSource;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(LevelStorageSource.LevelStorageAccess.class)
public abstract class LevelStorageSource$LevelStorageAccessMixin implements LevelStorageAccessExtension {
    private String customLevelId = null;

    @Override
    public void setCustom(String customLevelId) {
        this.customLevelId = customLevelId;
    }

    @ModifyArg(method = "getDimensionPath", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/dimension/DimensionType;getStorageFolder(Lnet/minecraft/resources/ResourceKey;Ljava/io/File;)Ljava/io/File;"))
    public ResourceKey<Level> changeKey(ResourceKey<Level> previous) {
        ResourceLocation location = previous.location();
        if (customLevelId != null && location.getNamespace().startsWith(customLevelId)) {
            return ResourceKey.create(Registry.DIMENSION_REGISTRY, new ResourceLocation(
                    previous.location().getNamespace().substring(customLevelId.length() + 1),
                    previous.location().getPath()));
        } else {
            return previous;
        }
    }
}
