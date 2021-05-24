package com.combatreforged.factory.builder.implementation.world.border;

import com.combatreforged.factory.api.world.border.WorldBorder;
import com.combatreforged.factory.builder.implementation.Wrapped;

public class WrappedWorldBorder extends Wrapped<net.minecraft.world.level.border.WorldBorder> implements WorldBorder {
    public WrappedWorldBorder(net.minecraft.world.level.border.WorldBorder wrapped) {
        super(wrapped);
    }

    @Override
    public void setCenter(double x, double z) {
        wrapped.setCenter(x, z);
    }

    @Override
    public double getSize() {
        return wrapped.getSize();
    }

    @Override
    public void setSize(double size, long time) {
        if (time > 0L) {
            wrapped.lerpSizeBetween(wrapped.getSize(), size, time);
        }
        else {
            wrapped.setSize(size);
        }
    }

    @Override
    public double getDamageBuffer() {
        return wrapped.getDamageSafeZone();
    }

    @Override
    public void setDamageBuffer(double distance) {
        wrapped.setDamageSafeZone(distance);
    }

    @Override
    public double getDamagePerBlockPerSec() {
        return wrapped.getDamagePerBlock();
    }

    @Override
    public void setDamagePerBlockPerSec(double damage) {
        wrapped.setDamagePerBlock(damage);
    }

    @Override
    public int getWarningDistance() {
        return wrapped.getWarningBlocks();
    }

    @Override
    public void setWarningDistance(int warningDistance) {
        wrapped.setWarningBlocks(warningDistance);
    }

    @Override
    public int getWarningTime() {
        return wrapped.getWarningTime();
    }

    @Override
    public void setWarningTime(int time) {
        wrapped.setWarningTime(time);
    }
}
