package com.combatreforged.factory.api;

import com.combatreforged.factory.api.builder.Builder;

public class FactoryAPI {
    private static FactoryAPI INSTANCE = null;

    private final Builder builder;
    private final FactoryServer server;

    public FactoryAPI(FactoryServer server, Builder builder) {
        this.builder = builder;
        this.server = server;

        INSTANCE = this;
    }

    public Builder getBuilder() {
        return builder;
    }

    public FactoryServer getServer() {
        return server;
    }

    public static FactoryAPI getInstance() {
        return INSTANCE;
    }
}
