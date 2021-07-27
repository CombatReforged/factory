package com.combatreforged.factory.builder.mixin.world.entity;

import com.combatreforged.factory.api.event.entity.LivingEntityDamageEvent;
import com.combatreforged.factory.api.event.entity.LivingEntityDeathEvent;
import com.combatreforged.factory.builder.implementation.Wrapped;
import com.combatreforged.factory.builder.implementation.world.damage.WrappedDamageData;
import com.combatreforged.factory.builder.implementation.world.entity.WrappedLivingEntity;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity {
    @Shadow protected abstract boolean shouldDropLoot();

    public LivingEntityMixin(EntityType<?> entityType, Level level) {
        super(entityType, level);
    }

    //BEGIN: LivingEntityDamageEvent
    @Unique LivingEntityDamageEvent damageEvent;
    @Inject(method = "hurt", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/LivingEntity;isSleeping()Z", shift = At.Shift.BEFORE), cancellable = true)
    public void injectLivingEntityDamageEvent(DamageSource damageSource, float f, CallbackInfoReturnable<Boolean> cir) {
        this.damageEvent = new LivingEntityDamageEvent(Wrapped.wrap(this, WrappedLivingEntity.class), Wrapped.wrap(damageSource, WrappedDamageData.class), f);
        LivingEntityDamageEvent.BACKEND.invoke(this.damageEvent);
        if (this.damageEvent.isCancelled()) {
            cir.setReturnValue(false);
        }
    }

    @ModifyVariable(method = "hurt", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/LivingEntity;isSleeping()Z", shift = At.Shift.BEFORE), argsOnly = true)
    public float changeDamage(float prev) {
        if (this.damageEvent != null) {
            return damageEvent.getDamage();
        }
        return prev;
    }

    @ModifyVariable(method = "hurt", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/LivingEntity;isSleeping()Z", shift = At.Shift.BEFORE), argsOnly = true)
    public DamageSource changeCause(DamageSource prev) {
        if (this.damageEvent != null) {
            return ((WrappedDamageData) damageEvent.getCause()).unwrap();
        }
        return prev;
    }
    //END: LivingEntityDamageEvent

    //BEGIN: LivingEntityDeathEvent
    @Unique LivingEntityDeathEvent deathEvent;
    //TODO make implementation
}
