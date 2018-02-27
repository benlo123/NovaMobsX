package com.pikycz.novamobs;

import com.pikycz.novamobs.entities.boss.ElderGuardian;
import com.pikycz.novamobs.entities.boss.Wither;
import com.pikycz.novamobs.entities.boss.EnderDragon;
import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.command.CommandSender;
import cn.nukkit.entity.Entity;
import cn.nukkit.entity.item.EntityItem;
import cn.nukkit.event.EventPriority;
import cn.nukkit.event.Listener;
import cn.nukkit.event.level.LevelLoadEvent;
import cn.nukkit.event.level.LevelUnloadEvent;
import cn.nukkit.level.Level;
import cn.nukkit.level.Location;
import cn.nukkit.level.Position;
import cn.nukkit.level.format.FullChunk;
import cn.nukkit.nbt.tag.CompoundTag;
import cn.nukkit.nbt.tag.DoubleTag;
import cn.nukkit.nbt.tag.FloatTag;
import cn.nukkit.nbt.tag.ListTag;
import cn.nukkit.plugin.PluginBase;
import cn.nukkit.utils.TextFormat;
import com.pikycz.novamobs.entities.BaseEntity;
import com.pikycz.novamobs.entities.animal.flying.*;
import com.pikycz.novamobs.entities.animal.walking.*;
import com.pikycz.novamobs.entities.animal.wateranimal.Squid;
import com.pikycz.novamobs.entities.monster.flying.*;
import com.pikycz.novamobs.entities.monster.walking.*;
import com.pikycz.novamobs.entities.monster.watermonsters.*;
import com.pikycz.novamobs.entities.projectile.*;
import com.pikycz.novamobs.event.EventListener;
import com.pikycz.novamobs.task.AutoSpawnTask;
import com.pikycz.novamobs.task.DespawnTask;
import java.util.HashMap;
import java.util.List;

public class NovaMobsX extends PluginBase implements Listener {

    public NovaMobsX plugin;

    public final HashMap<Integer, Level> levelsToSpawn = new HashMap<>();

    public List<String> worldsSpawnDisabled;

    public String PluginPrefix = TextFormat.colorize("&c[&bNova&6MobsX&c]");
    public String StringVersion = TextFormat.colorize("&c[&aRe&cC&ar&be&5a&ct&2e&c]");
    public String IntVersion = TextFormat.colorize("&c[&a1.1&c]");

    @Override
    public void onLoad() {
        this.getServer().getLogger().info(TextFormat.colorize("&bL&fo&ea&cd&di&en&fg" + PluginPrefix + StringVersion));
        this.getServer().getLogger().info(TextFormat.colorize(IntVersion));
    }

