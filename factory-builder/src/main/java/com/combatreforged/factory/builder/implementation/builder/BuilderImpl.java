package com.combatreforged.factory.builder.implementation.builder;

import com.combatreforged.factory.api.builder.Builder;
import com.combatreforged.factory.api.world.World;
import com.combatreforged.factory.api.world.damage.DamageData;
import com.combatreforged.factory.api.world.effect.StatusEffect;
import com.combatreforged.factory.api.world.effect.StatusEffectInstance;
import com.combatreforged.factory.api.world.entity.Entity;
import com.combatreforged.factory.api.world.entity.EntityType;
import com.combatreforged.factory.api.world.entity.projectile.Projectile;
import com.combatreforged.factory.api.world.item.ItemStack;
import com.combatreforged.factory.api.world.item.ItemType;
import com.combatreforged.factory.builder.implementation.Wrapped;
import com.combatreforged.factory.builder.implementation.util.Conversion;
import com.combatreforged.factory.builder.implementation.world.WrappedWorld;
import com.combatreforged.factory.builder.implementation.world.damage.WrappedDamageData;
import com.combatreforged.factory.builder.implementation.world.effect.WrappedStatusEffectInstance;
import com.combatreforged.factory.builder.implementation.world.entity.WrappedEntity;
import com.combatreforged.factory.builder.implementation.world.item.WrappedItemStack;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.kyori.adventure.nbt.api.BinaryTagHolder;
import net.minecraft.nbt.TagParser;
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
    public Entity createEntity(EntityType type, World world) {
        return Wrapped.wrap(Conversion.ENTITIES.get(type).create(((WrappedWorld) world).unwrap()), WrappedEntity.class);
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

    @Override
    public ItemStack createItemStack(ItemType itemType, int count, int damage, BinaryTagHolder tag) {
        net.minecraft.world.item.ItemStack stack = new net.minecraft.world.item.ItemStack(Conversion.ITEMS.get(itemType), count);
        stack.setDamageValue(damage);
        try {
            stack.setTag(TagParser.parseTag(tag.toString()));
        } catch (CommandSyntaxException e) {
            throw new UnsupportedOperationException("Tag is invalid");
        }

        return Wrapped.wrap(stack, WrappedItemStack.class);
    }
}
