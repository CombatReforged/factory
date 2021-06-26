package com.combatreforged.factory.builder.implementation.world.item.container.menu;

import com.combatreforged.factory.api.world.item.container.menu.MenuHolder;
import com.combatreforged.factory.builder.implementation.Wrapped;
import net.minecraft.world.SimpleMenuProvider;

public class WrappedMenuHolder extends Wrapped<SimpleMenuProvider> implements MenuHolder {
    public WrappedMenuHolder(SimpleMenuProvider wrapped) {
        super(wrapped);
    }
}
