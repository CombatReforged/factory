package com.combatreforged.factory.builder.implementation.world.entity;

import com.combatreforged.factory.api.world.World;
import com.combatreforged.factory.api.world.entity.Entity;
import com.combatreforged.factory.api.world.util.Location;
import com.combatreforged.factory.api.world.util.Vector3D;
import com.combatreforged.factory.builder.exception.WrappingException;
import com.combatreforged.factory.builder.extension.wrap.Wrap;
import com.combatreforged.factory.builder.implementation.Wrapped;
import com.combatreforged.factory.builder.implementation.world.WrappedWorld;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class WrappedEntity extends Wrapped<net.minecraft.world.entity.Entity> implements Entity {
    public WrappedEntity(net.minecraft.world.entity.Entity entity) {
        super(entity);
    }

    @Override
    public int getEntityId() {
        return wrapped.getId();
    }

    @Override
    public Component getName() {
        String mcCompString = net.minecraft.network.chat.Component.Serializer.toJson(wrapped.getName());
        return GsonComponentSerializer.gson().deserialize(mcCompString);
    }

    @Override
    public @Nullable Component getCustomName() {
        if (wrapped.getCustomName() != null) {
            String mcCompString = net.minecraft.network.chat.Component.Serializer.toJson(wrapped.getCustomName());
            return GsonComponentSerializer.gson().deserialize(mcCompString);
        }
        return null;
    }

    @Override
    public void setCustomName(Component customName) {
        String compString = GsonComponentSerializer.gson().serialize(customName);
        wrapped.setCustomName(net.minecraft.network.chat.Component.Serializer.fromJson(compString));
    }

    @Override
    public Location getLocation() {
        return new Location(wrapped.getX(), wrapped.getY(), wrapped.getZ(), wrapped.yRot, wrapped.xRot, getWorld());
    }

    @Override
    public World getWorld() {
        if (wrapped.level instanceof ServerLevel && wrapped.level instanceof Wrap) {
            return Wrapped.wrap(wrapped.level, WrappedWorld.class);
        } else {
            throw new WrappingException("Entity is not in a ServerLevel");
        }
    }

    @Override
    public void teleport(Location location, boolean ignoreDirection) {
        if (wrapped.isPassenger()) {
            wrapped.removeVehicle();
        }
        if (location.getWorld() != null)
            wrapped.setLevel(((WrappedWorld) location.getWorld()).unwrap());
        wrapped.teleportTo(location.getX(), location.getY(), location.getZ());
        if (!ignoreDirection) {
            wrapped.yRot = Math.min(Math.max(location.getYaw(), -180), 180);
            wrapped.xRot = Math.min(Math.max(location.getPitch(), -90), 90);
        }
    }

    @Override
    public void kill() {
        wrapped.kill();
    }

    @Override
    public boolean isOnFire() {
        return wrapped.isOnFire();
    }

    @Override
    public int getRemainingFireTicks() {
        return wrapped.getRemainingFireTicks();
    }

    @Override
    public void setRemainingFireTicks(int ticks) {
        wrapped.setRemainingFireTicks(ticks);
    }

    @Override
    public Vector3D getVelocity() {
        return new Vector3D(wrapped.getDeltaMovement().x, wrapped.getDeltaMovement().y, wrapped.getDeltaMovement().z);
    }

    @Override
    public void setVelocity(Vector3D velocity) {
        wrapped.setDeltaMovement(velocity.getX(), velocity.getY(), velocity.getZ());
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<Entity> getPassengers() {
        List<Entity> passengers = new ArrayList<>();
        for (net.minecraft.world.entity.Entity entity : wrapped.getPassengers()) {
            try {
                passengers.add(((Wrap<Entity>) entity).wrap());
            } catch (ClassCastException e) {
                throw new WrappingException("Unable to wrap Entity!");
            }
        }
        return passengers;
    }

    @Override
    public void removePassenger(Entity entity) {
        if (getPassengers().contains(entity)) {
            entity.stopRiding();
        }
    }

    @Override
    public void addPassenger(Entity entity) {
        entity.startRiding(this);
    }

    @Override
    public void startRiding(Entity entity) {
        wrapped.startRiding(((WrappedEntity) entity).unwrap());
    }

    @Override
    public void stopRiding() {
        wrapped.stopRiding();
    }

    @Override
    public boolean equals(Object other) {
        if (other == null)
            return false;
        else if (other.getClass() != getClass())
            return false;
        return getEntityId() == ((WrappedEntity) other).getEntityId();
    }

    @Override
    public int runCommand(String command) {
        MinecraftServer server = wrapped.getServer();
        if (server != null)
            return wrapped.getServer().getCommands().performCommand(wrapped.createCommandSourceStack(), command);
        else
            return 0;
    }

    @Override
    public net.minecraft.world.entity.Entity unwrap() {
        return wrapped;
    }
}
