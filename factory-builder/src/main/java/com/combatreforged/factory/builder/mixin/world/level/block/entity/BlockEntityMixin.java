package com.combatreforged.factory.builder.mixin.world.level.block.entity;

import com.combatreforged.factory.builder.extension.wrap.Wrap;
import com.combatreforged.factory.builder.implementation.world.block.WrappedBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BlockEntity.class)
public abstract class BlockEntityMixin implements Wrap<com.combatreforged.factory.api.world.block.BlockEntity> {
    private WrappedBlockEntity wrapped;

    @Inject(method = "<init>*", at = @At("TAIL"))
    public void injectWrap(CallbackInfo ci) {
        this.wrapped = new WrappedBlockEntity((BlockEntity) (Object) this);
    }

    @Override
    public com.combatreforged.factory.api.world.block.BlockEntity wrap() {
        return wrapped;
    }
}
