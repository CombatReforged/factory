package com.combatreforged.factory.builder.mixin.world.entity.player;

import com.combatreforged.factory.api.event.player.PlayerChangeMovementStateEvent;
import com.combatreforged.factory.api.event.player.PlayerHotbarDropItemEvent;
import com.combatreforged.factory.api.world.item.ItemStack;
import com.combatreforged.factory.builder.extension.world.entity.EntityExtension;
import com.combatreforged.factory.builder.extension.world.entity.LivingEntityExtension;
import com.combatreforged.factory.builder.implementation.Wrapped;
import com.combatreforged.factory.builder.implementation.world.entity.player.WrappedPlayer;
import net.minecraft.network.protocol.game.ClientboundContainerSetSlotPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Player.class)
public abstract class PlayerMixin extends LivingEntity implements LivingEntityExtension {
    protected PlayerMixin(EntityType<? extends LivingEntity> entityType, Level level) {
        super(entityType, level);
    }

    @Shadow public abstract void startFallFlying();

    @Shadow public abstract void stopFallFlying();

    @Shadow public abstract boolean drop(boolean bl);

    @Redirect(method = "dropEquipment", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/GameRules;getBoolean(Lnet/minecraft/world/level/GameRules$Key;)Z"))
    public boolean changeShouldDropEquipment(GameRules gameRules, GameRules.Key<GameRules.BooleanValue> key) {
        if (key.equals(GameRules.RULE_KEEPINVENTORY) && this.getDeathEvent() != null) {
            return !getDeathEvent().isDropEquipment();
        } else {
            return gameRules.getBoolean(key);
        }
    }

    @Redirect(method = "getExperienceReward", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/GameRules;getBoolean(Lnet/minecraft/world/level/GameRules$Key;)Z"))
    public boolean changeShouldDropExperience(GameRules gameRules, GameRules.Key<GameRules.BooleanValue> key) {
        if (key.equals(GameRules.RULE_KEEPINVENTORY) && this.getDeathEvent() != null) {
            return !this.getDeathEvent().isDropExperience();
        } else {
            return gameRules.getBoolean(key);
        }
    }

    @Unique private PlayerChangeMovementStateEvent changeMovementStateEvent;
    @SuppressWarnings("ConstantConditions")
    @Inject(method = "startFallFlying", at = @At("HEAD"), cancellable = true)
    public void injectChangeMovementStateEvent(CallbackInfo ci) {
        if (((EntityExtension) this).injectChangeMovementStateEvent() && (Object) this instanceof ServerPlayer && !this.isFallFlying()) {
            this.changeMovementStateEvent = new PlayerChangeMovementStateEvent(Wrapped.wrap(this, WrappedPlayer.class), PlayerChangeMovementStateEvent.ChangedState.FALL_FLYING, true);
            PlayerChangeMovementStateEvent.BACKEND.invoke(changeMovementStateEvent);
            ((EntityExtension) this).setInjectMovementStateEvent(false);
            if (changeMovementStateEvent.getChangedValue()) {
                this.startFallFlying();
            } else {
                this.stopFallFlying();
            }
            ((EntityExtension) this).setInjectMovementStateEvent(true);
            PlayerChangeMovementStateEvent.BACKEND.invokeEndFunctions(changeMovementStateEvent);
            changeMovementStateEvent = null;
        }
    }

    @SuppressWarnings("ConstantConditions")
    @Inject(method = "stopFallFlying", at = @At("HEAD"), cancellable = true)
    public void injectChangeMovementStateEvent2(CallbackInfo ci) {
        if (((EntityExtension) this).injectChangeMovementStateEvent() && (Object) this instanceof ServerPlayer && this.isFallFlying()) {
            this.changeMovementStateEvent = new PlayerChangeMovementStateEvent(Wrapped.wrap(this, WrappedPlayer.class), PlayerChangeMovementStateEvent.ChangedState.FALL_FLYING, false);
            PlayerChangeMovementStateEvent.BACKEND.invoke(changeMovementStateEvent);
            ((EntityExtension) this).setInjectMovementStateEvent(false);
            if (changeMovementStateEvent.getChangedValue()) {
                this.startFallFlying();
            } else {
                this.stopFallFlying();
            }
            ((EntityExtension) this).setInjectMovementStateEvent(true);
            PlayerChangeMovementStateEvent.BACKEND.invokeEndFunctions(changeMovementStateEvent);
            changeMovementStateEvent = null;
        }
    }

    @SuppressWarnings("ConstantConditions")
    @Inject(method = "drop(Z)Z", at = @At(value = "HEAD"), cancellable = true)
    public void injectDropItemEvent(boolean bl, CallbackInfoReturnable<Boolean> cir) {
        com.combatreforged.factory.api.world.entity.player.Player apiPlayer = Wrapped.wrap(this, WrappedPlayer.class);
        int selectedSlot = apiPlayer.getSelectedSlot();
        ItemStack itemStack = apiPlayer.getInventory().getItemStack(selectedSlot);
        PlayerHotbarDropItemEvent dropItemEvent = new PlayerHotbarDropItemEvent(apiPlayer, itemStack, bl, selectedSlot);
        PlayerHotbarDropItemEvent.BACKEND.invoke(dropItemEvent);

        if (dropItemEvent.isCancelled()) {
            if ((Object) this instanceof ServerPlayer) {
                ServerPlayer serverPlayer = (ServerPlayer) (Object) this;
                serverPlayer.connection.send(new ClientboundContainerSetSlotPacket(0, 36 + selectedSlot, serverPlayer.getMainHandItem()));
            }
            cir.setReturnValue(false);
        } else {
            PlayerHotbarDropItemEvent.BACKEND.invokeEndFunctions(dropItemEvent);
        }
    }
}
