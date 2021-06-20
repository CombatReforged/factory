package com.combatreforged.factory.builder.implementation.world.item.container;

import com.combatreforged.factory.builder.implementation.Wrapped;
import com.google.common.collect.ImmutableList;
import net.minecraft.world.Container;
import org.apache.logging.log4j.LogManager;

import java.util.List;

public class WrappedGenericContainer extends Wrapped<Container> implements WrappedContainer {
    public WrappedGenericContainer(Container container) {
        super(container);
    }

    @Override @Deprecated
    public List<Integer> getAvailableSlots() {
        LogManager.getLogger().info("WARNING: This method might not be accurate!");
        ImmutableList.Builder<Integer> slots = ImmutableList.builder();
        for (int i = 0; i < wrapped.getContainerSize(); i++) {
            slots.add(i);
        }

        return slots.build();
    }

    @Override
    public Container container() {
        return wrapped;
    }
}
