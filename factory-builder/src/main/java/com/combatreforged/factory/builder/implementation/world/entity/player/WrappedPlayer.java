package com.combatreforged.factory.builder.implementation.world.entity.player;

import com.combatreforged.factory.api.world.entity.player.GameModeType;
import com.combatreforged.factory.api.world.entity.player.Player;
import com.combatreforged.factory.api.world.item.container.PlayerInventory;
import com.combatreforged.factory.api.world.scoreboard.Scoreboard;
import com.combatreforged.factory.api.world.util.Vector3D;
import com.combatreforged.factory.builder.extension.FoodDataExtension;
import com.combatreforged.factory.builder.implementation.Wrapped;
import com.combatreforged.factory.builder.implementation.util.ObjectMappings;
import com.combatreforged.factory.builder.implementation.world.entity.WrappedLivingEntity;
import com.combatreforged.factory.builder.implementation.world.item.container.WrappedPlayerInventory;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.title.Title;
import net.minecraft.network.chat.ChatType;
import net.minecraft.network.protocol.game.ClientboundSetEntityMotionPacket;
import net.minecraft.server.level.ServerPlayer;

import java.util.UUID;

public class WrappedPlayer extends WrappedLivingEntity implements Player {
    final ServerPlayer wrappedPlayer;
    public WrappedPlayer(ServerPlayer wrappedPlayer) {
        super(wrappedPlayer);
        this.wrappedPlayer = wrappedPlayer;
    }

    @Override
    public int getFoodLevel() {
        return wrappedPlayer.getFoodData().getFoodLevel();
    }

    @Override
    public float getSaturation() {
        return wrappedPlayer.getFoodData().getSaturationLevel();
    }

    @Override
    public void setFoodLevel(int level) {
        wrappedPlayer.getFoodData().setFoodLevel(level);
    }

    @Override
    public void setSaturation(float saturation) {
        wrappedPlayer.getFoodData().setSaturation(saturation);
    }

    @Override
    public float getExhaustion() {
        return ((FoodDataExtension) wrappedPlayer.getFoodData()).getExhaustion();
    }

    @Override
    public void setExhaustion(float exhaustion) {
        ((FoodDataExtension) wrappedPlayer.getFoodData()).setExhaustion(exhaustion);
    }

    @Override
    public PlayerInventory getInventory() {
        return Wrapped.wrap(wrappedPlayer.inventory, WrappedPlayerInventory.class);
    }

    @Override
    public int getSelectedSlot() {
        return wrappedPlayer.inventory.selected;
    }

    @Override
    public void sendTitle(Title title) {
        //TODO
    }

    @Override
    public GameModeType getGameMode() {
        return ObjectMappings.GAME_MODES.inverse().get(wrappedPlayer.gameMode.getGameModeForPlayer());
    }

    @Override
    public void setGameMode(GameModeType gameMode) {
        wrappedPlayer.setGameMode(ObjectMappings.GAME_MODES.get(gameMode));
    }

    @Override
    public UUID getUUID() {
        return wrappedPlayer.getUUID();
    }

    @Override
    public Scoreboard getScoreboard() {
        return null; //TODO
    }

    @Override
    public void setScoreboard(Scoreboard scoreboard) {
        //TODO
    }

    @Override
    public void setServerScoreboard() {
        //TODO
    }

    @Override
    public void setVelocity(Vector3D velocity) {
        super.setVelocity(velocity);
        wrappedPlayer.connection.send(new ClientboundSetEntityMotionPacket(wrappedPlayer));
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
        wrappedPlayer.sendMessage(ObjectMappings.convertComponent(component), chatType, UUID.randomUUID());
    }
}
