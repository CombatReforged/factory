package com.combatreforged.factory.builder.mixin.world.level.block.state;

import com.combatreforged.factory.builder.extension.wrap.Wrap;
import com.combatreforged.factory.builder.implementation.world.block.WrappedBlock;
import com.google.common.collect.ImmutableMap;
import com.mojang.serialization.MapCodec;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.Property;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BlockState.class)
public abstract class BlockStateMixin extends BlockBehaviour.BlockStateBase implements Wrap<com.combatreforged.factory.api.world.block.Block> {
    private WrappedBlock wrapped;

    protected BlockStateMixin(Block block, ImmutableMap<Property<?>, Comparable<?>> immutableMap, MapCodec<BlockState> mapCodec) {
        super(block, immutableMap, mapCodec);
    }

    @Inject(method = "<init>", at = @At("TAIL"))
    public void injectWrap(Block block, ImmutableMap<Property<?>, Comparable<?>> immutableMap, MapCodec<BlockState> mapCodec, CallbackInfo ci) {
        this.wrapped = new WrappedBlock((BlockState) (Object) this);
    }

    @Override
    public com.combatreforged.factory.api.world.block.Block wrap() {
        return wrapped;
    }
}
