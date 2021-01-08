package com.combatreforged.factory.builder.implementation.world.entity;

import com.combatreforged.factory.api.world.damage.DamageData;
import com.combatreforged.factory.api.world.effect.StatusEffect;
import com.combatreforged.factory.api.world.effect.StatusEffectInstance;
import com.combatreforged.factory.api.world.entity.LivingEntity;

import java.util.List;

public class WrappedLivingEntity extends WrappedEntity implements LivingEntity {
    private final net.minecraft.world.entity.LivingEntity wrapped;

    public WrappedLivingEntity(net.minecraft.world.entity.LivingEntity entity) {
        super(entity);
        this.wrapped = entity;
    }

    @Override
    public float getHealth() {
        return wrapped.getHealth();
    }

    @Override
    public void setHealth(float health) {
        wrapped.setHealth(health);
    }

    @Override
    public void damage(float amount, DamageData damageData) {

    }

    @Override
    public List<StatusEffectInstance> getActiveEffects() {
        return null;
    }

    @Override
    public void addEffectInstance(StatusEffectInstance statusEffectInstance) {

    }

    @Override
    public void removeEffectInstance(StatusEffectInstance statusEffectInstance) {

    }

    @Override
    public void removeEffect(StatusEffect statusEffect) {

    }
}
