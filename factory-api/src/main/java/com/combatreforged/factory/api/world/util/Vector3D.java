package com.combatreforged.factory.api.world.util;

public class Vector3D {
    private double x, y, z;

    public Vector3D(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public double getX() {
        return x;
    }

    public Vector3D setX(double x) {
        this.x = x;
        return this;
    }

    public double getY() {
        return y;
    }

    public Vector3D setY(double y) {
        this.y = y;
        return this;
    }

    public double getZ() {
        return z;
    }

    public Vector3D setZ(double z) {
        this.z = z;
        return this;
    }

    public Vector3D set(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
        return this;
    }

    public Vector3D multiply(double amount) {
        return multiply(amount, amount, amount);
    }

    public Vector3D multiply(Vector3D vector) {
        return multiply(vector.x, vector.y, vector.z);
    }

    public Vector3D multiply(double x, double y, double z) {
        this.x *= x;
        this.y *= y;
        this.z *= z;
        return this;
    }

    public Vector3D add(double amount) {
        return add(amount, amount, amount);
    }

    public Vector3D add(Vector3D vector) {
        return add(vector.x, vector.y, vector.z);
    }

    public Vector3D add(double x, double y, double z) {
        this.x += x;
        this.y += y;
        this.z += z;
        return this;
    }

    public Vector3D subtract(double amount) {
        return subtract(amount, amount, amount);
    }

    public Vector3D subtract(Vector3D vector) {
        return subtract(vector.x, vector.y, vector.z);
    }

    public Vector3D subtract(double x, double y, double z) {
        return add(-x, -y, -z);
    }

    public Vector3D divide(double amount) {
        return divide(amount, amount, amount);
    }

    public Vector3D divide(Vector3D vector) {
        return divide(vector.x, vector.y, vector.z);
    }

    public Vector3D divide(double x, double y, double z) {
        return multiply(1 / x, 1 / y, 1 / z);
    }

    public Vector3D copy() {
        return new Vector3D(this.x, this.y, this.z);
    }
}
