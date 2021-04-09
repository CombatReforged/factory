package com.combatreforged.factory.api.world.item.container;

import com.combatreforged.factory.api.world.item.ItemStack;

import java.util.HashMap;
import java.util.Map;

public interface PlayerInventory extends Container {
    boolean addItemStack(ItemStack itemStack);
    boolean hasSpaceFor(ItemStack itemStack);
    void removeItemStack(ItemStack itemStack);
    void setItemStack(Slot slot, ItemStack itemStack);
    ItemStack getItemStack(Slot slot);
    Map<Integer, ItemStack> getInventoryContents();
    Map<Slot.ArmorSlot, ItemStack> getArmorContents();
    ItemStack getOffhand();

    interface Slot {
        Slot OFFHAND = () -> -106;

        int id();

        enum ArmorSlot implements Slot {
            HEAD(103), CHEST(102), LEGS(101), FEET(100);

            private final int id;
            ArmorSlot(int id) {
                this.id = id;
            }

            @Override
            public int id() {
                return id;
            }

            public static ArmorSlot of(int id) {
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
        }

        class InventorySlot implements Slot {
            private static final Map<Integer, InventorySlot> slotMap = new HashMap<>();

            private final int id;

            private InventorySlot(int id) {
                this.id = id;
            }

            public static InventorySlot of(int id) {
                if (id < 0 || id > 35) throw new UnsupportedOperationException("Slot id out of range [0; 35]");
                if (!slotMap.containsKey(id)) {
                    InventorySlot slot = new InventorySlot(id);
                    slotMap.put(id, slot);
                    return slot;
                }
                else {
                    return slotMap.get(id);
                }
            }

            @Override
            public int id() {
                return id;
            }
        }

    }
}
