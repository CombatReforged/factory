package com.combatreforged.factory.builder.extension.server.level;

import net.minecraft.network.chat.Component;
import net.minecraft.server.ServerScoreboard;
import net.minecraft.server.level.ServerPlayer;

import java.util.List;

public interface ServerPlayerExtension {
    ServerScoreboard getScoreboard();
    void setScoreboard(ServerScoreboard scoreboard);

    boolean hasDeathEventHappened();
    boolean isKeepExp();
    boolean isKeepInv();

    List<ServerPlayer> getHiddenInTabList();
    void showInTabList(ServerPlayer player, boolean show, boolean update);

    Component getLastContainerMenuTitle();
}
