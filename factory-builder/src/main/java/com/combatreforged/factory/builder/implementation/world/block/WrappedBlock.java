package com.combatreforged.factory.builder.implementation.world.block;

import com.combatreforged.factory.api.world.block.Block;
import com.combatreforged.factory.api.world.item.ItemType;
import com.combatreforged.factory.builder.exception.WrappingException;
import com.combatreforged.factory.builder.implementation.Wrapped;
import com.combatreforged.factory.builder.implementation.util.Conversion;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.block.state.properties.Property;

public class WrappedBlock extends Wrapped<BlockState> implements Block {
    public WrappedBlock(BlockState wrapped) {
        super(wrapped);
    }

    @Override
    public boolean isAir() {
        return wrapped.isAir();
    }

    @Override
    public ItemType getDrop() {
        if (!Conversion.ITEMS.inverse().containsKey(wrapped.getBlock().asItem())) {
            throw new WrappingException("Can't wrap Item");
        }
        return Conversion.ITEMS.inverse().get(wrapped.getBlock().asItem());
    }

    @Override
    public boolean hasPropertyValue(StateProperty<?> stateProperty) {
        return wrapped.hasProperty(Conversion.STATE_PROPERTIES.get(stateProperty));
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> T getPropertyValue(StateProperty<T> stateProperty) {
        Object mcValue = wrapped.getValue(Conversion.STATE_PROPERTIES.get(stateProperty));

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

        wrapped.setValue(mcProperty, mcValue);
    }

    @Override
    public BlockState unwrap() {
        return wrapped;
    }
}
