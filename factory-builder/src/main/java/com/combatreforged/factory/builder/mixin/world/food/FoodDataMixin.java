package com.combatreforged.factory.builder.mixin.world.food;

import com.combatreforged.factory.api.event.player.PlayerFoodLevelsChangeEvent;
import com.combatreforged.factory.builder.extension.world.food.FoodDataExtension;
import com.combatreforged.factory.builder.implementation.Wrapped;
import com.combatreforged.factory.builder.implementation.world.entity.player.WrappedPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodData;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(FoodData.class)
public abstract class FoodDataMixin implements FoodDataExtension {
    @Unique private Player player;
    @Shadow private float exhaustionLevel;
    @Shadow @SuppressWarnings("unused") private float saturationLevel;

    @Shadow private int foodLevel;

    @Override
    public void setExhaustion(float exhaustion) {
        this.exhaustionLevel = exhaustion;
    }

    @Override
    public float getExhaustion() {
        return this.exhaustionLevel;
    }

    @Override
    public void setSaturationServer(float saturation) {
        this.saturationLevel = saturation;
    }

    @Override
    public void setPlayer(Player player) {
        this.player = player;
    }

    @Override
    public Player getPlayer() {
        return player;
    }

    @Redirect(method = "*", at = @At(value = "FIELD", opcode = Opcodes.PUTFIELD, target = "Lnet/minecraft/world/food/FoodData;saturationLevel:F"))
    public void onSaturationChange(FoodData foodData, float value) {
        if (value != this.saturationLevel && this.player != null) {
            PlayerFoodLevelsChangeEvent event = new PlayerFoodLevelsChangeEvent(Wrapped.wrap(this.player, WrappedPlayer.class), value, this.foodLevel);
            PlayerFoodLevelsChangeEvent.BACKEND.invoke(event);

            if (!event.isCancelled()) {
                this.saturationLevel = event.getSaturation();
                this.foodLevel = event.getFoodLevel();
            }

            PlayerFoodLevelsChangeEvent.BACKEND.invokeEndFunctions(event);
        }
    }

    @Redirect(method = "*", at = @At(value = "FIELD", opcode = Opcodes.PUTFIELD, target = "Lnet/minecraft/world/food/FoodData;foodLevel:I"))
    public void onFoodLevelChange(FoodData foodData, int value) {
        if (value != this.foodLevel && this.player != null) {
            PlayerFoodLevelsChangeEvent event = new PlayerFoodLevelsChangeEvent(Wrapped.wrap(this.player, WrappedPlayer.class), this.saturationLevel, value);
            PlayerFoodLevelsChangeEvent.BACKEND.invoke(event);

            if (!event.isCancelled()) {
                this.saturationLevel = event.getSaturation();
                this.foodLevel = event.getFoodLevel();
            }

            PlayerFoodLevelsChangeEvent.BACKEND.invokeEndFunctions(event);
        }
    }
}
