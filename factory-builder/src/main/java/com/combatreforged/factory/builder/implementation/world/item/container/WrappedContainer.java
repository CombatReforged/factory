package com.combatreforged.factory.builder.implementation.world.item.container;

import com.combatreforged.factory.api.world.item.Item;
import com.combatreforged.factory.api.world.item.ItemStack;
import com.combatreforged.factory.api.world.item.container.Container;
import com.combatreforged.factory.builder.implementation.Wrapped;
import com.combatreforged.factory.builder.implementation.util.Conversion;
import com.combatreforged.factory.builder.implementation.world.item.WrappedItemStack;
import org.jetbrains.annotations.Nullable;

import java.util.Set;
import java.util.stream.Collectors;

public class WrappedContainer extends Wrapped<net.minecraft.world.Container> implements Container {
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
    public int count(Item item) {
        return wrapped.countItem(Conversion.ITEMS.get(item));
    }

    @Override
    public boolean contains(Set<Item> items) {
        return wrapped.hasAnyOf(items
                .stream()
                .map(Conversion.ITEMS::get)
                .collect(Collectors.toSet()));
    }
}
