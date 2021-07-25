package com.combatreforged.factory.api.event.server.tick;

import com.combatreforged.factory.api.FactoryServer;
import com.combatreforged.factory.api.event.EventBackend;

public class ServerStartTickEvent extends ServerTickEvent {
    public static final EventBackend<ServerStartTickEvent> BACKEND = EventBackend.create(ServerStartTickEvent.class);

    public ServerStartTickEvent(FactoryServer server, int tickID) {
        super(server, tickID);
    }
}
