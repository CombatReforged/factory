package com.combatreforged.factory.builder.mixin.server.dedicated;

import com.combatreforged.factory.api.FactoryAPI;
import com.combatreforged.factory.api.FactoryServer;
import com.combatreforged.factory.api.entrypoint.FactoryPlugin;
import com.combatreforged.factory.api.event.server.ServerStartupFinishEvent;
import com.combatreforged.factory.builder.FactoryBuilder;
import com.combatreforged.factory.builder.extension.server.MinecraftServerExtension;
import com.combatreforged.factory.builder.extension.wrap.Wrap;
import com.combatreforged.factory.builder.implementation.WrappedFactoryServer;
import com.combatreforged.factory.builder.implementation.builder.BuilderImpl;
import com.combatreforged.factory.builder.implementation.util.ObjectMappings;
import com.mojang.authlib.GameProfileRepository;
import com.mojang.authlib.minecraft.MinecraftSessionService;
import com.mojang.datafixers.DataFixer;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ModContainer;
import net.fabricmc.loader.api.entrypoint.EntrypointContainer;
import net.fabricmc.loader.api.metadata.ModDependency;
import net.minecraft.core.RegistryAccess;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.ServerResources;
import net.minecraft.server.dedicated.DedicatedServer;
import net.minecraft.server.level.progress.ChunkProgressListenerFactory;
import net.minecraft.server.packs.repository.PackRepository;
import net.minecraft.server.players.GameProfileCache;
import net.minecraft.world.level.storage.LevelStorageSource;
import net.minecraft.world.level.storage.WorldData;
import org.apache.logging.log4j.LogManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.net.Proxy;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Mixin(DedicatedServer.class)
public abstract class DedicatedServerMixin extends MinecraftServer implements Wrap<FactoryServer>, MinecraftServerExtension {
    private WrappedFactoryServer wrapped;
    private FactoryAPI api;
    public DedicatedServerMixin(Thread thread, RegistryAccess.RegistryHolder registryHolder, LevelStorageSource.LevelStorageAccess levelStorageAccess, WorldData worldData, PackRepository packRepository, Proxy proxy, DataFixer dataFixer, ServerResources serverResources, MinecraftSessionService minecraftSessionService, GameProfileRepository gameProfileRepository, GameProfileCache gameProfileCache, ChunkProgressListenerFactory chunkProgressListenerFactory) {
        super(thread, registryHolder, levelStorageAccess, worldData, packRepository, proxy, dataFixer, serverResources, minecraftSessionService, gameProfileRepository, gameProfileCache, chunkProgressListenerFactory);
    }

    @Inject(method = "initServer", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/dedicated/DedicatedServer;loadLevel()V", shift = At.Shift.AFTER))
    public void loadAPI(CallbackInfoReturnable<Boolean> cir) {
        FactoryBuilder.LOGGER.info("Injecting the API...");

        this.wrapped = new WrappedFactoryServer((DedicatedServer) (Object) this);
        this.api = new FactoryAPI(wrapped, new BuilderImpl((DedicatedServer) (Object) this, LogManager.getLogger("FactoryWrapBuilder")));
        ObjectMappings.initIndependent();

        FactoryBuilder.LOGGER.info("Initializing plugins...");

        this.loadPlugins();

        FactoryBuilder.LOGGER.info("Done.");
    }

    @Inject(method = "initServer", at = @At("TAIL"))
    public void callServerStartupFinishEvent(CallbackInfoReturnable<Boolean> cir) {
        ServerStartupFinishEvent event = new ServerStartupFinishEvent(this.wrapped);
        ServerStartupFinishEvent.BACKEND.invoke(event);
        ServerStartupFinishEvent.BACKEND.invokeEndFunctions(event);
    }

    public void loadPlugins() {
        List<EntrypointContainer<FactoryPlugin>> entrypoints = FabricLoader.getInstance()
                .getEntrypointContainers("factory", FactoryPlugin.class);
        StringBuilder sB = new StringBuilder().append("Found ").append(entrypoints.size()).append(" plugin entrypoints");
        for (int i = 0; i < entrypoints.size(); i++) {
            if (i == 0) sB.append(": \n");
            sB.append("    - ")
                    .append(entrypoints.get(i).getEntrypoint().getClass().getSimpleName())
                    .append(" [")
                    .append(entrypoints.get(i).getProvider().getMetadata().getId())
                    .append("@")
                    .append(entrypoints.get(i).getProvider().getMetadata().getVersion())
                    .append("]");
            if (i < (entrypoints.size() - 1)) sB.append(", \n");
        }
        FactoryBuilder.LOGGER.info(sB.toString());

        List<ModContainer> loaded = new ArrayList<>();
        Map<ModContainer, List<FactoryPlugin>> modEntrypoints = new HashMap<>();
        for (EntrypointContainer<FactoryPlugin> container : entrypoints) {
            if (modEntrypoints.containsKey(container.getProvider())) {
                modEntrypoints.get(container.getProvider()).add(container.getEntrypoint());
            } else {
                List<FactoryPlugin> list = new ArrayList<>();
                list.add(container.getEntrypoint());
                modEntrypoints.put(container.getProvider(), list);
            }
        }

        modEntrypoints.keySet().forEach(mod -> this.loadEntrypoints(mod, modEntrypoints, loaded));
    }

    public void loadEntrypoints(ModContainer modContainer, Map<ModContainer, List<FactoryPlugin>> entrypointMap, List<ModContainer> loaded) {
        for (ModDependency dependencyContainer : modContainer.getMetadata().getDepends()) {
            ModContainer dependency = FabricLoader.getInstance().getModContainer(dependencyContainer.getModId()).orElseThrow(() -> new IllegalStateException("Dependency not present"));
            if (entrypointMap.containsKey(dependency)) {
                this.loadEntrypoints(dependency, entrypointMap, loaded);
            }
        }
        if (!loaded.contains(modContainer)) {
            entrypointMap.get(modContainer).forEach(entrypoint -> entrypoint.onFactoryLoad(this.api, this.wrapped));
            loaded.add(modContainer);
        }
    }

    @Override
    public FactoryServer wrap() {
        return wrapped;
    }
}
