package com.combatreforged.factory.builder.mixin.world.inventory;

import com.combatreforged.factory.builder.implementation.util.ObjectMappings;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MenuType.class)
public abstract class MenuTypeMixin<T extends AbstractContainerMenu> {
    @Inject(method = "<clinit>", at = @At("TAIL"))
    private static void loadConversionTable(CallbackInfo ci) {
        ObjectMappings.setupMenuTypes();
    }
}
