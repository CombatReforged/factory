package com.combatreforged.factory.api.world.item.container.menu;

import com.combatreforged.factory.api.world.item.ItemStack;

import java.util.List;
import java.util.Map;

public interface ContainerMenu {
    Map<Integer, ItemStack> getItems();
    ItemStack getItem(int slot);
    void setItem(int slot, ItemStack itemStack);
    boolean isEmpty(int slot); //TODO continue
}
