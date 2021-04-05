package com.combatreforged.factory.api.event;

@FunctionalInterface
public interface Listener<T extends Event> {
    void onEvent(T event);
}
