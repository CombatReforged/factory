package com.combatreforged.factory.builder.mixin.world.level.block.entity;

import com.combatreforged.factory.api.world.item.container.BlockEntityContainer;
import com.combatreforged.factory.builder.extension.wrap.Wrap;
import com.combatreforged.factory.builder.implementation.world.item.container.WrappedBlockEntityContainer;
import net.minecraft.world.level.block.entity.BaseContainerBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BaseContainerBlockEntity.class)
public class BaseContainerBlockEntityMixin implements Wrap<BlockEntityContainer> {
    private WrappedBlockEntityContainer wrapped;

    @Inject(method = "<init>", at = @At("TAIL"))
    public void injectWrapped(BlockEntityType<?> blockEntityType, CallbackInfo ci) {
        this.wrapped = new WrappedBlockEntityContainer((BaseContainerBlockEntity) (Object) this);
    }

    @Override
    public BlockEntityContainer wrap() {
        return wrapped;
    }
}
