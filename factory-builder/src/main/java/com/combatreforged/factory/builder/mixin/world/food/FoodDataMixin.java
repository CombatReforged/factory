package com.combatreforged.factory.builder.mixin.world.food;

import com.combatreforged.factory.builder.extension.world.food.FoodDataExtension;
import net.minecraft.world.food.FoodData;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(FoodData.class)
public abstract class FoodDataMixin implements FoodDataExtension {
    @Shadow private float exhaustionLevel;
    @Shadow @SuppressWarnings("unused")
    private float saturationLevel;

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
}
