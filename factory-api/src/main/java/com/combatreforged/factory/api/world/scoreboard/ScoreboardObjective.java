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

    enum RenderType {
        HEARTS, NUMBER
    }
}
