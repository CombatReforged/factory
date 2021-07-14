package com.combatreforged.factory.builder.implementation.util;

import net.minecraft.world.inventory.ContainerLevelAccess;

public interface LevelAccessOwner {
    void setContainerLevelAccess(ContainerLevelAccess access);
    ContainerLevelAccess getContainerLevelAccess();
}
