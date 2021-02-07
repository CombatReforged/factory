package com.combatreforged.factory.api;

import com.combatreforged.factory.api.builder.Builder;

public class FactoryAPI {
    private static FactoryAPI INSTANCE = null;

    private final Builder builder;

    public FactoryAPI(Builder builder) {
        this.builder = builder;

        INSTANCE = this;
    }

    public Builder getBuilder() {
        return builder;
    }

    public static FactoryAPI getInstance() {
        return INSTANCE;
    }
}
