package com.combatreforged.factory.builder.implementation.world.item.container;

import com.combatreforged.factory.api.exception.UnassignableTypeException;
import com.combatreforged.factory.api.world.entity.equipment.ArmorSlot;
import com.combatreforged.factory.api.world.entity.equipment.EquipmentSlot;
import com.combatreforged.factory.api.world.entity.equipment.HandSlot;
import com.combatreforged.factory.api.world.item.ItemStack;
import com.combatreforged.factory.api.world.item.container.PlayerInventory;
import com.combatreforged.factory.builder.implementation.Wrapped;
import com.combatreforged.factory.builder.implementation.world.item.WrappedItemStack;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.combatreforged.factory.api.world.entity.equipment.ArmorSlot.*;

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
    public void setItemStack(EquipmentSlot slot, ItemStack itemStack) {
        setItemStack(transformToId(slot), itemStack);
    }

    @Override
    public ItemStack getItemStack(EquipmentSlot slot) {
        return getItemStack(transformToId(slot));
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
    public Map<ArmorSlot, ItemStack> getArmorContents() {
        Map<ArmorSlot, ItemStack> map = new HashMap<>();
        for (int i = 0; i < wrapped.armor.size(); i++) {
            map.put(transformToArmorSlot(i + 100), !wrapped.armor.get(i).isEmpty()
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
    public @Nullable ItemStack getCursorStack() {
        if (wrapped.getCarried().isEmpty()) return null;
        return Wrapped.wrap(wrapped.getCarried(), WrappedItemStack.class);
    }

    @Override
    public void setCursorStack(@Nullable ItemStack stack) {
        net.minecraft.world.item.ItemStack mcStack = stack == null ? net.minecraft.world.item.ItemStack.EMPTY : ((WrappedItemStack) stack).unwrap();
        wrapped.setCarried(mcStack);
        if (wrapped.player instanceof ServerPlayer) {
            ((ServerPlayer) wrapped.player).refreshContainer(wrapped.player.inventoryMenu);
        }
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
        else if (slot == -106) {
            chosen = wrapped.items.size() + wrapped.armor.size() - 1;
        }

        return chosen;
    }

    private ArmorSlot transformToArmorSlot(int id) {
        switch (id) {
            case 100:
                return FEET;
            case 101:
                return LEGS;
            case 102:
                return CHEST;
            case 103:
                return HEAD;
            default:
                throw new UnsupportedOperationException("Not an armor slot");
        }
    }

    private int transformToId(EquipmentSlot slot) {
        if (slot instanceof ArmorSlot) {
            ArmorSlot armor = (ArmorSlot) slot;
            switch (armor) {
                case HEAD:
                    return 103;
                case CHEST:
                    return 102;
                case LEGS:
                    return 101;
                case FEET:
                    return 100;
            }
        } else if (slot instanceof HandSlot) {
            HandSlot hand = (HandSlot) slot;
            switch (hand) {
                case MAIN_HAND:
                    return wrapped.selected;
                case OFF_HAND:
                    return -106;
            }
        }
        throw new UnassignableTypeException("Invalid equipment slot");
    }
}
