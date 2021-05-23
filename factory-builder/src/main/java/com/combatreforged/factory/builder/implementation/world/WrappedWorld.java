package com.combatreforged.factory.builder.implementation.world;

import com.combatreforged.factory.api.FactoryServer;
import com.combatreforged.factory.api.world.World;
import com.combatreforged.factory.api.world.block.Block;
import com.combatreforged.factory.api.world.block.BlockEntity;
import com.combatreforged.factory.api.world.entity.Entity;
import com.combatreforged.factory.api.world.util.Location;
import com.combatreforged.factory.builder.exception.WrappingException;
import com.combatreforged.factory.builder.implementation.Wrapped;
import com.combatreforged.factory.builder.implementation.WrappedFactoryServer;
import com.combatreforged.factory.builder.implementation.world.block.WrappedBlock;
import com.combatreforged.factory.builder.implementation.world.block.WrappedBlockEntity;
import com.combatreforged.factory.builder.implementation.world.entity.WrappedEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.storage.DerivedLevelData;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class WrappedWorld extends Wrapped<ServerLevel> implements World {
    public WrappedWorld(ServerLevel wrapped) {
        super(wrapped);
    }

    @Override
    public ServerLevel unwrap() {
        return wrapped;
    }

    @Override
    public Block getBlockAt(Location location) {
        return new WrappedBlock(location);
    }

    @Override
    public BlockEntity getBlockEntity(Location location) {
        return Wrapped.wrap(wrapped.getBlockEntity(new BlockPos(location.getX(), location.getY(), location.getZ())), WrappedBlockEntity.class);
    }

    @Override
    public List<Entity> getEntities() {
        List<Entity> entities = new ArrayList<>();
        wrapped.getAllEntities().forEach((entity) -> {
            entities.add(Wrapped.wrap(entity, WrappedEntity.class));
        });
        return entities;
    }

    @Override
    public boolean spawn(Entity entity) {
        if (!(entity instanceof WrappedEntity)) {
            throw new WrappingException("Entity not a WrappedEntity");
        }
        return wrapped.addFreshEntity(((WrappedEntity) entity).unwrap());
    }

    @Override
    public Entity getEntity(UUID uuid) {
        return Wrapped.wrap(wrapped.getEntity(uuid), WrappedEntity.class);
    }

    @Override
    public FactoryServer getServer() {
        return Wrapped.wrap(wrapped.getServer(), WrappedFactoryServer.class);
    }

    @Override
    public boolean isChild() {
        return wrapped.getLevelData() instanceof DerivedLevelData;
    }

    @Override
    public World getParent() {
        return null; //TODO
    }
}
