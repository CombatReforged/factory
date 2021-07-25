package com.combatreforged.factory.builder.mixin.server;

import com.combatreforged.factory.api.FactoryServer;
import com.combatreforged.factory.api.event.server.tick.ServerEndTickEvent;
import com.combatreforged.factory.api.event.server.tick.ServerStartTickEvent;
import com.combatreforged.factory.builder.implementation.Wrapped;
import com.combatreforged.factory.builder.implementation.WrappedFactoryServer;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ModContainer;
import net.fabricmc.loader.api.metadata.ModMetadata;
import net.minecraft.commands.CommandSource;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.TickTask;
import net.minecraft.util.thread.ReentrantBlockableEventLoop;
import net.minecraft.world.SnooperPopulator;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Optional;
import java.util.function.BooleanSupplier;

@Mixin(MinecraftServer.class)
public abstract class MinecraftServerMixin extends ReentrantBlockableEventLoop<TickTask> implements SnooperPopulator, CommandSource, AutoCloseable {
    @Shadow public abstract boolean isDedicatedServer();

    @Shadow private int tickCount;

    public MinecraftServerMixin(String string) {
        super(string);
    }

    @Inject(method = "getServerModName", at = @At("RETURN"), cancellable = true)
    public void changeBrandName(CallbackInfoReturnable<String> cir) {
        String string = cir.getReturnValue();
        string += "+factory@factory-builder-";
        Optional<ModContainer> modContainer = FabricLoader.getInstance().getModContainer("factory-builder");
        if (modContainer.isPresent()) {
            ModMetadata meta = modContainer.get().getMetadata();
            string += meta.getVersion();
        } else {
            string += "unknown";
        }
        cir.setReturnValue(string);
    }

    @Inject(method = "tickServer",
            at = @At(value = "INVOKE",
                    target = "Lnet/minecraft/server/MinecraftServer;tickChildren(Ljava/util/function/BooleanSupplier;)V",
                    shift = At.Shift.BEFORE)
    )
    public void callServerStartTickEvent(BooleanSupplier booleanSupplier, CallbackInfo ci) {
        if (this.isDedicatedServer()) {
            FactoryServer server = Wrapped.wrap(this, WrappedFactoryServer.class);
            ServerStartTickEvent.BACKEND.invoke(new ServerStartTickEvent(server, this.tickCount));
        }
    }

    @Inject(method = "tickServer",
            at = @At(value = "INVOKE",
                    target = "Lnet/minecraft/server/MinecraftServer;tickChildren(Ljava/util/function/BooleanSupplier;)V",
                    shift = At.Shift.AFTER)
    )
    public void callServerEndTickEvent(BooleanSupplier booleanSupplier, CallbackInfo ci) {
        if (this.isDedicatedServer()) {
            WrappedFactoryServer server = Wrapped.wrap(this, WrappedFactoryServer.class);
            ServerEndTickEvent.BACKEND.invoke(new ServerEndTickEvent(server, this.tickCount));
        }
    }
}
