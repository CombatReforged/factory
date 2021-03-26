package com.combatreforged.factory.builder.implementation.world.damage;

import com.combatreforged.factory.api.world.damage.DamageData;
import com.combatreforged.factory.api.world.entity.Entity;
import com.combatreforged.factory.builder.implementation.Wrapped;
import com.combatreforged.factory.builder.implementation.util.Conversion;
import com.combatreforged.factory.builder.implementation.world.entity.WrappedEntity;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.EntityDamageSource;
import net.minecraft.world.damagesource.IndirectEntityDamageSource;

public class WrappedDamageData extends Wrapped<DamageSource> implements DamageData {
    final Type type;

    public WrappedDamageData(DamageSource wrapped) {
        super(wrapped);
        if (Conversion.DAMAGE_TYPES.inverse().containsKey(wrapped)) {
            type = Conversion.DAMAGE_TYPES.inverse().get(wrapped);
        }
        else {
            if (wrapped.isExplosion()) {
                type = Type.EXPLOSION;
            }
            else if (wrapped instanceof IndirectEntityDamageSource) {
                type = Type.PROJECTILE;
            }
            else if (wrapped instanceof EntityDamageSource) {
                type = Type.ENTITY_ATTACK;
            }
            else type = Type.GENERIC;
        }
    }

    public DamageSource unwrap() {
        return wrapped;
    }

    @Override
    public Type getType() {
        return type;
    }

    @Override
    public Entity getDamagingEntity() {
        return Wrapped.wrap(wrapped.getEntity(), WrappedEntity.class);
    }
}
