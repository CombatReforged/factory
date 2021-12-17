package com.combatreforged.factory.builder.mixin.server.level;

import com.combatreforged.factory.api.event.entity.LivingEntityDeathEvent;
import com.combatreforged.factory.api.event.player.PlayerCloseContainerEvent;
import com.combatreforged.factory.api.event.player.PlayerDeathEvent;
import com.combatreforged.factory.api.event.player.PlayerOpenContainerEvent;
import com.combatreforged.factory.api.world.damage.DamageData;
import com.combatreforged.factory.api.world.entity.player.Player;
import com.combatreforged.factory.api.world.scoreboard.ScoreboardTeam;
import com.combatreforged.factory.builder.extension.server.level.ServerPlayerExtension;
import com.combatreforged.factory.builder.extension.world.entity.LivingEntityExtension;
import com.combatreforged.factory.builder.extension.wrap.ChangeableWrap;
import com.combatreforged.factory.builder.implementation.Wrapped;
import com.combatreforged.factory.builder.implementation.util.ObjectMappings;
import com.combatreforged.factory.builder.implementation.world.damage.WrappedDamageData;
import com.combatreforged.factory.builder.implementation.world.entity.player.WrappedPlayer;
import com.combatreforged.factory.builder.implementation.world.item.container.menu.WrappedContainerMenu;
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
import net.minecraft.world.MenuProvider;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
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
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.ArrayList;
import java.util.List;
import java.util.OptionalInt;
import java.util.UUID;
import java.util.stream.Collectors;

@Mixin(ServerPlayer.class)
public abstract class ServerPlayerMixin extends net.minecraft.world.entity.player.Player implements ServerPlayerExtension, LivingEntityExtension {
    @Shadow public ServerGamePacketListenerImpl connection;
    @Shadow @Final public MinecraftServer server;
    @Shadow private int containerCounter;
    private ServerScoreboard scoreboard;

    @Unique private Inventory prevInventory;
    @Unique private int prevExperienceLevel;
    @Unique private int prevTotalExperience;
    @Unique private float prevExperienceProgress;
    @Unique private int prevScore;

    @Unique private boolean deathEventHappened;
    @Unique private boolean keepExp;
    @Unique private boolean keepInv;

    @Unique private final List<UUID> hiddenInTabList = new ArrayList<>();

    @Unique private Component containerMenuTitle;

    public ServerPlayerMixin(Level level, BlockPos blockPos, float f, GameProfile gameProfile) {
        super(level, blockPos, f, gameProfile);
    }

    @Inject(method = "restoreFrom", at = @At("HEAD"))
    public void storeDefaultValues(ServerPlayer serverPlayer, boolean bl, CallbackInfo ci) {
        ServerPlayerExtension ext = (ServerPlayerExtension) serverPlayer;
        this.deathEventHappened = ext.hasDeathEventHappened();
        this.keepExp = ext.isKeepExp();
        this.keepInv = ext.isKeepInv();
        this.scoreboard = ext.getScoreboard();
        this.hiddenInTabList.clear();
        this.hiddenInTabList.addAll(ext.getHiddenInTabList().stream().map(Entity::getUUID).collect(Collectors.toList()));

        if (this.deathEventHappened) {
            this.prevInventory = this.inventory;
            this.prevExperienceLevel = this.experienceLevel;
            this.prevTotalExperience = this.totalExperience;
            this.prevExperienceProgress = this.experienceProgress;
            this.prevScore = this.getScore();
        }

        this.containerMenuTitle = null;
    }

    @Inject(method = "restoreFrom", at = @At(value = "FIELD", target = "Lnet/minecraft/server/level/ServerPlayer;enchantmentSeed:I", opcode = Opcodes.PUTFIELD, shift = At.Shift.BEFORE))
    public void injectInventoryAndExperienceKeeping(ServerPlayer serverPlayer, boolean bl, CallbackInfo ci) {
        this.inventory.replaceWith(this.keepInv ? serverPlayer.inventory : this.prevInventory);

        this.experienceLevel = this.keepExp ? serverPlayer.experienceLevel : this.prevExperienceLevel;
        this.totalExperience = this.keepExp ? serverPlayer.totalExperience : this.prevTotalExperience;
        this.experienceProgress = this.keepExp ? serverPlayer.experienceProgress : this.prevExperienceProgress;
        this.setScore(this.keepExp ? serverPlayer.getScore() : this.prevScore);
    }

