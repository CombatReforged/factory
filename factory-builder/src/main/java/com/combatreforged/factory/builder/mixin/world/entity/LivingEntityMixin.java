package com.combatreforged.factory.builder.mixin.world.entity;

import com.combatreforged.factory.api.event.entity.LivingEntityDamageEvent;
import com.combatreforged.factory.api.event.entity.LivingEntityDeathEvent;
import com.combatreforged.factory.api.event.player.PlayerChangeMovementStateEvent;
import com.combatreforged.factory.api.world.damage.DamageData;
import com.combatreforged.factory.builder.extension.world.entity.EntityExtension;
import com.combatreforged.factory.builder.extension.world.entity.LivingEntityExtension;
import com.combatreforged.factory.builder.implementation.Wrapped;
import com.combatreforged.factory.builder.implementation.world.damage.WrappedDamageData;
import com.combatreforged.factory.builder.implementation.world.entity.WrappedLivingEntity;
import com.combatreforged.factory.builder.implementation.world.entity.player.WrappedPlayer;
import net.minecraft.server.level.ServerPlayer;
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
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity implements LivingEntityExtension {
    @Shadow protected boolean dead;

    @Shadow protected abstract void dropEquipment();

    @Shadow protected abstract void dropExperience();

    @Shadow public abstract boolean isFallFlying();

    public LivingEntityMixin(EntityType<?> entityType, Level level) {
        super(entityType, level);
    }

    //BEGIN: LivingEntityDamageEvent
    @Unique LivingEntityDamageEvent damageEvent;
    @Inject(method = "hurt", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/LivingEntity;isSleeping()Z", shift = At.Shift.BEFORE), cancellable = true)
    public void injectLivingEntityDamageEvent(DamageSource damageSource, float f, CallbackInfoReturnable<Boolean> cir) {
        com.combatreforged.factory.api.world.entity.LivingEntity entity = Wrapped.wrap(this, WrappedLivingEntity.class);
        DamageData data = Wrapped.wrap(damageSource, WrappedDamageData.class);
        this.damageEvent = new LivingEntityDamageEvent(entity, data, f);
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

    @Inject(method = "hurt", at = @At("TAIL"))
    public void nullifyDamageEvent(DamageSource damageSource, float f, CallbackInfoReturnable<Boolean> cir) {
        LivingEntityDamageEvent.BACKEND.invokeEndFunctions(this.damageEvent);
        this.damageEvent = null;
    }
    //END: LivingEntityDamageEvent

    //BEGIN: LivingEntityDeathEvent
    @Unique LivingEntityDeathEvent deathEvent;
    @Inject(method = "die", at = @At("HEAD"))
    public void injectLivingEntityDeathEvent(DamageSource damageSource, CallbackInfo ci) {
        if (!this.removed && !this.dead && this.deathEvent == null) {
            com.combatreforged.factory.api.world.entity.LivingEntity entity = Wrapped.wrap(this, WrappedLivingEntity.class);
            DamageData data = Wrapped.wrap(damageSource, WrappedDamageData.class);
            boolean mobLoot = this.level.getGameRules().getBoolean(GameRules.RULE_DOMOBLOOT);
            this.deathEvent = new LivingEntityDeathEvent(entity, data, mobLoot, this.level.getGameRules().getBoolean(GameRules.RULE_KEEPINVENTORY), mobLoot);
            LivingEntityDeathEvent.BACKEND.invoke(this.deathEvent);
        }
    }

    @Redirect(method = "dropAllDeathLoot", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/GameRules;getBoolean(Lnet/minecraft/world/level/GameRules$Key;)Z"))
    public boolean changeShouldDropItems(GameRules gameRules, GameRules.Key<GameRules.BooleanValue> key) {
        if (key.equals(GameRules.RULE_DOMOBLOOT) && this.deathEvent != null) {
            return this.deathEvent.isDropLoot();
        } else {
            return gameRules.getBoolean(key);
        }
    }

    @Redirect(method = "dropAllDeathLoot", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/LivingEntity;dropEquipment()V"))
    public void disableShouldDropEquipment(LivingEntity livingEntity) {
        if (this.deathEvent == null || this.deathEvent.isDropEquipment()) {
            this.dropEquipment();
        }
    }

    @Redirect(method = "dropAllDeathLoot", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/LivingEntity;dropExperience()V"))
    public void disableShouldDropExperience(LivingEntity livingEntity) {
        if (this.deathEvent == null || this.deathEvent.isDropExperience()) {
            this.dropExperience();
        }
    }

    @Redirect(method = "dropExperience", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/GameRules;getBoolean(Lnet/minecraft/world/level/GameRules$Key;)Z"))
    public boolean changeShouldDropExperience(GameRules gameRules, GameRules.Key<GameRules.BooleanValue> key) {
        if (key.equals(GameRules.RULE_DOMOBLOOT) && deathEvent != null) {
            return deathEvent.isDropExperience();
        } else {
            return gameRules.getBoolean(key);
        }
    }

    @Override
    public boolean willDropItems() {
        return this.deathEvent == null || this.deathEvent.isDropLoot();
    }

    @Inject(method = "die", at = @At("TAIL"))
    public void nullifyEvent(DamageSource damageSource, CallbackInfo ci) {
        LivingEntityDeathEvent.BACKEND.invokeEndFunctions(this.deathEvent);
        this.deathEvent = null;
    }

    @Override
    public LivingEntityDeathEvent getDeathEvent() {
        return deathEvent;
    }

    @Override
    public void setDeathEvent(LivingEntityDeathEvent deathEvent) {
        this.deathEvent = deathEvent;
    }
    //END: LivingEntityDeathEvent

    @Unique private PlayerChangeMovementStateEvent changeMovementStateEvent;
    @SuppressWarnings("ConstantConditions")
    @Inject(method = "setSprinting", at = @At("HEAD"))
    public void injectChangeMovementStateEvent(boolean bl, CallbackInfo ci) {
        if ((Entity) this instanceof ServerPlayer && ((EntityExtension) this).injectChangeMovementStateEvent() && this.isSprinting() != bl) {
            ServerPlayer player = (ServerPlayer) (Object) this;
            this.changeMovementStateEvent = new PlayerChangeMovementStateEvent(Wrapped.wrap(player, WrappedPlayer.class), PlayerChangeMovementStateEvent.ChangedState.SPRINTING, bl);
            PlayerChangeMovementStateEvent.BACKEND.invoke(changeMovementStateEvent);
        }
    }

    @SuppressWarnings("ConstantConditions")
    @ModifyVariable(method = "setSprinting", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/Entity;setSprinting(Z)V", shift = At.Shift.BEFORE), argsOnly = true)
    public boolean modifyIsSprinting(boolean prev) {
        if (changeMovementStateEvent != null && (Entity) this instanceof ServerPlayer && ((EntityExtension) this).injectChangeMovementStateEvent()) {
            return changeMovementStateEvent.isCancelled() ? changeMovementStateEvent.getPreviousValue() : changeMovementStateEvent.getChangedValue();

        } else {
            return prev;
        }
    }

    @Inject(method = "setSprinting", at = @At("RETURN"))
    public void nullifyMovementStateEvent(boolean bl, CallbackInfo ci) {
        if (changeMovementStateEvent != null && ((EntityExtension) this).injectChangeMovementStateEvent()) {
            PlayerChangeMovementStateEvent.BACKEND.invokeEndFunctions(changeMovementStateEvent);
            changeMovementStateEvent = null;
        }
    }

    @SuppressWarnings("ConstantConditions")
    @Redirect(method = {"travel", "updateFallFlying"}, at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/LivingEntity;setSharedFlag(IZ)V"))
    public void injectChangeMovementStateEvent(LivingEntity livingEntity, int i, boolean bl) {
        if (i == 7 && (Entity) this instanceof ServerPlayer && ((EntityExtension) this).injectChangeMovementStateEvent() && this.isFallFlying() != bl) {
            this.changeMovementStateEvent = new PlayerChangeMovementStateEvent(Wrapped.wrap(this, WrappedPlayer.class), PlayerChangeMovementStateEvent.ChangedState.FALL_FLYING, bl);
            PlayerChangeMovementStateEvent.BACKEND.invoke(this.changeMovementStateEvent);

            this.setSharedFlag(i, changeMovementStateEvent.isCancelled() ? changeMovementStateEvent.getPreviousValue() : changeMovementStateEvent.getChangedValue());

            PlayerChangeMovementStateEvent.BACKEND.invokeEndFunctions(changeMovementStateEvent);
            changeMovementStateEvent = null;
        } else {
            this.setSharedFlag(i, bl);
        }
    }
}
