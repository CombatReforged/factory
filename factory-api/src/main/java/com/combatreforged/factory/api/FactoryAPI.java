package com.combatreforged.factory.api;

import com.combatreforged.factory.api.builder.Builder;
import com.combatreforged.factory.api.event.player.PlayerMoveEvent;
import com.combatreforged.factory.api.world.util.Location;

public class FactoryAPI {
    private static FactoryAPI INSTANCE = null;

    private final Builder builder;

    public FactoryAPI(Builder builder) {
        this.builder = builder;

        INSTANCE = this;

        PlayerMoveEvent.BACKEND.register(event -> {
            Location oPos = event.getOldPosition();
            Location nPos = event.getNewPosition();
            event.setNewPosition(new Location(
                    oPos.getX() + (nPos.getX() - oPos.getX()) * 2,
                    oPos.getY(),
                    oPos.getZ() + (nPos.getZ() - oPos.getZ()) * 2,
                    oPos.getYaw() + (nPos.getYaw() - oPos.getYaw()) * 2,
                    oPos.getPitch() + (nPos.getPitch() - oPos.getPitch()) * 2, null));
        });
    }

    public Builder getBuilder() {
        return builder;
    }

    public static FactoryAPI getInstance() {
        return INSTANCE;
    }
}
