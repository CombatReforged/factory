package net.rizecookey.factorytest;

import com.combatreforged.factory.api.FactoryAPI;
import com.combatreforged.factory.api.FactoryServer;
import com.combatreforged.factory.api.entrypoint.FactoryPlugin;
import com.combatreforged.factory.api.event.player.PlayerJoinEvent;
import com.combatreforged.factory.api.world.World;
import com.combatreforged.factory.api.world.block.Block;
import com.combatreforged.factory.api.world.entity.Entity;
import com.combatreforged.factory.api.world.entity.player.Player;
import com.combatreforged.factory.api.world.types.Minecraft;
import com.combatreforged.factory.api.world.util.Location;
import com.combatreforged.factory.api.world.util.Vector3D;
import net.kyori.adventure.nbt.api.BinaryTagHolder;
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
                String blockData = "{x:" + Math.floor(loc.getX()) + ",y:" + Math.floor(loc.getY()) + ",z:" + Math.floor(loc.getZ()) + ",Items:[{id:\"minecraft:stone\",Slot:8b,Count:1b}],id:\"minecraft:chest\"}";
                System.out.println(blockData);
                world.getBlockEntity(loc).setBlockData(BinaryTagHolder.of(blockData));
            }

            Entity sheep = Entity.create(Minecraft.Entity.SHEEP, world);
            sheep.teleport(player.getLocation());
            sheep.setVelocity(new Vector3D(1.0, 10.0, 1.0));
            world.spawn(sheep);
        });
    }
}
