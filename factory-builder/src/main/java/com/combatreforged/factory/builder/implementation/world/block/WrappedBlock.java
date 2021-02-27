package com.combatreforged.factory.builder.implementation.world.block;

import com.combatreforged.factory.api.world.block.Block;
import com.combatreforged.factory.api.world.item.Item;
import com.combatreforged.factory.builder.exception.WrappingException;
import com.combatreforged.factory.builder.implementation.Wrapped;
import com.combatreforged.factory.builder.implementation.util.ConversionTables;

public class WrappedBlock extends Wrapped<net.minecraft.world.level.block.Block> implements Block {
    public WrappedBlock(net.minecraft.world.level.block.Block wrapped) {
        super(wrapped);
    }

    @Override
    public Item getDrop() {
        if (!ConversionTables.ITEMS.inverse().containsKey(wrapped.asItem())) {
          throw new WrappingException("Can't wrap Item");
        }
        return ConversionTables.ITEMS.inverse().get(wrapped.asItem());
    }

    @Override
    public float getBreakingProgress() {
        return 0; //TODO
    }

    @Override
    public net.minecraft.world.level.block.Block unwrap() {
        return wrapped;
    }
}
