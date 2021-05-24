package com.combatreforged.factory.api.world.border;

public interface WorldBorder {
    void setCenter(double x, double z);

    double getSize();
    default void addSize(double add) {
        addSize(add, 0L);
    }
    default void addSize(double add, long time) {
        setSize(getSize() + add, time);
    }
    default void setSize(double size) {
        setSize(size, 0L);
    }
    void setSize(double size, long time);

    double getDamageBuffer();
    void setDamageBuffer(double distance);

    double getDamagePerBlockPerSec();
    void setDamagePerBlockPerSec(double damage);

    int getWarningDistance();
    void setWarningDistance(int warningDistance);

    int getWarningTime();
    void setWarningTime(int time);
}
