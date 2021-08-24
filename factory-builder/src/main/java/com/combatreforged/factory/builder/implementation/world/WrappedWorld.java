package com.combatreforged.factory.builder.implementation.world;

import com.combatreforged.factory.api.FactoryServer;
import com.combatreforged.factory.api.util.Identifier;
import com.combatreforged.factory.api.world.Weather;
import com.combatreforged.factory.api.world.World;
import com.combatreforged.factory.api.world.block.Block;
import com.combatreforged.factory.api.world.block.BlockEntity;
import com.combatreforged.factory.api.world.block.BlockState;
import com.combatreforged.factory.api.world.border.WorldBorder;
import com.combatreforged.factory.api.world.entity.Entity;
import com.combatreforged.factory.api.world.entity.player.GameModeType;
import com.combatreforged.factory.api.world.util.BoundingBox;
import com.combatreforged.factory.api.world.util.Location;
import com.combatreforged.factory.builder.exception.WrappingException;
import com.combatreforged.factory.builder.implementation.Wrapped;
import com.combatreforged.factory.builder.implementation.WrappedFactoryServer;
import com.combatreforged.factory.builder.implementation.util.ObjectMappings;
import com.combatreforged.factory.builder.implementation.world.block.WrappedBlock;
import com.combatreforged.factory.builder.implementation.world.block.WrappedBlockEntity;
import com.combatreforged.factory.builder.implementation.world.block.WrappedBlockState;
import com.combatreforged.factory.builder.implementation.world.border.WrappedWorldBorder;
import com.combatreforged.factory.builder.implementation.world.entity.WrappedEntity;
import com.google.common.collect.ImmutableList;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.DerivedLevelData;
import net.minecraft.world.level.storage.ServerLevelData;
import net.minecraft.world.phys.AABB;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.function.Predicate;

public class WrappedWorld extends Wrapped<ServerLevel> implements World {
    public WrappedWorld(ServerLevel wrapped) {
        super(wrapped);
    }

    @Override
    public ServerLevel unwrap() {
        return wrapped;
    }

    @Override
    public Identifier getIdentifier() {
        return new Identifier(wrapped.dimension().location().getNamespace(), wrapped.dimension().location().getPath());
    }

    @Override
    public Block getBlockAt(Location location) {
        return new WrappedBlock(location);
    }

    @Override
    public void setBlockAt(Location location, BlockState state) {
        BlockPos pos = new BlockPos(location.getX(), location.getY(), location.getZ());
        Level level = ((WrappedWorld) location.getWorld()).unwrap();
        level.setBlock(pos, ((WrappedBlockState) state).state(), 11);
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
    public List<Entity> getEntities(BoundingBox area, Predicate<Entity> filter) {
        return wrapped.getEntities((net.minecraft.world.entity.Entity) null,
                new AABB(area.getMinX(),
                        area.getMinY(),
                        area.getMinZ(),
                        area.getMaxX(),
                        area.getMaxY(),
                        area.getMaxZ()),
                (net.minecraft.world.entity.Entity entity) -> filter.test(Wrapped.wrap(entity, WrappedEntity.class)))
                .stream()
                .map(entity -> Wrapped.wrap(entity, WrappedEntity.class))
                .collect(ImmutableList.toImmutableList());
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
    public boolean isSubWorld() {
        return wrapped.getLevelData() instanceof DerivedLevelData;
    }

    @Override
    public Weather getWeather() {
        if (wrapped.isThundering()) {
            return Weather.THUNDER;
        } else if (wrapped.isRaining()) {
            return Weather.RAIN;
        }
        return Weather.CLEAR;
    }

    @Override
    public void setWeather(Weather weather) {
        this.setWeather(weather, 6000);
    }

    @Override
    public void setWeather(Weather weather, int duration) {
        int clearDur = 0;
        int weatherDur = 0;
        boolean raining = false;
        boolean thundering = false;
        switch (weather) {
            case CLEAR:
                clearDur = duration;
                break;
            case RAIN:
                weatherDur = duration;
                raining = true;
                break;
            case THUNDER:
                weatherDur = duration;
                raining = true;
                thundering = true;
                break;
        }
        wrapped.setWeatherParameters(clearDur, weatherDur, raining, thundering);
    }

    private ServerLevelData serverLevelData() {
        return ((ServerLevelData) wrapped.getLevelData());
    }

    @Override
    public WorldBorder getWorldBorder() {
        return Wrapped.wrap(wrapped.getWorldBorder(), WrappedWorldBorder.class);
    }

    @Override
    public long getDayTime() {
        return serverLevelData().getDayTime();
    }

    @Override
    public void setDayTime(long time) {
        serverLevelData().setDayTime(time);
    }

    @Override
    public long getGameTime() {
        return serverLevelData().getGameTime();
    }

    @Override
    public void setGameTime(long time) {
        serverLevelData().setGameTime(time);
    }

    @Override
    public GameModeType getDefaultGameMode() {
        return ObjectMappings.GAME_MODES.inverse().get(serverLevelData().getGameType());
    }

    @Override
    public void setDefaultGameMode(GameModeType gameMode) {
        serverLevelData().setGameType(ObjectMappings.GAME_MODES.get(gameMode));
    }
}
