package com.combatreforged.factory.api.world.item.container;

import com.combatreforged.factory.api.world.item.Item;
import com.combatreforged.factory.api.world.item.ItemStack;
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
    int count(Item item);
    default boolean contains(Item[] items) {
        return contains(new HashSet<>(Arrays.asList(items)));
    }
    boolean contains(Set<Item> items);
}
