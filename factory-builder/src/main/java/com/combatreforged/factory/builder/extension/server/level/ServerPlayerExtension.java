package com.combatreforged.factory.builder.extension.server.level;

import net.minecraft.server.ServerScoreboard;

public interface ServerPlayerExtension {
    ServerScoreboard getScoreboard();
    void setScoreboard(ServerScoreboard scoreboard);

    boolean hasDeathEventHappened();
    boolean isKeepExp();
    boolean isKeepInv();
}
