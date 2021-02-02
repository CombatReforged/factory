package com.combatreforged.factory.builder.implementation.world.entity;

import com.combatreforged.factory.api.world.effect.StatusEffect;
import com.combatreforged.factory.api.world.effect.StatusEffectInstance;
import com.combatreforged.factory.api.world.entity.LivingEntity;
import com.combatreforged.factory.builder.FactoryBuilder;
import com.combatreforged.factory.builder.extension.wrap.Wrap;
import com.combatreforged.factory.builder.implementation.world.effect.WrappedStatusEffectInstance;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;

import java.util.ArrayList;
import java.util.List;

public class WrappedLivingEntity extends WrappedEntity<LivingEntity> implements LivingEntity {
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
    public void damage(float amount) {
        wrapped.hurt(DamageSource.GENERIC, amount);
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<StatusEffectInstance> getActiveEffects() {
        List<StatusEffectInstance> effectInstances = new ArrayList<>();

        for (MobEffectInstance vanillaInstance : wrapped.getActiveEffects()) {
            try {
                effectInstances.add(((Wrap<StatusEffectInstance>) vanillaInstance).wrap());
            } catch (ClassCastException e) {
                FactoryBuilder.LOGGER.error("Unable to wrap StatusEffectInstance!");
                FactoryBuilder.LOGGER.error(e);
            }
        }

        return effectInstances;
    }

    @Override
    public void addEffectInstance(StatusEffectInstance.Settings effectSettings) {
        wrapped.addEffect(new MobEffectInstance(WrappedStatusEffectInstance.convert(effectSettings.getStatusEffect()),
                effectSettings.getDuration(), effectSettings.getAmplifier(), effectSettings.isAmbient(), effectSettings.isAmbient()));
    }

    @Override
    public void removeEffect(StatusEffect statusEffect) {
        wrapped.removeEffect(WrappedStatusEffectInstance.convert(statusEffect));
    }
}
