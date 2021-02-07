package com.combatreforged.factory.api.world.entity.player;

import com.combatreforged.factory.api.world.entity.LivingEntity;

public interface Player extends LivingEntity {
    int getFoodLevel();
    float getSaturation();
    float getExhaustion();
}
