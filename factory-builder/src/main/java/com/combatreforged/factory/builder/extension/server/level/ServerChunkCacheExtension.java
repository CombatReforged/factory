package com.combatreforged.factory.builder.extension.server.level;

public interface ServerChunkCacheExtension {
    void setThread(Thread thread);
    Thread getThread();
}
