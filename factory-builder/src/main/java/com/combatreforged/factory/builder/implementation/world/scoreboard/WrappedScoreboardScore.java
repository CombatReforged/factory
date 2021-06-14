package com.combatreforged.factory.builder.implementation.world.scoreboard;

import com.combatreforged.factory.api.world.scoreboard.ScoreboardScore;
import com.combatreforged.factory.builder.implementation.Wrapped;
import net.minecraft.world.scores.Score;

public class WrappedScoreboardScore extends Wrapped<Score> implements ScoreboardScore {
    public WrappedScoreboardScore(Score wrapped) {
        super(wrapped);
    }
}
