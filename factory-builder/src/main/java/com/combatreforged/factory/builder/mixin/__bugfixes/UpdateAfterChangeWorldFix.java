package com.combatreforged.factory.builder.mixin.__bugfixes;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerPlayer.class)
public abstract class UpdateAfterChangeWorldFix extends LivingEntity {
    protected UpdateAfterChangeWorldFix(EntityType<? extends LivingEntity> entityType, Level level) {
        super(entityType, level);
    }

    @Inject(method = "teleportTo(Lnet/minecraft/server/level/ServerLevel;DDDFF)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/level/ServerPlayer;setLevel(Lnet/minecraft/world/level/Level;)V", shift = At.Shift.AFTER))
    private void sendNBT(ServerLevel serverLevel, double d, double e, double f, float g, float h, CallbackInfo ci) {
        FixUtils.updateDelayed((ServerPlayer) (Object) this);
    }
}
