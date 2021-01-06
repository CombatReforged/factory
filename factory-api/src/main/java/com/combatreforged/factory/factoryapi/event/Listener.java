package com.combatreforged.factory.factoryapi.event;

/**Interface to be used when registering a listener
 * @param <T> the event to register the listener for.
 */
@FunctionalInterface
public interface Listener<T extends Event> {
    /**
     * Interface method that is called upon the event happening.
     * @param event Event data
     */
    void onEvent(T event);
}
