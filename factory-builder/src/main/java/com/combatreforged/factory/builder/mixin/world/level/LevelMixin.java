package com.combatreforged.factory.builder.mixin.world.level;

import com.combatreforged.factory.builder.extension.world.level.LevelExtension;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.TickableBlockEntity;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;
import java.util.List;

@Mixin(Level.class)
public class LevelMixin implements LevelExtension {
    @Shadow @Mutable @Final private Thread thread;
    private final List<TickableBlockEntity> independentContainers = new ArrayList<>();

    @Inject(method = "tickBlockEntities", at = @At("TAIL"))
    public void tickIndependentContainers(CallbackInfo ci) {
        List<TickableBlockEntity> toBeRemoved = new ArrayList<>();
        for (TickableBlockEntity container : independentContainers) {
            if (container instanceof BlockEntity) {
                BlockEntity blockEntity = (BlockEntity) container;
                if (blockEntity.isRemoved()) {
                    toBeRemoved.add(container);
                } else {
                    container.tick();
                }
            }
        }

        independentContainers.removeAll(toBeRemoved);
    }

    @Override
    public void addIndependentContainer(TickableBlockEntity blockEntity) {
        independentContainers.add(blockEntity);
    }

    @Override
    public void removeIndependentContainer(TickableBlockEntity blockEntity) {
        independentContainers.remove(blockEntity);
    }

    @Override
    public List<TickableBlockEntity> getIndependentContainers() {
        return independentContainers;
    }

    @Override
    public Thread getThread() {
        return thread;
    }

    @Override
    public void setThread(Thread thread) {
        this.thread = thread;
    }
}
