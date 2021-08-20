package com.combatreforged.factory.builder.mixin.world.entity.decoration;

import com.combatreforged.factory.api.event.player.PlayerInteractAtEntityEvent;
import com.combatreforged.factory.api.event.player.PlayerInteractEntityEvent;
import com.combatreforged.factory.api.world.entity.equipment.HandSlot;
import com.combatreforged.factory.api.world.entity.player.Player;
import com.combatreforged.factory.api.world.util.Location;
import com.combatreforged.factory.builder.implementation.Wrapped;
import com.combatreforged.factory.builder.implementation.world.entity.WrappedEntity;
import com.combatreforged.factory.builder.implementation.world.entity.player.WrappedPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.decoration.ArmorStand;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ArmorStand.class)
public abstract class ArmorStandMixin {
    @Unique
    private PlayerInteractAtEntityEvent interactAtEntityEvent;
    @Inject(method = "interactAt", at = @At("HEAD"), cancellable = true)
    public void injectInteractAtEntityEvent(net.minecraft.world.entity.player.Player player, Vec3 vec3, InteractionHand interactionHand, CallbackInfoReturnable<InteractionResult> cir) {
        Player apiPlayer = Wrapped.wrap(player, WrappedPlayer.class);
        WrappedEntity apiEntity = Wrapped.wrap(this, WrappedEntity.class);
        Location at = new Location(vec3.x, vec3.y, vec3.z, apiEntity.getWorld());
        HandSlot hand = interactionHand == InteractionHand.MAIN_HAND ? HandSlot.MAIN_HAND : HandSlot.OFF_HAND;
        this.interactAtEntityEvent = new PlayerInteractAtEntityEvent(apiPlayer, apiEntity, at, hand);
        PlayerInteractEntityEvent.BACKEND.invoke(interactAtEntityEvent);
        PlayerInteractAtEntityEvent.BACKEND.invoke(interactAtEntityEvent);
        if (interactAtEntityEvent.isCancelled()) {
            cir.setReturnValue(InteractionResult.FAIL);
        }
    }

    @Inject(method = "interactAt", at = @At("RETURN"))
    public void nullifyInteractAtEntityEvent(net.minecraft.world.entity.player.Player player, Vec3 vec3, InteractionHand interactionHand, CallbackInfoReturnable<InteractionResult> cir) {
        if (interactAtEntityEvent != null) {
            PlayerInteractEntityEvent.BACKEND.invokeEndFunctions(interactAtEntityEvent);
            PlayerInteractAtEntityEvent.BACKEND.invokeEndFunctions(interactAtEntityEvent);
            interactAtEntityEvent = null;
        }
    }
}
