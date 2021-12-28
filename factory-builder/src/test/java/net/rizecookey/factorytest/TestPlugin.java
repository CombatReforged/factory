package net.rizecookey.factorytest;

import com.combatreforged.factory.api.FactoryAPI;
import com.combatreforged.factory.api.FactoryServer;
import com.combatreforged.factory.api.command.CommandSourceInfo;
import com.combatreforged.factory.api.command.CommandUtils;
import com.combatreforged.factory.api.entrypoint.FactoryPlugin;
import com.combatreforged.factory.api.event.entity.LivingEntityDamageEvent;
import com.combatreforged.factory.api.event.entity.LivingEntityDeathEvent;
import com.combatreforged.factory.api.event.entity.LivingEntityHealEvent;
import com.combatreforged.factory.api.event.player.*;
import com.combatreforged.factory.api.event.server.ServerTickEvent;
import com.combatreforged.factory.api.scheduler.ScheduledRepeatingTask;
import com.combatreforged.factory.api.scheduler.TaskPointer;
import com.combatreforged.factory.api.scheduler.TaskScheduler;
import com.combatreforged.factory.api.util.Identifier;
import com.combatreforged.factory.api.world.Weather;
import com.combatreforged.factory.api.world.World;
import com.combatreforged.factory.api.world.block.Block;
import com.combatreforged.factory.api.world.block.BlockEntity;
import com.combatreforged.factory.api.world.block.BlockState;
import com.combatreforged.factory.api.world.damage.DamageData;
import com.combatreforged.factory.api.world.effect.StatusEffectInstance;
import com.combatreforged.factory.api.world.entity.Entity;
import com.combatreforged.factory.api.world.entity.equipment.ArmorSlot;
import com.combatreforged.factory.api.world.entity.equipment.HandSlot;
import com.combatreforged.factory.api.world.entity.player.Player;
import com.combatreforged.factory.api.world.item.ItemStack;
import com.combatreforged.factory.api.world.item.container.PlayerInventory;
import com.combatreforged.factory.api.world.item.container.menu.SlotClickType;
import com.combatreforged.factory.api.world.nbt.NBTList;
import com.combatreforged.factory.api.world.nbt.NBTObject;
import com.combatreforged.factory.api.world.nbt.NBTValue;
import com.combatreforged.factory.api.world.scoreboard.Scoreboard;
import com.combatreforged.factory.api.world.scoreboard.ScoreboardObjective;
import com.combatreforged.factory.api.world.scoreboard.ScoreboardTeam;
import com.combatreforged.factory.api.world.types.Minecraft;
import com.combatreforged.factory.api.world.util.Location;
import com.combatreforged.factory.api.world.util.Vector3D;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.title.Title;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.nio.file.Paths;
import java.util.function.Supplier;

public class TestPlugin implements FactoryPlugin {
    Logger logger = LogManager.getLogger();

    TaskPointer<ScheduledRepeatingTask> villagerSoundTask;
    long tickStartTime;

    long averageTickTime = -1;

