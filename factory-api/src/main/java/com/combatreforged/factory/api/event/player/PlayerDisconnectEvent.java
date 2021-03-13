package com.combatreforged.factory.api.event.player;

import com.combatreforged.factory.api.event.Event;
import com.combatreforged.factory.api.event.EventBackend;
import com.combatreforged.factory.api.world.entity.player.Player;
import net.kyori.adventure.text.Component;

public class PlayerDisconnectEvent implements Event {
    public static final EventBackend<PlayerDisconnectEvent> BACKEND = EventBackend.create(PlayerDisconnectEvent.class);

    private final Player player;
    private final Component disconnectReason;
    private Component leaveMessage;

    public PlayerDisconnectEvent(Player player, Component disconnectReason, Component leaveMessage) {
        this.player = player;
        this.disconnectReason = disconnectReason;
        this.leaveMessage = leaveMessage;
    }

    public Player getPlayer() {
        return player;
    }

    public Component getDisconnectReason() {
        return disconnectReason;
    }

    public Component getLeaveMessage() {
        return leaveMessage;
    }

    public void setLeaveMessage(Component leaveMessage) {
        this.leaveMessage = leaveMessage;
    }
}
