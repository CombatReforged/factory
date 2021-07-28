package com.combatreforged.factory.api.event.entity;

import com.combatreforged.factory.api.event.Event;
import com.combatreforged.factory.api.event.EventBackend;
import com.combatreforged.factory.api.world.damage.DamageData;
import com.combatreforged.factory.api.world.entity.LivingEntity;

public class LivingEntityDeathEvent implements Event {
    public static final EventBackend<LivingEntityDeathEvent> BACKEND = EventBackend.create(LivingEntityDeathEvent.class);

    private final LivingEntity entity;
    private DamageData cause;
    private boolean dropLoot;
    private boolean dropEquipment;
    private boolean dropExperience;

    public LivingEntityDeathEvent(LivingEntity entity, DamageData cause, boolean dropLoot, boolean dropEquipment, boolean dropExperience) {
        this.entity = entity;
        this.cause = cause;
        this.dropLoot = dropLoot;
        this.dropEquipment = dropEquipment;
        this.dropExperience = dropExperience;
    }

    public LivingEntity getEntity() {
        return entity;
    }

    public DamageData getCause() {
        return cause;
    }

    public void setCause(DamageData cause) {
        this.cause = cause;
    }

    public boolean isDropLoot() {
        return dropLoot;
    }

    public void setDropLoot(boolean dropLoot) {
        this.dropLoot = dropLoot;
    }

    public boolean isDropEquipment() {
        return dropEquipment;
    }

    public void setDropEquipment(boolean dropEquipment) {
        this.dropEquipment = dropEquipment;
    }

    public boolean isDropExperience() {
        return dropExperience;
    }

    public void setDropExperience(boolean dropExperience) {
        this.dropExperience = dropExperience;
    }
}
