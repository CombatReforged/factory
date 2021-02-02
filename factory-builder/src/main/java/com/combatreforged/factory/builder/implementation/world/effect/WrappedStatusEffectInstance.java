package com.combatreforged.factory.builder.implementation.world.effect;

import com.combatreforged.factory.api.world.effect.StatusEffect;
import com.combatreforged.factory.api.world.effect.StatusEffectInstance;
import com.combatreforged.factory.builder.implementation.Wrapped;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;

public class WrappedStatusEffectInstance extends Wrapped<net.minecraft.world.effect.MobEffectInstance> implements StatusEffectInstance {
    private final net.minecraft.world.effect.MobEffectInstance wrapped;
    private final StatusEffect effect;

    public WrappedStatusEffectInstance(MobEffectInstance instance) {
        super(instance);
        this.wrapped = instance;
        this.effect = convert(instance.getEffect());
    }

    @Override
    public StatusEffect getStatusEffect() {
        return effect;
    }

    @Override
    public int getTicksLeft() {
        return wrapped.getDuration();
    }

    @Override
    public int getAmplifier() {
        return wrapped.getAmplifier();
    }

    @Override
    public boolean isAmbient() {
        return wrapped.isAmbient();
    }

    private static StatusEffect convert(MobEffect effect) {
        ResourceLocation resourceLocation = Registry.MOB_EFFECT.getKey(effect);
        return resourceLocation != null ? StatusEffect.getRegisteredEffects().get(resourceLocation.toString()) : null;
    }

    public static MobEffect convert(StatusEffect statusEffect) {
        return Registry.MOB_EFFECT.get(new ResourceLocation(statusEffect.getId()));
    }
}
