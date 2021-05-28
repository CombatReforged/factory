package net.rizecookey.factorytest;

import com.combatreforged.factory.api.FactoryAPI;
import com.combatreforged.factory.api.FactoryServer;
import com.combatreforged.factory.api.entrypoint.FactoryPlugin;
import com.combatreforged.factory.api.event.player.PlayerJoinEvent;
import com.combatreforged.factory.api.event.player.PlayerMoveEvent;
import com.combatreforged.factory.api.world.Weather;
import com.combatreforged.factory.api.world.World;
import com.combatreforged.factory.api.world.block.Block;
import com.combatreforged.factory.api.world.block.BlockEntity;
import com.combatreforged.factory.api.world.entity.Entity;
import com.combatreforged.factory.api.world.entity.equipment.ArmorSlot;
import com.combatreforged.factory.api.world.entity.equipment.HandSlot;
import com.combatreforged.factory.api.world.entity.player.Player;
import com.combatreforged.factory.api.world.item.ItemStack;
import com.combatreforged.factory.api.world.nbt.NBTList;
import com.combatreforged.factory.api.world.nbt.NBTObject;
import com.combatreforged.factory.api.world.nbt.NBTValue;
import com.combatreforged.factory.api.world.types.Minecraft;
import com.combatreforged.factory.api.world.util.Location;
import com.combatreforged.factory.api.world.util.Vector3D;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.Style;
import net.kyori.adventure.text.format.TextDecoration;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class TestPlugin implements FactoryPlugin {
    Logger logger = LogManager.getLogger();
    @Override
    public void onFactoryLoad(FactoryAPI api, FactoryServer server) {
        logger.info("Hello! I was loaded within Factory!");

        PlayerMoveEvent.BACKEND.register(event -> {
            Player player = event.getPlayer();

            if (player.isSneaking()) player.addVelocity(new Vector3D(0.0, 0.2, 0.0));
        });

        PlayerJoinEvent.BACKEND.register(event -> {
            Player player = event.getPlayer();
            World world = player.getWorld();
            Block block = world.getBlockAt(player.getLocation());

            player.setGameMode(Minecraft.GameMode.CREATIVE);
            player.getInventory().clear();
            player.getInventory().addItemStack(ItemStack.create(Minecraft.Item.NETHERITE_SWORD));
            player.setEquipmentStack(ArmorSlot.HEAD, ItemStack.create(Minecraft.Item.NETHERITE_HELMET));
            player.setEquipmentStack(ArmorSlot.CHEST, ItemStack.create(Minecraft.Item.DIAMOND_CHESTPLATE));
            player.setEquipmentStack(ArmorSlot.LEGS, ItemStack.create(Minecraft.Item.GOLDEN_LEGGINGS));
            player.setEquipmentStack(ArmorSlot.FEET, ItemStack.create(Minecraft.Item.IRON_BOOTS));
            player.setEquipmentStack(HandSlot.OFF_HAND, ItemStack.create(Minecraft.Item.GOLDEN_APPLE, 16));

            ItemStack stick = ItemStack.create(Minecraft.Item.STICK);
            /*NBTObject nbt = stick.getItemNBT();
            NBTList enchantments = NBTList.create();
            NBTObject knockback = NBTObject.create();
            knockback.set("id", NBTValue.of("minecraft:knockback"));
            knockback.set("lvl", NBTValue.of((short) 2));
            enchantments.add(knockback);
            nbt.set("Enchantments", enchantments);
            stick.setItemNBT(nbt);*/
            stick.enchant(Minecraft.Enchantment.KNOCKBACK, 2);
            stick.getEnchantments().forEach(ench -> System.out.println(ench + " (lvl: " + stick.getLevel(ench) + ")"));
            stick.setLore(Component.text()
                    .content("Haha Gomme cool xDdDdDD")
                    .decoration(TextDecoration.BOLD, true)
                    .decoration(TextDecoration.ITALIC, false)
                    .build());

            player.getInventory().setItemStack(4, stick);

            world.setDayTime(0);
            world.setWeather(Weather.THUNDER, 60);
            if (block.hasPropertyValue(Block.StateProperty.SLAB_TYPE)) {
                block.setPropertyValue(Block.StateProperty.SLAB_TYPE, Block.StateProperty.SlabType.TOP);
            }
            if (world.isBlockEntity(player.getLocation())) {
                Location loc = player.getLocation();
                BlockEntity blockEntity = world.getBlockEntity(loc);
                NBTObject blockNBT = blockEntity.getBlockNBT();
                System.out.println("Current block: " + block.getType().toString() + blockNBT.asString());

                NBTList list = NBTList.create();
                NBTObject item = NBTObject.create();
                item.set("id", NBTValue.of("minecraft:stone"));
                item.set("Slot", NBTValue.of((byte) 8));
                item.set("Count", NBTValue.of((byte) 1));
                list.add(item);

                blockNBT.set("Items", list);

                blockEntity.setBlockNBT(blockNBT);
            }

            Entity firework = Entity.create(Minecraft.Entity.FIREWORK_ROCKET, world);
            firework.teleport(player.getLocation());
            NBTObject obj = firework.getEntityNBT();
            obj.set("LifeTime", NBTValue.of(30));
            firework.setEntityNBT(obj);
            world.spawn(firework);
        });
    }
}
