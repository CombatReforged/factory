package com.combatreforged.factory.builder.implementation.world.entity;

import com.combatreforged.factory.api.world.entity.LivingEntity;
import com.combatreforged.factory.api.world.entity.Mob;
import com.combatreforged.factory.builder.implementation.Wrapped;

public class WrappedMob extends WrappedLivingEntity implements Mob {
    private final net.minecraft.world.entity.Mob wrappedMob;
    public WrappedMob(net.minecraft.world.entity.Mob wrappedMob) {
        super(wrappedMob);
        this.wrappedMob = wrappedMob;
    }

    @Override
    public LivingEntity getTargetEntity() {
        return Wrapped.wrap(wrappedMob.getTarget(), WrappedLivingEntity.class);
    }

    @Override
    public void setTargetEntity(LivingEntity target) {
        wrappedMob.setTarget(((WrappedLivingEntity) target).unwrap());
    }

    @Override
    public net.minecraft.world.entity.Mob unwrap() {
        return this.wrappedMob;
    }
}
