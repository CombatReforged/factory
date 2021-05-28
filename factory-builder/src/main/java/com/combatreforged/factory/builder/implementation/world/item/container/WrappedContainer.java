package com.combatreforged.factory.builder.implementation.world.item.container;

import com.combatreforged.factory.api.world.item.ItemStack;
import com.combatreforged.factory.api.world.item.ItemType;
import com.combatreforged.factory.api.world.item.container.Container;
import com.combatreforged.factory.builder.implementation.Wrapped;
import com.combatreforged.factory.builder.implementation.util.ObjectMappings;
import com.combatreforged.factory.builder.implementation.world.item.WrappedItemStack;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public interface WrappedContainer extends Container {
    @Override
    default int getSize() {
        return container().getContainerSize();
    }

    @Override
    default @Nullable ItemStack getItemStack(int slot) {
        return Wrapped.wrap(container().getItem(slot), WrappedItemStack.class);
    }

    @Override
    default void setItemStack(int slot, @Nullable ItemStack itemStack) {
        container().setItem(slot, itemStack != null ? ((WrappedItemStack) itemStack).unwrap() : net.minecraft.world.item.ItemStack.EMPTY);
    }

    @Override
    default void clear() {
        container().clearContent();
    }

    @Override
    default int count(ItemType itemType) {
        return container().countItem(ObjectMappings.ITEMS.get(itemType));
    }

    @Override
    default boolean contains(Set<ItemType> itemTypes) {
        return container().hasAnyOf(itemTypes
                .stream()
                .map(ObjectMappings.ITEMS::get)
                .collect(Collectors.toSet()));
    }

    @Override
    List<Integer> getAvailableSlots();

    net.minecraft.world.Container container();
}
