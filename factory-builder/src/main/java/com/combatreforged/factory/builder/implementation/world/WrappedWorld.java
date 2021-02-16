package com.combatreforged.factory.builder.implementation.world;

import com.combatreforged.factory.api.world.World;
import com.combatreforged.factory.builder.implementation.Wrapped;
import net.minecraft.server.level.ServerLevel;

public class WrappedWorld extends Wrapped<ServerLevel> implements World {
    final ServerLevel wrapped;
    public WrappedWorld(ServerLevel wrapped) {
        super(wrapped);
        this.wrapped = wrapped;
    }

    @Override
    public ServerLevel unwrap() {
        return wrapped;
    }
}
