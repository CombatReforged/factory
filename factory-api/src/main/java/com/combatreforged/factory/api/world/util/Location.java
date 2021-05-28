package com.combatreforged.factory.api.world.util;

import com.combatreforged.factory.api.world.World;

import java.util.Objects;

public class Location {
    private double x, y, z;
    private float yaw, pitch;
    private World world;

    public Location(double x, double y, double z, World world) {
        this(x, y, z, 0, 0, world);
    }

    public Location(double x, double y, double z, float yaw, float pitch, World world) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.yaw = yaw;
        this.pitch = pitch;
        this.world = world;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public double getZ() {
        return z;
    }

    public void setZ(double z) {
        this.z = z;
    }

    public float getYaw() {
        return yaw;
    }

    public void setYaw(float yaw) {
        this.yaw = yaw;
    }

    public float getPitch() {
        return pitch;
    }

    public void setPitch(float pitch) {
        this.pitch = pitch;
    }

    public World getWorld() {
        return world;
    }

    public void setWorld(World world) {
        this.world = world;
    }

    public double distanceTo(Location other) {
        double xDif = other.x - this.x;
        double yDif = other.y - this.y;
        double zDif = other.z - this.z;
        return Math.sqrt(xDif * xDif + yDif * yDif + zDif * zDif);
    }

    public Vector3D toVector() {
        return new Vector3D(x, y, z);
    }

    public Location copy() {
        return new Location(x, y, z, yaw, pitch, world);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Location)) return false;
        Location location = (Location) o;
        return Double.compare(location.x, x) == 0 && Double.compare(location.y, y) == 0 && Double.compare(location.z, z) == 0 && Float.compare(location.yaw, yaw) == 0 && Float.compare(location.pitch, pitch) == 0 && Objects.equals(world, location.world);
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y, z, yaw, pitch, world);
    }
}
