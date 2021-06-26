package com.combatreforged.factory.api.world.item.container.menu;

import com.combatreforged.factory.api.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

public interface ContainerMenu {
    ContainerMenuType getType();
    int getSlotCount();
    int getDataSlotCount();
    int getContainerID();
    Map<Integer, ItemStack> getItems();
    Map<Integer, Integer> getDataSlots();
    @Nullable ItemStack getItem(int slot);
    void setItem(int slot, @Nullable ItemStack itemStack);
    int getData(int dataSlot);
    void setData(int dataSlot, int data);
    boolean isEmpty(int slot);
}
