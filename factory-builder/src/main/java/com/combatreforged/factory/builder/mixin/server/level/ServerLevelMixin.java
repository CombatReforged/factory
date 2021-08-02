package com.combatreforged.factory.builder.mixin.server.level;

import com.combatreforged.factory.api.event.world.WorldSpawnEntityEvent;
import com.combatreforged.factory.api.world.World;
import com.combatreforged.factory.builder.extension.wrap.Wrap;
import com.combatreforged.factory.builder.implementation.Wrapped;
import com.combatreforged.factory.builder.implementation.world.WrappedWorld;
import com.combatreforged.factory.builder.implementation.world.entity.WrappedEntity;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.level.storage.WritableLevelData;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

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

    @Unique private WorldSpawnEntityEvent spawnEntityEvent;
    @Inject(method = "addEntity", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/chunk/ChunkAccess;addEntity(Lnet/minecraft/world/entity/Entity;)V"), cancellable = true)
    public void injectSpawnEntityEvent(Entity entity, CallbackInfoReturnable<Boolean> cir) {
        if (!(entity instanceof ServerPlayer)) {
            com.combatreforged.factory.api.world.entity.Entity apiEntity = Wrapped.wrap(entity, WrappedEntity.class);
            this.spawnEntityEvent = new WorldSpawnEntityEvent(this.wrap(), apiEntity, apiEntity.getLocation());
            WorldSpawnEntityEvent.BACKEND.invoke(this.spawnEntityEvent);

            if (spawnEntityEvent.isCancelled()) {
                cir.setReturnValue(false);
            } else if (!spawnEntityEvent.getSpawnLocation().equals(apiEntity.getLocation())) {
                apiEntity.teleport(spawnEntityEvent.getSpawnLocation());
            }
        }
    }

    @Inject(method = "addEntity", at = @At("RETURN"))
    public void nullifySpawnEntityEvent(Entity entity, CallbackInfoReturnable<Boolean> cir) {
        if (spawnEntityEvent != null) {
            WorldSpawnEntityEvent.BACKEND.invokeEndFunctions(spawnEntityEvent);
            this.spawnEntityEvent = null;
        }
    }
}
