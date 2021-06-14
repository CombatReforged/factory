package com.combatreforged.factory.builder.implementation.world.scoreboard;

import com.combatreforged.factory.api.world.scoreboard.ScoreboardObjective;
import com.combatreforged.factory.builder.implementation.Wrapped;
import net.minecraft.world.scores.Objective;

public class WrappedScoreboardObjective extends Wrapped<Objective> implements ScoreboardObjective {
    public WrappedScoreboardObjective(Objective wrapped) {
        super(wrapped);
    }

    @Override
    public String getName() {
        return wrapped.getName();
    }
}
