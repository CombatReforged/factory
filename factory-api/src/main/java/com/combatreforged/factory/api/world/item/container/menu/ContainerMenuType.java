package com.combatreforged.factory.api.world.item.container.menu;

import com.combatreforged.factory.api.interfaces.ObjectMapped;
import net.kyori.adventure.text.Component;

public interface ContainerMenuType extends ObjectMapped {
    MenuHolder createMenu(Component title);
}
