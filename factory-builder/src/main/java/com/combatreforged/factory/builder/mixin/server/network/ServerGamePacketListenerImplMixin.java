package com.combatreforged.factory.builder.mixin.server.network;

import com.combatreforged.factory.api.FactoryAPI;
import com.combatreforged.factory.api.command.CommandSourceInfo;
import com.combatreforged.factory.api.event.player.PlayerChangeMovementStateEvent;
import com.combatreforged.factory.api.event.player.PlayerDisconnectEvent;
import com.combatreforged.factory.api.event.player.PlayerMoveEvent;
import com.combatreforged.factory.api.event.player.PlayerSwitchActiveSlotEvent;
import com.combatreforged.factory.api.world.entity.player.Player;
import com.combatreforged.factory.api.world.util.Location;
import com.combatreforged.factory.builder.extension.server.level.ServerPlayerExtension;
import com.combatreforged.factory.builder.implementation.Wrapped;
import com.combatreforged.factory.builder.implementation.util.ObjectMappings;
import com.combatreforged.factory.builder.implementation.world.entity.player.WrappedPlayer;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.ParseResults;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.suggestion.Suggestions;
import net.minecraft.Util;
import net.minecraft.network.chat.ChatType;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.*;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import net.minecraft.server.players.PlayerList;
import net.minecraft.world.entity.player.Abilities;
import net.minecraft.world.entity.player.Inventory;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.Slice;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.UUID;
import java.util.concurrent.ExecutionException;

@Mixin(ServerGamePacketListenerImpl.class)
public abstract class ServerGamePacketListenerImplMixin {
    @Shadow public ServerPlayer player;

    @Shadow @SuppressWarnings("unused")
    private static boolean containsInvalidValues(ServerboundMovePlayerPacket serverboundMovePlayerPacket) {
        return false;
    }

    @Shadow public abstract void teleport(double d, double e, double f, float g, float h);

    @Shadow @Final private MinecraftServer server;

    @Shadow public abstract void send(Packet<?> packet);

