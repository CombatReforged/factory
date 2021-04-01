package com.combatreforged.factory.api.world.item.container;

import com.combatreforged.factory.api.world.item.ItemStack;

public interface PlayerInventory extends Container {
    boolean addItemStack(ItemStack itemStack);
    boolean hasSpaceFor(ItemStack itemStack);
    void removeItemStack(ItemStack itemStack);
}
