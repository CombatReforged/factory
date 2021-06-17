package com.combatreforged.factory.builder.mixin.server.level;

import com.combatreforged.factory.builder.extension.server.level.ServerPlayerExtension;
import com.mojang.authlib.GameProfile;
import net.minecraft.network.protocol.game.ClientboundSetDisplayObjectivePacket;
import net.minecraft.network.protocol.game.ClientboundSetObjectivePacket;
import net.minecraft.network.protocol.game.ClientboundSetPlayerTeamPacket;
import net.minecraft.network.protocol.game.ClientboundSetScorePacket;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.level.ServerPlayerGameMode;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import net.minecraft.world.scores.Objective;
import net.minecraft.world.scores.PlayerTeam;
import net.minecraft.world.scores.Scoreboard;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerPlayer.class)
public abstract class ServerPlayerMixin implements ServerPlayerExtension {
    @Shadow public ServerGamePacketListenerImpl connection;
    private Scoreboard scoreboard;

    @Inject(method = "<init>", at = @At("TAIL"))
    public void injectScoreboard(MinecraftServer minecraftServer, ServerLevel serverLevel, GameProfile gameProfile, ServerPlayerGameMode serverPlayerGameMode, CallbackInfo ci) {
        this.scoreboard = minecraftServer.getScoreboard();
    }

    @Override
    public Scoreboard getScoreboard() {
        return this.scoreboard;
    }

    @Override
    public void setScoreboard(Scoreboard scoreboard) {
        scoreboard.getPlayerTeams().forEach(team -> this.connection.send(new ClientboundSetPlayerTeamPacket(team, 1)));
        scoreboard.getObjectives().forEach(objective -> {
            //TODO Filter to only tracked
            this.connection.send(new ClientboundSetObjectivePacket(objective, 1));
        });
        this.scoreboard = scoreboard;
        //TODO finish
    }
}
