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
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.ButtonBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(ButtonBlock.class)
public abstract class ButtonBlockMixin {
    @Unique
    private PlayerChangeBlockStateEvent changeBlockStateEvent;

    @Inject(method = "use", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/ButtonBlock;press(Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/world/level/Level;Lnet/minecraft/core/BlockPos;)V", shift = At.Shift.BEFORE))
    public void injectChangeBlockStateEvent(BlockState blockState, Level level, BlockPos blockPos, Player player, InteractionHand interactionHand, BlockHitResult blockHitResult, CallbackInfoReturnable<InteractionResult> cir) {
        if (!level.isClientSide()) {
            com.combatreforged.factory.api.world.entity.player.Player apiPlayer = Wrapped.wrap(player, WrappedPlayer.class);
            Location location = new Location(blockPos.getX(), blockPos.getY(), blockPos.getZ(), Wrapped.wrap(level, WrappedWorld.class));
            Block currentBlockState = new WrappedBlock(location);
            this.changeBlockStateEvent = new PlayerChangeBlockStateEvent(apiPlayer, location, currentBlockState, null, PlayerChangeBlockStateEvent.Action.PRESS_BUTTON);
        }
    }

    @Inject(method = "press", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/Level;setBlock(Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/block/state/BlockState;I)Z", shift = At.Shift.BEFORE), cancellable = true)
    public void runEvent(BlockState blockState, Level level, BlockPos blockPos, CallbackInfo ci) {
        if (changeBlockStateEvent != null) {
            changeBlockStateEvent.setNewBlockState(new WrappedBlockState(changeBlockStateEvent.getLocation(), blockState.setValue(ButtonBlock.POWERED, true)));
            PlayerChangeBlockStateEvent.BACKEND.invoke(changeBlockStateEvent);

            if (changeBlockStateEvent.isCancelled()) {
                ci.cancel();
            }
        }
    }

    @Inject(method = "use", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/ButtonBlock;press(Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/world/level/Level;Lnet/minecraft/core/BlockPos;)V", shift = At.Shift.AFTER), locals = LocalCapture.CAPTURE_FAILEXCEPTION, cancellable = true)
    public void continueEvent(BlockState blockState, Level level, BlockPos blockPos, Player player, InteractionHand interactionHand, BlockHitResult blockHitResult, CallbackInfoReturnable<InteractionResult> cir) {
        if (changeBlockStateEvent != null && changeBlockStateEvent.isCancelled()) {
            cir.setReturnValue(InteractionResult.FAIL);
        }
    }

    @ModifyArg(method = "press", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/Level;setBlock(Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/block/state/BlockState;I)Z"))
    public BlockState modifyBlockState(BlockState prev) {
        if (changeBlockStateEvent != null) {
            return ((WrappedBlockState) changeBlockStateEvent.getNewBlockState()).state();
        } else {
            return prev;
        }
    }

    @ModifyArg(method = "press", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/ButtonBlock;updateNeighbours(Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/world/level/Level;Lnet/minecraft/core/BlockPos;)V"))
    public BlockState modifyBlockState2(BlockState prev) {
        if (changeBlockStateEvent != null) {
            return ((WrappedBlockState) changeBlockStateEvent.getNewBlockState()).state();
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
