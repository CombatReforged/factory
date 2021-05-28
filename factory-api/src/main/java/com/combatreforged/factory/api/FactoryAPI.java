package com.combatreforged.factory.api;

import com.combatreforged.factory.api.builder.Builder;
import com.combatreforged.factory.api.entrypoint.FactoryPlugin;
import com.combatreforged.factory.api.exception.FactoryPluginException;
import com.combatreforged.factory.api.util.ImplementationUtils;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

public class FactoryAPI {
    private static FactoryAPI INSTANCE = null;

    private final Builder builder;
    private final ImplementationUtils implementationUtils;
    private final FactoryServer server;

    public FactoryAPI(FactoryServer server, Builder builder) {
        this.builder = builder;
        this.implementationUtils = builder.createImplementationUtils();
        this.server = server;

        INSTANCE = this;
    }

    public void initPlugins(List<FactoryPlugin> plugins) {
        plugins.forEach(pluginType -> {
            Class<? extends FactoryPlugin> pluginClass = pluginType.getClass();
            try {
                Constructor<? extends FactoryPlugin> constructor = pluginClass.getConstructor();
                constructor.newInstance().onFactoryLoad(this, this.getServer());
            } catch (NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException e) {
                throw new FactoryPluginException("Could not invoke no-argument constructor in plugin class '" + pluginClass.getSimpleName() + "'!", e);
            }
        });
    }

    public Builder getBuilder() {
        return builder;
    }

    public FactoryServer getServer() {
        return server;
    }

    public ImplementationUtils getImplementationUtils() {
        return implementationUtils;
    }

    public static FactoryAPI getInstance() {
        return INSTANCE;
    }
}
