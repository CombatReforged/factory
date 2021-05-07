package com.combatreforged.factory.builder.implementation.world.item.container;

import com.combatreforged.factory.api.world.item.container.BlockEntityContainer;
import com.combatreforged.factory.builder.implementation.util.Conversion;
import com.combatreforged.factory.builder.implementation.world.block.WrappedBlockEntity;
import net.kyori.adventure.text.Component;
import net.minecraft.world.Container;
import net.minecraft.world.level.block.entity.BaseContainerBlockEntity;

import java.util.ArrayList;
import java.util.List;

public class WrappedBlockEntityContainer extends WrappedBlockEntity implements BlockEntityContainer, WrappedContainer {
    private final BaseContainerBlockEntity wrappedBEC;
    public WrappedBlockEntityContainer(BaseContainerBlockEntity wrappedBEC) {
        super(wrappedBEC);
        this.wrappedBEC = wrappedBEC;
    }

    @Override
    public Component getName() {
        return Conversion.convertComponent(wrappedBEC.getName());
    }

    @Override
    public void setName(Component component) {
        wrappedBEC.setCustomName(Conversion.convertComponent(component));
    }

    @Override
    public List<Integer> getAvailableSlots() {
        List<Integer> slots = new ArrayList<>();
        for (int i = 0; i < wrappedBEC.getContainerSize(); i++) {
            slots.add(i);
        }

        return slots;
    }

    @Override
    public Container container() {
        return wrappedBEC;
    }

    @Override
    public BaseContainerBlockEntity unwrap() {
        return wrappedBEC;
    }
}
