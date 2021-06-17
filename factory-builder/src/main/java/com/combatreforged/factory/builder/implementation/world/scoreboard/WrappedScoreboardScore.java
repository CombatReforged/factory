package com.combatreforged.factory.builder.implementation.world.scoreboard;

import com.combatreforged.factory.api.world.scoreboard.Scoreboard;
import com.combatreforged.factory.api.world.scoreboard.ScoreboardObjective;
import com.combatreforged.factory.api.world.scoreboard.ScoreboardScore;
import com.combatreforged.factory.builder.implementation.Wrapped;
import net.minecraft.world.scores.Score;
import org.jetbrains.annotations.Nullable;

public class WrappedScoreboardScore extends Wrapped<Score> implements ScoreboardScore {
    public WrappedScoreboardScore(Score wrapped) {
        super(wrapped);
    }

    @Override
    public Scoreboard getScoreboard() {
        return Wrapped.wrap(wrapped.getScoreboard(), WrappedScoreboard.class);
    }

    @Override
    public @Nullable ScoreboardObjective getObjective() {
        return Wrapped.wrap(wrapped.getObjective(), WrappedScoreboardObjective.class);
    }

    @Override
    public String getOwner() {
        return wrapped.getOwner();
    }

    @Override
    public boolean isLocked() {
        return wrapped.isLocked();
    }

    @Override
    public void setLocked(boolean locked) {
        wrapped.setLocked(locked);
    }

    @Override
    public int get() {
        return wrapped.getScore();
    }

    @Override
    public void set(int score) {
        wrapped.setScore(score);
    }

    @Override
    public void add(int i) {
        wrapped.add(i);
    }

    @Override
    public void multiply(int i) {
        wrapped.setScore(get() * i);
    }

    @Override
    public void reset() {
        wrapped.reset();
    }
}
