package com.combatreforged.factory.api.world.scoreboard;

import com.combatreforged.factory.api.builder.Builder;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public interface Scoreboard {
    List<ScoreboardObjective> getAllObjectives();
    List<ScoreboardTeam> getAllTeams();
    List<ScoreboardScore> getAllScores(String name);
    List<ScoreboardScore> getAllScores(ScoreboardObjective objective);

    boolean hasObjective(String name);
    @Nullable ScoreboardObjective getObjective(String name);
    default ScoreboardObjective addObjective(String name) {
        return addObjective(name, "dummy");
    }
    ScoreboardObjective addObjective(String name, String criteria);
    void removeObjective(String name);
    void removeObjective(ScoreboardObjective objective);

    @Nullable ScoreboardObjective getDisplayedObjective(String slot);
    void setDisplayedObjective(String objectiveName, String slot);
    void setDisplayedObjective(@Nullable ScoreboardObjective objective, String slot);
    void clearDisplay(String slot);

    boolean hasScore(String name, ScoreboardObjective objective);
    ScoreboardScore getOrCreateScore(String name, ScoreboardObjective objective);
    void resetScore(String name, ScoreboardObjective objective);


    boolean hasTeam(String name);
    @Nullable ScoreboardTeam getTeam(String name);
    ScoreboardTeam addTeam(String name);
    void removeTeam(String name);
    void removeTeam(ScoreboardTeam team);

    void setTeam(String name, @Nullable ScoreboardTeam team);
    default void leaveTeam(String name) {
        setTeam(name, null);
    }

    static Scoreboard create() {
        return Builder.getInstance().createScoreboard();
    }
}
