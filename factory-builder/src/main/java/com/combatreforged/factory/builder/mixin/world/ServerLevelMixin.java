package com.combatreforged.factory.builder.mixin.world;

import com.combatreforged.factory.api.world.World;
import com.combatreforged.factory.builder.extension.wrap.Wrap;
import com.combatreforged.factory.builder.implementation.world.WrappedWorld;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.level.storage.WritableLevelData;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.function.Supplier;

@Mixin(ServerLevel.class)
public abstract class ServerLevelMixin extends Level implements WorldGenLevel, Wrap<World> {
    private World wrapped;

    protected ServerLevelMixin(WritableLevelData writableLevelData, ResourceKey<Level> resourceKey, DimensionType dimensionType, Supplier<ProfilerFiller> supplier, boolean bl, boolean bl2, long l) {
        super(writableLevelData, resourceKey, dimensionType, supplier, bl, bl2, l);
    }

    @Inject(method = "<init>*", at = @At("TAIL"))
    public void injectWrapped(CallbackInfo ci) {
        wrapped = new WrappedWorld((ServerLevel) (Object) this);
    }

    @Override
    public World wrap() {
        return wrapped;
    }
}
