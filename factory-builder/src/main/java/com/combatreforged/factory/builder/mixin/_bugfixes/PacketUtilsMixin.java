package com.combatreforged.factory.builder.mixin._bugfixes;

import com.combatreforged.factory.api.FactoryAPI;
import com.combatreforged.factory.builder.implementation.WrappedFactoryServer;
import net.minecraft.network.PacketListener;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.PacketUtils;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.RunningOnDifferentThreadException;
import net.minecraft.util.thread.BlockableEventLoop;
import org.apache.logging.log4j.Logger;
import org.spongepowered.asm.mixin.*;

@SuppressWarnings("InvalidBlockTag")
@Mixin(PacketUtils.class)
public class PacketUtilsMixin {
    @Shadow @Final private static Logger LOGGER;
    @Unique private static MinecraftServer SERVER = null;

    /**
     * Fixes the stack overflow exception that sometimes happens when stopping the server.
     * Courtesy Spigot (https://hub.spigotmc.org/stash/projects/SPIGOT/repos/craftbukkit/browse/nms-patches/net/minecraft/network/protocol/PlayerConnectionUtils.patch)
     * @author rizecookey
     * @reason The automatic refmap creation doesn't handle lambda targets correctly, so we'll have to overwrite
     */
    @Overwrite
    public static <T extends PacketListener> void ensureRunningOnSameThread(Packet<T> packet, T packetListener, BlockableEventLoop<?> blockableEventLoop) throws RunningOnDifferentThreadException {
        if (!blockableEventLoop.isSameThread()) {
            blockableEventLoop.execute(() -> {
                // start change
                if (getServer().isStopped() || ((ConnectionAccessor) packetListener.getConnection()).getDisconnectionHandled()) {
                    return;
                } // end change
                if (packetListener.getConnection().isConnected()) {
                    packet.handle(packetListener);
                } else {
                    LOGGER.debug("Ignoring packet due to disconnection: " + packet);
                }

            });
            throw RunningOnDifferentThreadException.RUNNING_ON_DIFFERENT_THREAD;
        }
        // start change
        else if (getServer().isStopped() || ((ConnectionAccessor) packetListener.getConnection()).getDisconnectionHandled()) {
            throw RunningOnDifferentThreadException.RUNNING_ON_DIFFERENT_THREAD;
        }
        // end change
    }

    private static MinecraftServer getServer() {
        return SERVER == null ? (SERVER = ((WrappedFactoryServer) FactoryAPI.getInstance().getServer()).unwrap()) : SERVER;
    }
}
