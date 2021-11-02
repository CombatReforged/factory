package com.combatreforged.factory.builder.implementation.world.entity.animal;

import com.combatreforged.factory.api.world.entity.animal.Animal;
import com.combatreforged.factory.builder.implementation.world.entity.WrappedAgeable;

public class WrappedAnimal extends WrappedAgeable implements Animal {
    public WrappedAnimal(net.minecraft.world.entity.animal.Animal wrappedAnimal) {
        super(wrappedAnimal);
    }

    @Override
    public boolean isInLove() {
        return wrappedAnimal().isInLove();
    }

    @Override
    public int getLoveTime() {
        return wrappedAnimal().getInLoveTime();
    }

    @Override
    public void setLoveTime(int loveTime) {
        wrappedAnimal().setInLoveTime(loveTime);
    }

    @Override
    public void resetLove() {
        wrappedAnimal().resetLove();
    }

    @Override
    public boolean canMateWith(Animal animal) {
        return wrappedAnimal().canMate(((WrappedAnimal) animal).unwrap());
    }

    @Override
    public net.minecraft.world.entity.animal.Animal unwrap() {
        return wrappedAnimal();
    }

    private net.minecraft.world.entity.animal.Animal wrappedAnimal() {
        return (net.minecraft.world.entity.animal.Animal) this.wrapped;
    }
}
