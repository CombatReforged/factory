package com.combatreforged.factory.builder.mixin.world.level.block;

import com.combatreforged.factory.api.event.player.PlayerChangeBlockStateEvent;
import com.combatreforged.factory.api.world.block.Block;
import com.combatreforged.factory.api.world.block.BlockState;
import com.combatreforged.factory.api.world.util.Location;
import com.combatreforged.factory.builder.implementation.Wrapped;
import com.combatreforged.factory.builder.implementation.world.WrappedWorld;
import com.combatreforged.factory.builder.implementation.world.block.WrappedBlock;
import com.combatreforged.factory.builder.implementation.world.block.WrappedBlockState;
import com.combatreforged.factory.builder.implementation.world.entity.player.WrappedPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.FarmBlock;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(FarmBlock.class)
public abstract class FarmBlockMixin {
    @Shadow public static void turnToDirt(net.minecraft.world.level.block.state.BlockState blockState, Level level, BlockPos blockPos) {}

    @Unique private PlayerChangeBlockStateEvent changeBlockStateEvent;
    @Unique private static PlayerChangeBlockStateEvent changeBlockStateEventStatic;

    @Inject(method = "fallOn", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/FarmBlock;turnToDirt(Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/world/level/Level;Lnet/minecraft/core/BlockPos;)V", shift = At.Shift.BEFORE), cancellable = true)
    public void injectChangeBlockStateEvent(Level level, BlockPos blockPos, Entity entity, float f, CallbackInfo ci) {
        if (entity instanceof ServerPlayer) {
            com.combatreforged.factory.api.world.entity.player.Player player = Wrapped.wrap(entity, WrappedPlayer.class);
            Location location = new Location(blockPos.getX(), blockPos.getY(), blockPos.getZ(), Wrapped.wrap(level, WrappedWorld.class));
            Block currentBlockState = new WrappedBlock(location);
            BlockState newBlockState = new WrappedBlockState(location, Blocks.DIRT.defaultBlockState());
            changeBlockStateEvent = new PlayerChangeBlockStateEvent(player, location, currentBlockState, newBlockState, PlayerChangeBlockStateEvent.Action.TRAMPLE_ON_FARMLAND);
            changeBlockStateEventStatic = changeBlockStateEvent;

            PlayerChangeBlockStateEvent.BACKEND.invoke(changeBlockStateEvent);
        }
    }

    @Redirect(method = "fallOn", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/FarmBlock;turnToDirt(Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/world/level/Level;Lnet/minecraft/core/BlockPos;)V"))
    public void shouldTurnToDirt(net.minecraft.world.level.block.state.BlockState blockState, Level level, BlockPos blockPos) {
        if (changeBlockStateEvent == null || !changeBlockStateEvent.isCancelled()) {
            turnToDirt(blockState, level, blockPos);
        }
    }

    @SuppressWarnings("unused")
    @ModifyArg(method = "turnToDirt", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/FarmBlock;pushEntitiesUp(Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/world/level/Level;Lnet/minecraft/core/BlockPos;)Lnet/minecraft/world/level/block/state/BlockState;"), index = 1)
    private static net.minecraft.world.level.block.state.BlockState modifyBlockState(net.minecraft.world.level.block.state.BlockState prev) {
        if (changeBlockStateEventStatic != null) {
            return ((WrappedBlockState) changeBlockStateEventStatic.getNewBlockState()).state();
        } else {
            return prev;
        }
    }

    @Inject(method = "fallOn", at = @At("TAIL"))
    public void nullifyChangeBlockStateEvent(Level level, BlockPos blockPos, Entity entity, float f, CallbackInfo ci) {
        if (changeBlockStateEvent != null) {
            PlayerChangeBlockStateEvent.BACKEND.invokeEndFunctions(changeBlockStateEvent);
            changeBlockStateEventStatic = null;
            changeBlockStateEvent = null;
        }
    }
}
