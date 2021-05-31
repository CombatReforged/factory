package com.combatreforged.factory.api.world.util;

public class BoundingBox {
    private final double minX;
    private final double maxX;

    private final double minY;
    private final double maxY;

    private final double minZ;
    private final double maxZ;

    public BoundingBox(double x1, double x2, double y1, double y2, double z1, double z2) {
        this.minX = Math.min(x1, x2);
        this.maxX = Math.max(x1, x2);

        this.minY = Math.min(y1, y2);
        this.maxY = Math.max(y1, y2);

        this.minZ = Math.min(z1, z2);
        this.maxZ = Math.max(z1, z2);
    }

    public BoundingBox(Vector3D first, Vector3D second) {
        this(first.getX(), second.getX(),
                first.getY(), second.getY(),
                first.getZ(), second.getZ());
    }

    public double getMinX() {
        return minX;
    }

    public double getMaxX() {
        return maxX;
    }

    public double getMinY() {
        return minY;
    }

    public double getMaxY() {
        return maxY;
    }

    public double getMinZ() {
        return minZ;
    }

    public double getMaxZ() {
        return maxZ;
    }

    public Vector3D getMin() {
        return new Vector3D(this.minX, this.minY, this.minZ);
    }

    public Vector3D getMax() {
        return new Vector3D(this.maxX, this.maxY, this.maxZ);
    }
}
