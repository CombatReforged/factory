package com.combatreforged.factory.api.event.server.tick;

import com.combatreforged.factory.api.FactoryServer;
import com.combatreforged.factory.api.event.EventBackend;

public class ServerEndTickEvent extends ServerTickEvent {
    public static final EventBackend<ServerEndTickEvent> BACKEND = EventBackend.create(ServerEndTickEvent.class);

    public ServerEndTickEvent(FactoryServer server, int tickID) {
        super(server, tickID);
    }
}