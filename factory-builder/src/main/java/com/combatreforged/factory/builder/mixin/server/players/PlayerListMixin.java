package com.combatreforged.factory.builder.mixin.server.players;

import com.combatreforged.factory.api.event.player.PlayerJoinEvent;
import com.combatreforged.factory.api.event.player.PlayerRespawnEvent;
import com.combatreforged.factory.api.world.util.Location;
import com.combatreforged.factory.builder.extension.server.MinecraftServerExtension;
import com.combatreforged.factory.builder.extension.server.SelectiveBorderChangeListener;
import com.combatreforged.factory.builder.implementation.Wrapped;
import com.combatreforged.factory.builder.implementation.util.ObjectMappings;
import com.combatreforged.factory.builder.implementation.world.WrappedWorld;
import com.combatreforged.factory.builder.implementation.world.entity.player.WrappedPlayer;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.network.Connection;
import net.minecraft.network.chat.ChatType;
import net.minecraft.network.chat.Component;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.players.PlayerList;
import net.minecraft.world.level.GameType;
import net.minecraft.world.level.border.BorderChangeListener;
import net.minecraft.world.level.border.WorldBorder;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.UUID;

@Mixin(PlayerList.class)
public abstract class PlayerListMixin {
    @Shadow public abstract void broadcastMessage(Component component, ChatType chatType, UUID uUID);

