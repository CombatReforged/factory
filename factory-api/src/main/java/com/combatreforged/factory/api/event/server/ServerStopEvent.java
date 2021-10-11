package com.combatreforged.factory.api.event.server;

import com.combatreforged.factory.api.FactoryServer;
import com.combatreforged.factory.api.event.Event;
import com.combatreforged.factory.api.event.EventBackend;

public class ServerStopEvent extends Event {
    public static final EventBackend<ServerStopEvent> BACKEND = EventBackend.create(ServerStopEvent.class);

    private final FactoryServer server;
    public ServerStopEvent(FactoryServer server) {
        this.server = server;
    }

    public FactoryServer getServer() {
        return server;
    }
}
