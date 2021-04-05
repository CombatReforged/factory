package com.combatreforged.factory.api.entrypoint;

import com.combatreforged.factory.api.FactoryAPI;
import com.combatreforged.factory.api.FactoryServer;

public interface FactoryPlugin {
    void onFactoryLoad(FactoryAPI api, FactoryServer server);
}
