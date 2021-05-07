package com.combatreforged.factory.api.world;

import com.combatreforged.factory.api.FactoryServer;
import com.combatreforged.factory.api.world.block.Block;
import com.combatreforged.factory.api.world.block.BlockEntity;
import com.combatreforged.factory.api.world.entity.Entity;
import com.combatreforged.factory.api.world.util.Location;

import java.util.List;
import java.util.UUID;

public interface World {
    Block getBlockAt(Location location);
    default boolean isBlockEntity(Location location) {
        return getBlockEntity(location) != null;
    }
    BlockEntity getBlockEntity(Location location);
    List<Entity> getEntities();

    boolean spawn(Entity entity);

    Entity getEntity(UUID uuid);

    FactoryServer getServer();
}
