package com.combatreforged.factory.builder.implementation.world.item.container;

import com.combatreforged.factory.api.world.item.ItemStack;
import com.combatreforged.factory.api.world.item.container.PlayerInventory;
import com.combatreforged.factory.builder.implementation.world.item.WrappedItemStack;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;

public class WrappedPlayerInventory extends WrappedContainer implements PlayerInventory {
    private final Inventory wrapped;
    public WrappedPlayerInventory(Inventory wrapped) {
        super(wrapped);
        this.wrapped = wrapped;
    }

    @Override
    public boolean addItemStack(ItemStack itemStack) {
        return wrapped.add(((WrappedItemStack) itemStack).unwrap());
    }

    @Override
    public boolean hasSpaceFor(ItemStack itemStack) {
        return wrapped.getSlotWithRemainingSpace(((WrappedItemStack) itemStack).unwrap()) != -1;
    }

    @Override
    public void removeItemStack(ItemStack itemStack) {
        wrapped.removeItem(((WrappedItemStack) itemStack).unwrap());
    }
}
