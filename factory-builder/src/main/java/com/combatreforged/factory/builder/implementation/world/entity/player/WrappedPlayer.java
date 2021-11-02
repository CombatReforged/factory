package com.combatreforged.factory.builder.implementation.world.entity.player;

import com.combatreforged.factory.api.world.entity.player.GameModeType;
import com.combatreforged.factory.api.world.entity.player.Player;
import com.combatreforged.factory.api.world.item.container.PlayerInventory;
import com.combatreforged.factory.api.world.item.container.menu.ContainerMenu;
import com.combatreforged.factory.api.world.item.container.menu.MenuHolder;
import com.combatreforged.factory.api.world.nbt.NBTObject;
import com.combatreforged.factory.api.world.scoreboard.Scoreboard;
import com.combatreforged.factory.api.world.util.Location;
import com.combatreforged.factory.api.world.util.Vector3D;
import com.combatreforged.factory.builder.exception.WrappingException;
import com.combatreforged.factory.builder.extension.server.level.ServerPlayerExtension;
import com.combatreforged.factory.builder.extension.world.food.FoodDataExtension;
import com.combatreforged.factory.builder.implementation.Wrapped;
import com.combatreforged.factory.builder.implementation.util.ObjectMappings;
import com.combatreforged.factory.builder.implementation.world.WrappedWorld;
import com.combatreforged.factory.builder.implementation.world.entity.WrappedLivingEntity;
import com.combatreforged.factory.builder.implementation.world.item.container.WrappedPlayerInventory;
import com.combatreforged.factory.builder.implementation.world.item.container.menu.WrappedContainerMenu;
import com.combatreforged.factory.builder.implementation.world.item.container.menu.WrappedMenuHolder;
import com.combatreforged.factory.builder.implementation.world.scoreboard.WrappedScoreboard;
import com.google.common.collect.ImmutableList;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.title.Title;
import net.minecraft.network.chat.ChatType;
import net.minecraft.network.protocol.game.ClientboundSetEntityMotionPacket;
import net.minecraft.network.protocol.game.ClientboundSetTitlesPacket;
import net.minecraft.network.protocol.game.ServerboundClientCommandPacket;
import net.minecraft.server.ServerScoreboard;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.SimpleMenuProvider;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

import static com.combatreforged.factory.builder.implementation.util.ObjectMappings.convertComponent;

public class WrappedPlayer extends WrappedLivingEntity implements Player {
    @Override
    public void teleport(Location location, boolean ignoreDirection) {
        ServerLevel level = ((WrappedWorld) location.getWorld()).unwrap();
        float yRot = !ignoreDirection ? location.getYaw() % 360 : wrappedPlayer().yRot;
        float xRot = !ignoreDirection ? Mth.clamp(location.getPitch(), -90.0F, 90.0F) % 360.0F : wrappedPlayer().xRot;
        wrappedPlayer().teleportTo(level, location.getX(), location.getY(), location.getZ(), yRot, xRot);
    }

    public WrappedPlayer(ServerPlayer wrappedPlayer) {
        super(wrappedPlayer);
    }

    @Override
    public void setEntityNBT(NBTObject nbt) {
        super.setEntityNBT(nbt);
        wrappedPlayer().onUpdateAbilities();
    }

    @Override
    public int getFoodLevel() {
        return wrappedPlayer().getFoodData().getFoodLevel();
    }

    @Override
    public float getSaturation() {
        return wrappedPlayer().getFoodData().getSaturationLevel();
    }

    @Override
    public void setFoodLevel(int level) {
        wrappedPlayer().getFoodData().setFoodLevel(level);
    }

    @Override
    public void setSaturation(float saturation) {
        ((FoodDataExtension) wrappedPlayer().getFoodData()).setSaturationServer(saturation);
    }

    @Override
    public float getExhaustion() {
        return ((FoodDataExtension) wrappedPlayer().getFoodData()).getExhaustion();
    }

