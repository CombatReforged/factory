package com.combatreforged.factory.builder.implementation.world.item.container;

import com.combatreforged.factory.api.world.item.container.CraftingContainer;

import java.util.ArrayList;
import java.util.List;

public class WrappedCraftingContainer extends WrappedContainer implements CraftingContainer {
    private final net.minecraft.world.inventory.CraftingContainer wrappedCrafting;
    public WrappedCraftingContainer(net.minecraft.world.inventory.CraftingContainer wrappedCrafting) {
        super(wrappedCrafting);
        this.wrappedCrafting = wrappedCrafting;
    }

    @Override
    public int getHeight() {
        return wrappedCrafting.getHeight();
    }

    @Override
    public int getWidth() {
        return wrappedCrafting.getWidth();
    }

    @Override
    public List<Integer> getAvailableSlots() {
        List<Integer> ids = new ArrayList<>();
        for (int i = 0; i < wrappedCrafting.getContainerSize(); i++) {
            ids.add(i);
        }

        return ids;
    }

    @Override
    public net.minecraft.world.inventory.CraftingContainer unwrap() {
        return wrappedCrafting;
    }
}
