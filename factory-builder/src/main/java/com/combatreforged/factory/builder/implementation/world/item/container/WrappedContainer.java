package com.combatreforged.factory.builder.implementation.world.item.container;

import com.combatreforged.factory.api.world.item.ItemStack;
import com.combatreforged.factory.api.world.item.ItemType;
import com.combatreforged.factory.api.world.item.container.Container;
import com.combatreforged.factory.builder.implementation.Wrapped;
import com.combatreforged.factory.builder.implementation.util.Conversion;
import com.combatreforged.factory.builder.implementation.world.item.WrappedItemStack;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public abstract class WrappedContainer extends Wrapped<net.minecraft.world.Container> implements Container {
    public WrappedContainer(net.minecraft.world.Container wrapped) {
        super(wrapped);
    }

    @Override
    public int getSize() {
        return wrapped.getContainerSize();
    }

    @Override
    public @Nullable ItemStack getItemStack(int slot) {
        return Wrapped.wrap(wrapped.getItem(slot), WrappedItemStack.class);
    }

    @Override
    public void setItemStack(int slot, @Nullable ItemStack itemStack) {
        wrapped.setItem(slot, itemStack != null ? ((WrappedItemStack) itemStack).unwrap() : net.minecraft.world.item.ItemStack.EMPTY);
    }

    @Override
    public void clear() {
        wrapped.clearContent();
    }

    @Override
    public int count(ItemType itemType) {
        return wrapped.countItem(Conversion.ITEMS.get(itemType));
    }

    @Override
    public boolean contains(Set<ItemType> itemTypes) {
        return wrapped.hasAnyOf(itemTypes
                .stream()
                .map(Conversion.ITEMS::get)
                .collect(Collectors.toSet()));
    }

    @Override
    public abstract List<Integer> getAvailableSlots();
}
