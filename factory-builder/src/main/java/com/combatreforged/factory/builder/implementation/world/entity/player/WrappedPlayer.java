package com.combatreforged.factory.builder.implementation.world.entity.player;

import com.combatreforged.factory.api.world.entity.player.Player;
import com.combatreforged.factory.builder.extension.FoodDataExtension;
import com.combatreforged.factory.builder.implementation.world.entity.WrappedLivingEntity;
import net.minecraft.server.level.ServerPlayer;

public class WrappedPlayer extends WrappedLivingEntity implements Player {
    final ServerPlayer wrapped;
    public WrappedPlayer(ServerPlayer wrapped) {
        super(wrapped);
        this.wrapped = wrapped;
    }

    @Override
    public int getFoodLevel() {
        return wrapped.getFoodData().getFoodLevel();
    }

    @Override
    public float getSaturation() {
        return wrapped.getFoodData().getSaturationLevel();
    }

    @Override
    public void setFoodLevel(int level) {
        wrapped.getFoodData().setFoodLevel(level);
    }

    @Override
    public void setSaturation(float saturation) {
        wrapped.getFoodData().setSaturation(saturation);
    }

    @Override
    public float getExhaustion() {
        return ((FoodDataExtension) wrapped.getFoodData()).getExhaustion();
    }

    @Override
    public void setExhaustion(float exhaustion) {
        ((FoodDataExtension) wrapped.getFoodData()).setExhaustion(exhaustion);
    }
}
