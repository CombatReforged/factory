package com.combatreforged.factory.builder.implementation.world.item;

import com.combatreforged.factory.api.world.item.Enchantment;
import com.combatreforged.factory.api.world.item.ItemStack;
import com.combatreforged.factory.api.world.item.ItemType;
import com.combatreforged.factory.api.world.nbt.NBTObject;
import com.combatreforged.factory.builder.implementation.Wrapped;
import com.combatreforged.factory.builder.implementation.util.ObjectMappings;
import com.combatreforged.factory.builder.implementation.world.nbt.WrappedNBTObject;
import com.google.common.collect.ImmutableList;
import com.google.gson.JsonParseException;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.kyori.adventure.nbt.api.BinaryTagHolder;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer;
import net.minecraft.ResourceLocationException;
import net.minecraft.core.Registry;
import net.minecraft.nbt.*;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;

import java.util.*;

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
        return ObjectMappings.ITEMS.inverse().get(wrapped.getItem());
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
        return ObjectMappings.convertComponent(wrapped.getDisplayName());
    }

    @Override
    public void setDisplayName(Component displayName) {
        wrapped.setHoverName(ObjectMappings.convertComponent(displayName));
    }

    @Override
    public List<Component> getLore() {
        List<Component> returnValue = new ArrayList<>();
        if (!wrapped.hasTag()) return returnValue;
        assert wrapped.getTag() != null;

        CompoundTag displayTag = wrapped.getTag().contains("display", 10) ? wrapped.getTag().getCompound("display") : null;
        if (displayTag != null && displayTag.getTagType("Lore") == 9) {
            ListTag listTag = displayTag.getList("Lore", 8);

            for (int i = 0; i < listTag.size(); ++i) {
                String string = listTag.getString(i);

                try {
                    MutableComponent component = net.minecraft.network.chat.Component.Serializer.fromJson(string);
                    if (component != null) {
                        returnValue.add(ObjectMappings.convertComponent(component));
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
                loreArray.add(StringTag.valueOf(GsonComponentSerializer.gson().serialize(component)));
            } catch (Exception e) {
                throw new UnsupportedOperationException("Component " + lore.indexOf(component) + " is invalid");
            }
        }

        display.put("Lore", loreArray);
        tag.put("display", display);
    }

    @Override
    public boolean hasEnchantment(Enchantment enchantment) {
        return this.getEnchantments().contains(enchantment);
    }

    @Override
    public int getLevel(Enchantment enchantment) {
        Optional<CompoundTag> enchTagOpt = this.getEnchantmentTags().stream()
                .filter(tag -> enchantment.equals(stringToEnchantment(tag.getString("id"))))
                .findFirst();
        if (enchTagOpt.isPresent()) {
            CompoundTag enchTag = enchTagOpt.get();
            return enchTag.getInt("lvl");
        }
        return 0;
    }

    @Override
    public void enchant(Enchantment enchantment, int level) {
        wrapped.enchant(ObjectMappings.ENCHANTMENTS.get(enchantment), level);
    }

    @Override
    public void removeEnchantment(Enchantment enchantment) {
        ListTag listTag = wrapped.getEnchantmentTags();
        if (listTag.isEmpty()) return;
        List<Tag> toBeRemoved = new ArrayList<>();
        this.getEnchantmentTags().stream()
                .filter(tag -> enchantment.equals(stringToEnchantment(tag.getString("id"))))
                .forEach(toBeRemoved::add);
        listTag.removeAll(toBeRemoved);

        if (wrapped.hasTag()) {
            CompoundTag root = wrapped.getTag();
            assert root != null;
            if (root.contains("Enchantments")) {
                root.put("Enchantments", listTag);
                wrapped.setTag(root);
            }
        }
    }

    private List<CompoundTag> getEnchantmentTags() {
        ListTag listTag = wrapped.getEnchantmentTags();
        if (listTag.isEmpty()) return ImmutableList.of();
        return listTag.stream()
                .filter(tag -> {
                    if (!(tag instanceof CompoundTag)) return false;
                    CompoundTag compoundTag = (CompoundTag) tag;
                    return compoundTag.contains("id")
                            && Registry.ENCHANTMENT.get(new ResourceLocation(compoundTag.getString("id"))) != null
                            && compoundTag.contains("lvl")
                            && compoundTag.getInt("lvl") > 0;
                })
                .map(tag -> (CompoundTag) tag)
                .collect(ImmutableList.toImmutableList());
    }

    @Override
    public List<Enchantment> getEnchantments() {
        return this.getEnchantmentTags()
                .stream()
                .map(tag -> stringToEnchantment(tag.getString("id")))
                .filter(Objects::nonNull)
                .collect(ImmutableList.toImmutableList());
    }

    private Enchantment stringToEnchantment(String s) {
        ResourceLocation loc;
        try {
            loc = new ResourceLocation(s);
        } catch (ResourceLocationException e) {
            return null;
        }
        net.minecraft.world.item.enchantment.Enchantment mcEnchant = Registry.ENCHANTMENT.get(loc);
        if (mcEnchant == null) return null;
        Enchantment enchantment;
        if (ObjectMappings.ENCHANTMENTS.inverse().containsKey(mcEnchant)) {
            enchantment = ObjectMappings.ENCHANTMENTS.inverse().get(mcEnchant);
        } else {
            enchantment = new Enchantment.Other() {
                private final ResourceLocation location = loc;
                private final net.minecraft.world.item.enchantment.Enchantment ench = mcEnchant;

                @Override
                public String getId() {
                    return location.toString();
                }

                @Override
                public boolean isCurse() {
                    return ench.isCurse();
                }

                @Override
                public int getMaxLevel() {
                    return ench.getMaxLevel();
                }

                @Override
                public boolean canBeAppliedTo(ItemStack itemStack) {
                    return ench.canEnchant(((WrappedItemStack) itemStack).unwrap(), true);
                }
            };

        }
        return enchantment;
    }

    @Deprecated
    @Override
    @Nullable
    public BinaryTagHolder getItemData() {
        return wrapped.getTag() != null ? BinaryTagHolder.of(wrapped.getTag().getAsString()) : null;
    }

    @Deprecated
    @Override
    public void setItemData(@Nullable BinaryTagHolder tag) {
        try {
            wrapped.setTag(tag != null ? TagParser.parseTag(tag.toString()) : null);
        } catch (CommandSyntaxException e) {
            throw new UnsupportedOperationException("Tag is invalid");
        }
    }

    @Override
    @Nullable
    public NBTObject getItemNBT() {
        return Wrapped.wrap(wrapped.getTag() != null ? wrapped.getTag().copy() : null, WrappedNBTObject.class);
    }

    @Override
    public void setItemNBT(@Nullable NBTObject nbt) {
        wrapped.setTag(nbt != null ? ((WrappedNBTObject) nbt).unwrap() : null);
    }

    @Override
    public ItemStack copy() {
        return Wrapped.wrap(wrapped.copy(), WrappedItemStack.class);
    }

    public static ItemStack conv(net.minecraft.world.item.ItemStack itemStack) {
        return itemStack.isEmpty() ? null : Wrapped.wrap(itemStack, WrappedItemStack.class);
    }

    public static net.minecraft.world.item.ItemStack conv(ItemStack itemStack) {
        return itemStack == null ? net.minecraft.world.item.ItemStack.EMPTY : ((WrappedItemStack) itemStack).unwrap();
    }
}
