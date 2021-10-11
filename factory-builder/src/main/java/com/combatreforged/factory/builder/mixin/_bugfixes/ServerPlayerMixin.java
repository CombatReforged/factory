package com.combatreforged.factory.builder.mixin._bugfixes;

import com.combatreforged.factory.builder.extension.server.FixUtils;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

// This fixes absorption hearts and effect particles not updating correctly when they are cleared during a dimension change.
@Mixin(ServerPlayer.class)
public abstract class ServerPlayerMixin {
    @Inject(method = "teleportTo(Lnet/minecraft/server/level/ServerLevel;DDDFF)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/level/ServerPlayer;setLevel(Lnet/minecraft/world/level/Level;)V", shift = At.Shift.AFTER))
    public void sendNBT(ServerLevel serverLevel, double d, double e, double f, float g, float h, CallbackInfo ci) {
        FixUtils.updateDelayed((ServerPlayer) (Object) this);
    }
}
