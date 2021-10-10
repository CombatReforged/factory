package com.combatreforged.factory.builder.implementation.world.entity;

import com.combatreforged.factory.api.FactoryAPI;
import com.combatreforged.factory.api.FactoryServer;
import com.combatreforged.factory.api.command.CommandSourceInfo;
import com.combatreforged.factory.api.world.World;
import com.combatreforged.factory.api.world.entity.Entity;
import com.combatreforged.factory.api.world.entity.EntityType;
import com.combatreforged.factory.api.world.nbt.NBTObject;
import com.combatreforged.factory.api.world.util.Location;
import com.combatreforged.factory.api.world.util.Vector3D;
import com.combatreforged.factory.builder.exception.WrappingException;
import com.combatreforged.factory.builder.extension.world.entity.EntityExtension;
import com.combatreforged.factory.builder.extension.wrap.Wrap;
import com.combatreforged.factory.builder.implementation.Wrapped;
import com.combatreforged.factory.builder.implementation.util.ObjectMappings;
import com.combatreforged.factory.builder.implementation.world.WrappedWorld;
import com.combatreforged.factory.builder.implementation.world.nbt.WrappedNBTObject;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.kyori.adventure.nbt.api.BinaryTagHolder;
import net.kyori.adventure.text.Component;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.TagParser;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class WrappedEntity extends Wrapped<net.minecraft.world.entity.Entity> implements Entity {
    public WrappedEntity(net.minecraft.world.entity.Entity wrapped) {
        super(wrapped);
    }

    @Override
    public int getEntityId() {
        return wrapped.getId();
    }

    @Override
    public EntityType getEntityType() {
        return ObjectMappings.ENTITIES.inverse().get(this.wrapped.getType());
    }

    @Override
    public Component getName() {
        return ObjectMappings.convertComponent(wrapped.getName());
    }

    @Override
    public @Nullable Component getCustomName() {
        if (wrapped.getCustomName() != null) {
            return ObjectMappings.convertComponent(wrapped.getCustomName());
        }
        return null;
    }

    @Override
    public void setCustomName(Component customName) {
        wrapped.setCustomName(ObjectMappings.convertComponent(customName));
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
            wrapped.yRot = location.getYaw() % 360.0F;
            wrapped.xRot = Mth.clamp(location.getPitch(), -90.0F, 90.0F) % 360.0F;
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
    public Set<String> getTags() {
        return wrapped.getTags();
    }

    @Override
    public boolean addTag(String tag) {
        return wrapped.addTag(tag);
    }

    @Override
    public void removeTag(String tag) {
        wrapped.removeTag(tag);
    }

    @Override
    public boolean hasTag(String tag) {
        return wrapped.getTags().contains(tag);
    }

    @Deprecated
    @Override
    public BinaryTagHolder getEntityData() {
        return BinaryTagHolder.of(wrapped.saveWithoutId(new CompoundTag()).getAsString());
    }

    @Deprecated
    @Override
    public void setEntityData(BinaryTagHolder tag) {
        try {
            wrapped.load(TagParser.parseTag(tag.toString()));
        } catch (CommandSyntaxException e) {
            throw new UnsupportedOperationException("Tag is invalid");
        }
    }

    @Override
    public NBTObject getEntityNBT() {
        return Wrapped.wrap(wrapped.saveWithoutId(new CompoundTag()), WrappedNBTObject.class);
    }

    @Override
    public void setEntityNBT(NBTObject nbt) {
        wrapped.load(((WrappedNBTObject) nbt).unwrap());
    }

    @Override
    public boolean isOnGround() {
        return wrapped.isOnGround();
    }

    @Override
    public CommandSourceInfo createCommandInfo() {
        CommandSourceStack stack = wrapped.createCommandSourceStack();
        return CommandSourceInfo.builder()
                .source(this)
                .executingEntity(Wrapped.wrap(stack.getEntity(), WrappedEntity.class))
                .location(new Location(
                        stack.getPosition().x,
                        stack.getPosition().y,
                        stack.getPosition().z,
                        stack.getRotation().y,
                        stack.getRotation().x,
                        Wrapped.wrap(stack.getLevel(), WrappedWorld.class)))
                .server(FactoryAPI.getInstance().getServer()).build();
    }

    @Override
    public boolean equals(Object other) {
        if (other == null)
            return false;
        else if (!(other instanceof WrappedEntity))
            return false;
        return getEntityId() == ((WrappedEntity) other).getEntityId();
    }

    @Override
    public int hashCode() {
        return wrapped.hashCode();
    }

    @Override
    public int runCommand(String command, int permissionLevel, boolean giveFeedback) {
        MinecraftServer server = wrapped.getServer();
        if (server == null) return 0;

        CommandSourceStack sourceStack = wrapped.createCommandSourceStack().withPermission(permissionLevel);
        if (!giveFeedback) sourceStack = sourceStack.withSuppressedOutput();
        return wrapped.getServer().getCommands().performCommand(sourceStack, command);
    }

    @Override
    public void sendMessage(Component message) {
    }

    @Override
    public int getPermissionLevel() {
        return ((EntityExtension) wrapped).invokeGetPermissionLevel();
    }

    @Override
    public FactoryServer getServer() {
        return this.getWorld().getServer();
    }

    @Override
    public net.minecraft.world.entity.Entity unwrap() {
        return wrapped;
    }
}
