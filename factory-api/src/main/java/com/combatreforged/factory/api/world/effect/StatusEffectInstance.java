package com.combatreforged.factory.api.world.effect;

public class StatusEffectInstance {
    private final StatusEffect statusEffect;
    private int ticks;

    public StatusEffectInstance(StatusEffect statusEffect, int ticks) {
        this.statusEffect = statusEffect;
        this.ticks = ticks;
    }

    public StatusEffect getStatusEffect() {
        return statusEffect;
    }

    public int getTicks() {
        return ticks;
    }

    public void setTicks(int ticks) {
        this.ticks = ticks;
    }

    public void tick() {
        ticks--;
    }
}
