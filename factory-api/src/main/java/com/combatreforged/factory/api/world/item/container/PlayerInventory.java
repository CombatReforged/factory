package com.combatreforged.factory.api.world.item.container;

import com.combatreforged.factory.api.world.entity.equipment.ArmorSlot;
import com.combatreforged.factory.api.world.entity.equipment.EquipmentSlot;
import com.combatreforged.factory.api.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

public interface PlayerInventory extends Container {
    boolean addItemStack(ItemStack itemStack);
    boolean hasSpaceFor(ItemStack itemStack);
    void removeItemStack(ItemStack itemStack);
    void setItemStack(EquipmentSlot slot, ItemStack itemStack);
    ItemStack getItemStack(EquipmentSlot slot);
    Map<Integer, ItemStack> getInventoryContents();
    Map<ArmorSlot, ItemStack> getArmorContents();
    ItemStack getOffhand();

    @Nullable ItemStack getCursorStack();
    void setCursorStack(@Nullable ItemStack stack);
}
