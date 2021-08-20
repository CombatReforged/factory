package com.combatreforged.factory.api.event.player;

import com.combatreforged.factory.api.event.EventBackend;
import com.combatreforged.factory.api.world.entity.Entity;
import com.combatreforged.factory.api.world.entity.equipment.HandSlot;
import com.combatreforged.factory.api.world.entity.player.Player;
import com.combatreforged.factory.api.world.util.Location;

public class PlayerInteractAtEntityEvent extends PlayerInteractEntityEvent {
    public static final EventBackend<PlayerInteractAtEntityEvent> BACKEND = EventBackend.create(PlayerInteractAtEntityEvent.class);

    private final Location interactionLocation;
    private final HandSlot hand;

    public PlayerInteractAtEntityEvent(Player player, Entity entity, Location interactionLocation, HandSlot hand) {
        super(player, entity);
        this.interactionLocation = interactionLocation;
        this.hand = hand;
    }

    public Location getInteractionLocation() {
        return interactionLocation;
    }

    public HandSlot getHand() {
        return hand;
    }
}
