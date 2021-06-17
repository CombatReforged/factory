package com.combatreforged.factory.builder.extension.server.level;

import net.minecraft.world.scores.Scoreboard;

public interface ServerPlayerExtension {
    Scoreboard getScoreboard();
    void setScoreboard(Scoreboard scoreboard);
}
