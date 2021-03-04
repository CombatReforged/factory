package com.combatreforged.factory.api.world;

import com.combatreforged.factory.api.world.block.Block;
import com.combatreforged.factory.api.world.entity.Entity;
import com.combatreforged.factory.api.world.util.Location;

import java.util.List;

/**
 * Represents a world on the server.
 */
public interface World {
    Block getBlockAt(Location location);
    List<Entity> getEntities();
}
