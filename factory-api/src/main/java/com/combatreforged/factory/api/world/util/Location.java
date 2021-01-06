package com.combatreforged.factory.api.world.util;

import com.combatreforged.factory.api.world.World;

/**
 * A util that represents a specific location in a world that includes a position
 * and optionally a direction.
 */
public class Location {
    private double x, y, z;
    private float yaw, pitch;
    private World world;

    /**
     * Creates a Location without a direction.
     * @param x the position's x coordinate
     * @param y the position's y coordinate
     * @param z the position's z coordinate
     * @param world the position's world
     */
    public Location(double x, double y, double z, World world) {
        this(x, y, z, 0, 0, world);
    }

    /**
     * Creates a Location.
     * @param x the position's x coordinate
     * @param y the position's y coordinate
     * @param z the position's z coordinate
     * @param yaw the direction's yaw
     * @param pitch the direction's pitch
     * @param world the position's world
     */
    public Location(double x, double y, double z, float yaw, float pitch, World world) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.yaw = yaw;
        this.pitch = pitch;
        this.world = world;
    }

    /**
     * Gets the x-coordinate of this position.
     * @return the x-coordinate
     */
    public double getX() {
        return x;
    }

    /**
     * Sets the x-coordinate of this position.
     * @param x the x-coordinate
     */
    public void setX(double x) {
        this.x = x;
    }

    /**
     * Gets the y-coordinate of this position.
     * @return the y-coordinate
     */
    public double getY() {
        return y;
    }

    /**
     * Sets the y-coordinate of this position.
     * @param y the y-coordinate
     */
    public void setY(double y) {
        this.y = y;
    }

    /**
     * Gets the z-coordinate of this position.
     * @return the z-coordinate
     */
    public double getZ() {
        return z;
    }

    /**
     * Sets the z-coordinate of this position
     * @param z the z-coordinate
     */
    public void setZ(double z) {
        this.z = z;
    }

    /**
     * Gets the yaw of this direction.
     * @return the yaw
     */
    public float getYaw() {
        return yaw;
    }

    /**
     * Sets the yaw of this direction.
     * @param yaw the yaw
     */
    public void setYaw(float yaw) {
        this.yaw = yaw;
    }

    /**
     * Gets the pitch of this direction.
     * @return the pitch
     */
    public float getPitch() {
        return pitch;
    }

    /**
     * Sets the pitch of this direction.
     * @param pitch the pitch
     */
    public void setPitch(float pitch) {
        this.pitch = pitch;
    }

    /**
     * Gets the world for this Location.
     * @return the world
     */
    public World getWorld() {
        return world;
    }

    /**
     * Sets the world for this Location.
     * @param world the World
     */
    public void setWorld(World world) {
        this.world = world;
    }

    /**
     * Calculate the distance from this location to another.
     * Directions are ignored in this calculation
     * @param other the other location to measure the distance to
     * @return the distance
     */
    public double distanceTo(Location other) {
        double xDif = other.x - this.x;
        double yDif = other.y - this.y;
        double zDif = other.z - this.z;
        return Math.sqrt(xDif * xDif + yDif * yDif + zDif * zDif);
    }
}
