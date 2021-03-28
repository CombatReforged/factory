package com.combatreforged.factory.api.builder;

import com.combatreforged.factory.api.FactoryAPI;
import com.combatreforged.factory.api.world.damage.DamageData;
import com.combatreforged.factory.api.world.effect.StatusEffect;
import com.combatreforged.factory.api.world.effect.StatusEffectInstance;
import com.combatreforged.factory.api.world.entity.Entity;
import com.combatreforged.factory.api.world.item.Item;
import com.combatreforged.factory.api.world.item.ItemStack;
import net.kyori.adventure.nbt.api.BinaryTagHolder;

public interface Builder {
    StatusEffectInstance createStatusEffectInstance(StatusEffect statusEffect, int duration,
                                                    int amplifier, boolean ambient);

    DamageData createDamageData(DamageData.Type type, Entity entityCause, boolean isIndirect);

    ItemStack createItemStack(Item item, int count, int damage, BinaryTagHolder tag);

    static Builder getInstance() {
        return FactoryAPI.getInstance().getBuilder();
    }
}
