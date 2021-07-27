package net.rizecookey.factorytest;

import com.combatreforged.factory.api.FactoryAPI;
import com.combatreforged.factory.api.FactoryServer;
import com.combatreforged.factory.api.entrypoint.FactoryPlugin;
import com.combatreforged.factory.api.event.entity.LivingEntityDamageEvent;
import com.combatreforged.factory.api.event.player.PlayerJoinEvent;
import com.combatreforged.factory.api.event.server.tick.ServerEndTickEvent;
import com.combatreforged.factory.api.event.server.tick.ServerStartTickEvent;
import com.combatreforged.factory.api.scheduler.ScheduledRepeatingTask;
import com.combatreforged.factory.api.scheduler.TaskPointer;
import com.combatreforged.factory.api.scheduler.TaskScheduler;
import com.combatreforged.factory.api.world.Weather;
import com.combatreforged.factory.api.world.World;
import com.combatreforged.factory.api.world.block.Block;
import com.combatreforged.factory.api.world.block.BlockEntity;
import com.combatreforged.factory.api.world.damage.DamageData;
import com.combatreforged.factory.api.world.effect.StatusEffectInstance;
import com.combatreforged.factory.api.world.entity.Entity;
import com.combatreforged.factory.api.world.entity.equipment.ArmorSlot;
import com.combatreforged.factory.api.world.entity.equipment.HandSlot;
import com.combatreforged.factory.api.world.entity.player.Player;
import com.combatreforged.factory.api.world.item.ItemStack;
import com.combatreforged.factory.api.world.item.container.PlayerInventory;
import com.combatreforged.factory.api.world.nbt.NBTList;
import com.combatreforged.factory.api.world.nbt.NBTObject;
import com.combatreforged.factory.api.world.nbt.NBTValue;
import com.combatreforged.factory.api.world.scoreboard.Scoreboard;
import com.combatreforged.factory.api.world.scoreboard.ScoreboardObjective;
import com.combatreforged.factory.api.world.scoreboard.ScoreboardTeam;
import com.combatreforged.factory.api.world.types.Minecraft;
import com.combatreforged.factory.api.world.util.Location;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.title.Title;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class TestPlugin implements FactoryPlugin {
    Logger logger = LogManager.getLogger();

    TaskPointer<ScheduledRepeatingTask> villagerSoundTask;
    long tickStartTime;

    @Override
    public void onFactoryLoad(FactoryAPI api, FactoryServer server) {
        logger.info("Hello! I was loaded within Factory!");

        TaskScheduler scheduler = api.getScheduler();
        villagerSoundTask = scheduler.scheduleRepeating(() -> server.getPlayers().forEach(player ->
                    player.runCommand("playsound minecraft:entity.villager.ambient master @s", 4, false)), 0, 40);

        ServerStartTickEvent.BACKEND.register(event -> this.tickStartTime = System.currentTimeMillis());

        ServerEndTickEvent.BACKEND.register(event -> event.getServer().getPlayers()
                .forEach(player -> player.sendActionBarMessage(Component.text("Tick time: " + (System.currentTimeMillis() - tickStartTime) + "ms").color(NamedTextColor.GRAY))));

        LivingEntityDamageEvent.BACKEND.register(event -> {
            if (event.getEntity() instanceof Player) {
                if (event.getCause().getType() != DamageData.Type.VOID) {
                    if (event.getCause().getType() == DamageData.Type.ENTITY_ATTACK) {
                        event.setCause(DamageData.create(DamageData.Type.GENERIC));
                    } else {
                        event.setCancelled(true);
                    }
                }
            }
        });

        PlayerJoinEvent.BACKEND.register(event -> {
            api.getScheduler().schedule(() -> villagerSoundTask.cancel(), 320);

            Player player = event.getPlayer();
            World world = player.getWorld();
            Block block = world.getBlockAt(player.getLocation());

            player.setGameMode(Minecraft.GameMode.ADVENTURE);

            PlayerInventory inventory = player.getInventory();
            inventory.clear();
            inventory.addItemStack(ItemStack.create(Minecraft.Item.NETHERITE_SWORD));
            inventory.setItemStack(18, ItemStack.create(Minecraft.Item.LAPIS_LAZULI, 16));
            player.setEquipmentStack(ArmorSlot.HEAD, ItemStack.create(Minecraft.Item.NETHERITE_HELMET));
            player.setEquipmentStack(ArmorSlot.CHEST, ItemStack.create(Minecraft.Item.DIAMOND_CHESTPLATE));
            player.setEquipmentStack(ArmorSlot.LEGS, ItemStack.create(Minecraft.Item.GOLDEN_LEGGINGS));
            player.setEquipmentStack(ArmorSlot.FEET, ItemStack.create(Minecraft.Item.IRON_BOOTS));
            player.setEquipmentStack(HandSlot.OFF_HAND, ItemStack.create(Minecraft.Item.GOLDEN_APPLE, 16));

            player.addEffectInstance(StatusEffectInstance
                    .create(Minecraft.Effect.HEALTH_BOOST, 1000000, 4, false));
            player.addEffectInstance(StatusEffectInstance
                    .create(Minecraft.Effect.INSTANT_HEALTH, 1, 9, false));
            player.setFoodLevel(20);
            player.setSaturation(5);

            ItemStack stick = ItemStack.create(Minecraft.Item.STICK);
            stick.enchant(Minecraft.Enchantment.KNOCKBACK, 2);
            stick.setLore(Component.text()
                    .content("GommeHD hmm?")
                    .decoration(TextDecoration.ITALIC, false)
                    .color(NamedTextColor.RED)
                    .build());

            inventory.setItemStack(4, stick);

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

            player.sendTitle(Title.title(Component.text("Hewwo owo"), Component.text("cool innit").color(NamedTextColor.GOLD)));

            player.openMenu(Minecraft.MenuType.FURNACE.createMenu(Component.text("Test lol")));
            inventory.setItemStack(9, ItemStack.create(Minecraft.Item.COAL, 16));
            inventory.setItemStack(10, ItemStack.create(Minecraft.Item.BEEF, 16));
            NBTObject nbt = NBTObject.create();
            nbt.set("Potion", NBTValue.of("minecraft:water"));
            inventory.setItemStack(18, ItemStack.create(Minecraft.Item.POTION, 1, 0, nbt));
            inventory.setItemStack(19, ItemStack.create(Minecraft.Item.BLAZE_POWDER, 16));
            inventory.setItemStack(20, ItemStack.create(Minecraft.Item.GUNPOWDER, 16));
            player.setExperienceLevel(16);
            player.setExperiencePoints(0);

            Scoreboard scoreboard = Scoreboard.create();

            ScoreboardObjective objective = scoreboard.addObjective("test");
            objective.getOrCreateScore(player.getRawName()).set(-1);
            scoreboard.setDisplayedObjective(objective, "sidebar");

            ScoreboardTeam team = scoreboard.addTeam("tester");
            team.setPrefix(Component.text("[").color(NamedTextColor.DARK_GRAY)
                    .append(Component.text("Tester").color(NamedTextColor.AQUA))
                    .append(Component.text("] ").color(NamedTextColor.DARK_GRAY)));

            team.setNameTagColor(NamedTextColor.GRAY);

            team.add(player.getRawName());

            player.setScoreboard(scoreboard);
        });
    }
}
