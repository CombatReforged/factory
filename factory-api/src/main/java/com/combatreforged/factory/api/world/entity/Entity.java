package com.combatreforged.factory.api.world.entity;

import com.combatreforged.factory.api.world.World;
import com.combatreforged.factory.api.world.util.Location;
import com.combatreforged.factory.api.world.util.Vector3D;
import net.kyori.adventure.text.Component;

import java.util.List;

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

    /**
     * Gets whether this Entity is on fire.
     * @return whether this Entity is on fire
     */
    boolean isOnFire();

    /**
     * Gets the amount of ticks this Entity has been on fire for.
     * @return how many ticks this Entity has been on fire
     */
    int getFireTicks();

    /**
     * Gets how long this Entity will remain on fire for.
     * @returns the amount of ticks this entity will burn for
     */
    int getRemainingFireTicks();

    /**
     * Changes for long this Entity will remain on fire for.
     * @param ticks the amount of ticks this entity will burn for
     */
    void setRemainingFireTicks(int ticks);

    /**
     * Gets a copy of the vector representing this Entity's velocity.
     * @return a copy of this Entity's velocity (to stop direct modification)
     */
    Vector3D getVelocity();

    /**
     * Sets this entity's velocity using a Vector3D.
     * @param velocity a vector representing this entity's new velocity.
     */
    void setVelocity(Vector3D velocity);

    /**
     * Gets this Entity's passengers.
     * @return the entity's passengers
     */
    List<Entity> getPassengers();

    /**
     * Removes a passenger from this Entity.
     * @param entity the entity to remove from the passengers of this Entity
     */
    void removePassenger(Entity entity);

    /**
     * Adds a passenger to this Entity.
     * @param entity the entity to add to this entity's passenger
     */
    void addPassenger(Entity entity);
}
