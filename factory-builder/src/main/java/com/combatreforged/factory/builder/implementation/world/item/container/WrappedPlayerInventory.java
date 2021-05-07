package com.combatreforged.factory.builder.implementation.world.item.container;

import com.combatreforged.factory.api.world.item.ItemStack;
import com.combatreforged.factory.api.world.item.container.PlayerInventory;
import com.combatreforged.factory.builder.implementation.Wrapped;
import com.combatreforged.factory.builder.implementation.world.item.WrappedItemStack;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WrappedPlayerInventory extends Wrapped<Inventory> implements PlayerInventory, WrappedContainer {
    public WrappedPlayerInventory(Inventory wrapped) {
        super(wrapped);
    }

    @Override
    public boolean addItemStack(ItemStack itemStack) {
        return wrapped.add(((WrappedItemStack) itemStack).unwrap());
    }

    @Override
    public boolean hasSpaceFor(ItemStack itemStack) {
        return wrapped.getSlotWithRemainingSpace(((WrappedItemStack) itemStack).unwrap()) != -1;
    }

    @Override
    public void removeItemStack(ItemStack itemStack) {
        wrapped.removeItem(((WrappedItemStack) itemStack).unwrap());
    }

    @Override
    public void setItemStack(Slot slot, ItemStack itemStack) {
        setItemStack(slot.id(), itemStack);
    }

    @Override
    public ItemStack getItemStack(Slot slot) {
        return getItemStack(slot.id());
    }

    @Override
    public @Nullable ItemStack getItemStack(int slot) {
        return Wrapped.wrap(wrapped.getItem(transformSlotId(slot)), WrappedItemStack.class);
    }

    @Override
    public void setItemStack(int slot, @Nullable ItemStack itemStack) {
        net.minecraft.world.item.ItemStack stack = itemStack != null
                ? ((WrappedItemStack) itemStack).unwrap()
                : net.minecraft.world.item.ItemStack.EMPTY;
        wrapped.setItem(transformSlotId(slot), stack);
    }

    @Override
    public Map<Integer, ItemStack> getInventoryContents() {
        Map<Integer, ItemStack> map = new HashMap<>();
        for (int i = 0; i < wrapped.items.size(); i++) {
            map.put(i, !wrapped.items.get(i).isEmpty()
                    ? Wrapped.wrap(wrapped.items.get(i), WrappedItemStack.class)
                    : null);
        }

        return map;
    }

    @Override
    public Map<Slot.ArmorSlot, ItemStack> getArmorContents() {
        Map<Slot.ArmorSlot, ItemStack> map = new HashMap<>();
        for (int i = 0; i < wrapped.armor.size(); i++) {
            map.put(Slot.ArmorSlot.of(i + 100), !wrapped.armor.get(i).isEmpty()
                    ? Wrapped.wrap(wrapped.armor.get(i), WrappedItemStack.class)
                    : null);
        }

        return map;
    }

    @Override
    public ItemStack getOffhand() {
        return Wrapped.wrap(wrapped.offhand.get(0), WrappedItemStack.class);
    }

    @Override
    public List<Integer> getAvailableSlots() {
        List<Integer> slots = new ArrayList<>();
        for (int i = 0; i < wrapped.items.size(); i++) {
            slots.add(i);
        }

        return slots;
    }

    @Override
    public Container container() {
        return wrapped;
    }

    private int transformSlotId(int slot) {
        int chosen = slot;
        if (slot >= 100) {
            chosen = slot - 100 + wrapped.items.size();
        }
        else if (slot == Slot.OFFHAND.id()) {
            chosen = wrapped.items.size() + wrapped.armor.size() - 1;
        }

        return chosen;
    }
}
