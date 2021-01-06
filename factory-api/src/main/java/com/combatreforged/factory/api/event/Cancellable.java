package com.combatreforged.factory.api.event;

/**
 * Represents an Event that is cancellable
 */
public interface Cancellable {
    /**
     * Set whether this event should be cancelled or not.
     * @param cancelled the cancellation status
     */
    void setCancelled(boolean cancelled);

    /**
     * Gets whether this event has been cancelled or not.
     * @return the cancellation status;
     */
    boolean isCancelled();
}
