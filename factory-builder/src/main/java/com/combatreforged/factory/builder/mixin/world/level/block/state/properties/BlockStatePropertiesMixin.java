package com.combatreforged.factory.builder.mixin.world.level.block.state.properties;

import com.combatreforged.factory.builder.implementation.util.Conversion;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BlockStateProperties.class)
public class BlockStatePropertiesMixin {
    @Inject(method = "<clinit>", at = @At("TAIL"))
    private static void setupConversionTable(CallbackInfo ci) {
        Conversion.setupStateProperties();
        Conversion.setupStatePropertyEnums();
    }
}
