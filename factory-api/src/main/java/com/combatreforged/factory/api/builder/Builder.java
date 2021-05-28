package com.combatreforged.factory.api.builder;

import com.combatreforged.factory.api.FactoryAPI;
import com.combatreforged.factory.api.util.ImplementationUtils;
import com.combatreforged.factory.api.world.World;
import com.combatreforged.factory.api.world.damage.DamageData;
import com.combatreforged.factory.api.world.effect.StatusEffect;
import com.combatreforged.factory.api.world.effect.StatusEffectInstance;
import com.combatreforged.factory.api.world.entity.Entity;
import com.combatreforged.factory.api.world.entity.EntityType;
import com.combatreforged.factory.api.world.item.ItemStack;
import com.combatreforged.factory.api.world.item.ItemType;
import com.combatreforged.factory.api.world.nbt.NBTList;
import com.combatreforged.factory.api.world.nbt.NBTObject;
import com.combatreforged.factory.api.world.nbt.NBTValue;
import net.kyori.adventure.nbt.api.BinaryTagHolder;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Map;

public interface Builder {
    ImplementationUtils createImplementationUtils();

    Entity createEntity(EntityType type, World world);

    NBTObject createNBTObject(@Nullable Map<String, NBTValue> values);
    NBTObject createNBTObjectFromString(String string);

    NBTList createNBTList(List<NBTValue> values);

    NBTValue createNBTValue(short s);
    NBTValue createNBTValue(double d);
    NBTValue createNBTValue(float f);
    NBTValue createNBTValue(byte b);
    NBTValue createNBTValue(int i);
    NBTValue createNBTValue(long l);
    NBTValue createNBTValue(long[] arr);
    NBTValue createNBTValue(byte[] arr);
    NBTValue createNBTValue(int[] arr);
    NBTValue createNBTValue(String string);

    StatusEffectInstance createStatusEffectInstance(StatusEffect statusEffect, int duration,
                                                    int amplifier, boolean ambient);

    DamageData createDamageData(DamageData.Type type, Entity entityCause, boolean isIndirect);

    @Deprecated ItemStack createItemStack(ItemType itemType, int count, int damage, BinaryTagHolder tag);
    ItemStack createItemStack(ItemType itemType, int count, int damage, NBTObject nbt);

    static Builder getInstance() {
        return FactoryAPI.getInstance().getBuilder();
    }
}