    @Override
    public void setExhaustion(float exhaustion) {
        ((FoodDataExtension) wrappedPlayer().getFoodData()).setExhaustion(exhaustion);
    }

    @Override
    public PlayerInventory getInventory() {
        return Wrapped.wrap(wrappedPlayer().inventory, WrappedPlayerInventory.class);
    }

    @Override
    public int getSelectedSlot() {
        return wrappedPlayer().inventory.selected;
    }

    @Override
    public boolean isFlying() {
        return wrappedPlayer().abilities.flying;
    }

    @Override
    public void setFlying(boolean flying) {
        wrappedPlayer().abilities.flying = flying;
        wrappedPlayer().onUpdateAbilities();
    }

    @Override
    public boolean isAbleToFly() {
        return wrappedPlayer().abilities.mayfly;
    }

    @Override
    public void setAbleToFly(boolean ableToFly) {
        wrappedPlayer().abilities.mayfly = ableToFly;
        wrappedPlayer().onUpdateAbilities();
        if (!ableToFly && this.isFlying()) {
            this.setFlying(false);
        }
    }

    @Override
    public boolean isFallFlying() {
        return wrappedPlayer().isFallFlying();
    }

    @Override
    public void setFallFlying(boolean fallFlying) {
        if (fallFlying && !this.isFallFlying()) {
            wrappedPlayer().startFallFlying();
        } else if (!fallFlying && this.isFallFlying()) {
            wrappedPlayer().stopFallFlying();
        }
    }

    @Override
    public void setExperienceLevel(int level) {
        wrappedPlayer().setExperienceLevels(level);
    }

    @Override
    public void setExperiencePoints(int points) {
        wrappedPlayer().setExperiencePoints(points);
    }

    @Override
    public int getExperienceLevel() {
        return wrappedPlayer().experienceLevel;
    }

    @Override
    public int getExperiencePoints() {
        return (int) (wrappedPlayer().experienceProgress * wrappedPlayer().getXpNeededForNextLevel());
    }

    @Override
    public void sendTitle(Title title) {
        net.minecraft.network.chat.Component mcTitle = convertComponent(title.title());
        net.minecraft.network.chat.Component mcSubTitle = convertComponent(title.subtitle());
        Title.Times times = title.times();

        if (times != null) {
            int in = (int) (times.fadeIn().toMillis() / 50);
            int stay = (int) (times.stay().toMillis() / 50);
            int out = (int) (times.fadeOut().toMillis() / 50);
            wrappedPlayer().connection.send(new ClientboundSetTitlesPacket(in, stay, out));
        }

        if (mcTitle != null) {
            wrappedPlayer().connection.send(new ClientboundSetTitlesPacket(ClientboundSetTitlesPacket.Type.TITLE, mcTitle));
        }

        if (mcSubTitle != null) {
            wrappedPlayer().connection.send(new ClientboundSetTitlesPacket(ClientboundSetTitlesPacket.Type.SUBTITLE, mcSubTitle));
        }
    }

    @Override
    public void clearTitle() {
        wrappedPlayer().connection.send(new ClientboundSetTitlesPacket(ClientboundSetTitlesPacket.Type.CLEAR, null));
    }

    @Override
    public void resetTitle() {
        wrappedPlayer().connection.send(new ClientboundSetTitlesPacket(ClientboundSetTitlesPacket.Type.RESET, null));
    }

    @Override
    public void sendActionBarMessage(Component component) {
        wrappedPlayer().connection.send(new ClientboundSetTitlesPacket(ClientboundSetTitlesPacket.Type.ACTIONBAR, convertComponent(component)));
    }

    @Override
    public void openMenu(MenuHolder creator) {
        SimpleMenuProvider provider = ((WrappedMenuHolder) creator).unwrap();
        wrappedPlayer().openMenu(provider);
    }

    @Override
    public void closeMenu() {
        this.wrappedPlayer().closeContainer();
    }

