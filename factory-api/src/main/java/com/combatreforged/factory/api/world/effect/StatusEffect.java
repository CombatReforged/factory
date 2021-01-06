package com.combatreforged.factory.api.world.effect;

public class StatusEffect {
    private final Type type;

    public StatusEffect(Type type) {
        this.type = type;
    }

    public Type getType() {
        return type;
    }

    public enum Type {
        BENEFICIAL, HARMFUL, NEUTRAL;
    }
}
