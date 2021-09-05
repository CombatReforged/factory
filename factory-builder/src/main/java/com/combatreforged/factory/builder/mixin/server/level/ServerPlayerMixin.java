package com.combatreforged.factory.builder.mixin.server.level;

import com.combatreforged.factory.api.event.entity.LivingEntityDeathEvent;
import com.combatreforged.factory.api.event.player.PlayerChangeMovementStateEvent;
import com.combatreforged.factory.api.event.player.PlayerDeathEvent;
import com.combatreforged.factory.api.world.damage.DamageData;
import com.combatreforged.factory.api.world.entity.Entity;
import com.combatreforged.factory.api.world.entity.player.Player;
import com.combatreforged.factory.builder.extension.server.level.ServerPlayerExtension;
import com.combatreforged.factory.builder.extension.world.entity.LivingEntityExtension;
import com.combatreforged.factory.builder.extension.wrap.ChangeableWrap;
import com.combatreforged.factory.builder.implementation.Wrapped;
import com.combatreforged.factory.builder.implementation.util.ObjectMappings;
import com.combatreforged.factory.builder.implementation.world.damage.WrappedDamageData;
import com.combatreforged.factory.builder.implementation.world.entity.player.WrappedPlayer;
import com.combatreforged.factory.builder.implementation.world.scoreboard.WrappedScoreboardTeam;
import com.combatreforged.factory.builder.mixin.server.players.PlayerListAccessor;
import com.google.common.collect.ImmutableList;
import com.mojang.authlib.GameProfile;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientboundPlayerInfoPacket;
import net.minecraft.network.protocol.game.ClientboundSetObjectivePacket;
import net.minecraft.network.protocol.game.ClientboundSetPlayerTeamPacket;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.ServerScoreboard;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.level.ServerPlayerGameMode;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.scores.Objective;
import net.minecraft.world.scores.PlayerTeam;
import net.minecraft.world.scores.Team;
import org.checkerframework.common.aliasing.qual.Unique;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Mixin(ServerPlayer.class)
public abstract class ServerPlayerMixin extends net.minecraft.world.entity.player.Player implements ServerPlayerExtension, LivingEntityExtension {
    @Shadow public ServerGamePacketListenerImpl connection;
    @Shadow @Final public MinecraftServer server;
    private ServerScoreboard scoreboard;

    @Unique private Inventory prevInventory;
    @Unique private int prevExperienceLevel;
    @Unique private int prevTotalExperience;
    @Unique private float prevExperienceProgress;
    @Unique private int prevScore;

    @Unique private boolean deathEventHappened;
    @Unique private boolean keepExp;
    @Unique private boolean keepInv;

    @Unique private boolean lastFlyingState;

    @Unique private List<UUID> hiddenInTabList = new ArrayList<>();

    public ServerPlayerMixin(Level level, BlockPos blockPos, float f, GameProfile gameProfile) {
        super(level, blockPos, f, gameProfile);
    }

    @Inject(method = "<init>", at = @At("TAIL"))
    public void initialize(MinecraftServer minecraftServer, ServerLevel serverLevel, GameProfile gameProfile, ServerPlayerGameMode serverPlayerGameMode, CallbackInfo ci) {
        this.lastFlyingState = this.abilities.flying;
    }

    @Inject(method = "restoreFrom", at = @At("HEAD"))
    public void storeDefaultValues(ServerPlayer serverPlayer, boolean bl, CallbackInfo ci) {
        ServerPlayerExtension ext = (ServerPlayerExtension) serverPlayer;
        this.deathEventHappened = ext.hasDeathEventHappened();
        this.keepExp = ext.isKeepExp();
        this.keepInv = ext.isKeepInv();

        if (this.deathEventHappened) {
            this.prevInventory = this.inventory;
            this.prevExperienceLevel = this.experienceLevel;
            this.prevTotalExperience = this.totalExperience;
            this.prevExperienceProgress = this.experienceProgress;
            this.prevScore = this.getScore();
        }
    }

