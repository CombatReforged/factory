package com.combatreforged.factory.api.world.entity;

import com.combatreforged.factory.api.world.effect.StatusEffect;
import com.combatreforged.factory.api.world.effect.StatusEffectInstance;
import com.combatreforged.factory.api.world.entity.equipment.EquipmentSlot;
import com.combatreforged.factory.api.world.item.ItemStack;

import java.util.List;

public interface LivingEntity extends Entity {
    ItemStack getEquipmentStack(EquipmentSlot slot);
    void setEquipmentStack(EquipmentSlot slot, ItemStack itemStack);

    float getHealth();

    void setHealth(float health);

    boolean isDead();

    int getInvulnerabilityTime();

    void setInvulnerabilityTime(int ticks);

    void damage(float amount);

    List<StatusEffectInstance> getActiveEffects();

    void addEffectInstance(StatusEffectInstance effectInstance);

    void removeEffect(StatusEffect statusEffect);

    void clearEffects();

    boolean isSprinting();
    void setSprinting(boolean sprinting);
    boolean isSneaking();
    void setSneaking(boolean sneaking);
    boolean isSwimming();
    void setSwimming(boolean swimming);
}
