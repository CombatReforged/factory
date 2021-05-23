package net.rizecookey.factorytest;

import com.combatreforged.factory.api.FactoryAPI;
import com.combatreforged.factory.api.FactoryServer;
import com.combatreforged.factory.api.entrypoint.FactoryPlugin;
import com.combatreforged.factory.api.event.player.PlayerJoinEvent;
import com.combatreforged.factory.api.world.World;
import com.combatreforged.factory.api.world.block.Block;
import com.combatreforged.factory.api.world.block.BlockEntity;
import com.combatreforged.factory.api.world.entity.Entity;
import com.combatreforged.factory.api.world.entity.player.Player;
import com.combatreforged.factory.api.world.nbt.NBTList;
import com.combatreforged.factory.api.world.nbt.NBTObject;
import com.combatreforged.factory.api.world.nbt.NBTValue;
import com.combatreforged.factory.api.world.types.Minecraft;
import com.combatreforged.factory.api.world.util.Location;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class TestPlugin implements FactoryPlugin {
    Logger logger = LogManager.getLogger();
    @Override
    public void onFactoryLoad(FactoryAPI api, FactoryServer server) {
        logger.info("Hello! I was loaded within Factory!");

        PlayerJoinEvent.BACKEND.register(event -> {
            Player player = event.getPlayer();
            World world = player.getWorld();
            Block block = world.getBlockAt(player.getLocation());

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
