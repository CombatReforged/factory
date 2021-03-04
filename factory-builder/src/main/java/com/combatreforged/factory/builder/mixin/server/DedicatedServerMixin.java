package com.combatreforged.factory.builder.mixin.server;

import com.combatreforged.factory.api.FactoryAPI;
import com.combatreforged.factory.api.FactoryServer;
import com.combatreforged.factory.builder.FactoryBuilder;
import com.combatreforged.factory.builder.extension.wrap.Wrap;
import com.combatreforged.factory.builder.implementation.WrappedFactoryServer;
import com.combatreforged.factory.builder.implementation.builder.BuilderImpl;
import com.mojang.authlib.GameProfileRepository;
import com.mojang.authlib.minecraft.MinecraftSessionService;
import com.mojang.datafixers.DataFixer;
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
import org.apache.logging.log4j.Logger;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.net.Proxy;

@Mixin(DedicatedServer.class)
public abstract class DedicatedServerMixin extends MinecraftServer implements Wrap<FactoryServer> {
    private WrappedFactoryServer wrapped;
    public DedicatedServerMixin(Thread thread, RegistryAccess.RegistryHolder registryHolder, LevelStorageSource.LevelStorageAccess levelStorageAccess, WorldData worldData, PackRepository packRepository, Proxy proxy, DataFixer dataFixer, ServerResources serverResources, MinecraftSessionService minecraftSessionService, GameProfileRepository gameProfileRepository, GameProfileCache gameProfileCache, ChunkProgressListenerFactory chunkProgressListenerFactory) {
        super(thread, registryHolder, levelStorageAccess, worldData, packRepository, proxy, dataFixer, serverResources, minecraftSessionService, gameProfileRepository, gameProfileCache, chunkProgressListenerFactory);
    }

    @Inject(method = "initServer", at = @At("HEAD"))
    public void loadAPI(CallbackInfoReturnable<Boolean> cir) {
        FactoryBuilder.LOGGER.info(FactoryBuilder.PREFIX + "Injecting the API...");
        FactoryAPI api = new FactoryAPI(new BuilderImpl(LogManager.getLogger("FactoryWrapBuilder")));
        this.wrapped = new WrappedFactoryServer((DedicatedServer) (Object) this, api);
    }
}