    // BEGIN: DISCONNECT EVENT
    @Unique private PlayerDisconnectEvent disconnectEvent;
    @Redirect(method = "onDisconnect", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/players/PlayerList;broadcastMessage(Lnet/minecraft/network/chat/Component;Lnet/minecraft/network/chat/ChatType;Ljava/util/UUID;)V"))
    public void callPlayerDisconnectEvent(PlayerList playerList, Component component, ChatType chatType, UUID uUID, Component component2) {
        this.disconnectEvent = new PlayerDisconnectEvent(Wrapped.wrap(this.player, WrappedPlayer.class),
                ObjectMappings.convertComponent(component),
                ObjectMappings.convertComponent(component2));
        PlayerDisconnectEvent.BACKEND.invoke(disconnectEvent);

        if (disconnectEvent.getLeaveMessage() != null) {
            playerList.broadcastMessage(ObjectMappings.convertComponent(disconnectEvent.getDisconnectReason()), ChatType.SYSTEM, Util.NIL_UUID);
        }
    }

    @Inject(method = "onDisconnect", at = @At("TAIL"))
    public void nullifyDisconnectEvent(Component component, CallbackInfo ci) {
        PlayerDisconnectEvent.BACKEND.invokeEndFunctions(this.disconnectEvent);
        this.server.getPlayerList().getPlayers().forEach(players -> ((ServerPlayerExtension) players).showInTabList(this.player, true, false));
        this.disconnectEvent = null;
    }
    // END: DISCONNECT EVENT

    // BEGIN: MOVE EVENT
    @Unique private PlayerMoveEvent moveEvent;
    boolean inject = true;
    Location oldLocation;
    @Inject(method = "handleMovePlayer", at = @At(value = "INVOKE", target = "Lnet/minecraft/network/protocol/PacketUtils;ensureRunningOnSameThread(Lnet/minecraft/network/protocol/Packet;Lnet/minecraft/network/PacketListener;Lnet/minecraft/server/level/ServerLevel;)V", shift = At.Shift.AFTER))
    public void injectPlayerMoveEvent(ServerboundMovePlayerPacket packet, CallbackInfo ci) {
        if (!containsInvalidValues(packet) && inject) {
            Player player = Wrapped.wrap(this.player, WrappedPlayer.class);
            if (oldLocation == null) oldLocation = player.getLocation().copy();
            Location newLocation = new Location(
                    packet.getX(oldLocation.getX()),
                    packet.getY(oldLocation.getY()),
                    packet.getZ(oldLocation.getZ()),
                    packet.getYRot(oldLocation.getYaw()),
                    packet.getXRot(oldLocation.getPitch()),
                    player.getWorld());

            double[] movDif = getMovDif(oldLocation, newLocation);
            boolean hasDif = false;
            for (double d : movDif) {
                if (d >= 0.001) {
                    hasDif = true;
                    break;
                }
            }
            if (!hasDif)
                return;

            oldLocation = player.getLocation().copy();

            this.moveEvent = new PlayerMoveEvent(player, oldLocation, newLocation.copy(), false);
            PlayerMoveEvent.BACKEND.invoke(moveEvent);

            if (moveEvent.isCancelled()) {
                inject = false;
                player.teleport(oldLocation);
            }
            else if (!moveEvent.getNewPosition().equals(newLocation)) {
                inject = false;
                Location eventLocation = moveEvent.getNewPosition();
                this.teleport(eventLocation.getX(), eventLocation.getY(), eventLocation.getZ(), eventLocation.getYaw(), eventLocation.getPitch());
            }
        }
        else {
            inject = true;
        }
    }

    @Inject(method = "handleMovePlayer", at = @At("TAIL"))
    public void nullifyMoveEvent(ServerboundMovePlayerPacket serverboundMovePlayerPacket, CallbackInfo ci) {
        if (inject && moveEvent != null) {
            PlayerMoveEvent.BACKEND.invokeEndFunctions(moveEvent);
            this.moveEvent = null;
        }
    }
    
    private static double[] getMovDif(Location first, Location second) {
        return new double[] {
                Math.abs(second.getX() - first.getX()),
                Math.abs(second.getY() - first.getY()),
                Math.abs(second.getZ() - first.getZ()),
                Math.abs(second.getYaw() - first.getYaw()),
                Math.abs(second.getPitch() - first.getPitch())
        };
    }

    // END: MOVE EVENT

    @Redirect(method = "*", at = @At(value = "FIELD", opcode = Opcodes.PUTFIELD, target = "Lnet/minecraft/world/entity/player/Abilities;flying:Z"))
    public void injectChangeMovementStateEvent(Abilities abilities, boolean value) {
        if (abilities == this.player.abilities && value != abilities.flying) {
            Player player = Wrapped.wrap(this.player, WrappedPlayer.class);
            PlayerChangeMovementStateEvent event = new PlayerChangeMovementStateEvent(player, PlayerChangeMovementStateEvent.ChangedState.FLYING, value);
            PlayerChangeMovementStateEvent.BACKEND.invoke(event);

            boolean update = event.isCancelled() || event.getChangedValue() != value;
            this.player.abilities.flying = event.isCancelled() ? this.player.abilities.flying : event.getChangedValue();

            if (update) {
                this.player.connection.send(new ClientboundPlayerAbilitiesPacket(this.player.abilities));
            }

            PlayerChangeMovementStateEvent.BACKEND.invokeEndFunctions(event);
        }
    }

    @Unique private PlayerSwitchActiveSlotEvent switchActiveSlotEvent;
    @Inject(method = "handleSetCarriedItem", at = @At(value = "INVOKE", target = "Lnet/minecraft/network/protocol/PacketUtils;ensureRunningOnSameThread(Lnet/minecraft/network/protocol/Packet;Lnet/minecraft/network/PacketListener;Lnet/minecraft/server/level/ServerLevel;)V", shift = At.Shift.AFTER), cancellable = true)
    public void injectSwitchActiveSlotEvent(ServerboundSetCarriedItemPacket serverboundSetCarriedItemPacket, CallbackInfo ci) {
        if (serverboundSetCarriedItemPacket.getSlot() >= 0 && serverboundSetCarriedItemPacket.getSlot() < Inventory.getSelectionSize()) {
            this.switchActiveSlotEvent = new PlayerSwitchActiveSlotEvent(Wrapped.wrap(player, WrappedPlayer.class), serverboundSetCarriedItemPacket.getSlot());
            PlayerSwitchActiveSlotEvent.BACKEND.invoke(switchActiveSlotEvent);

            if (switchActiveSlotEvent.isCancelled()) {
                player.connection.send(new ClientboundSetCarriedItemPacket(this.player.inventory.selected));
                ci.cancel();
            }
        }
    }

    @Redirect(method = "handleSetCarriedItem", slice = @Slice(
            from = @At(value = "INVOKE", target = "Lnet/minecraft/network/protocol/PacketUtils;ensureRunningOnSameThread(Lnet/minecraft/network/protocol/Packet;Lnet/minecraft/network/PacketListener;Lnet/minecraft/server/level/ServerLevel;)V", shift = At.Shift.AFTER),
            to = @At("TAIL")
    ), at = @At(value = "INVOKE", target = "Lnet/minecraft/network/protocol/game/ServerboundSetCarriedItemPacket;getSlot()I"))
    public int changeCarriedItem(ServerboundSetCarriedItemPacket serverboundSetCarriedItemPacket) {
        return switchActiveSlotEvent != null ? switchActiveSlotEvent.getNewSlot() : serverboundSetCarriedItemPacket.getSlot();
    }

    @Inject(method = "handleSetCarriedItem", at = @At("TAIL"))
    public void nullifySwitchActiveSlotEvent(ServerboundSetCarriedItemPacket serverboundSetCarriedItemPacket, CallbackInfo ci) {
        if (switchActiveSlotEvent != null) {
            if (player.inventory.selected != serverboundSetCarriedItemPacket.getSlot()) {
                player.connection.send(new ClientboundSetCarriedItemPacket(switchActiveSlotEvent.getNewSlot()));
            }
            PlayerSwitchActiveSlotEvent.BACKEND.invokeEndFunctions(switchActiveSlotEvent);
            switchActiveSlotEvent = null;
        }
    }

    // Enable api command suggestion
    @Inject(method = "lambda$handleCustomCommandSuggestions$1", at = @At(value = "INVOKE", target = "Lnet/minecraft/network/Connection;send(Lnet/minecraft/network/protocol/Packet;)V", shift = At.Shift.BEFORE), cancellable = true)
    public void sendVanillaSuggestions(ServerboundCommandSuggestionPacket serverboundCommandSuggestionPacket, Suggestions suggestions, CallbackInfo ci) {
        CommandDispatcher<CommandSourceInfo> apiDispatcher = FactoryAPI.getInstance().getServer().getCommandDispatcher();
        StringReader apiReader = new StringReader(serverboundCommandSuggestionPacket.getCommand());
        if (apiReader.canRead() && apiReader.peek() == '/') {
            apiReader.skip();
        }
        ParseResults<CommandSourceInfo> results = apiDispatcher.parse(apiReader, Wrapped.wrap(this.player, WrappedPlayer.class).createCommandInfo());
        try {
            apiDispatcher.getCompletionSuggestions(results).thenAccept(apiSuggestions -> {
                if (!apiSuggestions.isEmpty()) {
                    this.player.connection.send(new ClientboundCommandSuggestionsPacket(serverboundCommandSuggestionPacket.getId(), apiSuggestions));
                    ci.cancel();
                }
            }).get();
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }
    }
}
