package com.combatreforged.factory.api.event.player;

import com.combatreforged.factory.api.event.EventBackend;
import com.combatreforged.factory.api.world.entity.player.Player;
import net.kyori.adventure.text.Component;

public class PlayerJoinEvent extends PlayerEvent {
    public static final EventBackend<PlayerJoinEvent> BACKEND = EventBackend.create(PlayerJoinEvent.class);

    private Component joinMessage;

    public PlayerJoinEvent(Player player, Component joinMessage) {
        super(player);
        this.joinMessage = joinMessage;
    }

    public Component getJoinMessage() {
        return joinMessage;
    }

    public void setJoinMessage(Component joinMessage) {
        this.joinMessage = joinMessage;
    }
}
