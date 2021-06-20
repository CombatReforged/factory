package com.combatreforged.factory.builder.mixin.server.level;

import com.combatreforged.factory.builder.extension.server.level.ServerPlayerExtension;
import com.combatreforged.factory.builder.mixin.server.players.PlayerListAccessor;
import com.mojang.authlib.GameProfile;
import net.minecraft.network.protocol.game.ClientboundSetObjectivePacket;
import net.minecraft.network.protocol.game.ClientboundSetPlayerTeamPacket;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.ServerScoreboard;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.level.ServerPlayerGameMode;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import net.minecraft.world.scores.Objective;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerPlayer.class)
public abstract class ServerPlayerMixin implements ServerPlayerExtension {
    @Shadow public ServerGamePacketListenerImpl connection;
    @Shadow @Final public MinecraftServer server;
    private ServerScoreboard scoreboard;

    @Inject(method = "<init>", at = @At("TAIL"))
    public void injectScoreboard(MinecraftServer minecraftServer, ServerLevel serverLevel, GameProfile gameProfile, ServerPlayerGameMode serverPlayerGameMode, CallbackInfo ci) {
        this.scoreboard = minecraftServer.getScoreboard();
    }

    @Override
    public ServerScoreboard getScoreboard() {
        return this.scoreboard;
    }

    @Override
    public void setScoreboard(ServerScoreboard newScoreboard) {
        scoreboard.getPlayerTeams().forEach(team -> this.connection.send(new ClientboundSetPlayerTeamPacket(team, 1)));
        for (int i = 0; i < 19; i++) {
            Objective objective = scoreboard.getDisplayObjective(i);
            if (objective != null) {
                this.connection.send(new ClientboundSetObjectivePacket(objective, 1));
            }
        }
        this.scoreboard = newScoreboard;
        ((PlayerListAccessor) this.server.getPlayerList()).invokeUpdateEntireScoreboard(scoreboard, (ServerPlayer) (Object) this);
    }
}
