package com.combatreforged.factory.api.world.block.container;

import com.combatreforged.factory.api.world.entity.player.Player;
import com.combatreforged.factory.api.world.item.container.Container;
import com.combatreforged.factory.api.world.item.container.menu.ContainerMenu;
import net.kyori.adventure.text.Component;

public interface BlockEntityContainer extends Container {
    Component getName();
    void setName(Component component);

    ContainerMenu openToPlayer(Player player);
}
