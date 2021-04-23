package com.combatreforged.factory.builder.implementation.world.entity.projectile;

import com.combatreforged.factory.api.world.entity.Entity;
import com.combatreforged.factory.api.world.entity.projectile.Projectile;
import com.combatreforged.factory.builder.implementation.Wrapped;
import com.combatreforged.factory.builder.implementation.world.entity.WrappedEntity;

public class WrappedProjectile extends WrappedEntity implements Projectile {
    final net.minecraft.world.entity.projectile.Projectile wrappedProjectile;

    public WrappedProjectile(net.minecraft.world.entity.projectile.Projectile wrappedProjectile) {
        super(wrappedProjectile);
        this.wrappedProjectile = wrappedProjectile;
    }

    @Override
    public net.minecraft.world.entity.projectile.Projectile unwrap() {
        return wrappedProjectile;
    }

    @Override
    public Entity getOwner() {
        return Wrapped.wrap(wrappedProjectile.getOwner(), WrappedEntity.class);
    }
}
