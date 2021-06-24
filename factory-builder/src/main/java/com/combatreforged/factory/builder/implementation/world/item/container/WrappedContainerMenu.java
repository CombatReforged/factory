package com.combatreforged.factory.builder.implementation.world.item.container;

import com.combatreforged.factory.api.world.item.ItemStack;
import com.combatreforged.factory.api.world.item.container.ContainerMenuType;
import com.combatreforged.factory.api.world.item.container.menu.ContainerMenu;
import com.combatreforged.factory.builder.implementation.Wrapped;
import com.combatreforged.factory.builder.implementation.world.item.WrappedItemStack;
import com.combatreforged.factory.builder.mixin.world.inventory.AbstractContainerMenuAccessor;
import com.google.common.collect.ImmutableMap;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.DataSlot;

import java.util.List;
import java.util.Map;

public class WrappedContainerMenu<T extends ContainerMenuType> extends Wrapped<AbstractContainerMenu> implements ContainerMenu<T> {
    private final T containerMenuType;

    public WrappedContainerMenu(AbstractContainerMenu wrapped, T type) {
        super(wrapped);
        this.containerMenuType = type;
    }

    @Override
    public T getType() {
        return containerMenuType;
    }

    @Override
    public int getSlotCount() {
        return wrapped.slots.size();
    }

    @Override
    public int getDataSlotCount() {
        return ((AbstractContainerMenuAccessor) wrapped).getDataSlots().size();
    }

    @Override
    public int getContainerID() {
        return wrapped.containerId;
    }

    @Override
    public Map<Integer, ItemStack> getItems() {
        ImmutableMap.Builder<Integer, ItemStack> builder = ImmutableMap.builder();
        for (net.minecraft.world.item.ItemStack mcStack : wrapped.getItems()) {
            if (!mcStack.isEmpty()) {
                builder.put(wrapped.getItems().indexOf(mcStack),
                        Wrapped.wrap(mcStack, WrappedItemStack.class));
            }
        }
        return builder.build();
    }

    @Override
    public Map<Integer, Integer> getDataSlots() {
        ImmutableMap.Builder<Integer, Integer> builder = ImmutableMap.builder();
        for (DataSlot slot : ((AbstractContainerMenuAccessor) wrapped).getDataSlots()) {
            builder.put(((AbstractContainerMenuAccessor) wrapped).getDataSlots().indexOf(slot), slot.get());
        }
        return builder.build();
    }

    @Override
    public ItemStack getItem(int slot) {
        net.minecraft.world.item.ItemStack mcStack = wrapped.getSlot(slot).getItem();
        return Wrapped.wrap(mcStack.isEmpty() ? null : mcStack, WrappedItemStack.class);
    }

    @Override
    public void setItem(int slot, ItemStack itemStack) {

    }

    @Override
    public int getData(int dataSlot) {
        return 0;
    }

    @Override
    public void setData(int dataSlot, int data) {

    }

    @Override
    public boolean isEmpty(int slot) {
        return false;
    }
}
