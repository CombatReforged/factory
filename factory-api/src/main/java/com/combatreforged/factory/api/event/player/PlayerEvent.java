package com.combatreforged.factory.api.event.player;

import com.combatreforged.factory.api.event.entity.LivingEntityEvent;
import com.combatreforged.factory.api.world.entity.player.Player;

public abstract class PlayerEvent extends LivingEntityEvent {
    private final Player player;

    public PlayerEvent(Player player) {
        super(player);
        this.player = player;
    }

    public Player getPlayer() {
        return player;
    }
}
