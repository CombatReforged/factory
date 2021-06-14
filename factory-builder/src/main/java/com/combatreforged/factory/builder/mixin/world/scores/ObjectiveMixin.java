package com.combatreforged.factory.builder.mixin.world.scores;

import com.combatreforged.factory.api.world.scoreboard.ScoreboardObjective;
import com.combatreforged.factory.builder.extension.wrap.Wrap;
import com.combatreforged.factory.builder.implementation.world.scoreboard.WrappedScoreboardObjective;
import net.minecraft.network.chat.Component;
import net.minecraft.world.scores.Objective;
import net.minecraft.world.scores.Scoreboard;
import net.minecraft.world.scores.criteria.ObjectiveCriteria;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Objective.class)
public abstract class ObjectiveMixin implements Wrap<ScoreboardObjective> {
    private WrappedScoreboardObjective wrapped;

    @Inject(method = "<init>", at = @At("TAIL"))
    public void injectWrapped(Scoreboard scoreboard, String string, ObjectiveCriteria objectiveCriteria, Component component, ObjectiveCriteria.RenderType renderType, CallbackInfo ci) {
        this.wrapped = new WrappedScoreboardObjective((Objective) (Object) this);
    }

    @Override
    public ScoreboardObjective wrap() {
        return wrapped;
    }
}
