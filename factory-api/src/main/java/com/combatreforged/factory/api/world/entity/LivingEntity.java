package com.combatreforged.factory.api.world.entity;

import com.combatreforged.factory.api.world.effect.StatusEffect;
import com.combatreforged.factory.api.world.effect.StatusEffectInstance;
import com.combatreforged.factory.api.world.damage.DamageData;

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

    /**
     * Damages this entity.
     * @param amount the amount of damage to deal
     * @param damageData information about the damage source etc.
     */
    void damage(float amount, DamageData damageData);

    /**
     * Damages this entity.
     * Default damage type is GENERIC.
     * @param amount the amount of damage to deal
     */
    default void damage(float amount) { damage(amount, DamageData.Builder.create().damageType(DamageData.Type.GENERIC).build()); }

    /**
     * Get all active effects of this LivingEntity.
     * @return all active StatusEffectInstance s
     */
    List<StatusEffectInstance> getActiveEffects();

    /**
     * Gives this LivingEntity an effect.
     * @param statusEffectInstance the effect instance to add
     */
    void addEffectInstance(StatusEffectInstance statusEffectInstance);

    /**
     * Removes an active effect instance.
     * @param statusEffectInstance the effect instance to remove
     */
    void removeEffectInstance(StatusEffectInstance statusEffectInstance);

    /**
     * Removes all instances of a specific StatusEffect
     * @param statusEffect the effect to remove
     */
    void removeEffect(StatusEffect statusEffect);
}
