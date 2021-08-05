package com.combatreforged.factory.builder.mixin.world.level.block;

import com.combatreforged.factory.api.event.player.PlayerChangeBlockStateEvent;
import com.combatreforged.factory.api.world.block.Block;
import com.combatreforged.factory.api.world.util.Location;
import com.combatreforged.factory.builder.implementation.Wrapped;
import com.combatreforged.factory.builder.implementation.world.WrappedWorld;
import com.combatreforged.factory.builder.implementation.world.block.WrappedBlock;
import com.combatreforged.factory.builder.implementation.world.block.WrappedBlockState;
import com.combatreforged.factory.builder.implementation.world.entity.player.WrappedPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.network.protocol.game.ClientboundBlockUpdatePacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.DoorBlock;
import net.minecraft.world.level.block.FenceGateBlock;
import net.minecraft.world.level.block.TrapDoorBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.phys.BlockHitResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = {
        DoorBlock.class,
        TrapDoorBlock.class,
        FenceGateBlock.class
})
public abstract class OpenableMixins {
    @Unique private PlayerChangeBlockStateEvent changeBlockStateEvent;

    @Inject(method = "use", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/Level;setBlock(Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/block/state/BlockState;I)Z", shift = At.Shift.BEFORE), cancellable = true)
    public void injectPlayerChangeBlockStateEvent(BlockState blockState, Level level, BlockPos blockPos, Player player, InteractionHand interactionHand, BlockHitResult blockHitResult, CallbackInfoReturnable<InteractionResult> cir) {
        if (!level.isClientSide()) {
            com.combatreforged.factory.api.world.entity.player.Player apiPlayer = Wrapped.wrap(player, WrappedPlayer.class);
            Location location = new Location(blockPos.getX(), blockPos.getY(), blockPos.getZ(), Wrapped.wrap(level, WrappedWorld.class));
            Block currentBlockState = new WrappedBlock(location);
            com.combatreforged.factory.api.world.block.BlockState newBlockState = new WrappedBlockState(location, blockState);
            this.changeBlockStateEvent = new PlayerChangeBlockStateEvent(apiPlayer, location, currentBlockState, newBlockState, PlayerChangeBlockStateEvent.Action.OPEN_CLOSE);
            PlayerChangeBlockStateEvent.BACKEND.invoke(changeBlockStateEvent);

            if (changeBlockStateEvent.isCancelled()) {
                if (blockState.getBlock() instanceof DoorBlock) {
                    BlockPos otherPos = blockState.getValue(DoorBlock.HALF) == DoubleBlockHalf.UPPER ? blockPos.below() : blockPos.above();
                    BlockState otherState = level.getBlockState(otherPos);
                    ServerPlayer serverPlayer = (ServerPlayer) player;
                    serverPlayer.connection.send(new ClientboundBlockUpdatePacket(otherPos, otherState));
                }
                cir.setReturnValue(InteractionResult.FAIL);
            }
        }
    }

    @ModifyVariable(method = "use", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/Level;setBlock(Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/block/state/BlockState;I)Z", shift = At.Shift.BEFORE))
    public BlockState modifyBlockState(BlockState prev) {
        if (changeBlockStateEvent != null && !changeBlockStateEvent.isCancelled()) {
            return ((WrappedBlock) changeBlockStateEvent.getNewBlockState()).state();
        } else {
            return prev;
        }
    }

    @Inject(method = "use", at = @At("RETURN"))
    public void nullifyChangeBlockStateEvent(BlockState blockState, Level level, BlockPos blockPos, Player player, InteractionHand interactionHand, BlockHitResult blockHitResult, CallbackInfoReturnable<InteractionResult> cir) {
        if (changeBlockStateEvent != null) {
            PlayerChangeBlockStateEvent.BACKEND.invokeEndFunctions(changeBlockStateEvent);
            changeBlockStateEvent = null;
        }
    }
}
