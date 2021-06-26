package com.combatreforged.factory.builder.implementation.world.item.container.menu;

import com.combatreforged.factory.api.world.item.ItemStack;
import com.combatreforged.factory.api.world.item.container.menu.ContainerMenu;
import com.combatreforged.factory.api.world.item.container.menu.ContainerMenuType;
import com.combatreforged.factory.builder.implementation.Wrapped;
import com.combatreforged.factory.builder.implementation.world.item.WrappedItemStack;
import com.combatreforged.factory.builder.mixin.world.inventory.AbstractContainerMenuAccessor;
import com.google.common.collect.ImmutableMap;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.DataSlot;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

public class WrappedContainerMenu extends Wrapped<AbstractContainerMenu> implements ContainerMenu {
    private final ContainerMenuType containerMenuType;

    public WrappedContainerMenu(AbstractContainerMenu wrapped, ContainerMenuType type) {
        super(wrapped);
        this.containerMenuType = type;
    }

    @Override
    public ContainerMenuType getType() {
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

    @Override @Nullable
    public ItemStack getItem(int slot) {
        net.minecraft.world.item.ItemStack mcStack = wrapped.getSlot(slot).getItem();
        return Wrapped.wrap(mcStack.isEmpty() ? null : mcStack, WrappedItemStack.class);
    }

    @Override
    public void setItem(int slot, @Nullable ItemStack itemStack) {
        wrapped.setItem(slot, itemStack == null ? net.minecraft.world.item.ItemStack.EMPTY : ((WrappedItemStack) itemStack).unwrap());
    }

    @Override
    public int getData(int dataSlot) {
        return ((AbstractContainerMenuAccessor) wrapped).getDataSlots().get(dataSlot).get();
    }

    @Override
    public void setData(int dataSlot, int data) {
        wrapped.setData(dataSlot, data);
    }

    @Override
    public boolean isEmpty(int slot) {
        return wrapped.getSlot(slot).getItem().isEmpty();
    }
}
