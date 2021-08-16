package com.combatreforged.factory.api.event.player;

import com.combatreforged.factory.api.event.EventBackend;
import com.combatreforged.factory.api.world.entity.player.Player;

import java.util.function.BiConsumer;
import java.util.function.Function;

public class PlayerChangeMovementStateEvent extends PlayerEvent {
    public static final EventBackend<PlayerChangeMovementStateEvent> BACKEND = EventBackend.create(PlayerChangeMovementStateEvent.class);

    private final ChangedState state;
    private boolean sprinting;
    private boolean swimming;
    private boolean sneaking;
    private boolean fallFlying;
    private boolean flying;

    public PlayerChangeMovementStateEvent(Player player, ChangedState state, boolean changedValue) {
        super(player);
        this.state = state;
        for (ChangedState changedState : ChangedState.values()) {
            if (changedState == this.state) {
                changedState.eventSetter.accept(this, changedValue);
            } else {
                changedState.eventSetter.accept(this, changedState.playerGetter.apply(this.getPlayer()));
            }
        }
    }

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

    public boolean isSwimming() {
        return swimming;
    }

    public void setSwimming(boolean swimming) {
        this.swimming = swimming;
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

    public void applyExceptChanged() {
        for (ChangedState changedState : ChangedState.values()) {
            if (changedState != this.state) {
                changedState.playerSetter.accept(this.getPlayer(), changedState.eventGetter.apply(this));
            }
        }
    }

    @SuppressWarnings("ImmutableEnumChecker")
    public enum ChangedState {
        SPRINTING(PlayerChangeMovementStateEvent::isSprinting, PlayerChangeMovementStateEvent::setSprinting, Player::isSprinting, Player::setSprinting),
        SWIMMING(PlayerChangeMovementStateEvent::isSwimming, PlayerChangeMovementStateEvent::setSwimming, Player::isSwimming, Player::setSwimming),
        SNEAKING(PlayerChangeMovementStateEvent::isSneaking, PlayerChangeMovementStateEvent::setSneaking, Player::isSneaking, Player::setSneaking),
        FALL_FLYING(PlayerChangeMovementStateEvent::isFallFlying, PlayerChangeMovementStateEvent::setFallFlying, Player::isFallFlying, Player::setFallFlying),
        FLYING(PlayerChangeMovementStateEvent::isFlying, PlayerChangeMovementStateEvent::setFlying, Player::isFlying, Player::setFlying);

        private final Function<PlayerChangeMovementStateEvent, Boolean> eventGetter;
        private final BiConsumer<PlayerChangeMovementStateEvent, Boolean> eventSetter;
        private final Function<Player, Boolean> playerGetter;
        private final BiConsumer<Player, Boolean> playerSetter;

        ChangedState(Function<PlayerChangeMovementStateEvent, Boolean> eventGetter, BiConsumer<PlayerChangeMovementStateEvent, Boolean> eventSetter, Function<Player, Boolean> playerGetter, BiConsumer<Player, Boolean> playerSetter) {
            this.eventGetter = eventGetter;
            this.eventSetter = eventSetter;
            this.playerGetter = playerGetter;
            this.playerSetter = playerSetter;
        }

        public Function<PlayerChangeMovementStateEvent, Boolean> getEventGetter() {
            return eventGetter;
        }
    }
}
