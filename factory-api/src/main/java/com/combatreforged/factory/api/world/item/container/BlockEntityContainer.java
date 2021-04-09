package com.combatreforged.factory.api.world.item.container;

import net.kyori.adventure.text.Component;

public interface BlockEntityContainer extends Container {
    Component getName();
    void setName(Component component);
}
