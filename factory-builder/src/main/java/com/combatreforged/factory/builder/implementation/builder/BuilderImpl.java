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
import com.combatreforged.factory.api.world.nbt.NBTList;
import com.combatreforged.factory.api.world.nbt.NBTObject;
import com.combatreforged.factory.api.world.nbt.NBTValue;
import com.combatreforged.factory.builder.exception.NBTParsingException;
import com.combatreforged.factory.builder.implementation.Wrapped;
import com.combatreforged.factory.builder.implementation.util.Conversion;
import com.combatreforged.factory.builder.implementation.world.WrappedWorld;
import com.combatreforged.factory.builder.implementation.world.damage.WrappedDamageData;
import com.combatreforged.factory.builder.implementation.world.effect.WrappedStatusEffectInstance;
import com.combatreforged.factory.builder.implementation.world.entity.WrappedEntity;
import com.combatreforged.factory.builder.implementation.world.item.WrappedItemStack;
import com.combatreforged.factory.builder.implementation.world.nbt.WrappedNBTList;
import com.combatreforged.factory.builder.implementation.world.nbt.WrappedNBTObject;
import com.combatreforged.factory.builder.implementation.world.nbt.WrappedNBTValue;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.kyori.adventure.nbt.api.BinaryTagHolder;
import net.minecraft.nbt.*;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Explosion;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Map;

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
    public NBTObject createNBTObject(@Nullable Map<String, NBTValue> values) {
        NBTObject nbt = Wrapped.wrap(new CompoundTag(), WrappedNBTObject.class);
        if (values != null) {
            nbt.setAll(values);
        }
        return nbt;
    }

    @Override
    public NBTObject createNBTObjectFromString(String string) {
        try {
            return Wrapped.wrap(TagParser.parseTag(string), WrappedNBTObject.class);
        } catch (CommandSyntaxException e) {
            throw new NBTParsingException(string);
        }
    }

    @Override
    public NBTList createNBTList(List<NBTValue> values) {
        NBTList list = Wrapped.wrap(new ListTag(), WrappedNBTList.class);
        if (values != null) {
            list.addAll(values);
        }
        return list;
    }

    @Override
    public NBTValue createNBTValue(short s) {
        return fromTag(ShortTag.valueOf(s));
    }

    @Override
    public NBTValue createNBTValue(double d) {
        return fromTag(DoubleTag.valueOf(d));
    }

    @Override
    public NBTValue createNBTValue(float f) {
        return fromTag(FloatTag.valueOf(f));
    }

    @Override
    public NBTValue createNBTValue(byte b) {
        return fromTag(ByteTag.valueOf(b));
    }

    @Override
    public NBTValue createNBTValue(int i) {
        return fromTag(IntTag.valueOf(i));
    }

    @Override
    public NBTValue createNBTValue(long l) {
        return fromTag(LongTag.valueOf(l));
    }

    @Override
    public NBTValue createNBTValue(long[] arr) {
        return fromTag(new LongArrayTag(arr));
    }

    @Override
    public NBTValue createNBTValue(byte[] arr) {
        return fromTag(new ByteArrayTag(arr));
    }

    @Override
    public NBTValue createNBTValue(int[] arr) {
        return fromTag(new IntArrayTag(arr));
    }

    @Override
    public NBTValue createNBTValue(String string) {
        return fromTag(StringTag.valueOf(string));
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

    @Deprecated
    @Override
    public ItemStack createItemStack(ItemType itemType, int count, int damage, BinaryTagHolder tag) {
        net.minecraft.world.item.ItemStack stack = new net.minecraft.world.item.ItemStack(Conversion.ITEMS.get(itemType), count);
        if (damage != 0) {
            stack.setDamageValue(damage);
        }
        try {
            stack.setTag(TagParser.parseTag(tag.toString()));
        } catch (CommandSyntaxException e) {
            throw new UnsupportedOperationException("Tag is invalid");
        }

        return Wrapped.wrap(stack, WrappedItemStack.class);
    }

    @Override
    public ItemStack createItemStack(ItemType itemType, int count, int damage, @Nullable NBTObject nbt) {
        net.minecraft.world.item.ItemStack stack = new net.minecraft.world.item.ItemStack(Conversion.ITEMS.get(itemType), count);
        if (damage != 0) {
            stack.setDamageValue(damage);
        }
        if (nbt != null) {
            stack.setTag(((WrappedNBTObject) nbt).unwrap());
        }

        return Wrapped.wrap(stack, WrappedItemStack.class);
    }

    private NBTValue fromTag(Tag tag) {
        return Wrapped.wrap(tag, WrappedNBTValue.class);
    }
}
