package com.combatreforged.factory.builder.mixin._bugfixes;

import com.combatreforged.factory.builder.extension.server.FixUtils;
import com.mojang.authlib.GameProfile;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.Level;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.Slice;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerPlayer.class)
public abstract class ServerPlayerMixin extends Player {
    public ServerPlayerMixin(Level level, BlockPos blockPos, float f, GameProfile gameProfile) {
        super(level, blockPos, f, gameProfile);
    }

    // This fixes absorption hearts and effect particles not updating correctly when they are cleared during a dimension change.
    @Inject(method = "teleportTo(Lnet/minecraft/server/level/ServerLevel;DDDFF)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/level/ServerPlayer;setLevel(Lnet/minecraft/world/level/Level;)V", shift = At.Shift.AFTER))
    public void sendNBT(ServerLevel serverLevel, double d, double e, double f, float g, float h, CallbackInfo ci) {
        FixUtils.updateDelayed((ServerPlayer) (Object) this);
    }

    // Prevents the redundant reassign of containerMenu to inventoryMenu as the closeContainer() method already assigns it
    @Redirect(method = "tick", slice = @Slice(from = @At(value = "INVOKE", target = "Lnet/minecraft/server/level/ServerPlayer;closeContainer()V")), at = @At(value = "FIELD", target = "Lnet/minecraft/server/level/ServerPlayer;containerMenu:Lnet/minecraft/world/inventory/AbstractContainerMenu;", opcode = Opcodes.PUTFIELD, ordinal = 0))
    public void preventReassign(ServerPlayer instance, AbstractContainerMenu value) {
    }
}
