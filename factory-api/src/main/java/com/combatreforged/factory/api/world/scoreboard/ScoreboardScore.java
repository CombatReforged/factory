package com.combatreforged.factory.api.world.scoreboard;

import org.jetbrains.annotations.Nullable;

public interface ScoreboardScore {
    Scoreboard getScoreboard();
    @Nullable ScoreboardObjective getObjective();
    String getOwner();
    boolean isLocked();
    void setLocked(boolean locked);
    int get();
    void set(int score);
    void add(int i);
    void multiply(int i);
    void reset();
}
