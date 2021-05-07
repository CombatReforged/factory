package com.combatreforged.factory.builder.implementation.world.block;

import com.combatreforged.factory.api.world.block.Block;
import com.combatreforged.factory.api.world.item.ItemType;
import com.combatreforged.factory.builder.exception.WrappingException;
import com.combatreforged.factory.builder.implementation.Wrapped;
import com.combatreforged.factory.builder.implementation.util.Conversion;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.EnumProperty;

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

    @SuppressWarnings("unchecked")
    @Override
    public <T> T getPropertyValue(StateProperty<T> stateProperty) {
        Object value = wrapped.getValue(Conversion.STATE_PROPERTIES.get(stateProperty));

        Object returnValue = value instanceof EnumProperty ? Conversion.STATE_PROPERTY_ENUMS.inverse().get(value) : value;
        try {
            return (T) returnValue;
        } catch (ClassCastException e) {
            throw new WrappingException("Invalid property returned");
        }
    }

    @Override
    public <T> void setPropertyValue(StateProperty<T> stateProperty, T value) {
        //TODO
    }

    @Override
    public BlockState unwrap() {
        return wrapped;
    }
}
