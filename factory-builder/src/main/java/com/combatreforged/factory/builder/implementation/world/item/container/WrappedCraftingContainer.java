package com.combatreforged.factory.builder.implementation.world.item.container;

import com.combatreforged.factory.api.world.item.container.CraftingContainer;
import com.combatreforged.factory.builder.implementation.Wrapped;
import net.minecraft.world.Container;

import java.util.ArrayList;
import java.util.List;

public class WrappedCraftingContainer extends Wrapped<net.minecraft.world.inventory.CraftingContainer> implements CraftingContainer, WrappedContainer {
    public WrappedCraftingContainer(net.minecraft.world.inventory.CraftingContainer wrapped) {
        super(wrapped);
    }

    @Override
    public int getHeight() {
        return wrapped.getHeight();
    }

    @Override
    public int getWidth() {
        return wrapped.getWidth();
    }

    @Override
    public List<Integer> getAvailableSlots() {
        List<Integer> ids = new ArrayList<>();
        for (int i = 0; i < wrapped.getContainerSize(); i++) {
            ids.add(i);
        }

        return ids;
    }

    @Override
    public Container container() {
        return wrapped;
    }

    @Override
    public net.minecraft.world.inventory.CraftingContainer unwrap() {
        return wrapped;
    }
}
