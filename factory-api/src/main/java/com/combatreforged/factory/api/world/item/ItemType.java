package com.combatreforged.factory.api.world.item;

import com.combatreforged.factory.api.interfaces.Namespaced;
import com.combatreforged.factory.api.interfaces.ObjectMapped;
import com.combatreforged.factory.api.interfaces.StringIdentified;
import com.combatreforged.factory.api.world.block.BlockType;

public interface ItemType extends ObjectMapped, Namespaced {
    boolean isBlockItem();
    BlockType getBlock();
    int getMaxStackSize();
    int getMaxDamage();

    abstract class Other implements ItemType, StringIdentified {}
}
