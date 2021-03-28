package com.combatreforged.factory.builder.implementation.world.item;

import com.combatreforged.factory.api.world.item.Item;
import com.combatreforged.factory.api.world.item.ItemStack;
import com.combatreforged.factory.builder.implementation.Wrapped;
import com.combatreforged.factory.builder.implementation.util.Conversion;
import com.combatreforged.factory.builder.mixin.world.item.ItemStackAccessor;
import com.google.gson.JsonParseException;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.kyori.adventure.nbt.api.BinaryTagHolder;
import net.kyori.adventure.text.Component;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.TagParser;
import net.minecraft.network.chat.ComponentUtils;
import net.minecraft.network.chat.MutableComponent;

import java.util.ArrayList;
import java.util.List;

public class WrappedItemStack extends Wrapped<net.minecraft.world.item.ItemStack> implements ItemStack {
    public WrappedItemStack(net.minecraft.world.item.ItemStack wrapped) {
        super(wrapped);
    }

    @Override
    public int getCount() {
        return wrapped.getCount();
    }

    @Override
    public void setCount(int count) {
        wrapped.setCount(count);
    }

    @Override
    public int getDamage() {
        return wrapped.getDamageValue();
    }

    @Override
    public void setDamage(int damage) {
        wrapped.setDamageValue(damage);
    }

    @Override
    public Item getItem() {
        return Conversion.ITEMS.inverse().get(wrapped.getItem());
    }

    @Override
    public boolean isFull() {
        return wrapped.getCount() >= wrapped.getMaxStackSize();
    }

    @Override
    public void decrementCount() {
        wrapped.setCount(wrapped.getCount() - 1);
    }

    @Override
    public int getRepairCost() {
        return wrapped.getBaseRepairCost();
    }

    @Override
    public void setRepairCost(int cost) {
        wrapped.setRepairCost(cost);
    }

    @Override
    public Component getDisplayName() {
        return Conversion.convertComponent(wrapped.getDisplayName());
    }

    @Override
    public void setDisplayName(Component displayName) {
        wrapped.setHoverName(Conversion.convertComponent(displayName));
    }

    @Override
    public List<Component> getLore() {
        List<Component> returnValue = new ArrayList<>();
        if (wrapped.getTag() != null && wrapped.getTag().contains("Lore")) {
            ListTag listTag = wrapped.getTag().getList("Lore", 8);

            for(int i = 0; i < listTag.size(); ++i) {
                String string = listTag.getString(i);

                try {
                    MutableComponent component = net.minecraft.network.chat.Component.Serializer.fromJson(string);
                    if (component != null) {
                        returnValue.add(Conversion.convertComponent(ComponentUtils.mergeStyles(component, ItemStackAccessor.LORE_STYLE())));
                    }
                } catch (JsonParseException e) {
                    throw new IllegalStateException("Lore is invalid");
                }
            }
        }

        return returnValue;
    }

    @Override
    public void setLore(Component... lore) {
        //TODO
    }

    @Override
    public void setLore(List<Component> lore) {
        //TODO
    }

    @Override
    public BinaryTagHolder getItemData() {
        return BinaryTagHolder.of(wrapped.save(new CompoundTag()).getAsString());
    }

    @Override
    public void setItemData(BinaryTagHolder tag) {
        try {
            wrapped.setTag(TagParser.parseTag(tag.toString()));
        } catch (CommandSyntaxException e) {
            throw new UnsupportedOperationException("Tag is invalid");
        }
    }
}
