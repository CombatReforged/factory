package com.combatreforged.factory.api.world.item.container;

import com.combatreforged.factory.api.world.item.ItemStack;
import com.combatreforged.factory.api.world.item.ItemType;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public interface Container {
    int getSize();
    @Nullable ItemStack getItemStack(int slot);
    void setItemStack(int slot, @Nullable ItemStack itemStack);
    void clear();
    int count(ItemType itemType);
    default boolean contains(ItemType[] itemTypes) {
        return contains(new HashSet<>(Arrays.asList(itemTypes)));
    }
    boolean contains(Set<ItemType> itemTypes);
    List<Integer> getAvailableSlots();
}
