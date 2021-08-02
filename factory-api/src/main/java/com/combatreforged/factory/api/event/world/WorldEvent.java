package com.combatreforged.factory.api.event.world;

import com.combatreforged.factory.api.event.Event;
import com.combatreforged.factory.api.world.World;

public abstract class WorldEvent extends Event {
    private final World world;

    public WorldEvent(World world) {
        this.world = world;
    }

    public World getWorld() {
        return world;
    }
}
