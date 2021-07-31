package com.combatreforged.factory.api.event.player;

import com.combatreforged.factory.api.event.Cancellable;
import com.combatreforged.factory.api.event.EventBackend;
import com.combatreforged.factory.api.world.entity.player.Player;
import com.combatreforged.factory.api.world.util.Location;

public class PlayerMoveEvent extends PlayerEvent implements Cancellable {
    public static final EventBackend<PlayerMoveEvent> BACKEND = EventBackend.create(PlayerMoveEvent.class);

    private final Location oldPosition;
    private Location newPosition;
    private boolean cancelled;

    public PlayerMoveEvent(Player player, Location oldPosition, Location newPosition, boolean cancelled) {
        super(player);
        this.oldPosition = oldPosition;
        this.newPosition = newPosition;
        this.cancelled = cancelled;
    }

    @Override
    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    public Location getOldPosition() {
        return oldPosition;
    }

    public Location getNewPosition() {
        return newPosition;
    }

    public void setNewPosition(Location newPosition) {
        this.newPosition = newPosition;
    }
}