    @Inject(method = "restoreFrom", at = @At(value = "FIELD", target = "Lnet/minecraft/server/level/ServerPlayer;enchantmentSeed:I", opcode = Opcodes.PUTFIELD, shift = At.Shift.BEFORE))
    public void injectInventoryAndExperienceKeeping(ServerPlayer serverPlayer, boolean bl, CallbackInfo ci) {
        this.inventory.replaceWith(this.keepInv ? serverPlayer.inventory : this.prevInventory);

        this.experienceLevel = this.keepExp ? serverPlayer.experienceLevel : this.prevExperienceLevel;
        this.totalExperience = this.keepExp ? serverPlayer.totalExperience : this.prevTotalExperience;
        this.experienceProgress = this.keepExp ? serverPlayer.experienceProgress : this.prevExperienceProgress;
        this.setScore(this.keepExp ? serverPlayer.getScore() : this.prevScore);
    }

    @Inject(method = "restoreFrom", at = @At("TAIL"))
    public void copyWrappedOver(ServerPlayer serverPlayer, boolean bl, CallbackInfo ci) {
        @SuppressWarnings("unchecked") ChangeableWrap<Entity> wrap = (ChangeableWrap<Entity>) this;
        wrap.setWrap(Wrapped.wrap(serverPlayer, WrappedPlayer.class));
        if (wrap.wrap() instanceof WrappedPlayer) {
            ((WrappedPlayer) wrap.wrap()).updatePlayer((ServerPlayer) (Object) this);
        }
    }

    @Inject(method = "<init>", at = @At("TAIL"))
    public void injectScoreboard(MinecraftServer minecraftServer, ServerLevel serverLevel, GameProfile gameProfile, ServerPlayerGameMode serverPlayerGameMode, CallbackInfo ci) {
        this.scoreboard = minecraftServer.getScoreboard();
    }

    @Override
    public ServerScoreboard getScoreboard() {
        return this.scoreboard;
    }

    @Override
    public void setScoreboard(ServerScoreboard newScoreboard) {
        scoreboard.getPlayerTeams().forEach(team -> this.connection.send(new ClientboundSetPlayerTeamPacket(team, 1)));
        for (int i = 0; i < 19; i++) {
            Objective objective = scoreboard.getDisplayObjective(i);
            if (objective != null) {
                this.connection.send(new ClientboundSetObjectivePacket(objective, 1));
            }
        }
        this.scoreboard = newScoreboard;
        ((PlayerListAccessor) this.server.getPlayerList()).invokeUpdateEntireScoreboard(scoreboard, (ServerPlayer) (Object) this);
    }

    @Override
    public boolean isKeepExp() {
        return keepExp;
    }

    @Override
    public boolean isKeepInv() {
        return keepInv;
    }

    @Override
    public boolean hasDeathEventHappened() {
        return deathEventHappened;
    }

    @Override
    public List<ServerPlayer> getHiddenInTabList() {
        return this.hiddenInTabList.stream()
                .map(uuid -> this.server.getPlayerList().getPlayer(uuid))
                .collect(ImmutableList.toImmutableList());
    }

    @Override
    public void showInTabList(ServerPlayer player, boolean show, boolean update) {
        if (show && hiddenInTabList.contains(player.getUUID())) {
            if (update) this.connection.send(new ClientboundPlayerInfoPacket(ClientboundPlayerInfoPacket.Action.ADD_PLAYER, player));
            this.hiddenInTabList.remove(player.getUUID());
        } else if (!show && !hiddenInTabList.contains(player.getUUID())) {
            if (update) this.connection.send(new ClientboundPlayerInfoPacket(ClientboundPlayerInfoPacket.Action.REMOVE_PLAYER, player));
            this.hiddenInTabList.add(player.getUUID());
        }
    }

