package com.combatreforged.factory.api.world.item;

import com.combatreforged.factory.api.builder.Builder;
import com.combatreforged.factory.api.world.nbt.NBTObject;
import net.kyori.adventure.nbt.api.BinaryTagHolder;
import net.kyori.adventure.text.Component;

import javax.annotation.Nullable;
import java.util.List;

public interface ItemStack {
    int getCount();
    void setCount(int count);
    int getDamage();
    void setDamage(int damage);
    ItemType getItem();
    boolean isFull();
    void decrementCount();
    int getRepairCost();
    void setRepairCost(int cost);

    Component getDisplayName();
    void setDisplayName(Component displayName);
    List<Component> getLore();
    void setLore(Component... lore);
    void setLore(List<Component> lore);

    @Deprecated BinaryTagHolder getItemData();
    @Deprecated void setItemData(BinaryTagHolder tag);

    NBTObject getItemNBT();
    void setItemNBT(NBTObject nbt);

    static ItemStack create(ItemType itemType) {
        return create(itemType, 1, 0, (NBTObject) null);
    }

    static ItemStack create(ItemType itemType, int count) {
        return create(itemType, count, 0, (NBTObject) null);
    }

    static ItemStack create(ItemType itemType, int count, int damage) {
        return create(itemType, count, damage, (NBTObject) null);
    }

    @Deprecated static ItemStack create(ItemType itemType, int count, int damage, BinaryTagHolder tag) {
        return Builder.getInstance().createItemStack(itemType, count, damage, tag);
    }

    static ItemStack create(ItemType itemType, int count, int damage, @Nullable NBTObject nbt) {
        return Builder.getInstance().createItemStack(itemType, count, damage, nbt);
    }
}
