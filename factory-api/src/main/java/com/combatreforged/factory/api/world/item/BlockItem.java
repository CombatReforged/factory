package com.combatreforged.factory.api.world.item;

import com.combatreforged.factory.api.world.block.BlockType;

public class BlockItem extends Item {
    private final BlockType block;
    public BlockItem(BlockType type, int maxStackSize) {
        this(type, maxStackSize, 0);
    }

    public BlockItem(BlockType type, int maxStackSize, int maxDamage) {
        super(maxStackSize, maxDamage);
        this.block = type;
    }

    public BlockType getBlock() {
        return block;
    }
}