    @Override
    public void onEnable() {
        this.saveDefaultConfig();

        this.getServer().getPluginManager().registerEvents(new EventListener(), this);

        this.getServer().getScheduler().scheduleRepeatingTask(this, new AutoSpawnTask(this), 300);

        this.getServer().getScheduler().scheduleRepeatingTask(new DespawnTask(this), 1200);

        for (Level level : Server.getInstance().getLevels().values()) {
            if (worldsSpawnDisabled.contains(level.getFolderName().toLowerCase())) {
                continue;
            }
            levelsToSpawn.put(level.getId(), level);
        }

        if (!getConfig().get("ConfigVersion").equals("1.0")) {
            getLogger().info(PluginPrefix + TextFormat.RED + "Please remove NovaMobs file > config.yml");
        }

        //register Passive entities
        this.registerEntity(Bat.class.getSimpleName(), Bat.class); //Fly too high
        this.registerEntity(Chicken.class.getSimpleName(), Chicken.class);
        this.registerEntity(Cow.class.getSimpleName(), Cow.class);
        this.registerEntity(Donkey.class.getSimpleName(), Donkey.class);
        this.registerEntity(Horse.class.getSimpleName(), Horse.class);
        this.registerEntity(Llama.class.getSimpleName(), Llama.class);
        this.registerEntity(Mooshroom.class.getSimpleName(), Mooshroom.class);
        this.registerEntity(Mule.class.getSimpleName(), Mule.class);
        this.registerEntity(Ocelot.class.getSimpleName(), Ocelot.class);
        this.registerEntity(Parrot.class.getSimpleName(), Parrot.class);
        this.registerEntity(Pig.class.getSimpleName(), Pig.class);
        this.registerEntity(PolarBear.class.getSimpleName(), PolarBear.class);
        this.registerEntity(Rabbit.class.getSimpleName(), Rabbit.class);
        this.registerEntity(Sheep.class.getSimpleName(), Sheep.class);
        this.registerEntity(Squid.class.getSimpleName(), Squid.class);
        this.registerEntity(Villager.class.getSimpleName(), Villager.class);
        this.registerEntity(Wolf.class.getSimpleName(), Wolf.class);

        //register Monster entities
        this.registerEntity(Blaze.class.getSimpleName(), Blaze.class);
        this.registerEntity(EnderDragon.class.getSimpleName(), EnderDragon.class);//EnderDragon die after spawn
        this.registerEntity(Evoker.class.getSimpleName(), Evoker.class);
        this.registerEntity(Wither.class.getSimpleName(), Wither.class);
        this.registerEntity(WitherSkeleton.class.getSimpleName(), WitherSkeleton.class);
        this.registerEntity(ElderGuardian.class.getSimpleName(), ElderGuardian.class);
        this.registerEntity(Ghast.class.getSimpleName(), Ghast.class);
        this.registerEntity(Guardian.class.getSimpleName(), Guardian.class);
        this.registerEntity(CaveSpider.class.getSimpleName(), CaveSpider.class);
        this.registerEntity(Creeper.class.getSimpleName(), Creeper.class);
        this.registerEntity(Enderman.class.getSimpleName(), Enderman.class);//TODO: Move(teleport)
        this.registerEntity(MagmaCube.class.getSimpleName(), MagmaCube.class);
        this.registerEntity(PigZombie.class.getSimpleName(), PigZombie.class);
        this.registerEntity(Silverfish.class.getSimpleName(), Silverfish.class);//TODO: Spawn random from stone
        this.registerEntity(Skeleton.class.getSimpleName(), Skeleton.class);
        this.registerEntity(Slime.class.getSimpleName(), Slime.class);//TODO: Make random spawn Slime (Big,Small)
        this.registerEntity(Spider.class.getSimpleName(), Spider.class);
        this.registerEntity(Stray.class.getSimpleName(), Stray.class);
        this.registerEntity(Vindicator.class.getSimpleName(), Vindicator.class);
        this.registerEntity(Vex.class.getSimpleName(), Vex.class);
        this.registerEntity(Witch.class.getSimpleName(), Witch.class);
        this.registerEntity(Husk.class.getSimpleName(), Husk.class);
        this.registerEntity(Shulker.class.getSimpleName(), Shulker.class);
        this.registerEntity(SkeletonHorse.class.getSimpleName(), SkeletonHorse.class);
        this.registerEntity(Zombie.class.getSimpleName(), Zombie.class);
        this.registerEntity(ZombieVillager.class.getSimpleName(), ZombieVillager.class);
        this.registerEntity(ZombieHorse.class.getSimpleName(), ZombieHorse.class);

        //Register Projectile
        this.registerEntity(BlueWitherSkull.class.getSimpleName(), BlueWitherSkull.class);
        this.registerEntity(BlazeFireBall.class.getSimpleName(), BlazeFireBall.class);
        this.registerEntity(EnderCharge.class.getSimpleName(), EnderCharge.class);
        this.registerEntity(GhastFireBall.class.getSimpleName(), GhastFireBall.class);
        this.registerEntity(LlamaSpit.class.getSimpleName(), LlamaSpit.class);
        this.registerEntity(EvocationFangs.class.getSimpleName(), EvocationFangs.class);
        this.registerEntity(ShulkerBullet.class.getSimpleName(), ShulkerBullet.class);

        getLogger().info(PluginPrefix + TextFormat.YELLOW + " Register: Entites - Done.");
    }

    private void registerEntity(String name, Class<? extends Entity> clazz) {
        Entity.registerEntity(name, clazz, true);
    }

    public void onDisable() {
        this.saveConfig();
    }

