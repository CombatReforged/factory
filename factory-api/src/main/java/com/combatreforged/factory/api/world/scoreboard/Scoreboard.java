package com.combatreforged.factory.api.world.scoreboard;

import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.Nullable;

public interface Scoreboard {
    //TODO

    boolean hasObjective(String name);
    @Nullable ScoreboardObjective getObjective(String name);
    default ScoreboardObjective addObjective(String name) {
        return addObjective(name, "dummy");
    }
    ScoreboardObjective addObjective(String name, String criteria);
    void removeObjective(String name);
    void removeObjective(ScoreboardObjective objective);

    void setDisplayedObjective(@Nullable String objectiveName, String slot);
    void setDisplayedObjective(@Nullable ScoreboardObjective objective, String slot);
    void clearDisplay(String slot);

    ScoreboardScore getScore(String name);

    //TODO finish, implementation


    boolean hasTeam(String name);
    ScoreboardTeam getTeam(String name);
    ScoreboardTeam addTeam(String name);

    void setTeam(String name, @Nullable ScoreboardTeam team);
    default void leaveTeam(String name) {
        setTeam(name, null);
    }

    //TODO creation
}
