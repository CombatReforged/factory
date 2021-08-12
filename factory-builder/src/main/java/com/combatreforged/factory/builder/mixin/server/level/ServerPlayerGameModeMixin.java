package com.combatreforged.factory.builder.mixin.server.level;

import com.combatreforged.factory.api.event.player.PlayerBreakBlockEvent;
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
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.level.ServerPlayerGameMode;
import net.minecraft.world.level.GameRules;
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
}
