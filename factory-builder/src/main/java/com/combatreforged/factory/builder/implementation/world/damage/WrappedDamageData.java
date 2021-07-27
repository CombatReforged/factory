package com.combatreforged.factory.builder.implementation.world.damage;

import com.combatreforged.factory.api.world.damage.DamageData;
import com.combatreforged.factory.api.world.entity.Entity;
import com.combatreforged.factory.builder.implementation.Wrapped;
import com.combatreforged.factory.builder.implementation.util.ObjectMappings;
import com.combatreforged.factory.builder.implementation.world.entity.WrappedEntity;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.EntityDamageSource;
import net.minecraft.world.damagesource.IndirectEntityDamageSource;

public class WrappedDamageData extends Wrapped<DamageSource> implements DamageData {
    private Type type;

    public WrappedDamageData(DamageSource wrapped) {
        super(wrapped);
        type = null;
    }

    @Override
    public DamageSource unwrap() {
        return wrapped;
    }

    @Override
    public Type getType() {
        if (type == null) {
            if (ObjectMappings.DAMAGE_TYPES.inverse().containsKey(wrapped)) {
                type = ObjectMappings.DAMAGE_TYPES.inverse().get(wrapped);
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
        return type;
    }

    @Override
    public Entity getDamagingEntity() {
        return Wrapped.wrap(wrapped.getEntity(), WrappedEntity.class);
    }
}
