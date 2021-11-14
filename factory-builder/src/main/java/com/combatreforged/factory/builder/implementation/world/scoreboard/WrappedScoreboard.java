package com.combatreforged.factory.builder.implementation.world.scoreboard;

import com.combatreforged.factory.api.world.scoreboard.Scoreboard;
import com.combatreforged.factory.api.world.scoreboard.ScoreboardObjective;
import com.combatreforged.factory.api.world.scoreboard.ScoreboardScore;
import com.combatreforged.factory.api.world.scoreboard.ScoreboardTeam;
import com.combatreforged.factory.builder.implementation.Wrapped;
import com.google.common.collect.ImmutableList;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.world.scores.Objective;
import net.minecraft.world.scores.criteria.ObjectiveCriteria;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class WrappedScoreboard extends Wrapped<net.minecraft.world.scores.Scoreboard> implements Scoreboard {
    public WrappedScoreboard(net.minecraft.world.scores.Scoreboard wrapped) {
        super(wrapped);
    }

    @Override
    public List<ScoreboardObjective> getAllObjectives() {
        return wrapped.getObjectives().stream()
                .map(mcObj -> Wrapped.wrap(mcObj, WrappedScoreboardObjective.class))
                .collect(ImmutableList.toImmutableList());
    }

    @Override
    public List<ScoreboardTeam> getAllTeams() {
        return wrapped.getPlayerTeams().stream()
                .map(mcTeam -> Wrapped.wrap(mcTeam, WrappedScoreboardTeam.class))
                .collect(ImmutableList.toImmutableList());
    }

    @Override
    public List<ScoreboardScore> getAllScores(String name) {
        return wrapped.getPlayerScores(name).values().stream()
                .map(mcScore -> Wrapped.wrap(mcScore, WrappedScoreboardScore.class))
                .collect(ImmutableList.toImmutableList());
    }

    @Override
    public List<ScoreboardScore> getAllScores(ScoreboardObjective objective) {
        return wrapped.getPlayerScores(((WrappedScoreboardObjective) objective).unwrap()).stream()
                .map(mcScore -> Wrapped.wrap(mcScore, WrappedScoreboardScore.class))
                .collect(ImmutableList.toImmutableList());
    }

    @Override
    public boolean hasObjective(String name) {
        return wrapped.hasObjective(name);
    }

    @Override
    public @Nullable ScoreboardObjective getObjective(String name) {
        return Wrapped.wrap(wrapped.getObjective(name), WrappedScoreboardObjective.class);
    }

    @Override
    public ScoreboardObjective addObjective(String name, String criteria) {
        return Wrapped.wrap(wrapped.addObjective(name, ObjectiveCriteria.byName(criteria)
                        .orElseThrow(() -> new IllegalStateException("Invalid criteria: " + criteria)),
                new TextComponent(name), ObjectiveCriteria.RenderType.INTEGER),
                WrappedScoreboardObjective.class);
    }

    @Override
    public void removeObjective(String name) {
        Objective obj;
        if ((obj = wrapped.getObjective(name)) != null) {
            wrapped.removeObjective(obj);
        }
    }

    @Override
    public void removeObjective(ScoreboardObjective objective) {
        validateObjective(objective);
        wrapped.removeObjective(((WrappedScoreboardObjective) objective).unwrap());
    }

    @Override
    public ScoreboardObjective getDisplayedObjective(String slot) {
        validateSlot(slot);
        return Wrapped.wrap(wrapped.getDisplayObjective(net.minecraft.world.scores.Scoreboard.getDisplaySlotByName(slot)),
                WrappedScoreboardObjective.class);
    }

    @Override
    public void setDisplayedObjective(String objectiveName, String slot) {
        validateObjective(objectiveName);
        this.setDisplayedObjective(Wrapped.wrap(wrapped.getObjective(objectiveName), WrappedScoreboardObjective.class), slot);
    }

    @Override
    public void setDisplayedObjective(@Nullable ScoreboardObjective objective, String slot) {
        int slotID = net.minecraft.world.scores.Scoreboard.getDisplaySlotByName(validateSlot(slot));
        if (objective == null) {
            wrapped.setDisplayObjective(slotID, null);
            return;
        }
        Objective mcObjective = ((WrappedScoreboardObjective) validateObjective(objective)).unwrap();
        wrapped.setDisplayObjective(slotID, mcObjective);
    }

    @Override
    public void clearDisplay(String slot) {
        int slotID = net.minecraft.world.scores.Scoreboard.getDisplaySlotByName(validateSlot(slot));
        wrapped.setDisplayObjective(slotID, null);
    }

    @Override
    public boolean hasScore(String name, ScoreboardObjective objective) {
        return wrapped.hasPlayerScore(name, ((WrappedScoreboardObjective) validateObjective(objective)).unwrap());
    }

    @Override
    public ScoreboardScore getOrCreateScore(String name, ScoreboardObjective objective) {
        return Wrapped.wrap(wrapped.getOrCreatePlayerScore(name, ((WrappedScoreboardObjective) validateObjective(objective)).unwrap()),
                WrappedScoreboardScore.class);
    }

    @Override
    public void resetScore(String name, ScoreboardObjective objective) {
        wrapped.resetPlayerScore(name, ((WrappedScoreboardObjective) validateObjective(objective)).unwrap());
    }

    @Override
    public boolean hasTeam(String name) {
        return wrapped.getPlayerTeam(name) != null;
    }

    @Override
    @Nullable public ScoreboardTeam getTeam(String name) {
        return Wrapped.wrap(wrapped.getPlayerTeam(name), WrappedScoreboardTeam.class);
    }

    @Override
    public ScoreboardTeam addTeam(String name) {
        return Wrapped.wrap(wrapped.addPlayerTeam(name), WrappedScoreboardTeam.class);
    }

    @Override
    public void removeTeam(String name) {
        this.removeTeam(validateTeam(this.getTeam(name)));
    }

    @Override
    public void removeTeam(ScoreboardTeam team) {
        wrapped.removePlayerTeam(((WrappedScoreboardTeam) validateTeam(team)).unwrap());
    }

    @Override
    public void setTeam(String name, @Nullable ScoreboardTeam team) {
        if (team == null) {
            wrapped.removePlayerFromTeam(name);
        }
        else {
            wrapped.addPlayerToTeam(name, ((WrappedScoreboardTeam) validateTeam(team)).unwrap());
        }
    }

    private String validateSlot(String slot) {
        if (net.minecraft.world.scores.Scoreboard.getDisplaySlotByName(slot) == -1 || slot == null) {
            throw new IllegalStateException("Not a valid slot: " + slot);
        }
        return slot;
    }

    private String validateObjective(String name) {
        if (!wrapped.hasObjective(name) || name == null) {
            throw new IllegalStateException("Objective does not exist on this scoreboard: " + name);
        }
        return name;
    }

    private Objective validateObjective(Objective objective) {
        if (objective == null) {
            throw new IllegalStateException("Objective is null");
        }
        if (!wrapped.getObjectives().contains(objective)) {
            throw new IllegalStateException("Objective does not exist on this scoreboard: " + objective.getName());
        }
        return objective;
    }

    private ScoreboardObjective validateObjective(ScoreboardObjective objective) {
        validateObjective(((WrappedScoreboardObjective) objective).unwrap());
        return objective;
    }

    private ScoreboardTeam validateTeam(ScoreboardTeam team) {
        if (team == null) {
            throw new IllegalStateException("ScoreboardTeam is null");
        }
        if (!this.getAllTeams().contains(team)) {
            throw new IllegalStateException("Team does not exist on this scoreboard: " + team.getName());
        }

        return team;
    }
}
