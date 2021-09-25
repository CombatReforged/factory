package com.combatreforged.factory.builder.implementation.builder;

import com.combatreforged.factory.api.FactoryServer;
import com.combatreforged.factory.api.builder.Builder;
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
import com.combatreforged.factory.api.world.entity.projectile.Projectile;
import com.combatreforged.factory.api.world.item.ItemStack;
import com.combatreforged.factory.api.world.item.ItemType;
import com.combatreforged.factory.api.world.nbt.NBTList;
import com.combatreforged.factory.api.world.nbt.NBTObject;
import com.combatreforged.factory.api.world.nbt.NBTValue;
import com.combatreforged.factory.api.world.scoreboard.Scoreboard;
import com.combatreforged.factory.api.world.util.Location;
import com.combatreforged.factory.builder.exception.NBTParsingException;
import com.combatreforged.factory.builder.implementation.Wrapped;
import com.combatreforged.factory.builder.implementation.command.CommandSourceInfoImpl;
import com.combatreforged.factory.builder.implementation.util.ImplementationUtilsImpl;
import com.combatreforged.factory.builder.implementation.util.ObjectMappings;
import com.combatreforged.factory.builder.implementation.world.WrappedWorld;
import com.combatreforged.factory.builder.implementation.world.block.WrappedBlockState;
import com.combatreforged.factory.builder.implementation.world.damage.WrappedDamageData;
import com.combatreforged.factory.builder.implementation.world.effect.WrappedStatusEffectInstance;
import com.combatreforged.factory.builder.implementation.world.entity.WrappedEntity;
import com.combatreforged.factory.builder.implementation.world.item.WrappedItemStack;
import com.combatreforged.factory.builder.implementation.world.nbt.WrappedNBTList;
import com.combatreforged.factory.builder.implementation.world.nbt.WrappedNBTObject;
import com.combatreforged.factory.builder.implementation.world.nbt.WrappedNBTValue;
import com.combatreforged.factory.builder.implementation.world.scoreboard.WrappedScoreboard;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.kyori.adventure.nbt.api.BinaryTagHolder;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.*;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.ServerScoreboard;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import org.apache.commons.lang3.NotImplementedException;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public class BuilderImpl implements Builder {
    final Logger logger;
    final MinecraftServer server;

    public BuilderImpl(MinecraftServer server, Logger logger) {
        this.logger = logger;
        this.server = server;
    }

    @Override
    public ImplementationUtils createImplementationUtils() {
        return new ImplementationUtilsImpl();
    }

    @Override
    public Entity createEntity(EntityType type, World world) {
        net.minecraft.world.entity.EntityType<?> entityType = ObjectMappings.ENTITIES.get(type);
        if (!entityType.canSummon()) {
            throw new UnsupportedOperationException("Cannot summon " + type.toString());
        }
        return Wrapped.wrap(entityType.create(((WrappedWorld) world).unwrap()), WrappedEntity.class);
    }

    @Override
    public Player createNPCPlayer(World world, UUID uuid, String name) {
        throw new NotImplementedException("Creating NPC players not supported yet");
    }

    @Override
    public NBTObject createNBTObject(@Nullable Map<String, NBTValue> values) {
        NBTObject nbt = Wrapped.wrap(new CompoundTag(), WrappedNBTObject.class);
        if (values != null) {
            nbt.setAll(values);
        }
        return nbt;
    }

    @Override
    public NBTObject createNBTObjectFromString(String string) {
        try {
            return Wrapped.wrap(TagParser.parseTag(string), WrappedNBTObject.class);
        } catch (CommandSyntaxException e) {
            throw new NBTParsingException(string);
        }
    }

    @Override
    public NBTList createNBTList(List<NBTValue> values) {
        NBTList list = Wrapped.wrap(new ListTag(), WrappedNBTList.class);
        if (values != null) {
            list.addAll(values);
        }
        return list;
    }

    @Override
    public NBTValue createNBTValue(short s) {
        return fromTag(ShortTag.valueOf(s));
    }

    @Override
    public NBTValue createNBTValue(double d) {
        return fromTag(DoubleTag.valueOf(d));
    }

    @Override
    public NBTValue createNBTValue(float f) {
        return fromTag(FloatTag.valueOf(f));
    }

    @Override
    public NBTValue createNBTValue(byte b) {
        return fromTag(ByteTag.valueOf(b));
    }

    @Override
    public NBTValue createNBTValue(int i) {
        return fromTag(IntTag.valueOf(i));
    }

    @Override
    public NBTValue createNBTValue(long l) {
        return fromTag(LongTag.valueOf(l));
    }

    @Override
    public NBTValue createNBTValue(long[] arr) {
        return fromTag(new LongArrayTag(arr));
    }

    @Override
    public NBTValue createNBTValue(byte[] arr) {
        return fromTag(new ByteArrayTag(arr));
    }

    @Override
    public NBTValue createNBTValue(int[] arr) {
        return fromTag(new IntArrayTag(arr));
    }

    @Override
    public NBTValue createNBTValue(String string) {
        return fromTag(StringTag.valueOf(string));
    }

    @Override
    public StatusEffectInstance createStatusEffectInstance(StatusEffect statusEffect, int duration, int amplifier, boolean ambient) {
        MobEffectInstance mei = new MobEffectInstance(ObjectMappings.EFFECTS.get(statusEffect), duration, amplifier, ambient, ambient, ambient);
        return Wrapped.wrap(mei, WrappedStatusEffectInstance.class);
    }

    @Override
    public DamageData createDamageData(DamageData.Type type, Entity entityCause, boolean isIndirect) {
        DamageSource ds = DamageSource.GENERIC;
        if (ObjectMappings.DAMAGE_TYPES.containsKey(type)) {
            ds = ObjectMappings.DAMAGE_TYPES.get(type);
        }
        else {
            switch (type) {
                case ENTITY_ATTACK:
                    ds = DamageSource.mobAttack((LivingEntity) ((WrappedEntity) entityCause).unwrap());
                    break;
                case EXPLOSION:
                    if (entityCause != null) {
                        ds = DamageSource.explosion((LivingEntity) ((WrappedEntity) entityCause).unwrap());
                    } else {
                        ds = DamageSource.explosion((Explosion) null);
                    }
                    break;
                case PROJECTILE:
                    if (entityCause instanceof Projectile) {
                        Projectile projectile = (Projectile) entityCause;
                        ds = DamageSource.indirectMobAttack(((WrappedEntity) projectile).unwrap(), (LivingEntity) ((WrappedEntity) projectile.getOwner()).unwrap());
                    }
                    break;
                default:

            }
        }
        return Wrapped.wrap(ds, WrappedDamageData.class);
    }

    @Deprecated
    @Override
    public ItemStack createItemStack(ItemType itemType, int count, int damage, BinaryTagHolder tag) {
        net.minecraft.world.item.ItemStack stack = new net.minecraft.world.item.ItemStack(ObjectMappings.ITEMS.get(itemType), count);
        if (damage != 0) {
            stack.setDamageValue(damage);
        }
        try {
            stack.setTag(TagParser.parseTag(tag.toString()));
        } catch (CommandSyntaxException e) {
            throw new UnsupportedOperationException("Tag is invalid");
        }

        return Wrapped.wrap(stack, WrappedItemStack.class);
    }

    @Override
    public ItemStack createItemStack(ItemType itemType, int count, int damage, @Nullable NBTObject nbt) {
        net.minecraft.world.item.ItemStack stack = new net.minecraft.world.item.ItemStack(ObjectMappings.ITEMS.get(itemType), count);
        if (nbt != null) {
            stack.setTag(((WrappedNBTObject) nbt).unwrap());
        }
        if (damage != 0) {
            stack.setDamageValue(damage);
        }

        return Wrapped.wrap(stack, WrappedItemStack.class);
    }

    @Override
    public Scoreboard createScoreboard() {
        return Wrapped.wrap(new ServerScoreboard(server), WrappedScoreboard.class);
    }

    @Override
    public BlockState createBlockState(BlockType type, Location location) {
        return new WrappedBlockState(location, ObjectMappings.BLOCKS.get(type).defaultBlockState());
    }

    @Override
    public BlockState blockStateOfBlock(Block block) {
        Location loc = block.getLocation();
        BlockPos pos = new BlockPos(loc.getX(), loc.getY(), loc.getZ());
        Level level = ((WrappedWorld) loc.getWorld()).unwrap();
        return new WrappedBlockState(block.getLocation(), level.getBlockState(pos));
    }

    @Override
    public CommandSourceInfo createCommandSourceInfo(CommandSender sender, @Nullable Entity executingEntity, Location location, FactoryServer server) {
        return new CommandSourceInfoImpl(sender, executingEntity, location, server);
    }

    @Override
    public CommandDispatcher<CommandSourceInfo> getCommandDispatcher() {
        return new CommandDispatcher<>();
    }

    private NBTValue fromTag(Tag tag) {
        return Wrapped.wrap(tag, WrappedNBTValue.class);
    }
}
