package com.combatreforged.factory.api.event.server;

import com.combatreforged.factory.api.FactoryServer;
import com.combatreforged.factory.api.event.Event;
import com.combatreforged.factory.api.event.EventBackend;

public class ServerStartupFinishEvent extends Event {
    public static final EventBackend<ServerStartupFinishEvent> BACKEND = EventBackend.create(ServerStartupFinishEvent.class);

    private final FactoryServer server;
    public ServerStartupFinishEvent(FactoryServer server) {
        this.server = server;
    }

    public FactoryServer getServer() {
        return server;
    }
}
