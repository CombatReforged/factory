package com.combatreforged.factory.builder.implementation.world;

import com.combatreforged.factory.api.world.World;
import com.combatreforged.factory.api.world.block.Block;
import com.combatreforged.factory.api.world.entity.Entity;
import com.combatreforged.factory.api.world.util.Location;
import com.combatreforged.factory.builder.exception.WrappingException;
import com.combatreforged.factory.builder.implementation.Wrapped;
import com.combatreforged.factory.builder.implementation.world.block.WrappedBlock;
import com.combatreforged.factory.builder.implementation.world.entity.WrappedEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;

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
        Wrapped.wrap(wrapped.getBlockState(new BlockPos
                (location.getX(), location.getY(), location.getZ())), WrappedBlock.class);
        return null;
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
}
