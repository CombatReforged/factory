package com.combatreforged.factory.api.event.player;

import com.combatreforged.factory.api.event.Cancellable;
import com.combatreforged.factory.api.event.EventBackend;
import com.combatreforged.factory.api.world.entity.player.Player;

import java.util.function.BiConsumer;
import java.util.function.Function;

public class PlayerChangeMovementStateEvent extends PlayerEvent implements Cancellable {
    public static final EventBackend<PlayerChangeMovementStateEvent> BACKEND = EventBackend.create(PlayerChangeMovementStateEvent.class);

    private boolean cancelled;
    private final ChangedState changedState;
    private boolean changedValue;
    private final boolean previousValue;

    public PlayerChangeMovementStateEvent(Player player, ChangedState changedState, boolean changedValue) {
        super(player);
        this.changedState = changedState;
        this.changedValue = changedValue;
        this.previousValue = changedState.playerGetter.apply(player);
    }

    public ChangedState getChangedState() {
        return changedState;
    }

    public boolean getChangedValue() {
        return changedValue;
    }

    public void setChangedValue(boolean changedValue) {
        this.changedValue = changedValue;
    }

    public boolean getPreviousValue() {
        return previousValue;
    }

    @Override
    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @SuppressWarnings("ImmutableEnumChecker")
    public enum ChangedState {
        SPRINTING(Player::isSprinting, Player::setSprinting),
        SWIMMING(Player::isSwimming, Player::setSwimming),
        SNEAKING(Player::isSneaking, Player::setSneaking),
        FALL_FLYING(Player::isFallFlying, Player::setFallFlying),
        FLYING(Player::isFlying, Player::setFlying);

        public final Function<Player, Boolean> playerGetter;
        public final BiConsumer<Player, Boolean> playerSetter;

        ChangedState(Function<Player, Boolean> playerGetter, BiConsumer<Player, Boolean> playerSetter) {
            this.playerGetter = playerGetter;
            this.playerSetter = playerSetter;
        }
    }
}
