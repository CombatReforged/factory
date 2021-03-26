package com.combatreforged.factory.builder.mixin.server;

import net.minecraft.commands.CommandSource;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.TickTask;
import net.minecraft.util.thread.ReentrantBlockableEventLoop;
import net.minecraft.world.SnooperPopulator;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(MinecraftServer.class)
public abstract class MinecraftServerMixin extends ReentrantBlockableEventLoop<TickTask> implements SnooperPopulator, CommandSource, AutoCloseable {
    public MinecraftServerMixin(String string) {
        super(string);
    }

    @Inject(method = "getServerModName", at = @At("RETURN"), cancellable = true)
    public void changeBrandName(CallbackInfoReturnable<String> cir) {
        cir.setReturnValue("Factory");
    }
}
