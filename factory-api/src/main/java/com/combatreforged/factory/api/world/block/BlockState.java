package com.combatreforged.factory.api.world.block;

import com.combatreforged.factory.api.builder.Builder;
import com.combatreforged.factory.api.world.util.Location;

public interface BlockState extends Block {
    static BlockState create(BlockType type, Location location) {
        return Builder.getInstance().createBlockState(type, location);
    }

    static BlockState of(Block block) {
        return Builder.getInstance().blockStateOfBlock(block);
    }
}
