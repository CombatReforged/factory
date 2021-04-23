package com.combatreforged.factory.builder.implementation.world.entity;

import com.combatreforged.factory.api.world.effect.StatusEffect;
import com.combatreforged.factory.api.world.effect.StatusEffectInstance;
import com.combatreforged.factory.api.world.entity.LivingEntity;
import com.combatreforged.factory.builder.exception.WrappingException;
import com.combatreforged.factory.builder.extension.wrap.Wrap;
import com.combatreforged.factory.builder.implementation.world.effect.WrappedStatusEffectInstance;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;

import java.util.ArrayList;
import java.util.List;

public class WrappedLivingEntity extends WrappedEntity implements LivingEntity {
    private final net.minecraft.world.entity.LivingEntity wrappedLiving;

    public WrappedLivingEntity(net.minecraft.world.entity.LivingEntity wrappedLiving) {
        super(wrappedLiving);
        this.wrappedLiving = wrappedLiving;
    }

    @Override
    public net.minecraft.world.entity.LivingEntity unwrap() {
        return wrappedLiving;
    }

    @Override
    public float getHealth() {
        return wrappedLiving.getHealth();
    }

    @Override
    public void setHealth(float health) {
        wrappedLiving.setHealth(health);
    }

    @Override
    public int getInvulnerabilityTime() {
        return wrappedLiving.invulnerableTime;
    }

    @Override
    public void setInvulnerabilityTime(int ticks) {
        wrappedLiving.invulnerableTime = ticks;
    }

    @Override
    public void damage(float amount) {
        wrappedLiving.hurt(DamageSource.GENERIC, amount);
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<StatusEffectInstance> getActiveEffects() {
        List<StatusEffectInstance> effectInstances = new ArrayList<>();

        for (MobEffectInstance vanillaInstance : wrappedLiving.getActiveEffects()) {
            try {
                effectInstances.add(((Wrap<StatusEffectInstance>) vanillaInstance).wrap());
            } catch (ClassCastException e) {
                throw new WrappingException("Unable to wrap StatusEffectInstance!");
            }
        }

        return effectInstances;
    }

    @Override
    public void addEffectInstance(StatusEffectInstance effectInstance) {
        wrappedLiving.addEffect(new MobEffectInstance(WrappedStatusEffectInstance.convert(effectInstance.getStatusEffect()),
                effectInstance.getTicksLeft(), effectInstance.getAmplifier(), effectInstance.isAmbient(), effectInstance.isAmbient()));
    }

    @Override
    public void removeEffect(StatusEffect statusEffect) {
        wrappedLiving.removeEffect(WrappedStatusEffectInstance.convert(statusEffect));
    }
}
