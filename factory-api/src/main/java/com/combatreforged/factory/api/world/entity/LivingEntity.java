package com.combatreforged.factory.api.world.entity;

import com.combatreforged.factory.api.world.effect.StatusEffect;
import com.combatreforged.factory.api.world.effect.StatusEffectInstance;

import java.util.List;

public interface LivingEntity extends Entity {
    float getHealth();

    void setHealth(float health);

    int getInvulnerabilityTime();

    void setInvulnerabilityTime(int ticks);

    void damage(float amount);

    List<StatusEffectInstance> getActiveEffects();

    void addEffectInstance(StatusEffectInstance effectInstance);

    void removeEffect(StatusEffect statusEffect);
}
