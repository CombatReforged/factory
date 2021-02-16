package com.combatreforged.factory.api.world.entity.player;

import com.combatreforged.factory.api.world.entity.LivingEntity;

public interface Player extends LivingEntity {
    int getFoodLevel();
    void setFoodLevel(int level);
    float getSaturation();
    void setSaturation(float saturation);
    float getExhaustion();
    void setExhaustion(float exhaustion);
}