    @Override
    @SuppressWarnings("FutureReturnValueIgnored")
    public void onFactoryLoad(FactoryAPI api, FactoryServer server) {
        logger.info("Hello! I was loaded within Factory!");

        TaskScheduler scheduler = api.getScheduler();
        villagerSoundTask = scheduler.scheduleRepeating(() -> server.getPlayers().forEach(player ->
                player.runCommand("playsound minecraft:entity.villager.ambient master @s", 4, false)), 0, 40);

        ServerTickEvent.BACKEND.register(event -> {
            this.tickStartTime = System.currentTimeMillis();

            server.getPlayers().forEach(player -> player.setAbleToFly(true));

            event.runAfterwards(() -> event.getServer().getPlayers()
                    .forEach(player -> {
                        long currentTickTime = System.currentTimeMillis() - tickStartTime;
                        this.averageTickTime = this.averageTickTime == -1 ? currentTickTime : (this.averageTickTime + currentTickTime) / 2;
                        player.sendActionBarMessage(Component.text("Average tick time: " + this.averageTickTime + "ms").color(NamedTextColor.GRAY));
                    }));
        });

        LivingEntityDamageEvent.BACKEND.register(event -> {
            DamageData cause = event.getCause();
            if (cause.getType() != DamageData.Type.VOID) {
                if (cause.getType() == DamageData.Type.ENTITY_ATTACK) {
                    event.setCause(DamageData.create(DamageData.Type.GENERIC));
                } else {
                    event.setCancelled(true);
                }
            }
        }, event -> event.getEntity() instanceof Player);

        Supplier<ItemStack> netheriteSword = () -> ItemStack.create(Minecraft.Item.byIdentifier(new Identifier("minecraft", "netherite_sword")));
        LivingEntityDeathEvent.BACKEND.register(event -> event.setDropLoot(false));
        PlayerDeathEvent.BACKEND.register(event -> {
            final Player player = event.getPlayer();
            event.setDropExperience(false);
            event.setDropEquipment(false);
            event.setDeathMessage(player.getName().append(Component.text(" took the L.").color(NamedTextColor.GRAY)));
            event.setVisibleFor(ScoreboardTeam.VisibleFor.NO_ONE);
            scheduler.schedule(() -> {
                if (player.isDead()) {
                    player.respawn();
                    System.out.println("Respawning...");
                }
            }, 15);
        });

        PlayerJoinEvent.BACKEND.register(event -> {
            api.getScheduler().schedule(() -> villagerSoundTask.cancel(), 320);

            Player player = event.getPlayer();
            World world = player.getWorld();
            Block block = world.getBlockAt(player.getLocation());

            player.setGameMode(Minecraft.GameMode.ADVENTURE);

            PlayerInventory inventory = player.getInventory();
            inventory.clear();
            inventory.addItemStack(netheriteSword.get());
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

        PlayerRespawnEvent.BACKEND.register(event -> {
            event.setSpawnpointForced(true);
            event.setSpawnpoint(new Location(-13, 76, 12, server.getWorld(new Identifier("minecraft", "the_nether"))));
            event.runAfterwards(() -> {
                event.getPlayer().addEffectInstance(StatusEffectInstance.create(Minecraft.Effect.BLINDNESS, 1000000 * 20));
            });
        });


        PlayerBreakBlockEvent.BACKEND.register(event -> {
            if (event.getBlock().getType() == Minecraft.Block.GRASS || event.getBlock().getType() == Minecraft.Block.TALL_GRASS || event.getBlock().getType() == Minecraft.Block.GRASS_BLOCK || event.getBlock().getType() == Minecraft.Block.BLACK_WALL_BANNER) {
                event.setCancelled(true);
            } else {
                event.setDropBlock(false);
            }
        });

        PlayerChangeBlockStateEvent.BACKEND.register(event -> {
            if (Math.random() <= 0.1) {
                Location loc = event.getLocation();
                event.runAfterwards(() -> loc.getWorld().setBlockAt(loc, BlockState.create(Minecraft.Block.CHEST, loc)));
            } else {
                event.setCancelled(true);
            }
        });

        PlayerPlaceBlockEvent.BACKEND.register(event -> {
            if (event.getPlayer().isSneaking()) {
                event.setCancelled(true);
            } else {
                if (Math.random() <= 0.1) {
                    Location loc = event.getLocation();
                    event.setNewBlockState(BlockState.create(Minecraft.Block.CHEST, loc));
                }
            }
        }, event -> false);

        server.getCommandDispatcher().register(CommandUtils.literal("test")
                .then(RequiredArgumentBuilder.<CommandSourceInfo, String>argument("test_suggestion", StringArgumentType.word())
                        .suggests((ctx, builder) -> builder.suggest("foobar").buildFuture())
                        .executes(ctx -> {
                            ctx.getSource().sendMessage(Component.text("Success! lol"));
                            FactoryAPI.getInstance().getServer().loadWorldAsync(Paths.get("test"), "test");
                            return 0;
                        })));

        PlayerChangeMovementStateEvent.BACKEND.register(event -> System.out.println("Changed state: " + event.getChangedState().toString()));

        PlayerContainerClickEvent.BACKEND.register(event -> {
            if (event.getMenu().getType() == Minecraft.MenuType.INVENTORY && event.getTargetSlot() >= 0 && event.getTargetSlot() < 5) {
                event.setCancelled(true);
            } else if (event.getTargetSlot() == -999 && event.getClickType() == SlotClickType.CLICK) {
                event.setCancelled(true);
            }
        });

        PlayerHotbarDropItemEvent.BACKEND.register(event -> event.setCancelled(true));

        PlayerUseItemEvent.BACKEND.register(event -> event.setCancelled(true));

        PlayerInteractEntityEvent.BACKEND.register(event -> {
            event.setCancelled(true);
            event.getPlayer().sendTitle(Title.title(Component.text("nope"), Component.text("")));
        });

        PlayerInteractBlockEvent.BACKEND.register(event -> {
            if (event.getPlayer().isSprinting()) {
                event.setCancelled(true);
            }
        });

        PlayerChangeMovementStateEvent.BACKEND.register(event -> {
            Player player = event.getPlayer();
            if (event.getChangedState() == PlayerChangeMovementStateEvent.ChangedState.FLYING && event.getChangedValue() && player.getGameMode() == Minecraft.GameMode.ADVENTURE) {
                player.addVelocity(new Vector3D(0.0, 1.5, 0.0));
                event.setChangedValue(false);
            }
        });

        PlayerFoodLevelsChangeEvent.BACKEND.register(event -> {
            Player player = event.getPlayer();
            if (event.getSaturation() < player.getSaturation() || event.getFoodLevel() < player.getFoodLevel()) {
                event.setCancelled(true);
            }
        });

        PlayerSwitchActiveSlotEvent.BACKEND.register(event -> {
            if (event.getPlayer().isSneaking()) {
                event.setCancelled(true);
            } else if (event.getPlayer().isSprinting()) {
                event.setNewSlot(4);
            }
        });

        PlayerOpenContainerEvent.BACKEND.register(event -> {
            if (event.getPlayer().isFlying()) {
                event.setCancelled(true);
            }
        });

        PlayerCloseContainerEvent.BACKEND.register(event -> {
            if (event.getPlayer().getGameMode().equals(Minecraft.GameMode.SURVIVAL)) {
                event.setCancelled(true);
            }
        });

        LivingEntityHealEvent.BACKEND.register(event -> {
            if (event.getLivingEntity() instanceof Player) {
                if (event.getCause().equals(LivingEntityHealEvent.HealCause.NATURAL_REGENERATION)) {
                    event.setCancelled(true);
                }
                else {
                    event.setAmount(20.0F);
                }
            }
        });
    }
}
