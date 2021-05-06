package com.combatreforged.factory.builder.implementation.world.item;

import com.combatreforged.factory.api.world.item.ItemType;
import com.combatreforged.factory.api.world.item.ItemStack;
import com.combatreforged.factory.builder.implementation.Wrapped;
import com.combatreforged.factory.builder.implementation.util.Conversion;
import com.google.gson.JsonParseException;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.kyori.adventure.nbt.api.BinaryTagHolder;
import net.kyori.adventure.text.Component;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.TagParser;
import net.minecraft.network.chat.MutableComponent;

import java.util.ArrayList;
import java.util.Arrays;
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
    public ItemType getItem() {
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
        if (!wrapped.hasTag()) return returnValue;
        assert wrapped.getTag() != null;

        CompoundTag displayTag = wrapped.getTag().contains("display", 10) ? wrapped.getTag().getCompound("display") : null;
        if (displayTag != null && displayTag.getTagType("Lore") == 9) {
            ListTag listTag = displayTag.getList("Lore", 8);

            for(int i = 0; i < listTag.size(); ++i) {
                String string = listTag.getString(i);

                try {
                    MutableComponent component = net.minecraft.network.chat.Component.Serializer.fromJson(string);
                    if (component != null) {
                        returnValue.add(Conversion.convertComponent(component));
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
        setLore(Arrays.asList(lore));
    }

    @Override
    public void setLore(List<Component> lore) {
        CompoundTag tag = wrapped.getOrCreateTag();
        CompoundTag display = wrapped.getOrCreateTagElement("display");

        ListTag loreArray = new ListTag();

        for (Component component : lore) {
            try {
                loreArray.add(TagParser.parseTag(Conversion.convertComponent(component).toString()));
            } catch (CommandSyntaxException e) {
                throw new UnsupportedOperationException("Component " + lore.indexOf(component) + " is invalid");
            }
        }

        display.put("Lore", loreArray);
        tag.put("display", display);
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