    @SuppressWarnings("unchecked")
    @Inject(method = "restoreFrom", at = @At("TAIL"))
    public void copyWrappedOver(ServerPlayer serverPlayer, boolean bl, CallbackInfo ci) {
        WrappedPlayer oldWrap = Wrapped.wrap(serverPlayer, WrappedPlayer.class);
        oldWrap.updatePlayer((ServerPlayer) (Object) this);
        ((ChangeableWrap<Player>) this).setWrap(oldWrap);
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

    @Override
    public Component getLastContainerMenuTitle() {
        return containerMenuTitle;
    }

    //BEGIN: LivingEntityDeathEvent
    @Inject(method = "die", at = @At("HEAD"))
    public void injectLivingEntityDeathEvent(DamageSource damageSource, CallbackInfo ci) {
        Player player = Wrapped.wrap(this, WrappedPlayer.class);
        DamageData data = Wrapped.wrap(damageSource, WrappedDamageData.class);
        boolean mobLoot = this.level.getGameRules().getBoolean(GameRules.RULE_DOMOBLOOT);
        boolean keepInventory = this.level.getGameRules().getBoolean(GameRules.RULE_KEEPINVENTORY);
        this.playerDeathEvent = new PlayerDeathEvent(player, data, mobLoot, !keepInventory, !keepInventory, null, ScoreboardTeam.VisibleFor.NO_ONE);
        this.setDeathEvent(playerDeathEvent);
    }

    @Unique private PlayerDeathEvent playerDeathEvent;
    @Inject(method = "die", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/network/ServerGamePacketListenerImpl;send(Lnet/minecraft/network/protocol/Packet;Lio/netty/util/concurrent/GenericFutureListener;)V", shift = At.Shift.BEFORE, ordinal = 0), locals = LocalCapture.CAPTURE_FAILEXCEPTION)
    public void injectPlayerDeathEvent(DamageSource damageSource, CallbackInfo ci, boolean b, Component component) {
        this.playerDeathEvent.setDeathMessage(ObjectMappings.convertComponent(component));
        this.playerDeathEvent.setVisibleFor(WrappedScoreboardTeam.VISIBLE_MAP.inverse().get(this.getTeam() != null ? this.getTeam().getDeathMessageVisibility() : Team.Visibility.ALWAYS));
        LivingEntityDeathEvent.BACKEND.invoke(this.playerDeathEvent);
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

    @Inject(method = "die", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/level/ServerPlayer;removeEntitiesOnShoulder()V", shift = At.Shift.BEFORE))
    public void loadValues(DamageSource damageSource, CallbackInfo ci) {
        this.deathEventHappened = true;
        this.keepInv = !this.getDeathEvent().isDropEquipment();
        this.keepExp = !this.getDeathEvent().isDropExperience();
    }

    @Inject(method = "die", at = @At("TAIL"))
    public void nullifyLivingEntityDeathEvent(DamageSource damageSource, CallbackInfo ci) {
        LivingEntityDeathEvent.BACKEND.invokeEndFunctions(this.getDeathEvent());
        this.setDeathEvent(null);
        PlayerDeathEvent.BACKEND.invokeEndFunctions(this.playerDeathEvent);
        this.playerDeathEvent = null;
    }
    //END: LivingEntityDeathEvent

    @Unique private boolean injectOpenContainerEvent = true;
    @Inject(method = "openMenu", at = @At(value = "INVOKE_ASSIGN", target = "Lnet/minecraft/world/MenuProvider;createMenu(ILnet/minecraft/world/entity/player/Inventory;Lnet/minecraft/world/entity/player/Player;)Lnet/minecraft/world/inventory/AbstractContainerMenu;", shift = At.Shift.AFTER), locals = LocalCapture.CAPTURE_FAILEXCEPTION, cancellable = true)
    public void injectOpenContainerEvent(MenuProvider menuProvider, CallbackInfoReturnable<OptionalInt> cir, AbstractContainerMenu abstractContainerMenu) {
        this.containerMenuTitle = menuProvider.getDisplayName();
        if (injectOpenContainerEvent) {
            injectOpenContainerEvent = false;
            PlayerOpenContainerEvent event = new PlayerOpenContainerEvent(Wrapped.wrap(this, WrappedPlayer.class),
                    Wrapped.wrap(abstractContainerMenu, WrappedContainerMenu.class));
            PlayerOpenContainerEvent.BACKEND.invoke(event);

            if (event.isCancelled()) {
                this.rollbackContainerCounter();
                cir.cancel();
            }

            PlayerOpenContainerEvent.BACKEND.invokeEndFunctions(event);

            injectOpenContainerEvent = true;
        }
    }

    @Inject(method = "closeContainer", at = @At("HEAD"), cancellable = true)
    public void injectCloseContainerEvent(CallbackInfo ci) {
        PlayerCloseContainerEvent event = new PlayerCloseContainerEvent(Wrapped.wrap(this, WrappedPlayer.class), Wrapped.wrap(this.containerMenu, WrappedContainerMenu.class));
        PlayerCloseContainerEvent.BACKEND.invoke(event);

        if (event.isCancelled()) {
            ci.cancel();
        }

        PlayerCloseContainerEvent.BACKEND.invokeEndFunctions(event);
    }

    private void rollbackContainerCounter() {
        if (this.containerCounter <= 0) {
            this.containerCounter = 99;
        }
        else {
            this.containerCounter--;
        }
    }
}
