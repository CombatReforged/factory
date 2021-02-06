package com.combatreforged.factory.builder.implementation.world.effect;

import com.combatreforged.factory.api.world.effect.StatusEffect;
import com.combatreforged.factory.api.world.effect.StatusEffectInstance;
import com.combatreforged.factory.builder.FactoryBuilder;
import com.combatreforged.factory.builder.implementation.ConversionTables;
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
        StatusEffect statusEffect;
        if (ConversionTables.EFFECTS.inverse().containsKey(effect))
            statusEffect = ConversionTables.EFFECTS.inverse().get(effect);
        else {
            ResourceLocation resourceLocation = Registry.MOB_EFFECT.getKey(effect);
            if (resourceLocation != null)
                statusEffect = new StatusEffect.Unidentified(resourceLocation.toString(), effect.isBeneficial() ? StatusEffect.Type.BENEFICIAL : StatusEffect.Type.HARMFUL);
            else {
                FactoryBuilder.LOGGER.error("MobEffect " + effect.toString() + " not registered!");
                statusEffect = null;
            }
        }
        return statusEffect;
    }

    public static MobEffect convert(StatusEffect statusEffect) {
        if (ConversionTables.EFFECTS.containsKey(statusEffect))
            return ConversionTables.EFFECTS.get(statusEffect);
        else
            FactoryBuilder.LOGGER.error("StatusEffect has no pendant in vanilla!");
        return null;
    }
}
