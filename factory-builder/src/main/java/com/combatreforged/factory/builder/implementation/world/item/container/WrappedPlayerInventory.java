package com.combatreforged.factory.builder.implementation.world.item.container;

import com.combatreforged.factory.api.world.item.ItemStack;
import com.combatreforged.factory.api.world.item.container.PlayerInventory;
import com.combatreforged.factory.builder.implementation.Wrapped;
import com.combatreforged.factory.builder.implementation.world.item.WrappedItemStack;
import net.minecraft.world.entity.player.Inventory;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WrappedPlayerInventory extends WrappedContainer implements PlayerInventory {
    private final Inventory wrappedInventory;
    public WrappedPlayerInventory(Inventory wrappedInventory) {
        super(wrappedInventory);
        this.wrappedInventory = wrappedInventory;
    }

    @Override
    public boolean addItemStack(ItemStack itemStack) {
        return wrappedInventory.add(((WrappedItemStack) itemStack).unwrap());
    }

    @Override
    public boolean hasSpaceFor(ItemStack itemStack) {
        return wrappedInventory.getSlotWithRemainingSpace(((WrappedItemStack) itemStack).unwrap()) != -1;
    }

    @Override
    public void removeItemStack(ItemStack itemStack) {
        wrappedInventory.removeItem(((WrappedItemStack) itemStack).unwrap());
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
        return Wrapped.wrap(wrappedInventory.getItem(transformSlotId(slot)), WrappedItemStack.class);
    }

    @Override
    public void setItemStack(int slot, @Nullable ItemStack itemStack) {
        net.minecraft.world.item.ItemStack stack = itemStack != null
                ? ((WrappedItemStack) itemStack).unwrap()
                : net.minecraft.world.item.ItemStack.EMPTY;
        wrappedInventory.setItem(transformSlotId(slot), stack);
    }

    @Override
    public Map<Integer, ItemStack> getInventoryContents() {
        Map<Integer, ItemStack> map = new HashMap<>();
        for (int i = 0; i < wrappedInventory.items.size(); i++) {
            map.put(i, !wrappedInventory.items.get(i).isEmpty()
                    ? Wrapped.wrap(wrappedInventory.items.get(i), WrappedItemStack.class)
                    : null);
        }

        return map;
    }

    @Override
    public Map<Slot.ArmorSlot, ItemStack> getArmorContents() {
        Map<Slot.ArmorSlot, ItemStack> map = new HashMap<>();
        for (int i = 0; i < wrappedInventory.armor.size(); i++) {
            map.put(Slot.ArmorSlot.of(i + 100), !wrappedInventory.armor.get(i).isEmpty()
                    ? Wrapped.wrap(wrappedInventory.armor.get(i), WrappedItemStack.class)
                    : null);
        }

        return map;
    }

    @Override
    public ItemStack getOffhand() {
        return Wrapped.wrap(wrappedInventory.offhand.get(0), WrappedItemStack.class);
    }

    @Override
    public List<Integer> getAvailableSlots() {
        List<Integer> slots = new ArrayList<>();
        for (int i = 0; i < wrappedInventory.items.size(); i++) {
            slots.add(i);
        }

        return slots;
    }

    private int transformSlotId(int slot) {
        int chosen = slot;
        if (slot >= 100) {
            chosen = slot - 100 + wrappedInventory.items.size();
        }
        else if (slot == Slot.OFFHAND.id()) {
            chosen = wrappedInventory.items.size() + wrappedInventory.armor.size() - 1;
        }

        return chosen;
    }
}
