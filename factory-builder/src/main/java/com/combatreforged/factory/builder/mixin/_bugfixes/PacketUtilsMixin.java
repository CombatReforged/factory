package com.combatreforged.factory.builder.mixin._bugfixes;

import com.combatreforged.factory.api.FactoryAPI;
import com.combatreforged.factory.builder.implementation.WrappedFactoryServer;
import net.minecraft.network.PacketListener;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.PacketUtils;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.RunningOnDifferentThreadException;
import net.minecraft.util.thread.BlockableEventLoop;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

// Fixes the stack overflow exception that sometimes happens when stopping the server. Courtesy Spigot (https://hub.spigotmc.org/stash/projects/SPIGOT/repos/craftbukkit/browse/nms-patches/net/minecraft/network/protocol/PlayerConnectionUtils.patch)
@Mixin(PacketUtils.class)
public class PacketUtilsMixin {
    @Unique private static MinecraftServer SERVER = null;
    @SuppressWarnings("unused")
    @Inject(method = "lambda$ensureRunningOnSameThread$0", at = @At(value = "INVOKE", target = "Lnet/minecraft/network/PacketListener;getConnection()Lnet/minecraft/network/Connection;", shift = At.Shift.BEFORE), cancellable = true)
    private static void preventStackOverflowSchedule(PacketListener packetListener, Packet<?> packet, CallbackInfo ci) {
        if (getServer().isStopped() || ((ConnectionAccessor) packetListener.getConnection()).getDisconnectionHandled()) {
            ci.cancel();
        }
    }

    @SuppressWarnings("unused")
    @Inject(method = "ensureRunningOnSameThread(Lnet/minecraft/network/protocol/Packet;Lnet/minecraft/network/PacketListener;Lnet/minecraft/util/thread/BlockableEventLoop;)V", at = @At("TAIL"))
    private static <T extends PacketListener> void preventStackOverflowEnd(Packet<T> packet, T packetListener, BlockableEventLoop<?> blockableEventLoop, CallbackInfo ci) {
        if (getServer().isStopped() || ((ConnectionAccessor) packetListener.getConnection()).getDisconnectionHandled()) {
            throw RunningOnDifferentThreadException.RUNNING_ON_DIFFERENT_THREAD;
        }
    }

    private static MinecraftServer getServer() {
        return SERVER == null ? (SERVER = ((WrappedFactoryServer) FactoryAPI.getInstance().getServer()).unwrap()) : SERVER;
    }
}
