package com.combatreforged.factory.builder.implementation.world.block;

import com.combatreforged.factory.api.world.block.Block;
import com.combatreforged.factory.builder.implementation.Wrapped;
import net.kyori.adventure.nbt.api.BinaryTagHolder;
import net.minecraft.world.level.block.entity.BlockEntity;

public class WrappedBlockEntity extends Wrapped<BlockEntity> implements com.combatreforged.factory.api.world.block.BlockEntity {
    public WrappedBlockEntity(BlockEntity wrapped) {
        super(wrapped);
    }

    @Override
    public Block getBlock() {
        return Wrapped.wrap(wrapped.getBlockState(), WrappedBlock.class);
    }

    @Override
    public BinaryTagHolder getBlockData() {
        return BinaryTagHolder.of(wrapped.getUpdateTag().getAsString());
    }
}
