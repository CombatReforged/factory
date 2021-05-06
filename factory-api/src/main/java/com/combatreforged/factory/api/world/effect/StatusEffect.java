package com.combatreforged.factory.api.world.effect;

import com.combatreforged.factory.api.interfaces.StringIdentified;

public interface StatusEffect {
    Type getType();

    enum Type {
        BENEFICIAL, HARMFUL, NEUTRAL
    }

    abstract class Other implements StatusEffect, StringIdentified {
    }
}
