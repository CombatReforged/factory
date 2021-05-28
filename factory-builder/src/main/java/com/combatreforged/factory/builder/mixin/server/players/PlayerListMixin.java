package com.combatreforged.factory.builder.mixin.server.players;

import com.combatreforged.factory.api.event.player.PlayerJoinEvent;
import com.combatreforged.factory.builder.implementation.Wrapped;
import com.combatreforged.factory.builder.implementation.util.ObjectMappings;
import com.combatreforged.factory.builder.implementation.world.entity.player.WrappedPlayer;
import net.minecraft.Util;
import net.minecraft.network.Connection;
import net.minecraft.network.chat.ChatType;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.players.PlayerList;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.UUID;

@Mixin(PlayerList.class)
public abstract class PlayerListMixin {
    @Shadow public abstract void broadcastMessage(Component component, ChatType chatType, UUID uUID);

    // BEGIN: JOIN EVENT
    Component joinMessage;

    @Redirect(method = "placeNewPlayer", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/players/PlayerList;broadcastMessage(Lnet/minecraft/network/chat/Component;Lnet/minecraft/network/chat/ChatType;Ljava/util/UUID;)V", ordinal = 0))
    public void catchJoinMessage(PlayerList playerList, Component component, ChatType chatType, UUID uUID) {
        joinMessage = component;
    }

    @Inject(method = "placeNewPlayer", at = @At("TAIL"))
    public void callPlayerJoinEvent(Connection connection, ServerPlayer serverPlayer, CallbackInfo ci) {
        PlayerJoinEvent event = new PlayerJoinEvent(Wrapped.wrap(serverPlayer, WrappedPlayer.class),
                ObjectMappings.convertComponent(joinMessage));
        PlayerJoinEvent.BACKEND.invoke(event);

        if (event.getJoinMessage() != null) {
            this.broadcastMessage(ObjectMappings.convertComponent(event.getJoinMessage()), ChatType.SYSTEM, Util.NIL_UUID);
        }
    }

    // END: JOIN EVENT
}
