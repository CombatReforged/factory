package com.combatreforged.factory.api.event.entity;

import com.combatreforged.factory.api.event.Event;
import com.combatreforged.factory.api.world.entity.Entity;

public abstract class EntityEvent extends Event {
    private final Entity entity;

    public EntityEvent(Entity entity) {
        this.entity = entity;
    }

    public Entity getEntity() {
        return entity;
    }
}
