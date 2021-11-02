package com.combatreforged.factory.builder.implementation.world.entity;

import com.combatreforged.factory.api.world.entity.Ageable;
import net.minecraft.world.entity.AgableMob;

public class WrappedAgeable extends WrappedMob implements Ageable {
    public WrappedAgeable(AgableMob wrappedAgeable) {
        super(wrappedAgeable);
    }

    @Override
    public int getAge() {
        return wrappedAgeable().getAge();
    }

    @Override
    public void setAge(int age) {
        wrappedAgeable().setAge(age);
    }

    @Override
    public boolean isBreedable() {
        return wrappedAgeable().canBreed();
    }

    @Override
    public boolean isBaby() {
        return wrappedAgeable().isBaby();
    }

    @Override
    public void setBaby(boolean baby) {
        wrappedAgeable().setBaby(baby);
    }

    @Override
    public AgableMob unwrap() {
        return wrappedAgeable();
    }

    private AgableMob wrappedAgeable() {
        return (AgableMob) this.wrapped;
    }
}