    //BEGIN: LivingEntityDeathEvent
    @Inject(method = "die", at = @At("HEAD"))
    public void injectLivingEntityDeathEvent(DamageSource damageSource, CallbackInfo ci) {
        Player player = Wrapped.wrap(this, WrappedPlayer.class);
        DamageData data = Wrapped.wrap(damageSource, WrappedDamageData.class);
        boolean mobLoot = this.level.getGameRules().getBoolean(GameRules.RULE_DOMOBLOOT);
        boolean keepInventory = this.level.getGameRules().getBoolean(GameRules.RULE_KEEPINVENTORY);
        LivingEntityDeathEvent deathEvent = new LivingEntityDeathEvent(player, data, mobLoot, keepInventory, keepInventory);
        this.setDeathEvent(deathEvent);
        LivingEntityDeathEvent.BACKEND.invoke(this.getDeathEvent());
        this.deathEventHappened = true;
        this.keepInv = !this.getDeathEvent().isDropEquipment();
        this.keepExp = !this.getDeathEvent().isDropExperience();
    }

    @Unique private PlayerDeathEvent playerDeathEvent;
    @Inject(method = "die", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/network/ServerGamePacketListenerImpl;send(Lnet/minecraft/network/protocol/Packet;Lio/netty/util/concurrent/GenericFutureListener;)V", shift = At.Shift.BEFORE), locals = LocalCapture.CAPTURE_FAILEXCEPTION)
    public void injectPlayerDeathEvent(DamageSource damageSource, CallbackInfo ci, boolean b, Component component) {
        LivingEntityDeathEvent deathEvent = this.getDeathEvent();
        net.kyori.adventure.text.Component deathMessage = ObjectMappings.convertComponent(component);
        this.playerDeathEvent = new PlayerDeathEvent(
                Wrapped.wrap(this, WrappedPlayer.class),
                deathEvent.getCause(),
                deathEvent.isDropLoot(),
                deathEvent.isDropEquipment(),
                deathEvent.isDropExperience(),
                deathMessage,
                WrappedScoreboardTeam.VISIBLE_MAP.inverse().get(this.getTeam() != null ? this.getTeam().getDeathMessageVisibility() : Team.Visibility.ALWAYS));
        PlayerDeathEvent.BACKEND.invoke(this.playerDeathEvent);
    }

    @ModifyVariable(method = "die", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/level/ServerPlayer;getTeam()Lnet/minecraft/world/scores/Team;", shift = At.Shift.BEFORE), ordinal = 0)
    public Component modifyDeathMessage(Component prev) {
        return this.playerDeathEvent != null ? ObjectMappings.convertComponent(playerDeathEvent.getDeathMessage()) : prev;
    }

    @Redirect(method = "die", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/level/ServerPlayer;getTeam()Lnet/minecraft/world/scores/Team;"))
    public Team modifyTeam(ServerPlayer serverPlayer) {
        if (this.playerDeathEvent != null) {
            PlayerTeam team = new PlayerTeam(this.server.getScoreboard(), "_deathEventTeam");
            team.setDeathMessageVisibility(WrappedScoreboardTeam.VISIBLE_MAP.get(playerDeathEvent.getVisibleFor()));
            return team;
        } else {
            return this.getTeam();
        }
    }

    @Inject(method = "die", at = @At("TAIL"))
    public void nullifyLivingEntityDeathEvent(DamageSource damageSource, CallbackInfo ci) {
        LivingEntityDeathEvent.BACKEND.invokeEndFunctions(this.getDeathEvent());
        this.setDeathEvent(null);
        PlayerDeathEvent.BACKEND.invokeEndFunctions(this.playerDeathEvent);
        this.playerDeathEvent = null;
    }
    //END: LivingEntityDeathEvent

    @Inject(method = "onUpdateAbilities", at = @At("HEAD"))
    public void injectChangeMovementStateEvent(CallbackInfo ci) {
        if (this.lastFlyingState != this.abilities.flying) {
            PlayerChangeMovementStateEvent changeMovementStateEvent = new PlayerChangeMovementStateEvent(Wrapped.wrap(this, WrappedPlayer.class), PlayerChangeMovementStateEvent.ChangedState.FLYING, this.abilities.flying);
            PlayerChangeMovementStateEvent.BACKEND.invoke(changeMovementStateEvent);
            if (changeMovementStateEvent.isCancelled()) {
                this.abilities.flying = this.lastFlyingState;
            } else {
                this.abilities.flying = changeMovementStateEvent.getChangedValue();
            }
            PlayerChangeMovementStateEvent.BACKEND.invokeEndFunctions(changeMovementStateEvent);
        }
    }
}
