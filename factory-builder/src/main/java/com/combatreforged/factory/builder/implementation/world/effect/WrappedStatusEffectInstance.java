package com.combatreforged.factory.builder.implementation.world.effect;

import com.combatreforged.factory.api.world.effect.StatusEffect;
import com.combatreforged.factory.api.world.effect.StatusEffectInstance;
import com.combatreforged.factory.builder.exception.WrappingException;
import com.combatreforged.factory.builder.extension.MobEffectExtension;
import com.combatreforged.factory.builder.implementation.Wrapped;
import com.combatreforged.factory.builder.implementation.util.Conversion;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;

public class WrappedStatusEffectInstance extends Wrapped<MobEffectInstance> implements StatusEffectInstance {
    private final StatusEffect effect;

    public WrappedStatusEffectInstance(MobEffectInstance wrapped) {
        super(wrapped);
        this.effect = convert(wrapped.getEffect());
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
        if (Conversion.EFFECTS.inverse().containsKey(effect))
            statusEffect = Conversion.EFFECTS.inverse().get(effect);
        else {
            ResourceLocation resourceLocation = Registry.MOB_EFFECT.getKey(effect);
            if (resourceLocation != null) {
                StatusEffect.Type type;
                MobEffectExtension mex = ((MobEffectExtension) effect);
                switch (mex.getCategory()) {
                    case BENEFICIAL:
                        type = StatusEffect.Type.BENEFICIAL;
                        break;
                    case HARMFUL:
                        type = StatusEffect.Type.HARMFUL;
                        break;
                    default:
                        type = StatusEffect.Type.NEUTRAL;
                }
                statusEffect = new StatusEffect.Unidentified(resourceLocation.toString(), type);
            } else {
                throw new WrappingException("MobEffect " + effect.toString() + " not registered!");
            }
        }
        return statusEffect;
    }

    public static MobEffect convert(StatusEffect statusEffect) {
        if (Conversion.EFFECTS.containsKey(statusEffect))
            return Conversion.EFFECTS.get(statusEffect);
        else
            throw new WrappingException("StatusEffect has no pendant in vanilla!");
    }

    @Override
    public MobEffectInstance unwrap() {
        return this.wrapped;
    }
}