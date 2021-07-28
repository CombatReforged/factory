package com.combatreforged.factory.builder.extension.world.entity;

import com.combatreforged.factory.api.event.entity.LivingEntityDeathEvent;

public interface LivingEntityExtension {
    boolean willDropItems();

    void setDeathEvent(LivingEntityDeathEvent event);
    LivingEntityDeathEvent getDeathEvent();
}
