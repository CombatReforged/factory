package com.combatreforged.factory.api.world.entity;

public interface Ageable extends Mob {
    int getAge();
    void setAge(int age);
    boolean isBreedable();

    boolean isBaby();
    void setBaby(boolean baby);
}
