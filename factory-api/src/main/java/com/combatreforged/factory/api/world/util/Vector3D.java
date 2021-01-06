package com.combatreforged.factory.api.world.util;

/**
 * Represents a vector.
 */
public class Vector3D {
    private double x, y, z;

    /**
     * Creates a vector.
     * @param x x axis value
     * @param y y axis value
     * @param z z axis value
     */
    public Vector3D(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    /**
     * Gets this vector's x value.
     * @return x value
     */
    public double getX() {
        return x;
    }

    /**
     * Sets this vector's x value.
     * @param x x value
     * @return this Vector3D
     */
    public Vector3D setX(double x) {
        this.x = x;
        return this;
    }

    /**
     * Gets this vector's y value.
     * @return y value
     */
    public double getY() {
        return y;
    }

    /**
     * Sets this vector's y value.
     * @param y y value
     * @return this Vector3D
     */
    public Vector3D setY(double y) {
        this.y = y;
        return this;
    }

    /**
     * Gets this vector's z value.
     * @return z value
     */
    public double getZ() {
        return z;
    }

    /**
     * Sets this vector's z value.
     * @param z z value
     * @return this Vector3D
     */
    public Vector3D setZ(double z) {
        this.z = z;
        return this;
    }

    /**
     * Sets the value of all axes.
     * @param x x value
     * @param y y value
     * @param z z value
     * @return this Vector3D
     */
    public Vector3D set(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
        return this;
    }

    /**
     * Multiplies all axes of the vector with a special amount.
     * @param amount the amount to multiply all axes with
     * @return this Vector3D
     */
    public Vector3D multiply(double amount) {
        return multiply(amount, amount, amount);
    }

    /**
     * Multiplies this vector with another vector.
     * @param vector the other vector
     * @return this Vector3D
     */
    public Vector3D multiply(Vector3D vector) {
        return multiply(vector.x, vector.y, vector.z);
    }

    /**
     * Multiplies each axis with a separate value.
     * @param x value to multiply x-axis with
     * @param y value to multiply y-axis with
     * @param z value to multiply z-axis with
     * @return this Vector3D
     */
    public Vector3D multiply(double x, double y, double z) {
        this.x *= x;
        this.y *= y;
        this.z *= z;
        return this;
    }

    /**
     * Adds a specific amount to all axes of this vector.
     * @param amount amount to add
     * @return this Vector3D
     */
    public Vector3D add(double amount) {
        return add(amount, amount, amount);
    }

    /**
     * Adds another vector to this vector.
     * @param vector the other vector
     * @return this Vector3D
     */
    public Vector3D add(Vector3D vector) {
        return add(vector.x, vector.y, vector.z);
    }

    /**
     * Adds a sepearate value to each axis of this vector.
     * @param x amount to add to the x axis
     * @param y amount to add to the y axis
     * @param z amount to add to the z axis
     * @return this Vector3D
     */
    public Vector3D add(double x, double y, double z) {
        this.x += x;
        this.y += y;
        this.z += z;
        return this;
    }

    /**
     * Subtracts all axes by a specific amount.
     * @param amount the amount to subtract
     * @return this Vector3D
     */
    public Vector3D subtract(double amount) {
        return subtract(amount, amount, amount);
    }

    /**
     * Subtracts another vector from this vector
     * @param vector the other vector
     * @return this Vector3D
     */
    public Vector3D subtract(Vector3D vector) {
        return subtract(vector.x, vector.y, vector.z);
    }

    /**
     * Subtracts from each axis of the vector with a separate value.
     * @param x the amount to subtract from the x axis
     * @param y the amount to subtract from the y axis
     * @param z the amount to subtract from the z axis
     * @return this Vector3D
     */
    public Vector3D subtract(double x, double y, double z) {
        return add(-x, -y, -z);
    }

    /**
     * Divides all axis of this vector by a specific amount.
     * @param amount the amount to divide this vector by
     * @return this Vector3D
     */
    public Vector3D divide(double amount) {
        return divide(amount, amount, amount);
    }

    /**
     * Divides this vector by another vector.
     * @param vector the other vector
     * @return this Vector3D
     */
    public Vector3D divide(Vector3D vector) {
        return divide(vector.x, vector.y, vector.z);
    }

    /**
     * Divides all axis of this vector by a seperate value.
     * @param x the amount to divide the x axis by
     * @param y the amount to divide the y axis by
     * @param z the amount to divide the z axis by
     * @return this Vector3D
     */
    public Vector3D divide(double x, double y, double z) {
        return multiply(1 / x, 1 / y, 1 / z);
    }

    /**
     * Makes a copy of this Vector3D.
     * This creates another object with a different reference in order to stop the old vector from being modified.
     * @return a copy of this vector
     */
    public Vector3D copy() {
        return new Vector3D(this.x, this.y, this.z);
    }
}
