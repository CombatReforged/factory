package com.combatreforged.factory.api.event.player;

import com.combatreforged.factory.api.event.Cancellable;
import com.combatreforged.factory.api.event.EventBackend;
import com.combatreforged.factory.api.world.entity.player.Player;

public class PlayerSwitchActiveSlotEvent extends PlayerEvent implements Cancellable {
    public static final EventBackend<PlayerSwitchActiveSlotEvent> BACKEND = EventBackend.create(PlayerSwitchActiveSlotEvent.class);

    private boolean cancelled;
    private int newSlot;

    public PlayerSwitchActiveSlotEvent(Player player, int newSlot) {
        super(player);
        this.newSlot = newSlot;
    }

    public int getNewSlot() {
        return newSlot;
    }

    public void setNewSlot(int newSlot) {
        if (newSlot < 0 || newSlot > 8) {
            throw new IllegalArgumentException("New slot must be between 0-8");
        }
        this.newSlot = newSlot;
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
