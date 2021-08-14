package com.combatreforged.factory.api.event.player;

import com.combatreforged.factory.api.event.EventBackend;
import com.combatreforged.factory.api.world.entity.player.Player;

public class PlayerChangeMovementStateEvent extends PlayerEvent {
    public static final EventBackend<PlayerChangeMovementStateEvent> BACKEND = EventBackend.create(PlayerChangeMovementStateEvent.class);

    private final ChangedState state;
    private boolean sprinting;
    private boolean sneaking;
    private boolean fallFlying;
    private boolean flying;

    public PlayerChangeMovementStateEvent(Player player, ChangedState state, boolean sprinting, boolean sneaking, boolean fallFlying, boolean flying) {
        super(player);
        this.state = state;
        this.sprinting = sprinting;
        this.sneaking = sneaking;
        this.fallFlying = fallFlying;
        this.flying = flying;
    }

    public ChangedState getState() {
        return state;
    }

    public boolean isSprinting() {
        return sprinting;
    }

    public void setSprinting(boolean sprinting) {
        this.sprinting = sprinting;
    }

    public boolean isSneaking() {
        return sneaking;
    }

    public void setSneaking(boolean sneaking) {
        this.sneaking = sneaking;
    }

    public boolean isFallFlying() {
        return fallFlying;
    }

    public void setFallFlying(boolean fallFlying) {
        this.fallFlying = fallFlying;
    }

    public boolean isFlying() {
        return flying;
    }

    public void setFlying(boolean flying) {
        this.flying = flying;
    }

    public enum ChangedState {
        SPRINTING, CROUCHING, FALL_FLYING, FLYING //TODO finish
    }
}
