package com.combatreforged.factory.api.event;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

public class EventBackend<T extends Event> {
    public static final Logger LOGGER = LogManager.getLogger("EventBackend");

    private final Map<Listener<T>, Integer> priorityMap;
    private final List<Listener<T>> listeners;

    private EventBackend() {
        priorityMap = new HashMap<>();
        listeners = new ArrayList<>();
    }

    public void register(final Listener<T> listener) {
        register(listener, 0);
    }

    public void register(final Listener<T> listener, int priority) {
        if (priority < -100 || priority > 100) {
            LOGGER.error("Listener " + listener.toString() + " tried to register with an invalid priority of " + priority + " (has to be between -100 and 100)!");
        }
        else if (!listeners.contains(listener)) {
            int index;
            for (index = 0; index < listeners.size(); index++) {
                if (!priorityMap.containsKey(listeners.get(index))) {
                    LOGGER.error("Priority map doesn't contain entry for registered listener "  + listener.toString() + "!");
                }
                else if (priorityMap.get(listeners.get(index)) < priority) {
                    break;
                }
            }
            priorityMap.put(listener, priority);
            listeners.add(index, listener);
        }
        else if (listeners.contains(listener)) {
            LOGGER.warn("Listener " + listener.toString() + " is already registered!");
        }
    }

    public void register(final Listener<T> listener, Predicate<T> filter) {
        this.register(listener, filter, 0);
    }

    public void register(final Listener<T> listener, final Predicate<T> filter, int priority) {
        this.register((event) -> {
            if (filter.test(event)) {
                listener.onEvent(event);
            }
        }, priority);
    }

    public void unregister(Listener<T> listener) {
        listeners.remove(listener);
        priorityMap.remove(listener);
    }

    public void invoke(T event) {
        for (Listener<T> listener : listeners) {
            try { listener.onEvent(event); }
            catch (Exception e) {
                LOGGER.error("An error occurred while executing event " + event.getClass().getSimpleName() + " in listener " + listener.toString() + ": ", e);
            }
        }
    }

    public void invokeEndFunctions(T event) {
        for (Runnable runnable : event.getAfterEventFunctions()) {
            try {
                runnable.run();
            } catch (Exception e) {
                LOGGER.error("An error occurred while executing after event functions for event " + event.getClass().getSimpleName() + " in runnable " + runnable.toString() + ": ", e);
            }
        }
    }

    public static <T extends Event> EventBackend<T> create(Class<T> eventClass) {
        return new EventBackend<>();
    }

    public static final class Priority {
        public static final int HIGHEST = 100;
        public static final int VERY_HIGH = 75;
        public static final int HIGH = 50;
        public static final int HIGHER = 25;
        public static final int DEFAULT = 0;
        public static final int LOWER = -25;
        public static final int LOW = -50;
        public static final int VERY_LOW = -75;
        public static final int LOWEST = -100;
    }
}
