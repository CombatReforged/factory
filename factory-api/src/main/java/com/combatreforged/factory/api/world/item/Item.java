package com.combatreforged.factory.api.world.item;

import com.combatreforged.factory.api.world.block.BlockType;

public interface Item {
    boolean isBlockItem();
    BlockType getBlock();
    int getMaxStackSize();
    int getMaxDamage();
}
