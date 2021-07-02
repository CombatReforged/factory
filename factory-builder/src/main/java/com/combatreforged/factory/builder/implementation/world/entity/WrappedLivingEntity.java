package com.combatreforged.factory.builder.implementation.world.entity;

import com.combatreforged.factory.api.world.effect.StatusEffect;
import com.combatreforged.factory.api.world.effect.StatusEffectInstance;
import com.combatreforged.factory.api.world.entity.LivingEntity;
import com.combatreforged.factory.api.world.entity.equipment.EquipmentSlot;
import com.combatreforged.factory.api.world.item.ItemStack;
import com.combatreforged.factory.api.world.nbt.NBTObject;
import com.combatreforged.factory.builder.exception.WrappingException;
import com.combatreforged.factory.builder.extension.wrap.Wrap;
import com.combatreforged.factory.builder.implementation.Wrapped;
import com.combatreforged.factory.builder.implementation.util.ObjectMappings;
import com.combatreforged.factory.builder.implementation.world.effect.WrappedStatusEffectInstance;
import com.combatreforged.factory.builder.implementation.world.item.WrappedItemStack;
import com.combatreforged.factory.builder.implementation.world.nbt.WrappedNBTObject;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;

import java.util.ArrayList;
import java.util.List;

public class WrappedLivingEntity extends WrappedEntity implements LivingEntity {
    private final net.minecraft.world.entity.LivingEntity wrappedLiving;

    public WrappedLivingEntity(net.minecraft.world.entity.LivingEntity wrappedLiving) {
        super(wrappedLiving);
        this.wrappedLiving = wrappedLiving;
    }

    @Override
    public NBTObject getEntityNBT() {
        CompoundTag tag = ((WrappedNBTObject) super.getEntityNBT()).unwrap();
        wrappedLiving.addAdditionalSaveData(tag);
        return Wrapped.wrap(tag, WrappedNBTObject.class);
    }

    @Override
    public void setEntityNBT(NBTObject nbt) {
        super.setEntityNBT(nbt);
        wrappedLiving.readAdditionalSaveData(((WrappedNBTObject) nbt).unwrap());
    }

    @Override
    public net.minecraft.world.entity.LivingEntity unwrap() {
        return wrappedLiving;
    }

    @Override
    public ItemStack getEquipmentStack(EquipmentSlot slot) {
        return Wrapped.wrap(wrappedLiving.getItemBySlot(ObjectMappings.EQUIPMENT_SLOTS.get(slot)), WrappedItemStack.class);
    }

    @Override
    public void setEquipmentStack(EquipmentSlot slot, ItemStack itemStack) {
        wrappedLiving.setItemSlot(ObjectMappings.EQUIPMENT_SLOTS.get(slot), ((WrappedItemStack) itemStack).unwrap());
    }

    @Override
    public float getHealth() {
        return wrappedLiving.getHealth();
    }

    @Override
    public void setHealth(float health) {
        wrappedLiving.setHealth(health);
    }

    @Override
    public int getInvulnerabilityTime() {
        return wrappedLiving.invulnerableTime;
    }

    @Override
    public void setInvulnerabilityTime(int ticks) {
        wrappedLiving.invulnerableTime = ticks;
    }

    @Override
    public void damage(float amount) {
        wrappedLiving.hurt(DamageSource.GENERIC, amount);
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<StatusEffectInstance> getActiveEffects() {
        List<StatusEffectInstance> effectInstances = new ArrayList<>();

        for (MobEffectInstance vanillaInstance : wrappedLiving.getActiveEffects()) {
            try {
                effectInstances.add(((Wrap<StatusEffectInstance>) vanillaInstance).wrap());
            } catch (ClassCastException e) {
                throw new WrappingException("Unable to wrap StatusEffectInstance!");
            }
        }

        return effectInstances;
    }

    @Override
    public void addEffectInstance(StatusEffectInstance effectInstance) {
        wrappedLiving.addEffect(new MobEffectInstance(WrappedStatusEffectInstance.convert(effectInstance.getStatusEffect()),
                effectInstance.getTicksLeft(), effectInstance.getAmplifier(), effectInstance.isAmbient(), effectInstance.isAmbient()));
    }

    @Override
    public void removeEffect(StatusEffect statusEffect) {
        wrappedLiving.removeEffect(WrappedStatusEffectInstance.convert(statusEffect));
    }

    @Override
    public boolean isSprinting() {
        return wrappedLiving.isSprinting();
    }

    @Override
    public void setSprinting(boolean sprinting) {
        wrappedLiving.setSprinting(sprinting);
    }

    @Override
    public boolean isSneaking() {
        return wrappedLiving.isShiftKeyDown();
    }

    @Override
    public void setSneaking(boolean sneaking) {
        wrappedLiving.setShiftKeyDown(sneaking);
    }

    @Override
    public boolean isSwimming() {
        return wrappedLiving.isSwimming();
    }

    @Override
    public void setSwimming(boolean swimming) {
        wrappedLiving.setSwimming(swimming);
    }
}
