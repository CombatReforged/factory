package com.combatreforged.factory.api.builder;

import com.combatreforged.factory.api.FactoryAPI;
import com.combatreforged.factory.api.world.block.Block;
import com.combatreforged.factory.api.world.damage.DamageData;
import com.combatreforged.factory.api.world.effect.StatusEffect;
import com.combatreforged.factory.api.world.effect.StatusEffectInstance;
import com.combatreforged.factory.api.world.entity.Entity;

public interface Builder {
    StatusEffectInstance createStatusEffectInstance(StatusEffect statusEffect, int duration,
                                                    int amplifier, boolean ambient);

    DamageData createDamageData(DamageData.Type type, Entity entityCause, boolean isIndirect);


    static Builder getInstance() {
        return FactoryAPI.getInstance().getBuilder();
    }
}
