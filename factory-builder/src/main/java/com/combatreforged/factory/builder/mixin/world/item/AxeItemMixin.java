package com.combatreforged.factory.builder.mixin.world.item;

import com.combatreforged.factory.api.event.player.PlayerChangeBlockStateEvent;
import com.combatreforged.factory.api.world.util.Location;
import com.combatreforged.factory.builder.implementation.Wrapped;
import com.combatreforged.factory.builder.implementation.world.WrappedWorld;
import com.combatreforged.factory.builder.implementation.world.block.WrappedBlock;
import com.combatreforged.factory.builder.implementation.world.block.WrappedBlockState;
import com.combatreforged.factory.builder.implementation.world.entity.player.WrappedPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(AxeItem.class)
public abstract class AxeItemMixin {
    @Unique private PlayerChangeBlockStateEvent changeBlockStateEvent;

    @Inject(method = "useOn", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/Level;playSound(Lnet/minecraft/world/entity/player/Player;Lnet/minecraft/core/BlockPos;Lnet/minecraft/sounds/SoundEvent;Lnet/minecraft/sounds/SoundSource;FF)V", shift = At.Shift.BEFORE), cancellable = true, locals = LocalCapture.CAPTURE_FAILEXCEPTION)
    public void injectChangeBlockStateEvent(UseOnContext useOnContext, CallbackInfoReturnable<InteractionResult> cir, Level level, BlockPos blockPos, BlockState blockState, Block block, Player player) {
        if (!level.isClientSide()) {
            com.combatreforged.factory.api.world.entity.player.Player apiPlayer = Wrapped.wrap(player, WrappedPlayer.class);
            Location location = new Location(blockPos.getX(), blockPos.getY(), blockPos.getZ(), Wrapped.wrap(level, WrappedWorld.class));
            com.combatreforged.factory.api.world.block.Block currentBlockState = new WrappedBlock(location);
            com.combatreforged.factory.api.world.block.BlockState newBlockState = new WrappedBlockState(location, block.defaultBlockState().setValue(RotatedPillarBlock.AXIS, blockState.getValue(RotatedPillarBlock.AXIS)));
            this.changeBlockStateEvent = new PlayerChangeBlockStateEvent(apiPlayer, location, currentBlockState, newBlockState, PlayerChangeBlockStateEvent.Action.STRIP_BLOCK);
            PlayerChangeBlockStateEvent.BACKEND.invoke(changeBlockStateEvent);

            if (changeBlockStateEvent.isCancelled()) {
                cir.setReturnValue(InteractionResult.FAIL);
            }
        }
    }

    @ModifyArg(method = "useOn", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/Level;setBlock(Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/block/state/BlockState;I)Z"))
    public BlockState modifyBlockState(BlockState prev) {
        if (changeBlockStateEvent != null && !changeBlockStateEvent.isCancelled()) {
            return ((WrappedBlock) changeBlockStateEvent.getNewBlockState()).state();
        } else {
            return prev;
        }
    }

    @Inject(method = "useOn", at = @At("RETURN"))
    public void nullifyChangeBlockStateEvent(UseOnContext useOnContext, CallbackInfoReturnable<InteractionResult> cir) {
        if (changeBlockStateEvent != null) {
            PlayerChangeBlockStateEvent.BACKEND.invokeEndFunctions(changeBlockStateEvent);
            changeBlockStateEvent = null;
        }
    }
}
