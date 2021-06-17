package com.combatreforged.factory.api.world.scoreboard;

import net.kyori.adventure.text.Component;

import java.util.Set;

public interface ScoreboardTeam {
    Scoreboard getScoreboard();
    String getName();
    Set<String> getPlayers();
    Component getDisplayName();
    void setDisplayName(Component displayName);
    Component getPrefix();
    void setPrefix(Component prefix);
    Component getSuffix();
    void setSuffix(Component suffix);
    boolean isFriendlyFire();
    void setFriendlyFire(boolean friendlyFire);
    boolean canSeeInvisibleMates();
    void setSeeInvisibleMates(boolean seeInvisibleMates);

    //TODO rest
}
