package com.combatreforged.factory.api.event.player;

import com.combatreforged.factory.api.event.Cancellable;
import com.combatreforged.factory.api.event.EventBackend;
import com.combatreforged.factory.api.world.entity.player.Player;

public class PlayerFoodLevelsChangeEvent extends PlayerEvent implements Cancellable {
    public static final EventBackend<PlayerFoodLevelsChangeEvent> BACKEND = EventBackend.create(PlayerFoodLevelsChangeEvent.class);

    private boolean cancelled;

    private float saturation;
    private int foodLevel;

    public PlayerFoodLevelsChangeEvent(Player player, float saturation, int foodLevel) {
        super(player);
        this.saturation = saturation;
        this.foodLevel = foodLevel;
    }

    public float getSaturation() {
        return saturation;
    }

    public void setSaturation(float saturation) {
        this.saturation = saturation;
    }

    public int getFoodLevel() {
        return foodLevel;
    }

    public void setFoodLevel(int foodLevel) {
        this.foodLevel = foodLevel;
    }

    @Override
    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }
}
