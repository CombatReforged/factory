package com.combatreforged.factory.api.event.player;

import com.combatreforged.factory.api.event.Cancellable;
import com.combatreforged.factory.api.event.EventBackend;
import com.combatreforged.factory.api.world.entity.Entity;
import com.combatreforged.factory.api.world.entity.player.Player;

public class PlayerInteractEntityEvent extends PlayerEvent implements Cancellable {
    public static final EventBackend<PlayerInteractEntityEvent> BACKEND = EventBackend.create(PlayerInteractEntityEvent.class);

    private boolean cancelled;
    private final Entity entity;

    public PlayerInteractEntityEvent(Player player, Entity entity) {
        super(player);
        this.entity = entity;
    }

    @Override
    public Entity getEntity() {
        return entity;
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
