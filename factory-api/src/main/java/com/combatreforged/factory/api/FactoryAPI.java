package com.combatreforged.factory.api;

import com.combatreforged.factory.api.builder.Builder;
import com.combatreforged.factory.api.entrypoint.FactoryPlugin;
import com.combatreforged.factory.api.event.server.tick.ServerTickEvent;
import com.combatreforged.factory.api.exception.FactoryPluginException;
import com.combatreforged.factory.api.scheduler.TaskScheduler;
import com.combatreforged.factory.api.scheduler.TickFunction;
import com.combatreforged.factory.api.util.ImplementationUtils;
import com.combatreforged.factory.api.world.util.Pair;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

public class FactoryAPI {
    private static FactoryAPI INSTANCE = null;

    private final Builder builder;
    private final ImplementationUtils implementationUtils;
    private final FactoryServer server;
    private final TaskScheduler scheduler;
    private final TickFunction tickFunction;

    public FactoryAPI(FactoryServer server, Builder builder) {
        this.builder = builder;
        this.implementationUtils = builder.createImplementationUtils();
        this.server = server;
        Pair<TaskScheduler, TickFunction> sched = TaskScheduler.create();
        this.scheduler = sched.a();
        this.tickFunction = sched.b();

        ServerTickEvent.BACKEND.register(event -> event.runAfterwards(tickFunction::tick));

        INSTANCE = this;
    }

    public void init(List<FactoryPlugin> plugins) {
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

    public TaskScheduler getScheduler() {
        return scheduler;
    }
}
