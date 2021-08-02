package com.combatreforged.factory.api.event.server;

import com.combatreforged.factory.api.FactoryServer;
import com.combatreforged.factory.api.event.Event;
import com.combatreforged.factory.api.event.EventBackend;

public class ServerTickEvent extends Event {
    public static final EventBackend<ServerTickEvent> BACKEND = EventBackend.create(ServerTickEvent.class);

    private final FactoryServer server;
    private final int tickID;

    public ServerTickEvent(FactoryServer server, int tickID) {
        this.server = server;
        this.tickID = tickID;
    }

    public FactoryServer getServer() {
        return server;
    }

    public int getTickID() {
        return tickID;
    }
}
