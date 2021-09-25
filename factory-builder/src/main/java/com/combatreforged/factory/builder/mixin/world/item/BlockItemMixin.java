package com.combatreforged.factory.builder.mixin.world.item;

import com.combatreforged.factory.api.event.player.PlayerPlaceBlockEvent;
import com.combatreforged.factory.api.world.block.Block;
import com.combatreforged.factory.api.world.entity.equipment.HandSlot;
import com.combatreforged.factory.api.world.entity.player.Player;
import com.combatreforged.factory.api.world.item.ItemStack;
import com.combatreforged.factory.api.world.util.Location;
import com.combatreforged.factory.builder.implementation.Wrapped;
import com.combatreforged.factory.builder.implementation.world.WrappedWorld;
import com.combatreforged.factory.builder.implementation.world.block.WrappedBlock;
import com.combatreforged.factory.builder.implementation.world.block.WrappedBlockState;
import com.combatreforged.factory.builder.implementation.world.entity.player.WrappedPlayer;
import com.combatreforged.factory.builder.implementation.world.item.WrappedItemStack;
import net.minecraft.core.BlockPos;
import net.minecraft.network.protocol.game.ClientboundBlockUpdatePacket;
import net.minecraft.network.protocol.game.ClientboundContainerSetSlotPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(BlockItem.class)
public abstract class BlockItemMixin {
    @Unique private PlayerPlaceBlockEvent placeBlockEvent;

    @Inject(method = "place", at = @At(value = "INVOKE_ASSIGN", target = "Lnet/minecraft/world/item/BlockItem;getPlacementState(Lnet/minecraft/world/item/context/BlockPlaceContext;)Lnet/minecraft/world/level/block/state/BlockState;", shift = At.Shift.AFTER), cancellable = true, locals = LocalCapture.CAPTURE_FAILEXCEPTION)
    public void injectPlayerPlaceBlockEvent(BlockPlaceContext blockPlaceContext, CallbackInfoReturnable<InteractionResult> cir, BlockPlaceContext blockPlaceContext2, BlockState blockState) {
        if (!blockPlaceContext2.getLevel().isClientSide()) {
            Player player = Wrapped.wrap(blockPlaceContext2.getPlayer(), WrappedPlayer.class);
            BlockPos clickedPos = blockPlaceContext2.getClickedPos();
            Location location = new Location(clickedPos.getX(), clickedPos.getY(), clickedPos.getZ(), Wrapped.wrap(blockPlaceContext2.getLevel(), WrappedWorld.class));
            Block currentBlockState = blockPlaceContext2.getLevel().getBlockState(clickedPos) != null ? new WrappedBlock(location) : null;
            com.combatreforged.factory.api.world.block.BlockState newBlockState = blockState != null ? new WrappedBlockState(location, blockState) : null;
            ItemStack blockStack = Wrapped.wrap(blockPlaceContext2.getItemInHand(), WrappedItemStack.class);
            HandSlot handSlot = blockPlaceContext2.getHand() == InteractionHand.MAIN_HAND ? HandSlot.MAIN_HAND : HandSlot.OFF_HAND;

            this.placeBlockEvent = new PlayerPlaceBlockEvent(player, location, currentBlockState, newBlockState, blockStack, handSlot);
            PlayerPlaceBlockEvent.BACKEND.invoke(placeBlockEvent);

            if (this.placeBlockEvent.isCancelled()) {
                assert blockPlaceContext2.getPlayer() instanceof ServerPlayer;
                ServerPlayer sPlayer = ((ServerPlayer) blockPlaceContext2.getPlayer());
                BlockPos above = blockPlaceContext2.getClickedPos().above();
                sPlayer.connection.send(new ClientboundBlockUpdatePacket(above, sPlayer.level.getBlockState(above)));
                sPlayer.connection.send(new ClientboundContainerSetSlotPacket(0, handSlot == HandSlot.MAIN_HAND ? sPlayer.inventory.selected + 36 : 45, blockPlaceContext2.getItemInHand()));
                cir.setReturnValue(InteractionResult.FAIL);
            }
        }
    }

    @ModifyVariable(method = "place", at = @At(value = "INVOKE_ASSIGN", target = "Lnet/minecraft/world/item/BlockItem;getPlacementState(Lnet/minecraft/world/item/context/BlockPlaceContext;)Lnet/minecraft/world/level/block/state/BlockState;", shift = At.Shift.AFTER))
    public BlockState modifyBlockState(BlockState prev) {
        if (this.placeBlockEvent != null && !this.placeBlockEvent.isCancelled()) {
            if ((placeBlockEvent.getNewBlockState() != null ? placeBlockEvent.getNewBlockState().getType() : null) != (placeBlockEvent.getCurrentBlockState() != null ? placeBlockEvent.getCurrentBlockState().getType() : null)) {
                ServerPlayer mcPlayer = ((WrappedPlayer) placeBlockEvent.getPlayer()).unwrap();
                BlockPos blockPos = new BlockPos(placeBlockEvent.getLocation().getX(), placeBlockEvent.getLocation().getY() + 1, placeBlockEvent.getLocation().getZ());
                mcPlayer.connection.send(new ClientboundBlockUpdatePacket(blockPos, ((WrappedWorld) placeBlockEvent.getWorld()).unwrap().getBlockState(blockPos)));
            }
            return placeBlockEvent.getNewBlockState() != null ? ((WrappedBlockState) placeBlockEvent.getNewBlockState()).state() : null;
        } else {
            return prev;
        }
    }

    @Inject(method = "place", at = @At("RETURN"))
    public void nullifyPlaceBlockEvent(BlockPlaceContext blockPlaceContext, CallbackInfoReturnable<InteractionResult> cir) {
        if (this.placeBlockEvent != null) {
            PlayerPlaceBlockEvent.BACKEND.invokeEndFunctions(placeBlockEvent);
            this.placeBlockEvent = null;
        }
    }
}
