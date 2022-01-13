package com.combatreforged.factory.builder.mixin.server;

import com.combatreforged.factory.api.FactoryAPI;
import com.combatreforged.factory.api.FactoryServer;
import com.combatreforged.factory.api.event.server.ServerStopEvent;
import com.combatreforged.factory.api.event.server.ServerTickEvent;
import com.combatreforged.factory.builder.extension.server.MinecraftServerExtension;
import com.combatreforged.factory.builder.extension.server.SelectiveBorderChangeListener;
import com.combatreforged.factory.builder.extension.server.level.ServerChunkCacheExtension;
import com.combatreforged.factory.builder.extension.world.level.LevelExtension;
import com.combatreforged.factory.builder.extension.world.level.storage.LevelStorageAccessExtension;
import com.combatreforged.factory.builder.extension.world.level.storage.PrimaryLevelDataExtension;
import com.combatreforged.factory.builder.implementation.Wrapped;
import com.combatreforged.factory.builder.implementation.WrappedFactoryServer;
import com.combatreforged.factory.builder.implementation.dynamicworld.DynamicWorld;
import com.google.common.collect.ImmutableList;
import net.minecraft.commands.CommandSource;
import net.minecraft.core.BlockPos;
import net.minecraft.core.MappedRegistry;
import net.minecraft.core.Registry;
import net.minecraft.core.RegistryAccess;
import net.minecraft.nbt.NbtOps;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.RegistryReadOps;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.ServerResources;
import net.minecraft.server.TickTask;
import net.minecraft.server.bossevents.CustomBossEvents;
import net.minecraft.server.dedicated.DedicatedServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.level.progress.ChunkProgressListener;
import net.minecraft.server.level.progress.ChunkProgressListenerFactory;
import net.minecraft.server.players.PlayerList;
import net.minecraft.util.thread.ReentrantBlockableEventLoop;
import net.minecraft.world.SnooperPopulator;
import net.minecraft.world.entity.ai.village.VillageSiege;
import net.minecraft.world.entity.npc.CatSpawner;
import net.minecraft.world.entity.npc.WanderingTraderSpawner;
import net.minecraft.world.level.CustomSpawner;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelSettings;
import net.minecraft.world.level.biome.BiomeManager;
import net.minecraft.world.level.border.BorderChangeListener;
import net.minecraft.world.level.border.WorldBorder;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.level.dimension.LevelStem;
import net.minecraft.world.level.levelgen.NoiseBasedChunkGenerator;
import net.minecraft.world.level.levelgen.PatrolSpawner;
import net.minecraft.world.level.levelgen.PhantomSpawner;
import net.minecraft.world.level.levelgen.WorldGenSettings;
import net.minecraft.world.level.storage.*;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.BooleanSupplier;
import java.util.stream.Collectors;

@Mixin(MinecraftServer.class)
public abstract class MinecraftServerMixin extends ReentrantBlockableEventLoop<TickTask> implements SnooperPopulator, CommandSource, AutoCloseable, MinecraftServerExtension {
    @Shadow
    public abstract boolean isDedicatedServer();

    @Shadow
    private int tickCount;

    @Shadow
    public abstract String getServerModName();

    @Shadow
    public abstract Optional<String> getModdedStatus();

    @Shadow
    @Final
    private ChunkProgressListenerFactory progressListenerFactory;

    @Shadow
    @Final
    protected RegistryAccess.RegistryHolder registryHolder;

    @Shadow
    @Final
    private Map<ResourceKey<Level>, ServerLevel> levels;

    @Shadow
    @SuppressWarnings("unused")
    private static void setInitialSpawn(ServerLevel serverLevel, ServerLevelData serverLevelData, boolean bl, boolean bl2, boolean bl3) {
    }

    @Shadow
    public abstract CustomBossEvents getCustomBossEvents();

    @Shadow
    @Final
    private Executor executor;

    @Shadow
    public abstract ServerLevel overworld();

    @Shadow
    private ServerResources resources;

    @Shadow
    public abstract PlayerList getPlayerList();

    @Shadow
    @Final
    private static Logger LOGGER;

