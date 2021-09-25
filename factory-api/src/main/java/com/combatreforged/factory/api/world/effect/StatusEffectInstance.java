package com.combatreforged.factory.api.world.effect;

import com.combatreforged.factory.api.builder.Builder;

public interface StatusEffectInstance {
    StatusEffect getStatusEffect();
    int getTicksLeft();
    default int getSecondsLeft() {
        return getTicksLeft() / 20;
    }
    int getAmplifier();
    boolean isAmbient();

    StatusEffectInstance copy();

    static StatusEffectInstance create(StatusEffect statusEffect, int duration, int amplifier,
                                       boolean ambient) {
        return Builder.getInstance()
                .createStatusEffectInstance(statusEffect, duration, amplifier, ambient);
    }
    static StatusEffectInstance create(StatusEffect statusEffect, int duration, int amplifier) {
        return create(statusEffect, duration, amplifier, true);
    }
    static StatusEffectInstance create(StatusEffect statusEffect, int duration) {
        return create(statusEffect, duration, 0, true);
    }
}
