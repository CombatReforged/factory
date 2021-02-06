package com.combatreforged.factory.api.world.entity;

import com.combatreforged.factory.api.world.effect.StatusEffect;
import com.combatreforged.factory.api.world.effect.StatusEffectInstance;

import java.util.List;

public interface LivingEntity extends Entity {
    /**
     * Gets this LivingEntity's health.
     * @return the health of this entity
     */
    float getHealth();

    /**
     * Sets this LivingEntity's health.
     * If the new health is bigger than the old one, the player will regenerate,
     * while if the new health is lower, the LivingEntity will automatically be
     * damaged. The damage type, in that case, will be GENERIC.
     */
    void setHealth(float health);

    int getInvulnerabilityTime();

    void setInvulnerabilityTime(int ticks);


    /**
     * Damages this entity.
     * Default damage type is GENERIC.
     * @param amount the amount of damage to deal
     */
    void damage(float amount);

    /**
     * Get all active effects of this LivingEntity.
     * @return all active StatusEffectInstance s
     */
    List<StatusEffectInstance> getActiveEffects();

    /**
     * Gives this LivingEntity an effect.
     * @param effectSettings the StatusEffectInstance settings to create the StatusEffectInstance with
     */
    void addEffectInstance(StatusEffectInstance.Settings effectSettings);

    /**
     * Removes all instances of a specific StatusEffect
     * @param statusEffect the effect to remove
     */
    void removeEffect(StatusEffect statusEffect);
}