    @Shadow
    @Final
    private Thread serverThread;

    @Shadow private PlayerList playerList;

    public MinecraftServerMixin(String string) {
        super(string);
    }

    @Inject(method = "getServerModName", at = @At("RETURN"), cancellable = true)
    public void changeBrandName(CallbackInfoReturnable<String> cir) {
        cir.setReturnValue(cir.getReturnValue() + "+factory-builder");
    }

    @Unique
    private ServerTickEvent tickEvent;

    @Inject(method = "tickServer",
            at = @At(value = "INVOKE",
                    target = "Lnet/minecraft/server/MinecraftServer;tickChildren(Ljava/util/function/BooleanSupplier;)V",
                    shift = At.Shift.BEFORE)
    )
    public void callServerStartTickEvent(BooleanSupplier booleanSupplier, CallbackInfo ci) {
        if (this.isDedicatedServer()) {
            FactoryServer server = Wrapped.wrap(this, WrappedFactoryServer.class);
            this.tickEvent = new ServerTickEvent(server, this.tickCount);
            ServerTickEvent.BACKEND.invoke(tickEvent);
        }
    }

    @Inject(method = "tickServer",
            at = @At(value = "INVOKE",
                    target = "Lnet/minecraft/server/MinecraftServer;tickChildren(Ljava/util/function/BooleanSupplier;)V",
                    shift = At.Shift.AFTER)
    )
    public void callServerEndTickEvent(BooleanSupplier booleanSupplier, CallbackInfo ci) {
        if (this.isDedicatedServer()) {
            ServerTickEvent.BACKEND.invokeEndFunctions(tickEvent);
            tickEvent = null;
        }
    }

    @Inject(method = "saveAllChunks", at = @At("RETURN"))
    public void saveDynamicWorlds(boolean bl, boolean bl2, boolean bl3, CallbackInfoReturnable<Boolean> cir) {
        dynamicWorlds.forEach((key, value) -> this.saveOverworldData(key));
    }

    @SuppressWarnings("ConstantConditions")
    @Inject(method = "stopServer", at = @At(value = "INVOKE", target = "Lorg/apache/logging/log4j/Logger;info(Ljava/lang/String;)V", ordinal = 0, shift = At.Shift.AFTER, remap = false))
    public void callServerStopEvent(CallbackInfo ci) {
        if ((Object) this instanceof DedicatedServer) {
            ServerStopEvent event = new ServerStopEvent(Wrapped.wrap(this, WrappedFactoryServer.class));
            ServerStopEvent.BACKEND.invoke(event);
            ServerStopEvent.BACKEND.invokeEndFunctions(event);
        }

        this.worldExec.shutdown();
    }

    @Unique
    private final Map<String, DynamicWorld> dynamicWorlds = new HashMap<>();

    @Override @ApiStatus.Experimental
    public void loadDynamicWorldSync(@Nullable String name, LevelStorageSource.LevelStorageAccess access) {
        RegistryReadOps<Tag> registryReadOps = RegistryReadOps.create(NbtOps.INSTANCE, this.resources.getResourceManager(), registryHolder);
        WorldData worldData = access.getDataTag(registryReadOps, access.getDataPacks());
        if (worldData == null) {
            throw new IllegalStateException("Can't read world data options from directory");
        }
        if (worldData instanceof PrimaryLevelData && name != null) {
            PrimaryLevelData primLevelData = ((PrimaryLevelData) worldData);
            LevelSettings settings = ((PrimaryLevelDataExtension) primLevelData).getSettings();
            LevelSettings newSettings = new LevelSettings(name, settings.gameType(), settings.hardcore(), settings.difficulty(), settings.allowCommands(), settings.gameRules(), settings.getDataPackConfig());
            ((PrimaryLevelDataExtension) primLevelData).setSettings(newSettings);
            this.loadDynamicWorldSync0(worldData, access);
        } else {
            throw new IllegalStateException("Invalid world data");
        }
    }

