package com.combatreforged.factory.builder.mixin.server.network;

import com.combatreforged.factory.api.event.player.PlayerDisconnectEvent;
import com.combatreforged.factory.api.event.player.PlayerMoveEvent;
import com.combatreforged.factory.api.world.entity.player.Player;
import com.combatreforged.factory.api.world.util.Location;
import com.combatreforged.factory.builder.implementation.Wrapped;
import com.combatreforged.factory.builder.implementation.util.Conversion;
import com.combatreforged.factory.builder.implementation.world.entity.player.WrappedPlayer;
import net.minecraft.Util;
import net.minecraft.network.chat.ChatType;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ServerboundMovePlayerPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import net.minecraft.server.players.PlayerList;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.UUID;

@Mixin(ServerGamePacketListenerImpl.class)
public abstract class ServerGamePacketListenerImplMixin {
    @Shadow public ServerPlayer player;

    @Shadow
    private static boolean containsInvalidValues(ServerboundMovePlayerPacket serverboundMovePlayerPacket) {
        return false;
    }

    // BEGIN: DISCONNECT EVENT
    @Redirect(method = "onDisconnect", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/players/PlayerList;broadcastMessage(Lnet/minecraft/network/chat/Component;Lnet/minecraft/network/chat/ChatType;Ljava/util/UUID;)V"))
    public void callPlayerDisconnectEvent(PlayerList playerList, Component component, ChatType chatType, UUID uUID, Component component2) {
        PlayerDisconnectEvent event = new PlayerDisconnectEvent(Wrapped.wrap(this.player, WrappedPlayer.class),
                Conversion.convertComponent(component),
                Conversion.convertComponent(component2));
        PlayerDisconnectEvent.BACKEND.invoke(event);

        if (event.getLeaveMessage() != null) {
            playerList.broadcastMessage(Conversion.convertComponent(event.getDisconnectReason()), ChatType.SYSTEM, Util.NIL_UUID);
        }
    }
    // END: DISCONNECT EVENT

    // BEGIN: MOVE EVENT

    boolean inject = true;
    @Inject(method = "handleMovePlayer", at = @At(value = "INVOKE", target = "Lnet/minecraft/network/protocol/PacketUtils;ensureRunningOnSameThread(Lnet/minecraft/network/protocol/Packet;Lnet/minecraft/network/PacketListener;Lnet/minecraft/server/level/ServerLevel;)V", shift = At.Shift.AFTER))
    public void injectPlayerMoveEvent(ServerboundMovePlayerPacket packet, CallbackInfo ci) {
        if (!containsInvalidValues(packet) && inject) {
            Player player = Wrapped.wrap(this.player, WrappedPlayer.class);
            Location oldLocation = player.getLocation().copy();
            Location newLocation = new Location(
                    packet.getX(oldLocation.getX()),
                    packet.getY(oldLocation.getY()),
                    packet.getZ(oldLocation.getZ()),
                    packet.getYRot(oldLocation.getYaw()),
                    packet.getXRot(oldLocation.getPitch()),
                    player.getWorld());

            double[] movDif = {
                    newLocation.getX() - oldLocation.getX(),
                    newLocation.getY() - oldLocation.getY(),
                    newLocation.getZ() - oldLocation.getZ(),
                    newLocation.getYaw() - oldLocation.getYaw(),
                    newLocation.getPitch() - oldLocation.getPitch()
            };
            boolean hasDif = false;
            for (double d : movDif) {
                if (Math.abs(d) >= 0.001) {
                    hasDif = true;
                    break;
                }
            }
            if (!hasDif) return;

            PlayerMoveEvent event = new PlayerMoveEvent(player, oldLocation, newLocation.copy(), false);
            PlayerMoveEvent.BACKEND.invoke(event);

            if (event.isCancelled()) {
                inject = false;
                player.teleport(oldLocation);
            }
            else if (!event.getNewPosition().equals(newLocation)) {
                inject = false;
                player.teleport(event.getNewPosition());
            }
        }
        else {
            inject = true;
        }
    }
}
