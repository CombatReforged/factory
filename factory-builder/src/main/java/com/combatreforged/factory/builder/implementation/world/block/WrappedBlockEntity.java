package com.combatreforged.factory.builder.implementation.world.block;

import com.combatreforged.factory.api.world.block.Block;
import com.combatreforged.factory.api.world.util.Location;
import com.combatreforged.factory.builder.implementation.Wrapped;
import com.combatreforged.factory.builder.implementation.world.WrappedWorld;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.kyori.adventure.nbt.api.BinaryTagHolder;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.TagParser;
import net.minecraft.world.level.block.entity.BlockEntity;

public class WrappedBlockEntity extends Wrapped<BlockEntity> implements com.combatreforged.factory.api.world.block.BlockEntity {
    public WrappedBlockEntity(BlockEntity wrapped) {
        super(wrapped);
    }

    @Override
    public Block getBlock() {
        BlockPos pos = wrapped.getBlockPos();
        return new WrappedBlock
                (new Location(pos.getX(), pos.getY(), pos.getZ(), Wrapped.wrap(wrapped.getLevel(), WrappedWorld.class)));
    }

    @Override
    public BinaryTagHolder getBlockData() {
        return BinaryTagHolder.of(wrapped.getUpdateTag().getAsString());
    }

    @Override
    public void setBlockData(BinaryTagHolder tag) {
        try {
            wrapped.load(wrapped.getBlockState(), TagParser.parseTag(tag.toString()));
        } catch (CommandSyntaxException e) {
            throw new UnsupportedOperationException("Tag is invalid");
        }
    }
}
