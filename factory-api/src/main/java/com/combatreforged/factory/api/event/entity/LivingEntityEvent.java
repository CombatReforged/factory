package com.combatreforged.factory.api.event.entity;

import com.combatreforged.factory.api.event.Event;
import com.combatreforged.factory.api.world.entity.LivingEntity;

public abstract class LivingEntityEvent extends Event {
    private final LivingEntity entity;

    public LivingEntityEvent(LivingEntity entity) {
        this.entity = entity;
    }

    public LivingEntity getEntity() {
        return entity;
    }
}
