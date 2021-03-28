package com.combatreforged.factory.api.world.entity;

import com.combatreforged.factory.api.world.World;
import com.combatreforged.factory.api.world.command.CommandSource;
import com.combatreforged.factory.api.world.util.Location;
import com.combatreforged.factory.api.world.util.Vector3D;
import net.kyori.adventure.nbt.api.BinaryTagHolder;
import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Set;

/**
 * Represents an Entity on the server.
 */
public interface Entity extends CommandSource {
    /**
     * Gets the Entity's internal id.
     * @return the Entity's id
     */
    int getEntityId();

    /**
     * Gets the Entity's name.
     * @return the Entity's name
     */
    Component getName();

    /**
     * Gets the Entity's custom name. Can be null.
     * @return the custom name
     */
    @Nullable
    Component getCustomName();

    /**
     * Sets the Entity's custom name. Can be null.
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
     * Yaw will automatically be set between -180 and 180.
     * Pitch will automatically be set between -90 and 90.
     * @param location the Location to teleport to.
     * @param ignoreDirection whether to keep the Entity's previous facing direction.
     */
    void teleport(Location location, boolean ignoreDirection);

    /**
     * Teleports the Entity to the specified location.
     * @param location the Location to teleport to
     * @see this.teleport(Location, boolean)
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
     * Gets how long this Entity will remain on fire for.
     * @return the amount of ticks this entity will burn for
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

    /**
     * Makes this Entity start riding another Entity.
     * @param entity the Entity to ride
     */
    void startRiding(Entity entity);

    /**
     * Stops this Entity from riding another one.
     */
    void stopRiding();

    Set<String> getTags();

    boolean addTag(String tag);

    void removeTag(String tag);

    boolean hasTag(String tag);

    BinaryTagHolder getEntityData();

    void setEntityData(BinaryTagHolder tag);
}
