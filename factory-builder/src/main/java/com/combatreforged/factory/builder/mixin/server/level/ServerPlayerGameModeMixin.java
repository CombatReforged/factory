package com.combatreforged.factory.builder.mixin.server.level;

import com.combatreforged.factory.api.event.player.PlayerBreakBlockEvent;
import com.combatreforged.factory.api.event.player.PlayerInteractBlockEvent;
import com.combatreforged.factory.api.world.block.Block;
import com.combatreforged.factory.api.world.entity.equipment.HandSlot;
import com.combatreforged.factory.api.world.entity.player.Player;
import com.combatreforged.factory.api.world.item.ItemStack;
import com.combatreforged.factory.api.world.util.Location;
import com.combatreforged.factory.builder.extension.world.level.BlockExtension;
import com.combatreforged.factory.builder.implementation.Wrapped;
import com.combatreforged.factory.builder.implementation.world.WrappedWorld;
import com.combatreforged.factory.builder.implementation.world.block.WrappedBlock;
import com.combatreforged.factory.builder.implementation.world.entity.player.WrappedPlayer;
import com.combatreforged.factory.builder.implementation.world.item.WrappedItemStack;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.protocol.game.ClientboundBlockUpdatePacket;
import net.minecraft.network.protocol.game.ClientboundContainerSetSlotPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.level.ServerPlayerGameMode;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.DoubleHighBlockItem;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.CrossCollisionBlock;
import net.minecraft.world.level.block.DoorBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.phys.BlockHitResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ServerPlayerGameMode.class)
public abstract class ServerPlayerGameModeMixin {
    @Shadow public ServerPlayer player;
    @Shadow public ServerLevel level;
    @Unique PlayerBreakBlockEvent breakBlockEvent;

    @Inject(method = "destroyBlock", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/Block;playerWillDestroy(Lnet/minecraft/world/level/Level;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/world/entity/player/Player;)V", shift = At.Shift.BEFORE), cancellable = true)
    public void injectPlayerBreakBlockEvent(BlockPos blockPos, CallbackInfoReturnable<Boolean> cir) {
        Player player = Wrapped.wrap(this.player, WrappedPlayer.class);
        Location location = new Location(blockPos.getX(), blockPos.getY(), blockPos.getZ(), Wrapped.wrap(this.level, WrappedWorld.class));
        Block block = new WrappedBlock(location);
        ItemStack miningStack = player.getEquipmentStack(HandSlot.MAIN_HAND);
        boolean drop = this.level.getGameRules().getBoolean(GameRules.RULE_DOBLOCKDROPS);

        this.breakBlockEvent = new PlayerBreakBlockEvent(player, block, location, miningStack, drop);
        PlayerBreakBlockEvent.BACKEND.invoke(breakBlockEvent);

        if (breakBlockEvent.isCancelled()) {
            cir.setReturnValue(false);
            BlockState state = level.getBlockState(blockPos);
            this.player.connection.send(new ClientboundBlockUpdatePacket(blockPos, state));
            if (level.getBlockEntity(blockPos) != null) {
                BlockEntity blockEntity = level.getBlockEntity(blockPos);
                assert blockEntity != null;
                this.player.connection.send(blockEntity.getUpdatePacket());
            }
            if (state.getBlock() instanceof CrossCollisionBlock) {
                for (Direction direction : Direction.values()) {
                    BlockPos relPos = blockPos.relative(direction);
                    BlockState relState = level.getBlockState(relPos);
                    if (relState.getBlock() instanceof CrossCollisionBlock) {
                        this.player.connection.send(new ClientboundBlockUpdatePacket(relPos, relState));
                    }
                }
            }
        } else {
            ((BlockExtension) this.level.getBlockState(blockPos).getBlock()).currentBreakEvent(breakBlockEvent);
        }
    }

    @Inject(method = "destroyBlock", at = @At("RETURN"))
    public void nullifyPlayerBreakBlockEvent(BlockPos blockPos, CallbackInfoReturnable<Boolean> cir) {
        if (this.breakBlockEvent != null) {
            ((BlockExtension) this.level.getBlockState(blockPos).getBlock()).currentBreakEvent(breakBlockEvent);
            PlayerBreakBlockEvent.BACKEND.invokeEndFunctions(breakBlockEvent);
            breakBlockEvent = null;
        }
    }

    @Unique private PlayerInteractBlockEvent interactBlockEvent;
    @Inject(method = "useItemOn", at = @At("HEAD"), cancellable = true)
    public void injectPlayerInteractBlockEvent(ServerPlayer serverPlayer, Level level, net.minecraft.world.item.ItemStack itemStack, InteractionHand interactionHand, BlockHitResult blockHitResult, CallbackInfoReturnable<InteractionResult> cir) {
        Player player = Wrapped.wrap(serverPlayer, WrappedPlayer.class);
        Location location = new Location(blockHitResult.getBlockPos().getX(), blockHitResult.getBlockPos().getY(), blockHitResult.getBlockPos().getZ(), Wrapped.wrap(level, WrappedWorld.class));
        Block block = new WrappedBlock(location);
        ItemStack stack = Wrapped.wrap(itemStack, WrappedItemStack.class);
        HandSlot hand = interactionHand == InteractionHand.MAIN_HAND ? HandSlot.MAIN_HAND : HandSlot.OFF_HAND;
        this.interactBlockEvent = new PlayerInteractBlockEvent(player, block, location, hand, stack);
        PlayerInteractBlockEvent.BACKEND.invoke(interactBlockEvent);

        if (interactBlockEvent.isCancelled()) {
            cir.setReturnValue(InteractionResult.PASS);
            BlockState blockState = level.getBlockState(blockHitResult.getBlockPos());
            if (blockState.getBlock() instanceof DoorBlock) {
                BlockPos otherPos = blockState.getValue(DoorBlock.HALF) == DoubleBlockHalf.UPPER ? blockHitResult.getBlockPos().below() : blockHitResult.getBlockPos().above();
                BlockState otherState = level.getBlockState(otherPos);
                serverPlayer.connection.send(new ClientboundBlockUpdatePacket(otherPos, otherState));
            }
            BlockPos placePos = blockHitResult.getBlockPos();
            if (itemStack.getItem() instanceof DoubleHighBlockItem) {
                serverPlayer.connection.send(new ClientboundBlockUpdatePacket(placePos.above(), level.getBlockState(placePos.above())));
                serverPlayer.connection.send(new ClientboundBlockUpdatePacket(placePos.relative(blockHitResult.getDirection()).above(), level.getBlockState(placePos.relative(blockHitResult.getDirection()).above())));
            }
            serverPlayer.connection.send(new ClientboundContainerSetSlotPacket(0, hand == HandSlot.MAIN_HAND ? serverPlayer.inventory.selected + 36 : 45, itemStack));
        }
    }

    @Inject(method = "useItemOn", at = @At("RETURN"))
    public void nullifyInteractBlockEvent(ServerPlayer serverPlayer, Level level, net.minecraft.world.item.ItemStack itemStack, InteractionHand interactionHand, BlockHitResult blockHitResult, CallbackInfoReturnable<InteractionResult> cir) {
        if (interactBlockEvent != null) {
            PlayerInteractBlockEvent.BACKEND.invoke(interactBlockEvent);
            interactBlockEvent = null;
        }
    }
}
