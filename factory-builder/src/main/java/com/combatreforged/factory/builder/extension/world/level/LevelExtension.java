package com.combatreforged.factory.builder.extension.world.level;

import net.minecraft.world.level.block.entity.TickableBlockEntity;

import java.util.List;

public interface LevelExtension {
    void addIndependentContainer(TickableBlockEntity blockEntity);
    void removeIndependentContainer(TickableBlockEntity blockEntity);
    List<TickableBlockEntity> getIndependentContainers();

    Thread getThread();
    void setThread(Thread thread);
}
