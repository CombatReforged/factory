package com.combatreforged.factory.builder.mixin.server;

import com.combatreforged.factory.builder.extension.server.level.ServerPlayerExtension;
import net.minecraft.network.protocol.Packet;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.ServerScoreboard;
import net.minecraft.server.players.PlayerList;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(ServerScoreboard.class)
public abstract class ServerScoreboardMixin {
    @Shadow @Final private MinecraftServer server;

    @SuppressWarnings("ConstantConditions")
    @Redirect(method = "*", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/players/PlayerList;broadcastAll(Lnet/minecraft/network/protocol/Packet;)V"))
    public void sendPacketsToScoreboardSubscribers(PlayerList playerList, final Packet<?> packet) {
        this.server.getPlayerList().getPlayers().forEach(player -> {
            if (((ServerPlayerExtension) player).getScoreboard() == (Object) this) {
                player.connection.send(packet);
            }
        });
    }
}
