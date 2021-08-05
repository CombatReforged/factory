package com.combatreforged.factory.builder.implementation.world.block;

import com.combatreforged.factory.api.world.util.Location;
import net.minecraft.world.level.block.state.BlockState;

public class WrappedBlockState extends WrappedBlock implements com.combatreforged.factory.api.world.block.BlockState {
    private BlockState blockState;

    public WrappedBlockState(Location location, BlockState blockState) {
        super(location);
        this.blockState = blockState;
    }

    @Override
    public void update(BlockState state) {
        this.blockState = state;
    }

    @Override
    public BlockState state() {
        return this.blockState;
    }
}
