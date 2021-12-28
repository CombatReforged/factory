package com.combatreforged.factory.builder.mixin.world.item;

import com.combatreforged.factory.api.event.player.PlayerUseItemEvent;
import com.combatreforged.factory.api.world.entity.equipment.HandSlot;
import com.combatreforged.factory.builder.extension.wrap.Wrap;
import com.combatreforged.factory.builder.implementation.Wrapped;
import com.combatreforged.factory.builder.implementation.world.entity.player.WrappedPlayer;
import com.combatreforged.factory.builder.implementation.world.item.WrappedItemStack;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ItemStack.class)
public abstract class ItemStackMixin implements Wrap<com.combatreforged.factory.api.world.item.ItemStack> {
    private WrappedItemStack wrapped;
    @Inject(method = { "<init>(Lnet/minecraft/world/level/ItemLike;I)V", "<init>(Lnet/minecraft/nbt/CompoundTag;)V" }, at = @At("TAIL"))
    public void injectWrapped(CallbackInfo ci) {
        this.wrapped = new WrappedItemStack((ItemStack) (Object) this);
    }

    @Unique private PlayerUseItemEvent useItemEvent;
    @Inject(method = "use", at = @At(value = "HEAD"), cancellable = true)
    public void injectUseItemEvent(Level level, Player player, InteractionHand interactionHand, CallbackInfoReturnable<InteractionResultHolder<ItemStack>> cir) {
        com.combatreforged.factory.api.world.entity.player.Player apiPlayer = Wrapped.wrap(player, WrappedPlayer.class);
        HandSlot hand = interactionHand == InteractionHand.MAIN_HAND ? HandSlot.MAIN_HAND : HandSlot.OFF_HAND;
        WrappedItemStack apiStack = Wrapped.wrap(player.getItemInHand(interactionHand), WrappedItemStack.class);
        this.useItemEvent = new PlayerUseItemEvent(apiPlayer, apiStack, hand);
        PlayerUseItemEvent.BACKEND.invoke(useItemEvent);

        if (useItemEvent.isCancelled()) {
            cir.setReturnValue(InteractionResultHolder.fail(apiStack.unwrap()));
            if (player instanceof ServerPlayer) {
                ((ServerPlayer) player).refreshContainer(player.inventoryMenu);
            }
        }
    }

    @Inject(method = "use", at = @At("RETURN"))
    public void nullifyUseItemEvent(Level level, Player player, InteractionHand interactionHand, CallbackInfoReturnable<InteractionResultHolder<ItemStack>> cir) {
        if (useItemEvent != null) {
            PlayerUseItemEvent.BACKEND.invokeEndFunctions(useItemEvent);
            useItemEvent = null;
        }
    }

    @Override
    public com.combatreforged.factory.api.world.item.ItemStack wrap() {
        return wrapped;
    }
}
