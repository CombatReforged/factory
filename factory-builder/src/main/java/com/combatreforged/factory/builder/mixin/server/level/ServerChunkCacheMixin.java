package com.combatreforged.factory.builder.mixin.server.level;

import com.combatreforged.factory.builder.extension.server.level.ServerChunkCacheExtension;
import net.minecraft.server.level.ServerChunkCache;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(ServerChunkCache.class)
public abstract class ServerChunkCacheMixin implements ServerChunkCacheExtension {
    @Shadow @Mutable @Final private Thread mainThread;

    @Override
    public void setThread(Thread thread) {
        this.mainThread = thread;
    }

    @Override
    public Thread getThread() {
        return mainThread;
    }
}
