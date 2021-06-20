package com.combatreforged.factory.builder.mixin.server.players;

import net.minecraft.server.ServerScoreboard;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.players.PlayerList;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(PlayerList.class)
public interface PlayerListAccessor {
    @Invoker
    void invokeUpdateEntireScoreboard(ServerScoreboard serverScoreboard, ServerPlayer serverPlayer);
}
