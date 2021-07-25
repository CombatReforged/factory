package com.combatreforged.factory.api.event.server.tick;

import com.combatreforged.factory.api.FactoryServer;
import com.combatreforged.factory.api.event.Event;

public abstract class ServerTickEvent implements Event {
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
