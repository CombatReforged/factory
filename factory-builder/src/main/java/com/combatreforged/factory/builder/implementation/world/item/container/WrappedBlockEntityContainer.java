package com.combatreforged.factory.builder.implementation.world.item.container;

import com.combatreforged.factory.api.world.item.container.BlockEntityContainer;
import com.combatreforged.factory.builder.implementation.util.Conversion;
import net.kyori.adventure.text.Component;
import net.minecraft.world.level.block.entity.BaseContainerBlockEntity;

import java.util.ArrayList;
import java.util.List;

public class WrappedBlockEntityContainer extends WrappedContainer implements BlockEntityContainer {
    private final BaseContainerBlockEntity wrapped;
    public WrappedBlockEntityContainer(BaseContainerBlockEntity wrapped) {
        super(wrapped);
        this.wrapped = wrapped;
    }

    @Override
    public Component getName() {
        return Conversion.convertComponent(wrapped.getName());
    }

    @Override
    public void setName(Component component) {
        wrapped.setCustomName(Conversion.convertComponent(component));
    }

    @Override
    public List<Integer> getAvailableSlots() {
        List<Integer> slots = new ArrayList<>();
        for (int i = 0; i < wrapped.getContainerSize(); i++) {
            slots.add(i);
        }

        return slots;
    }

    @Override
    public BaseContainerBlockEntity unwrap() {
        return wrapped;
    }
}
