package com.combatreforged.factory.builder.implementation.builder;

import com.combatreforged.factory.api.builder.Builder;
import com.combatreforged.factory.api.world.damage.DamageData;
import com.combatreforged.factory.api.world.effect.StatusEffect;
import com.combatreforged.factory.api.world.effect.StatusEffectInstance;
import com.combatreforged.factory.api.world.entity.Entity;
import com.combatreforged.factory.api.world.entity.projectile.Projectile;
import com.combatreforged.factory.builder.implementation.Wrapped;
import com.combatreforged.factory.builder.implementation.util.Conversion;
import com.combatreforged.factory.builder.implementation.world.damage.WrappedDamageData;
import com.combatreforged.factory.builder.implementation.world.effect.WrappedStatusEffectInstance;
import com.combatreforged.factory.builder.implementation.world.entity.WrappedEntity;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Explosion;
import org.apache.logging.log4j.Logger;

public class BuilderImpl implements Builder {
    final Logger logger;

    public BuilderImpl(Logger logger) {
        this.logger = logger;
    }

    @Override
    public StatusEffectInstance createStatusEffectInstance(StatusEffect statusEffect, int duration, int amplifier, boolean ambient) {
        MobEffectInstance mei = new MobEffectInstance(Conversion.EFFECTS.get(statusEffect), duration, amplifier, ambient, ambient, ambient);
        return Wrapped.wrap(mei, WrappedStatusEffectInstance.class);
    }

    @Override
    public DamageData createDamageData(DamageData.Type type, Entity entityCause, boolean isIndirect) {
        DamageSource ds = DamageSource.GENERIC;
        if (Conversion.DAMAGE_TYPES.containsKey(type)) {
            ds = Conversion.DAMAGE_TYPES.get(type);
        }
        else {
            switch (type) {
                case ENTITY_ATTACK:
                    ds = DamageSource.mobAttack((LivingEntity) ((WrappedEntity) entityCause).unwrap());
                    break;
                case EXPLOSION:
                    if (entityCause != null) {
                        ds = DamageSource.explosion((LivingEntity) ((WrappedEntity) entityCause).unwrap());
                    } else {
                        ds = DamageSource.explosion((Explosion) null);
                    }
                    break;
                case PROJECTILE:
                    if (entityCause instanceof Projectile) {
                        Projectile projectile = (Projectile) entityCause;
                        ds = DamageSource.indirectMobAttack(((WrappedEntity) projectile).unwrap(), (LivingEntity) ((WrappedEntity) projectile.getOwner()).unwrap());
                    }
                    break;
                default:

            }
        }
        return Wrapped.wrap(ds, WrappedDamageData.class);
    }
}
