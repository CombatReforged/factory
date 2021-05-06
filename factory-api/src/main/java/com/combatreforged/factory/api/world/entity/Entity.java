package com.combatreforged.factory.api.world.entity;

import com.combatreforged.factory.api.builder.Builder;
import com.combatreforged.factory.api.world.World;
import com.combatreforged.factory.api.world.command.CommandSource;
import com.combatreforged.factory.api.world.util.Location;
import com.combatreforged.factory.api.world.util.Vector3D;
import net.kyori.adventure.nbt.api.BinaryTagHolder;
import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Set;

public interface Entity extends CommandSource {
    int getEntityId();

    EntityType getEntityType();

    Component getName();

    @Nullable
    Component getCustomName();

    void setCustomName(Component customName);

    Location getLocation();

    World getWorld();

    void teleport(Location location, boolean ignoreDirection);

    default void teleport(Location location) { teleport(location, false); }

    void kill();

    boolean isOnFire();

    int getRemainingFireTicks();

    void setRemainingFireTicks(int ticks);

    Vector3D getVelocity();

    void setVelocity(Vector3D velocity);

    List<Entity> getPassengers();

    void removePassenger(Entity entity);

    void addPassenger(Entity entity);

    void startRiding(Entity entity);

    void stopRiding();

    Set<String> getTags();

    boolean addTag(String tag);

    void removeTag(String tag);

    boolean hasTag(String tag);

    BinaryTagHolder getEntityData();

    void setEntityData(BinaryTagHolder tag);

    static Entity create(EntityType type, World world) {
        return Builder.getInstance().createEntity(type, world);
    }
}
