package com.combatreforged.factory.api.world.scoreboard;

import net.kyori.adventure.text.Component;

public interface ScoreboardObjective {
    String getName();
    Scoreboard getScoreboard();
    String getCriteria();
    Component getDisplayName();
    void setDisplayName(Component displayName);
    RenderType getRenderType();
    void setRenderType(RenderType type);

    default boolean hasScore(String name) {
        return this.getScoreboard().hasScore(name, this);
    }
    default ScoreboardScore getOrCreateScore(String name) {
        return this.getScoreboard().getOrCreateScore(name, this);
    }
    default void resetScore(String name) {
        this.getScoreboard().resetScore(name, this);
    }

    enum RenderType {
        HEARTS, NUMBER
    }
}
