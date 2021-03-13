package com.combatreforged.factory.api.event.player;

import com.combatreforged.factory.api.event.Event;
import com.combatreforged.factory.api.event.EventBackend;
import com.combatreforged.factory.api.world.entity.player.Player;
import net.kyori.adventure.text.Component;

public class PlayerJoinEvent implements Event {
    public static final EventBackend<PlayerJoinEvent> BACKEND = EventBackend.create(PlayerJoinEvent.class);

    private final Player player;
    private Component joinMessage;

    public PlayerJoinEvent(Player player, Component joinMessage) {
        this.player = player;
        this.joinMessage = joinMessage;
    }

    public Player getPlayer() {
        return player;
    }

    public Component getJoinMessage() {
        return joinMessage;
    }

    public void setJoinMessage(Component joinMessage) {
        this.joinMessage = joinMessage;
    }
}
