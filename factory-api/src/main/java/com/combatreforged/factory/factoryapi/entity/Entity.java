package com.combatreforged.factory.factoryapi.entity;

import com.combatreforged.factory.factoryapi.world.World;
import com.combatreforged.factory.factoryapi.world.util.Location;
import net.kyori.adventure.text.Component;

/**
 * Represents an Entity on the server.
 */
public interface Entity {
    /**
     * Gets the Entity's internal id.
     * @return the Entity's id
     */
    int getEntityId();

    /**
     * Gets the Entity's custom name.
     * @return the custom name
     */
    Component getCustomName();

    /**
     * Sets the Entity's custom name.
     * @param customName the new custom name
     */
    void setCustomName(Component customName);

    /**
     * Gets the Entity's location.
     * @return the Entity's location
     */
    Location getLocation();

    /**
     * Gets the world that this Entity is currently in.
     * @return the world this Entity is in
     */
    World getWorld();

    /**
     * Teleports the Entity to the specified location.
     * @param location the Location to teleport to.
     * @param ignoreDirection whether to keep the Entity's previous facing direction.
     */
    void teleport(Location location, boolean ignoreDirection);

    /**
     * Teleports the Entity to the specified location.
     * @param location the Location to teleport to
     */
    default void teleport(Location location) { teleport(location, false); }

    /**
     * Kills the Entity.
     */
    void kill();
}