    @Override
    @Nullable public ContainerMenu getOpenMenu() {
        return Wrapped.wrap(wrappedPlayer().containerMenu, WrappedContainerMenu.class);
    }

    @Override
    public GameModeType getGameMode() {
        return ObjectMappings.GAME_MODES.inverse().get(wrappedPlayer().gameMode.getGameModeForPlayer());
    }

    @Override
    public void setGameMode(GameModeType gameMode) {
        wrappedPlayer().setGameMode(ObjectMappings.GAME_MODES.get(gameMode));
    }

    @Override
    public String getRawName() {
        return wrappedPlayer().getGameProfile().getName();
    }

    @Override
    public UUID getUUID() {
        return wrappedPlayer().getUUID();
    }

    @Override
    public Scoreboard getScoreboard() {
        return Wrapped.wrap(((ServerPlayerExtension) wrappedPlayer()).getScoreboard(), WrappedScoreboard.class);
    }

    @Override
    public void setScoreboard(Scoreboard scoreboard) {
        WrappedScoreboard wrappedScoreboard;
        try {
            wrappedScoreboard = (WrappedScoreboard) scoreboard;
        } catch (ClassCastException e) {
            throw new WrappingException("Scoreboard not a WrappedScoreboard");
        }
        if (!(wrappedScoreboard.unwrap() instanceof ServerScoreboard)) {
            throw new IllegalStateException("Scoreboard is not a ServerScoreboard");
        }
        ((ServerPlayerExtension) wrappedPlayer()).setScoreboard(((ServerScoreboard) wrappedScoreboard.unwrap()));
    }

    @Override
    public void setServerScoreboard() {
        ((ServerPlayerExtension) wrappedPlayer())
                .setScoreboard(Objects.requireNonNull(wrappedPlayer().getServer()).getScoreboard());
    }

    @Override
    public Component getDisplayName() {
        return convertComponent(wrappedPlayer().getDisplayName());
    }

    public void updatePlayer(ServerPlayer player) {
        this.wrapped = player;
    }

    @Override
    public void respawn() {
        if (this.wrappedPlayer().isDeadOrDying()) {
            wrappedPlayer().connection.handleClientCommand(new ServerboundClientCommandPacket(ServerboundClientCommandPacket.Action.PERFORM_RESPAWN));
        }
    }

    @Override
    public void showPlayerInTabList(Player player, boolean show) {
        ((ServerPlayerExtension) wrappedPlayer()).showInTabList(((WrappedPlayer) player).unwrap(), show, true);
    }

    @Override
    public List<Player> getShownInTabList() {
        return this.getServer().getPlayers().stream()
                .filter(player -> !((ServerPlayerExtension) wrappedPlayer()).getHiddenInTabList().contains(((WrappedPlayer) player).unwrap()))
                .collect(ImmutableList.toImmutableList());
    }

    @Override
    public void setVelocity(Vector3D velocity) {
        super.setVelocity(velocity);
        wrappedPlayer().connection.send(new ClientboundSetEntityMotionPacket(wrappedPlayer()));
    }

    @Override
    public void sendMessage(Component component) {
        sendMessage(component, Type.SYSTEM);
    }

    @Override
    public void sendMessage(Component component, Type type) {
        ChatType chatType;
        switch (type) {
            case CHAT:
                chatType = ChatType.CHAT;
                break;
            case SYSTEM:
            default:
                chatType = ChatType.SYSTEM;
                break;
            case ACTION_BAR:
                chatType = ChatType.GAME_INFO;
                break;
        }
        wrappedPlayer().sendMessage(ObjectMappings.convertComponent(component), chatType, UUID.randomUUID());
    }

    @Override
    public ServerPlayer unwrap() {
        return wrappedPlayer();
    }

    private ServerPlayer wrappedPlayer() {
        return (ServerPlayer) this.wrapped;
    }
}
