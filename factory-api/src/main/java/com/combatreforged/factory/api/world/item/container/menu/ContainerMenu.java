package com.combatreforged.factory.api.world.item.container.menu;

import com.combatreforged.factory.api.world.item.ItemStack;
import com.combatreforged.factory.api.world.item.container.ContainerMenuType;

import java.util.List;
import java.util.Map;

public interface ContainerMenu<T extends ContainerMenuType> {
    T getType();
    int getSlotCount();
    int getDataSlotCount();
    int getContainerID();
    Map<Integer, ItemStack> getItems();
    Map<Integer, Integer> getDataSlots();
    ItemStack getItem(int slot);
    void setItem(int slot, ItemStack itemStack);
    int getData(int dataSlot);
    void setData(int dataSlot, int data);
    boolean isEmpty(int slot);
}