    @Shadow @Final private MinecraftServer server;
    // BEGIN: JOIN EVENT
    Component joinMessage;
    @Redirect(method = "placeNewPlayer", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/players/PlayerList;broadcastMessage(Lnet/minecraft/network/chat/Component;Lnet/minecraft/network/chat/ChatType;Ljava/util/UUID;)V", ordinal = 0))
    public void catchJoinMessage(PlayerList playerList, Component component, ChatType chatType, UUID uUID) {
        joinMessage = component;
    }

    @Inject(method = "placeNewPlayer", at = @At("TAIL"))
    public void callPlayerJoinEvent(Connection connection, ServerPlayer serverPlayer, CallbackInfo ci) {
        PlayerJoinEvent joinEvent = new PlayerJoinEvent(Wrapped.wrap(serverPlayer, WrappedPlayer.class),
                ObjectMappings.convertComponent(joinMessage));
        PlayerJoinEvent.BACKEND.invoke(joinEvent);

        if (joinEvent.getJoinMessage() != null) {
            this.broadcastMessage(ObjectMappings.convertComponent(joinEvent.getJoinMessage()), ChatType.SYSTEM, Util.NIL_UUID);
        }

        PlayerJoinEvent.BACKEND.invokeEndFunctions(joinEvent);
    }
    // END: JOIN EVENT

    // BEGIN: PlayerRespawnEvent
    @Unique private PlayerRespawnEvent respawnEvent;
    @Inject(method = "respawn", at = @At(value = "INVOKE_ASSIGN", target = "Lnet/minecraft/server/MinecraftServer;getLevel(Lnet/minecraft/resources/ResourceKey;)Lnet/minecraft/server/level/ServerLevel;", shift = At.Shift.AFTER), locals = LocalCapture.CAPTURE_FAILEXCEPTION)
    public void injectRespawnEvent(ServerPlayer serverPlayer, boolean arg1, CallbackInfoReturnable<ServerPlayer> cir, BlockPos blockPos, float f, boolean bl2, ServerLevel serverLevel) {
        Location respawnPoint = blockPos != null ? new Location(blockPos.getX(), blockPos.getY(), blockPos.getZ(), f, 0, Wrapped.wrap(serverLevel, WrappedWorld.class)) : null;
        GameType gameType = serverPlayer.gameMode.getGameModeForPlayer();
        this.respawnEvent = new PlayerRespawnEvent(Wrapped.wrap(serverPlayer, WrappedPlayer.class), respawnPoint, bl2, ObjectMappings.GAME_MODES.inverse().get(gameType));
        PlayerRespawnEvent.BACKEND.invoke(respawnEvent);
    }

    @ModifyVariable(method = "respawn", at = @At(value = "INVOKE_ASSIGN", target = "Lnet/minecraft/server/MinecraftServer;getLevel(Lnet/minecraft/resources/ResourceKey;)Lnet/minecraft/server/level/ServerLevel;", ordinal = 0, shift = At.Shift.AFTER))
    public BlockPos modifyBlockPos(BlockPos prev) {
        if (respawnEvent != null) {
            Location respawnLoc = respawnEvent.getSpawnpoint();
            return respawnLoc != null ? new BlockPos(respawnLoc.getX(), respawnLoc.getY(), respawnLoc.getZ()) : null;
        } else {
            return prev;
        }
    }

    @ModifyVariable(method = "respawn", at = @At(value = "INVOKE_ASSIGN", target = "Lnet/minecraft/server/MinecraftServer;getLevel(Lnet/minecraft/resources/ResourceKey;)Lnet/minecraft/server/level/ServerLevel;", ordinal = 0, shift = At.Shift.AFTER))
    public float modifyRespawnAngle(float prev) {
        if (respawnEvent != null) {
            return respawnEvent.getSpawnpoint() != null ? respawnEvent.getSpawnpoint().getYaw() : 0.0f;
        } else {
            return prev;
        }
    }

    @ModifyVariable(method = "respawn", at = @At(value = "INVOKE_ASSIGN", target = "Lnet/minecraft/server/MinecraftServer;getLevel(Lnet/minecraft/resources/ResourceKey;)Lnet/minecraft/server/level/ServerLevel;", ordinal = 0, shift = At.Shift.AFTER), ordinal = 1)
    public boolean modifyIsForced(boolean prev) {
        if (respawnEvent != null) {
            return respawnEvent.isSpawnpointForced();
        } else {
            return prev;
        }
    }

    @ModifyVariable(method = "respawn", at = @At(value = "INVOKE_ASSIGN", target = "Lnet/minecraft/server/MinecraftServer;getLevel(Lnet/minecraft/resources/ResourceKey;)Lnet/minecraft/server/level/ServerLevel;", ordinal = 0, shift = At.Shift.AFTER))
    public ServerLevel modifyLevel(ServerLevel prev) {
        if (respawnEvent != null) {
            return respawnEvent.getSpawnpoint() != null ? ((WrappedWorld) respawnEvent.getSpawnpoint().getWorld()).unwrap() : null;
        } else {
            return prev;
        }
    }

    @Inject(method = "respawn", at = @At(value = "NEW", target = "net/minecraft/server/level/ServerPlayer", shift = At.Shift.BEFORE))
    public void modifyGameMode(ServerPlayer serverPlayer, boolean bl, CallbackInfoReturnable<ServerPlayer> cir) {
        if (respawnEvent != null) {
            serverPlayer.setGameMode(ObjectMappings.GAME_MODES.get(respawnEvent.getRespawnMode()));
        }
    }

    @Inject(method = "respawn", at = @At("TAIL"))
    public void nullifyRespawnEvent(ServerPlayer serverPlayer, boolean bl, CallbackInfoReturnable<ServerPlayer> cir) {
        if (respawnEvent != null) {
            respawnEvent.getPlayer().setGameMode(respawnEvent.getRespawnMode());
            PlayerRespawnEvent.BACKEND.invokeEndFunctions(respawnEvent);
            this.respawnEvent = null;
        }
    }
    // END: PlayerRespawnEvent

    @Redirect(method = "sendLevelInfo", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/MinecraftServer;overworld()Lnet/minecraft/server/level/ServerLevel;"))
    public ServerLevel modifyServerLevel(MinecraftServer instance, ServerPlayer serverPlayer, ServerLevel serverLevel) {
        return ((MinecraftServerExtension) instance).getOverworldForLevel(serverLevel);
    }

    @Redirect(method = "setLevel", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/border/WorldBorder;addListener(Lnet/minecraft/world/level/border/BorderChangeListener;)V"))
    public void changeBorderChangeListener(WorldBorder border, BorderChangeListener prev, ServerLevel level) {
        border.addListener(new SelectiveBorderChangeListener(((MinecraftServerExtension) this.server).getRelatedLevels(level)));
    }
}
