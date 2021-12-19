package com.combatreforged.factory.builder.mixin.world.effect;

import com.combatreforged.factory.api.event.entity.LivingEntityHealEvent;
import com.combatreforged.factory.builder.extension.world.effect.MobEffectExtension;
import com.combatreforged.factory.builder.implementation.Wrapped;
import com.combatreforged.factory.builder.implementation.util.ObjectMappings;
import com.combatreforged.factory.builder.implementation.world.entity.WrappedLivingEntity;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MobEffect.class)
public abstract class MobEffectMixin implements MobEffectExtension {
    @Shadow @Final private MobEffectCategory category;

    @Override
    public MobEffectCategory getCategory() {
        return category;
    }

    @Unique private LivingEntity currentlyHealedEntity;
    @Unique private LivingEntityHealEvent entityHealEvent;
    @Inject(method = "applyEffectTick", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/LivingEntity;heal(F)V", shift = At.Shift.BEFORE))
    public void injectPreHealEvent(LivingEntity livingEntity, int i, CallbackInfo ci) {
        currentlyHealedEntity = livingEntity;
    }
    @Inject(method = "applyInstantenousEffect", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/LivingEntity;heal(F)V", shift = At.Shift.BEFORE))
    public void injectPreHealEvent(Entity entity, Entity entity2, LivingEntity livingEntity, int i, double d, CallbackInfo ci) {
        currentlyHealedEntity = livingEntity;
    }

    @ModifyArg(method = {"applyEffectTick", "applyInstantenousEffect"}, at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/LivingEntity;heal(F)V"))
    public float injectHealEvent(float previous) {
        if (currentlyHealedEntity != null) {
            entityHealEvent = new LivingEntityHealEvent(Wrapped.wrap(currentlyHealedEntity, WrappedLivingEntity.class), previous,
                    LivingEntityHealEvent.HealCause.effect(ObjectMappings.EFFECTS.inverse().get((MobEffect) (Object) this)));
            LivingEntityHealEvent.BACKEND.invoke(entityHealEvent);

            if (entityHealEvent.isCancelled()) {
                return 0F;
            } else {
                return entityHealEvent.getAmount();
            }
        }
        return previous;
    }

    @Inject(method = {"applyEffectTick", "applyInstantenousEffect"}, at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/LivingEntity;heal(F)V", shift = At.Shift.AFTER))
    public void nullifyHealEvent(CallbackInfo ci) {
        if (entityHealEvent != null) {
            LivingEntityHealEvent.BACKEND.invokeEndFunctions(entityHealEvent);
            entityHealEvent = null;
            currentlyHealedEntity = null;
        }
    }
}
