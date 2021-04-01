package com.combatreforged.factory.api.world.entity.player;

import com.combatreforged.factory.api.world.entity.LivingEntity;
import com.combatreforged.factory.api.world.item.container.PlayerInventory;

public interface Player extends LivingEntity {
    int getFoodLevel();
    void setFoodLevel(int level);
    float getSaturation();
    void setSaturation(float saturation);
    float getExhaustion();
    void setExhaustion(float exhaustion);
    PlayerInventory getInventory();
    int getSelectedSlot();
}
