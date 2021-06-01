package com.combatreforged.factory.api.world.block.container;

import com.combatreforged.factory.api.world.item.container.Container;
import net.kyori.adventure.text.Component;

public interface BlockEntityContainer extends Container {
    Component getName();
    void setName(Component component);
}
