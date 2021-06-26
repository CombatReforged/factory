package com.combatreforged.factory.builder.mixin.world.inventory;

import com.combatreforged.factory.builder.extension.world.inventory.MenuSupplierExtension;
import com.combatreforged.factory.builder.extension.world.inventory.MenuTypeExtension;
import com.combatreforged.factory.builder.implementation.util.ObjectMappings;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MenuType.class)
public abstract class MenuTypeMixin<T extends AbstractContainerMenu> implements MenuTypeExtension {
    @Shadow @Final private MenuType.MenuSupplier<T> constructor;

    @Inject(method = "<clinit>", at = @At("TAIL")) @SuppressWarnings("unused")
    private static void loadConversionTable(CallbackInfo ci) {
        ObjectMappings.setupMenuTypes();
    }

    @Override
    public AbstractContainerMenu createServer(int i, Inventory inventory) {
        return ((MenuSupplierExtension<?>) constructor).createServer(i, inventory);
    }
}
