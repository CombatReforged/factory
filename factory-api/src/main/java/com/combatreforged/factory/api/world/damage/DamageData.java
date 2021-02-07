package com.combatreforged.factory.api.world.damage;

import com.combatreforged.factory.api.builder.Builder;
import com.combatreforged.factory.api.world.block.Block;
import com.combatreforged.factory.api.world.entity.Entity;
import com.combatreforged.factory.api.world.entity.projectile.Projectile;

public interface DamageData {
    Type getType();
    default Projectile getProjectile() {
        return this::getDamagingEntity;
    }
    Entity getDamagingEntity();
    Block getBlock();
    default boolean hasIndirectCause() {
        return getType() == Type.MAGIC
                || (getType() == Type.PROJECTILE && getProjectile().getOwner() != null)
                || getType() == Type.DRAGON_BREATH || getType() == Type.ENTITY_ATTACK;
    }

    enum Type {
        IN_FIRE, ON_FIRE, LIGHTNING, LAVA, MAGMA, SUFFOCATION, CRAMMING, DROWNING,
        STARVING, CACTUS, FALLING, CRASHING, VOID, GENERIC, MAGIC, WITHERING,
        FALLING_ANVIL, FALLING_BLOCK, DRAGON_BREATH, DRY_OUT, SWEET_BERRY_BUSH,
        EXPLOSION, ENTITY_ATTACK, PROJECTILE
    }

    static DamageData create(Type type, Entity cause, boolean isIndirect) {
        return Builder.getInstance().createDamageData(type, cause, null, isIndirect);
    }

    static DamageData create(Type type, Block cause) {
        return Builder.getInstance().createDamageData(type, null, cause, false);
    }

    static DamageData create(Type type) {
        return Builder.getInstance().createDamageData(type, null, null,
                false);
    }
}
