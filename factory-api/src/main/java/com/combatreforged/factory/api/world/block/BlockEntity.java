package com.combatreforged.factory.api.world.block;

import net.kyori.adventure.nbt.api.BinaryTagHolder;

public interface BlockEntity {
    Block getBlock();
    BinaryTagHolder getBlockData();
    void setBlockData(BinaryTagHolder tag);
}
