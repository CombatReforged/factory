package com.combatreforged.factory.builder.implementation.world.scoreboard;

import com.combatreforged.factory.api.world.scoreboard.Scoreboard;
import com.combatreforged.factory.api.world.scoreboard.ScoreboardObjective;
import com.combatreforged.factory.builder.extension.world.scores.ObjectiveExtension;
import com.combatreforged.factory.builder.implementation.Wrapped;
import com.combatreforged.factory.builder.implementation.util.ObjectMappings;
import net.kyori.adventure.text.Component;
import net.minecraft.world.scores.Objective;
import net.minecraft.world.scores.criteria.ObjectiveCriteria;

public class WrappedScoreboardObjective extends Wrapped<Objective> implements ScoreboardObjective {
    public WrappedScoreboardObjective(Objective wrapped) {
        super(wrapped);
    }

    @Override
    public String getName() {
        return wrapped.getName();
    }

    @Override
    public Scoreboard getScoreboard() {
        return Wrapped.wrap(((ObjectiveExtension) wrapped).getScoreboardServer(), WrappedScoreboard.class);
    }

    @Override
    public String getCriteria() {
        return wrapped.getCriteria().getName();
    }

    @Override
    public Component getDisplayName() {
        return ObjectMappings.convertComponent(wrapped.getDisplayName());
    }

    @Override
    public void setDisplayName(Component displayName) {
        wrapped.setDisplayName(ObjectMappings.convertComponent(displayName));
    }

    @Override
    public RenderType getRenderType() {
        if (wrapped.getRenderType() == ObjectiveCriteria.RenderType.HEARTS) {
            return RenderType.HEARTS;
        }
        return RenderType.NUMBER;
    }

    @Override
    public void setRenderType(RenderType type) {
        ObjectiveCriteria.RenderType mcType;
        if (type == RenderType.HEARTS) {
            mcType = ObjectiveCriteria.RenderType.HEARTS;
        }
        else {
            mcType = ObjectiveCriteria.RenderType.INTEGER;
        }
        wrapped.setRenderType(mcType);
    }
}
