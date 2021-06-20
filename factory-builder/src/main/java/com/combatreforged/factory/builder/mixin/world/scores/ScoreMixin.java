package com.combatreforged.factory.builder.mixin.world.scores;

import com.combatreforged.factory.api.world.scoreboard.ScoreboardScore;
import com.combatreforged.factory.builder.extension.wrap.Wrap;
import com.combatreforged.factory.builder.implementation.world.scoreboard.WrappedScoreboardScore;
import net.minecraft.world.scores.Score;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Score.class)
public abstract class ScoreMixin implements Wrap<ScoreboardScore> {
    private WrappedScoreboardScore wrapped;

    @Inject(method = "<init>*", at = @At("TAIL"))
    public void injectWrapped(CallbackInfo ci) {
        this.wrapped = new WrappedScoreboardScore((Score) (Object) this);
    }

    @Override
    public ScoreboardScore wrap() {
        return wrapped;
    }
}
