package com.combatreforged.factory.builder.implementation.world.block;

import com.combatreforged.factory.api.world.block.Block;
import com.combatreforged.factory.api.world.entity.player.Player;
import com.combatreforged.factory.api.world.item.Item;
import com.combatreforged.factory.builder.exception.WrappingException;
import com.combatreforged.factory.builder.implementation.Wrapped;
import com.combatreforged.factory.builder.implementation.util.ConversionTables;
import net.minecraft.world.level.block.state.BlockState;

public class WrappedBlock extends Wrapped<BlockState> implements Block {
    public WrappedBlock(BlockState wrapped) {
        super(wrapped);
    }

    @Override
    public Item getDrop() {
        if (!ConversionTables.ITEMS.inverse().containsKey(wrapped.getBlock().asItem())) {
          throw new WrappingException("Can't wrap Item");
        }
        return ConversionTables.ITEMS.inverse().get(wrapped.getBlock().asItem());
    }

    @Override
    public float getBreakingProgress(Player player) {
        return 0; //TODO
    }

    @Override
    public BlockState unwrap() {
        return wrapped;
    }
}