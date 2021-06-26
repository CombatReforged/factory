package com.combatreforged.factory.builder.mixin.world.inventory;

import com.combatreforged.factory.builder.extension.world.inventory.MenuSupplierExtension;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(MenuType.MenuSupplier.class)
public interface MenuSupplierMixin<T extends AbstractContainerMenu> extends MenuSupplierExtension<T> {
    @Environment(EnvType.SERVER)
    T create(int i, Inventory inventory);

    @Override
    default T createServer(int i, Inventory inventory) {
        return create(i, inventory);
    }
}