    @Override @ApiStatus.Experimental
    public CompletableFuture<Void> loadDynamicWorldAsync(@Nullable String name, LevelStorageSource.LevelStorageAccess access) {
        RegistryReadOps<Tag> registryReadOps = RegistryReadOps.create(NbtOps.INSTANCE, this.resources.getResourceManager(), registryHolder);
        WorldData worldData = access.getDataTag(registryReadOps, access.getDataPacks());
        if (worldData == null) {
            throw new IllegalStateException("Can't read world data options from directory");
        }
        if (worldData instanceof PrimaryLevelData && name != null) {
            PrimaryLevelData primLevelData = ((PrimaryLevelData) worldData);
            LevelSettings settings = ((PrimaryLevelDataExtension) primLevelData).getSettings();
            LevelSettings newSettings = new LevelSettings(name, settings.gameType(), settings.hardcore(), settings.difficulty(), settings.allowCommands(), settings.gameRules(), settings.getDataPackConfig());
            ((PrimaryLevelDataExtension) primLevelData).setSettings(newSettings);
            return this.loadDynamicWorldAsync0(worldData, access);
        } else {
            throw new IllegalStateException("Invalid world data");
        }
    }

    private int worldExecCount = 0;
    private final ExecutorService worldExec = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors(), runnable -> new Thread(runnable, "DynamicWorldLoader-" + (worldExecCount += 1)));

    @SuppressWarnings("FutureReturnValueIgnored")
    private CompletableFuture<Void> loadDynamicWorldAsync0(WorldData worldData, LevelStorageSource.LevelStorageAccess access) {
        DimensionType overworldType = this.registryHolder.dimensionTypes().get(DimensionType.OVERWORLD_LOCATION);
        NoiseBasedChunkGenerator defaultGenerator = WorldGenSettings.makeDefaultOverworld(this.registryHolder.registryOrThrow(Registry.BIOME_REGISTRY), this.registryHolder.registryOrThrow(Registry.NOISE_GENERATOR_SETTINGS_REGISTRY), new Random().nextLong());

        CompletableFuture<Void> future = new CompletableFuture<>();

        CompletableFuture.runAsync(() -> {
            DynamicWorld world = this.loadDynamicWorldFromFile(worldData, access, overworldType, defaultGenerator);
            FactoryAPI.getInstance().getScheduler().schedule(() -> {
                this.registerDynamicWorld(world);
                future.complete(null);
            }, 0);
        }, this.worldExec).whenComplete((void_, t) -> future.completeExceptionally(t));

        return future;
    }

    private void loadDynamicWorldSync0(WorldData worldData, LevelStorageSource.LevelStorageAccess access) {
        DynamicWorld dynamicWorld = this.loadDynamicWorldFromFile(worldData, access,
                this.registryHolder.dimensionTypes().get(DimensionType.OVERWORLD_LOCATION),
                WorldGenSettings.makeDefaultOverworld(this.registryHolder.registryOrThrow(Registry.BIOME_REGISTRY), this.registryHolder.registryOrThrow(Registry.NOISE_GENERATOR_SETTINGS_REGISTRY), new Random().nextLong()));
        this.registerDynamicWorld(dynamicWorld);
    }

    @SuppressWarnings("ConstantConditions")
    private DynamicWorld loadDynamicWorldFromFile(WorldData worldData, LevelStorageSource.LevelStorageAccess access, DimensionType overworldType, NoiseBasedChunkGenerator defaultGenerator) {
        String levelName = worldData.getLevelName().toLowerCase(Locale.ROOT).replaceAll("[^a-z0-9.\\-_]", "_");

        if (dynamicWorlds.containsKey(levelName)) {
            throw new IllegalArgumentException("Custom world '" + levelName + "' is already registered on this server");
        }
        LOGGER.info("Loading custom world '" + levelName + "' dynamically...");

        ChunkProgressListener chunkProgressListener = this.progressListenerFactory.create(11);
        worldData.setModdedInfo(this.getServerModName(), this.getModdedStatus().isPresent());
        worldData.setDifficulty(this.overworld().getDifficulty());

        ((LevelStorageAccessExtension) access).setCustom(levelName);

        // INFO copy from MinecraftServer.createLevels with custom world data
        ServerLevelData serverLevelData = worldData.overworldData();
        WorldGenSettings worldGenSettings = worldData.worldGenSettings();
        long seed = worldGenSettings.seed();
        long biomeSeed = BiomeManager.obfuscateSeed(seed);
        List<CustomSpawner> customSpawners = ImmutableList.of(new PhantomSpawner(), new PatrolSpawner(), new CatSpawner(), new VillageSiege(), new WanderingTraderSpawner(serverLevelData));
        MappedRegistry<LevelStem> dimensions = worldGenSettings.dimensions();
        LevelStem levelStem = dimensions.get(LevelStem.OVERWORLD);
        ChunkGenerator generator;
        DimensionType dimensionType;

        if (levelStem == null) {
            dimensionType = overworldType;
            generator = defaultGenerator;
        } else {
            dimensionType = levelStem.type();
            generator = levelStem.generator();
        }

        List<ServerLevel> worldDimensions = new ArrayList<>();
        ResourceKey<Level> adaptedOverworldKey = ResourceKey.create(Registry.DIMENSION_REGISTRY, new ResourceLocation(levelName + "." + Level.OVERWORLD.location().getNamespace(), Level.OVERWORLD.location().getPath()));
        ServerLevel overworld = new ServerLevel((MinecraftServer) (Object) this, this.executor, access, serverLevelData, adaptedOverworldKey, dimensionType, chunkProgressListener, generator, false, biomeSeed, customSpawners, true);
        ((LevelExtension) overworld).setThread(this.serverThread);
        ((ServerChunkCacheExtension) overworld.getChunkSource()).setThread(this.serverThread);
        worldDimensions.add(overworld);

        WorldBorder worldBorder = overworld.getWorldBorder();
        worldBorder.applySettings(serverLevelData.getWorldBorder());
        if (!serverLevelData.isInitialized()) {
            setInitialSpawn(overworld, serverLevelData, worldGenSettings.generateBonusChest(), false, true);
            serverLevelData.setInitialized(true);
        }

        if (worldData.getCustomBossEvents() != null) {
            this.getCustomBossEvents().load(worldData.getCustomBossEvents());
        }

        for (ResourceLocation location : dimensions.keySet()) {
            LevelStem stem = dimensions.get(location);
            if (stem != null && stem.type() != overworldType) {
                ResourceLocation adaptedLocation = new ResourceLocation(levelName + "." + location.getNamespace(), location.getPath());
                ResourceKey<Level> adaptedKey = ResourceKey.create(Registry.DIMENSION_REGISTRY, adaptedLocation);
                DimensionType dimType = stem.type();
                ChunkGenerator dimGenerator = stem.generator();
                DerivedLevelData derivedLevelData = new DerivedLevelData(worldData, serverLevelData);
                ServerLevel dimension = new ServerLevel((MinecraftServer) (Object) this, this.executor, access, derivedLevelData, adaptedKey, dimType, chunkProgressListener, dimGenerator, false, biomeSeed, ImmutableList.of(), false);
                ((LevelExtension) dimension).setThread(this.serverThread);
                ((ServerChunkCacheExtension) dimension.getChunkSource()).setThread(this.serverThread);
                worldBorder.addListener(new BorderChangeListener.DelegateBorderChangeListener(dimension.getWorldBorder()));
                worldDimensions.add(dimension);
            }
        }
        worldBorder.addListener(new SelectiveBorderChangeListener(worldDimensions));
        LOGGER.info("Done loading '" + levelName + "'.");
        return new DynamicWorld(levelName, overworld, worldDimensions, access, worldData);
    }

    // IMPORTANT: has to be run in server thread!
    private void registerDynamicWorld(DynamicWorld world) {
        if (Thread.currentThread() != this.serverThread) {
            throw new IllegalThreadStateException("Not run in server thread");
        }
        this.dynamicWorlds.put(world.getLevelName(), world);
        this.levels.putAll(world.getDimensions().stream()
                .collect(Collectors.<ServerLevel, ResourceKey<Level>, ServerLevel>toMap(Level::dimension, obj -> obj)));
        LOGGER.info("Registered dynamic world '" + world.getLevelName() + "'.");
    }


    @Override
    @ApiStatus.Experimental
    public void unloadDynamicWorld(String name, boolean save) throws IOException {
        if (dynamicWorlds.containsKey(name)) {
            DynamicWorld dynamicWorld = dynamicWorlds.get(name);
            if (dynamicWorld.isLoaded()) {
                LOGGER.info("Unloading dynamically loaded custom world '" + name + "'...");
                ServerLevel defaultOverworld = this.overworld();
                BlockPos spawnPos = defaultOverworld.getSharedSpawnPos();
                for (ServerLevel level : new ArrayList<>(dynamicWorld.getDimensions())) {
                    for (ServerPlayer player : playerList.getPlayers()) {
                        if (player.getLevel().equals(level)) {
                            player.teleportTo(this.overworld(), spawnPos.getX() + 0.5, spawnPos.getY() + 0.5, spawnPos.getZ() + 0.5, defaultOverworld.getSharedSpawnAngle(), 0.0F);
                        }
                        if (player.getRespawnDimension().equals(level.dimension())) {
                            player.setRespawnPosition(defaultOverworld.dimension(), spawnPos, defaultOverworld.getSharedSpawnAngle(), true, false);
                        }
                    }

                    if (save) {
                        level.save(null, true, false);
                        level.close();
                    } else {
                        level.getChunkSource().getLightEngine().close();
                        level.getChunkSource().chunkMap.close();
                    }

                    this.levels.remove(level.dimension());
                }

                if (save) {
                    this.saveOverworldData(name);
                }

                dynamicWorld.getSource().close();
                dynamicWorld.unloaded();

                this.dynamicWorlds.remove(name);

                LOGGER.info("Done unloading.");

                return;
            }
        }
        LOGGER.warn("Tried unloading non-loaded world '" + name + "'!");
    }

    @Override
    @ApiStatus.Experimental
    public void saveDynamicWorld(String name) {
        if (dynamicWorlds.containsKey(name)) {
            DynamicWorld dynamicWorld = dynamicWorlds.get(name);
            if (dynamicWorld.isLoaded()) {
                for (ServerLevel level : dynamicWorld.getDimensions()) {
                    level.save(null, true, false);
                }

                this.saveOverworldData(name);
            }
        }
    }

    @ApiStatus.Experimental
    public void saveOverworldData(String name) {
        if (dynamicWorlds.containsKey(name)) {
            DynamicWorld dynamicWorld = dynamicWorlds.get(name);
            ServerLevel overworld = dynamicWorld.getOverworld();
            WorldData worldData = dynamicWorld.getWorldData();
            worldData.overworldData().setWorldBorder(overworld.getWorldBorder().createSettings());
            worldData.setCustomBossEvents(this.getCustomBossEvents().save());
            dynamicWorld.getSource().saveDataTag(this.registryHolder, worldData, this.getPlayerList().getSingleplayerData());
        }
    }

    @Override
    public boolean hasDynamicWorld(String name) {
        return dynamicWorlds.containsKey(name);
    }

    @Override
    public ServerLevel getOverworldForLevel(ServerLevel serverLevel) {
        return this.dynamicWorlds.values().stream()
                .filter(dynamicWorld -> dynamicWorld.getDimensions().contains(serverLevel))
                .map(DynamicWorld::getOverworld)
                .findFirst().orElse(serverLevel);
    }

    @Override
    public List<ServerLevel> getRelatedLevels(ServerLevel serverLevel) {
        List<ServerLevel> dynamicLevels = this.dynamicWorlds.values().stream()
                .flatMap(dynWorld -> dynWorld.getDimensions().stream())
                .collect(Collectors.toList());
        if (!dynamicLevels.contains(serverLevel)) {
            return this.levels.values().stream()
                    .filter(level -> !dynamicLevels.contains(level))
                    .collect(Collectors.toList());
        } else {
            return dynamicWorlds.values().stream()
                    .map(DynamicWorld::getDimensions)
                    .filter(dimensions -> dimensions.contains(serverLevel))
                    .findFirst()
                    .orElse(ImmutableList.of());
        }
    }
}
