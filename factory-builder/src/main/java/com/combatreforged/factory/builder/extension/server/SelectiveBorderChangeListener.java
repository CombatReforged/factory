package com.combatreforged.factory.builder.extension.server;

import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientboundSetBorderPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.border.BorderChangeListener;
import net.minecraft.world.level.border.WorldBorder;

import java.util.List;

public class SelectiveBorderChangeListener implements BorderChangeListener {
    private final List<ServerLevel> dimensions;

    public SelectiveBorderChangeListener(List<ServerLevel> dimensions) {
        this.dimensions = dimensions;
    }

    @Override
    public void onBorderSizeSet(WorldBorder worldBorder, double d) {
        this.sendPacket(new ClientboundSetBorderPacket(worldBorder, ClientboundSetBorderPacket.Type.SET_SIZE));
    }

    @Override
    public void onBorderSizeLerping(WorldBorder worldBorder, double d, double e, long l) {
        this.sendPacket(new ClientboundSetBorderPacket(worldBorder, ClientboundSetBorderPacket.Type.LERP_SIZE));
    }

    @Override
    public void onBorderCenterSet(WorldBorder worldBorder, double d, double e) {
        this.sendPacket(new ClientboundSetBorderPacket(worldBorder, ClientboundSetBorderPacket.Type.SET_CENTER));
    }

    @Override
    public void onBorderSetWarningTime(WorldBorder worldBorder, int i) {
        this.sendPacket(new ClientboundSetBorderPacket(worldBorder, ClientboundSetBorderPacket.Type.SET_WARNING_TIME));
    }

    @Override
    public void onBorderSetWarningBlocks(WorldBorder worldBorder, int i) {
        this.sendPacket(new ClientboundSetBorderPacket(worldBorder, ClientboundSetBorderPacket.Type.SET_WARNING_BLOCKS));
    }

    @Override
    public void onBorderSetDamagePerBlock(WorldBorder worldBorder, double d) {
    }

    @Override
    public void onBorderSetDamageSafeZOne(WorldBorder worldBorder, double d) {
    }

    private void sendPacket(Packet<?> packet) {
        for (ServerLevel dimension : dimensions) {
            dimension.getPlayers(player -> true).forEach(player -> player.connection.send(packet));
        }
    }
}
