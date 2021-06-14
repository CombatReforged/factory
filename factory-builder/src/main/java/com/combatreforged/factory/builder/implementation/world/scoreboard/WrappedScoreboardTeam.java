package com.combatreforged.factory.builder.implementation.world.scoreboard;

import com.combatreforged.factory.api.world.scoreboard.ScoreboardTeam;
import com.combatreforged.factory.builder.implementation.Wrapped;
import net.minecraft.world.scores.PlayerTeam;

public class WrappedScoreboardTeam extends Wrapped<PlayerTeam> implements ScoreboardTeam {
    public WrappedScoreboardTeam(PlayerTeam wrapped) {
        super(wrapped);
    }

    @Override
    public String getName() {
        return wrapped.getName();
    }
}
