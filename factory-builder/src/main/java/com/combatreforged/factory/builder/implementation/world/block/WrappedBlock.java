package com.combatreforged.factory.builder.implementation.world.block;

import com.combatreforged.factory.api.world.block.Block;
import com.combatreforged.factory.api.world.block.BlockType;
import com.combatreforged.factory.api.world.item.ItemType;
import com.combatreforged.factory.api.world.util.Location;
import com.combatreforged.factory.builder.exception.WrappingException;
import com.combatreforged.factory.builder.implementation.util.Conversion;
import com.combatreforged.factory.builder.implementation.world.WrappedWorld;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.block.state.properties.Property;

public class WrappedBlock implements Block {
    private final Location location;
    private final Level mcLevel;

    public WrappedBlock(Location location) {
        this.location = location;
        this.mcLevel = ((WrappedWorld) location.getWorld()).unwrap();
    }

    @Override
    public BlockType getType() {
        return Conversion.BLOCKS.inverse().get(state().getBlock());
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
        if (!Conversion.ITEMS.inverse().containsKey(state().getBlock().asItem())) {
            throw new WrappingException("Can't wrap Item");
        }
        return Conversion.ITEMS.inverse().get(state().getBlock().asItem());
    }

    @Override
    public boolean hasPropertyValue(StateProperty<?> stateProperty) {
        return state().hasProperty(Conversion.STATE_PROPERTIES.get(stateProperty));
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> T getPropertyValue(StateProperty<T> stateProperty) {
        Object mcValue = state().getValue(Conversion.STATE_PROPERTIES.get(stateProperty));

        Object returnValue = mcValue instanceof EnumProperty ? Conversion.STATE_PROPERTY_ENUMS.inverse().get(mcValue) : mcValue;
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
        Property<T> mcProperty = (Property<T>) Conversion.STATE_PROPERTIES.get(stateProperty);
        V mcValue = (V) (mcProperty instanceof EnumProperty ? Conversion.STATE_PROPERTY_ENUMS.get(value) : value);

        BlockState state = state().setValue(mcProperty, mcValue);
        this.update(state);
    }

    public void update(BlockState state) {
        mcLevel.setBlockAndUpdate(blockPos(), state);
    }

    private BlockPos blockPos() {
        return new BlockPos(location.getX(), location.getY(), location.getZ());
    }

    private BlockState state() {
        return mcLevel.getBlockState(blockPos());
    }
}
