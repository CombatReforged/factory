package com.combatreforged.factory.builder.implementation.world.block;

import com.combatreforged.factory.api.world.block.Block;
import com.combatreforged.factory.api.world.nbt.NBTObject;
import com.combatreforged.factory.api.world.util.Location;
import com.combatreforged.factory.builder.implementation.Wrapped;
import com.combatreforged.factory.builder.implementation.world.WrappedWorld;
import com.combatreforged.factory.builder.implementation.world.nbt.WrappedNBTObject;
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

    @Deprecated
    @Override
    public BinaryTagHolder getBlockData() {
        return BinaryTagHolder.of(wrapped.getUpdateTag().getAsString());
    }

    @Deprecated
    @Override
    public void setBlockData(BinaryTagHolder tag) {
        try {
            wrapped.load(wrapped.getBlockState(), TagParser.parseTag(tag.toString()));
        } catch (CommandSyntaxException e) {
            throw new UnsupportedOperationException("Tag is invalid");
        }
    }

    @Override
    public NBTObject getBlockNBT() {
        return Wrapped.wrap(wrapped.getUpdateTag(), WrappedNBTObject.class);
    }

    @Override
    public void setBlockNBT(NBTObject nbt) {
        wrapped.load(wrapped.getBlockState(), ((WrappedNBTObject) nbt).unwrap());
    }
}
