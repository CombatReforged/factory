package com.combatreforged.factory.builder.implementation.world.block;

import com.combatreforged.factory.api.world.block.Block;
import com.combatreforged.factory.api.world.block.BlockType;
import com.combatreforged.factory.api.world.item.ItemType;
import com.combatreforged.factory.api.world.util.Location;
import com.combatreforged.factory.builder.exception.WrappingException;
import com.combatreforged.factory.builder.implementation.util.ObjectMappings;
import com.combatreforged.factory.builder.implementation.world.WrappedWorld;
import com.google.common.collect.ImmutableList;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.block.state.properties.Property;

import java.util.List;
import java.util.Objects;

public class WrappedBlock implements Block {
    private final Location location;
    private final Level mcLevel;

    public WrappedBlock(Location location) {
        this.location = location;
        this.mcLevel = ((WrappedWorld) location.getWorld()).unwrap();
    }

    @Override
    public BlockType getType() {
        return ObjectMappings.BLOCKS.inverse().get(state().getBlock());
    }

    @Override
    public Location getLocation() {
        return location.copy();
    }

    @Override
    public boolean isAir() {
        return state().isAir();
    }

    @Override
    public ItemType getDrop() {
        if (!ObjectMappings.ITEMS.inverse().containsKey(state().getBlock().asItem())) {
            throw new WrappingException("Can't wrap Item");
        }
        return ObjectMappings.ITEMS.inverse().get(state().getBlock().asItem());
    }

    @Override
    public List<StateProperty<?>> getProperties() {
        return state().getProperties().stream()
                .map(mcProp -> ObjectMappings.STATE_PROPERTIES.inverse().get(mcProp))
                .collect(ImmutableList.toImmutableList());
    }

    @Override
    public boolean hasPropertyValue(StateProperty<?> stateProperty) {
        return state().hasProperty(ObjectMappings.STATE_PROPERTIES.get(stateProperty));
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> T getPropertyValue(StateProperty<T> stateProperty) {
        Object mcValue = state().getValue(ObjectMappings.STATE_PROPERTIES.get(stateProperty));

        Object returnValue = mcValue instanceof EnumProperty ? ObjectMappings.STATE_PROPERTY_ENUMS.inverse().get(mcValue) : mcValue;
        try {
            return (T) returnValue;
        } catch (ClassCastException e) {
            throw new WrappingException("Unable to return property: property conversion invalid");
        }
    }

    @Override
    public <T> void setPropertyValue(StateProperty<T> stateProperty, T value) {
        try {
            this.proxy_setPropertyValue(stateProperty, value);
        } catch (ClassCastException e) {
            throw new WrappingException("Unable to set property: conversion to MC failed");
        }
    }

    @SuppressWarnings("unchecked")
    private <T extends Comparable<T>, V extends T> void proxy_setPropertyValue(StateProperty<?> stateProperty, Object value) {
        Property<T> mcProperty = (Property<T>) ObjectMappings.STATE_PROPERTIES.get(stateProperty);
        V mcValue = (V) (mcProperty instanceof EnumProperty ? ObjectMappings.STATE_PROPERTY_ENUMS.get(value) : value);

        BlockState state = state().setValue(mcProperty, mcValue);
        this.update(state);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof WrappedBlock)) return false;
        WrappedBlock that = (WrappedBlock) o;
        return location.equals(that.location) && mcLevel.equals(that.mcLevel);
    }

    @Override
    public int hashCode() {
        return Objects.hash(location, mcLevel);
    }

    public void update(BlockState state) {
        mcLevel.setBlockAndUpdate(blockPos(), state);
    }

    public BlockPos blockPos() {
        return new BlockPos(location.getX(), location.getY(), location.getZ());
    }

    public BlockState state() {
        return mcLevel.getBlockState(blockPos());
    }
}
