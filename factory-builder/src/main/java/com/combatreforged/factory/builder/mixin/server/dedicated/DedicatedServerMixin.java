package com.combatreforged.factory.builder.mixin.server.dedicated;

import com.combatreforged.factory.api.FactoryAPI;
import com.combatreforged.factory.api.FactoryServer;
import com.combatreforged.factory.api.entrypoint.FactoryPlugin;
import com.combatreforged.factory.builder.FactoryBuilder;
import com.combatreforged.factory.builder.extension.wrap.Wrap;
import com.combatreforged.factory.builder.implementation.WrappedFactoryServer;
import com.combatreforged.factory.builder.implementation.builder.BuilderImpl;
import com.mojang.authlib.GameProfileRepository;
import com.mojang.authlib.minecraft.MinecraftSessionService;
import com.mojang.datafixers.DataFixer;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loom.util.FabricApiExtension;
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
import java.util.List;

@Mixin(DedicatedServer.class)
public abstract class DedicatedServerMixin extends MinecraftServer implements Wrap<FactoryServer> {
    private WrappedFactoryServer wrapped;
    public DedicatedServerMixin(Thread thread, RegistryAccess.RegistryHolder registryHolder, LevelStorageSource.LevelStorageAccess levelStorageAccess, WorldData worldData, PackRepository packRepository, Proxy proxy, DataFixer dataFixer, ServerResources serverResources, MinecraftSessionService minecraftSessionService, GameProfileRepository gameProfileRepository, GameProfileCache gameProfileCache, ChunkProgressListenerFactory chunkProgressListenerFactory) {
        super(thread, registryHolder, levelStorageAccess, worldData, packRepository, proxy, dataFixer, serverResources, minecraftSessionService, gameProfileRepository, gameProfileCache, chunkProgressListenerFactory);
    }

    @Inject(method = "initServer", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/dedicated/DedicatedServer;loadLevel()V", shift = At.Shift.AFTER))
    public void loadAPI(CallbackInfoReturnable<Boolean> cir) {
        FactoryBuilder.LOGGER.info("Injecting the API...");

        this.wrapped = new WrappedFactoryServer((DedicatedServer) (Object) this);
        FactoryAPI api = new FactoryAPI(wrapped, new BuilderImpl(LogManager.getLogger("FactoryWrapBuilder")));

        FactoryBuilder.LOGGER.info("Initializing plugins...");

        List<FactoryPlugin> plugins = FabricLoader.getInstance().getEntrypoints("factory", FactoryPlugin.class);
        StringBuilder sB = new StringBuilder().append("Found ").append(plugins.size()).append(" plugin entrypoints");
        for (int i = 0; i < plugins.size(); i++) {
            if (i == 0) sB.append(": ");
            sB.append(plugins.get(i).getClass().getSimpleName());
            if (i < (plugins.size() - 1)) sB.append(",");
        }
        FactoryBuilder.LOGGER.info(sB.toString());
        api.initPlugins(plugins);

        FactoryBuilder.LOGGER.info("Done.");
    }

    @Override
    public FactoryServer wrap() {
        return wrapped;
    }
}
