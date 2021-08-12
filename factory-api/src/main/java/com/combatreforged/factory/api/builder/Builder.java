package com.combatreforged.factory.api.builder;

import com.combatreforged.factory.api.FactoryAPI;
import com.combatreforged.factory.api.FactoryServer;
import com.combatreforged.factory.api.command.CommandSender;
import com.combatreforged.factory.api.command.CommandSourceInfo;
import com.combatreforged.factory.api.util.ImplementationUtils;
import com.combatreforged.factory.api.world.World;
import com.combatreforged.factory.api.world.block.Block;
import com.combatreforged.factory.api.world.block.BlockState;
import com.combatreforged.factory.api.world.block.BlockType;
import com.combatreforged.factory.api.world.damage.DamageData;
import com.combatreforged.factory.api.world.effect.StatusEffect;
import com.combatreforged.factory.api.world.effect.StatusEffectInstance;
import com.combatreforged.factory.api.world.entity.Entity;
import com.combatreforged.factory.api.world.entity.EntityType;
import com.combatreforged.factory.api.world.entity.player.Player;
import com.combatreforged.factory.api.world.item.ItemStack;
import com.combatreforged.factory.api.world.item.ItemType;
import com.combatreforged.factory.api.world.nbt.NBTList;
import com.combatreforged.factory.api.world.nbt.NBTObject;
import com.combatreforged.factory.api.world.nbt.NBTValue;
import com.combatreforged.factory.api.world.scoreboard.Scoreboard;
import com.combatreforged.factory.api.world.util.Location;
import com.mojang.brigadier.CommandDispatcher;
import net.kyori.adventure.nbt.api.BinaryTagHolder;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public interface Builder {
    ImplementationUtils createImplementationUtils();

    Entity createEntity(EntityType type, World world);

    Player createNPCPlayer(World world, UUID uuid, String name);

    NBTObject createNBTObject(@Nullable Map<String, NBTValue> values);
    NBTObject createNBTObjectFromString(String string);

    NBTList createNBTList(List<NBTValue> values);

    NBTValue createNBTValue(short s);
    NBTValue createNBTValue(double d);
    NBTValue createNBTValue(float f);
    NBTValue createNBTValue(byte b);
    NBTValue createNBTValue(int i);
    NBTValue createNBTValue(long l);
    NBTValue createNBTValue(long[] arr);
    NBTValue createNBTValue(byte[] arr);
    NBTValue createNBTValue(int[] arr);
    NBTValue createNBTValue(String string);

    StatusEffectInstance createStatusEffectInstance(StatusEffect statusEffect, int duration,
                                                    int amplifier, boolean ambient);

    DamageData createDamageData(DamageData.Type type, Entity entityCause, boolean isIndirect);

    @Deprecated ItemStack createItemStack(ItemType itemType, int count, int damage, BinaryTagHolder tag);
    ItemStack createItemStack(ItemType itemType, int count, int damage, NBTObject nbt);

    Scoreboard createScoreboard();

    BlockState createBlockState(BlockType type, Location location);
    BlockState blockStateOfBlock(Block block);

    CommandSourceInfo createCommandSourceInfo(CommandSender sender, @Nullable Entity executingEntity, Location location, FactoryServer server);

    CommandDispatcher<CommandSourceInfo> getCommandDispatcher();

    static Builder getInstance() {
        return FactoryAPI.getInstance().getBuilder();
    }
}