    @Override
    public boolean onCommand(CommandSender sender, cn.nukkit.command.Command cmd, String label, String[] args) {
        if (sender instanceof Player) {
            if (cmd.getName().toLowerCase().equals("mob")) {
                if (!sender.hasPermission("novamobs.mob")) {
                    sender.sendMessage(PluginPrefix + TextFormat.RED + " You don't have permission to use this command.");
                    return false;
                }

                if (args.length == 0) {
                    sender.sendMessage(TextFormat.GOLD + "-- " + PluginPrefix + " --");
                    sender.sendMessage(TextFormat.GREEN + "/mob spawn <mob>" + TextFormat.YELLOW + "- Spawn Mob");
                    sender.sendMessage(TextFormat.GREEN + "/mob removemobs" + TextFormat.YELLOW + "- Remove all Mobs");
                    sender.sendMessage(TextFormat.GREEN + "/mob removeitems" + TextFormat.YELLOW + "- Remove all items on ground");
                    sender.sendMessage(TextFormat.RED + "/mob version" + TextFormat.YELLOW + "- Show MobPlugin Version");
                } else {
                    switch (args[0]) {

                        case "spawn":
                            String mob = args[1];
                            Player playerThatSpawns = null;

                            if (args.length == 3) {
                                playerThatSpawns = this.getServer().getPlayer(args[2]);
                            } else {
                                playerThatSpawns = (Player) sender;
                            }

                            if (playerThatSpawns != null) {
                                Position pos = playerThatSpawns.getPosition();

                                Entity ent;
                                if ((ent = NovaMobsX.create(mob, pos)) != null) {
                                    ent.spawnToAll();
                                    sender.sendMessage(PluginPrefix + " Spawned " + mob + " to " + playerThatSpawns.getName());
                                } else {
                                    sender.sendMessage(PluginPrefix + " Unable to spawn " + mob);
                                }
                            } else {
                                sender.sendMessage(PluginPrefix + " Unknown player " + (args.length == 3 ? args[2] : ((Player) sender).getName()));
                            }
                            break;
                        case "removemobs":
                            int count = 0;
                            for (Level level : getServer().getLevels().values()) {
                                for (Entity entity : level.getEntities()) {
                                    if (entity instanceof BaseEntity) {
                                        entity.close();
                                        count++;
                                    }
                                }
                            }
                            sender.sendMessage(PluginPrefix + " Removed " + count + " entities from all levels.");
                            break;
                        case "removeitems":
                            count = 0;
                            for (Level level : getServer().getLevels().values()) {
                                for (Entity entity : level.getEntities()) {
                                    if (entity instanceof EntityItem && entity.isOnGround()) {
                                        entity.close();
                                        count++;
                                    }
                                }
                            }
                            sender.sendMessage(PluginPrefix + " Removed " + count + " items on ground from all levels.");
                            break;
                        case "version":
                            sender.sendMessage(PluginPrefix + TextFormat.GREEN + " Version 1.1 working with MCPE 1.2.10");
                            break;
                        default:
                            sender.sendMessage(PluginPrefix + " Unknow command.");
                            break;
                    }
                }
            }
            return true;

        } else {
            sender.sendMessage(PluginPrefix + TextFormat.RED + " Only player can use this command!");
            return true;
        }
    }

    public static Entity create(String type, Position source, Object... args) {
        FullChunk chunk = source.getLevel().getChunk((int) source.x >> 4, (int) source.z >> 4, true);

        CompoundTag nbt = new CompoundTag().putList(new ListTag<DoubleTag>("Pos").add(new DoubleTag("", source.x)).add(new DoubleTag("", source.y)).add(new DoubleTag("", source.z)))
                .putList(new ListTag<DoubleTag>("Motion").add(new DoubleTag("", 0)).add(new DoubleTag("", 0)).add(new DoubleTag("", 0)))
                .putList(new ListTag<FloatTag>("Rotation").add(new FloatTag("", source instanceof Location ? (float) ((Location) source).yaw : 0))
                        .add(new FloatTag("", source instanceof Location ? (float) ((Location) source).pitch : 0)));

        return Entity.createEntity(type, chunk, nbt, args);
    }

    @cn.nukkit.event.EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
    public void onLevelLoad(LevelLoadEvent e) {
        Level level = e.getLevel();

        if (!plugin.getConfig().getStringList("WorldsSpawnDisabled").contains(level.getFolderName())) {
            levelsToSpawn.put(level.getId(), level);
        }
    }

    @cn.nukkit.event.EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
    public void onLevelUnload(LevelUnloadEvent e) {
        Level level = e.getLevel();

        levelsToSpawn.remove(level.getId());
    }

}
