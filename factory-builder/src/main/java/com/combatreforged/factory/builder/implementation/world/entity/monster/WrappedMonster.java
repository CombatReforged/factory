package com.combatreforged.factory.builder.implementation.world.entity.monster;

import com.combatreforged.factory.api.world.entity.monster.Monster;
import com.combatreforged.factory.builder.implementation.world.entity.WrappedMob;

public class WrappedMonster extends WrappedMob implements Monster {
    private final net.minecraft.world.entity.monster.Monster wrappedMonster;

    public WrappedMonster(net.minecraft.world.entity.monster.Monster wrappedMonster) {
        super(wrappedMonster);
        this.wrappedMonster = wrappedMonster;
    }

    @Override
    public net.minecraft.world.entity.monster.Monster unwrap() {
        return wrappedMonster;
    }
}
