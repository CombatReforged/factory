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
                System.out.println(world.getBlockEntity(player.getLocation()).getBlockData().toString());
            }

            Entity sheep = Entity.create(Minecraft.Entity.SHEEP, world);
            sheep.teleport(player.getLocation());
            world.spawn(sheep);
        });
    }
}
