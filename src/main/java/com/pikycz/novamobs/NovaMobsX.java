package com.pikycz.novamobs;

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
import com.pikycz.novamobs.entities.monster.flying.*;
import com.pikycz.novamobs.entities.monster.walking.*;
import com.pikycz.novamobs.entities.projectile.*;
import com.pikycz.novamobs.event.EventListener;
import com.pikycz.novamobs.task.AutoSpawnTask;
import java.io.File;
import java.util.HashMap;

public class NovaMobsX extends PluginBase implements Listener {

    public NovaMobsX plugin;

    public final HashMap<Integer, Level> levelsToSpawn = new HashMap<>();

    //public List<String> disabledWorlds;
    public String PluginPrefix = TextFormat.colorize("&c[&bNova&6Mobs&c]");
    public String StringVersion = TextFormat.colorize("&c[&aRe&cC&ar&be&5a&ct&2e&c]");
    public String IntVersion = TextFormat.colorize("&c[&a1.0&c]");

    @Override
    public void onLoad() {

        this.getServer().getLogger().info(TextFormat.colorize("&bL&fo&ea&cd&di&en&fg" + PluginPrefix + StringVersion));
        this.getServer().getLogger().info(TextFormat.colorize(IntVersion));
    }

    @Override
    public void onEnable() {
        this.getServer().getPluginManager().registerEvents(new EventListener(), this);
        registerEntities();

        File config = new File(this.getDataFolder() + "\\data");
        config.mkdirs();
        saveResource("config.yml");
        if (!getConfig().get("ConfigVersion").equals("1.0")) {
            getLogger().info(PluginPrefix + TextFormat.RED + "Please remove NovaMobs file > config.yml");
        }

        if (getConfig().getInt("autoSpawnTime") > 0) {
            this.getServer().getScheduler().scheduleRepeatingTask(this, new AutoSpawnTask(this), getConfig().getInt("autoSpawnTime"), true);
        }

        for (Level level : Server.getInstance().getLevels().values()) {
            if (getConfig().getStringList("worldsSpawnDisabled").contains(level.getFolderName())) {
                continue;
            }
            levelsToSpawn.put(level.getId(), level);
        }
    }

    public void onDisable() {
        getConfig().save();
    }

    private void registerEntities() {
        //register Passive entities
        Entity.registerEntity(Bat.class.getSimpleName(), Bat.class); //Fly too high
        Entity.registerEntity(Chicken.class.getSimpleName(), Chicken.class);
        Entity.registerEntity(Cow.class.getSimpleName(), Cow.class);
        Entity.registerEntity(Donkey.class.getSimpleName(), Donkey.class);
        Entity.registerEntity(Horse.class.getSimpleName(), Horse.class);
        Entity.registerEntity(Mooshroom.class.getSimpleName(), Mooshroom.class);
        Entity.registerEntity(Mule.class.getSimpleName(), Mule.class);
        Entity.registerEntity(Ocelot.class.getSimpleName(), Ocelot.class);
        Entity.registerEntity(Pig.class.getSimpleName(), Pig.class);
        Entity.registerEntity(PolarBear.class.getSimpleName(), PolarBear.class);
        Entity.registerEntity(Rabbit.class.getSimpleName(), Rabbit.class);
        Entity.registerEntity(Sheep.class.getSimpleName(), Sheep.class);
        Entity.registerEntity(Villager.class.getSimpleName(), Villager.class);
        Entity.registerEntity(Wolf.class.getSimpleName(), Wolf.class);

        //register Monster entities
        Entity.registerEntity(Blaze.class.getSimpleName(), Blaze.class);
        Entity.registerEntity(EnderDragon.class.getSimpleName(), EnderDragon.class);//TODO: Spawn in End
        Entity.registerEntity(Wither.class.getSimpleName(), Wither.class);
        Entity.registerEntity(WitherSkeleton.class.getSimpleName(), WitherSkeleton.class);
        //Entity.registerEntity(ElderGuardian.class.getSimpleName(), ElderGuardian.class);//TODO: Spawn in Ocean palace swim , attack
        Entity.registerEntity(Ghast.class.getSimpleName(), Ghast.class);//TODO: Spawn in Nether
        //Entity.registerEntity(Guardian.class.getSimpleName(), Guardian.class);//TODO: Spawn in Ocean palace swim , attack
        Entity.registerEntity(CaveSpider.class.getSimpleName(), CaveSpider.class);
        Entity.registerEntity(Creeper.class.getSimpleName(), Creeper.class);
        Entity.registerEntity(Enderman.class.getSimpleName(), Enderman.class);//TODO: Move(teleport) , attack
        //Entity.registerEntity(MagmaCube.class.getSimpleName(), MagmaCube.class);//Spawn In Nether
        Entity.registerEntity(PigZombie.class.getSimpleName(), PigZombie.class);//Spawn in Nether
        Entity.registerEntity(Silverfish.class.getSimpleName(), Silverfish.class);//TODO: Spawn random from stone
        Entity.registerEntity(Skeleton.class.getSimpleName(), Skeleton.class);
        //Entity.registerEntity(Slime.class.getSimpleName(), Slime.class);//TODO: Make random spawn Slime (Big,Small)
        Entity.registerEntity(Spider.class.getSimpleName(), Spider.class);
        Entity.registerEntity(Stray.class.getSimpleName(), Stray.class);
        Entity.registerEntity(Witch.class.getSimpleName(), Witch.class);
        Entity.registerEntity(Husk.class.getSimpleName(), Husk.class);
        Entity.registerEntity(SkeletonHorse.class.getSimpleName(), SkeletonHorse.class);
        Entity.registerEntity(Zombie.class.getSimpleName(), Zombie.class);
        Entity.registerEntity(ZombieVillager.class.getSimpleName(), ZombieVillager.class);
        Entity.registerEntity(ZombieHorse.class.getSimpleName(), ZombieHorse.class);

        // register the fireball entity
        Entity.registerEntity(BlueWitherSkull.class.getSimpleName(), BlueWitherSkull.class);
        Entity.registerEntity(BlazeFireBall.class.getSimpleName(), BlazeFireBall.class);
        Entity.registerEntity(EnderCharge.class.getSimpleName(), EnderCharge.class);
        Entity.registerEntity(GhastFireBall.class.getSimpleName(), GhastFireBall.class);

        this.getServer().getLogger().info(PluginPrefix + " Register: Entites - Done.");
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
                    sender.sendMessage(TextFormat.GOLD + "-- NovaMobsX --");
                    sender.sendMessage(TextFormat.GREEN + "/mob summon <mob>" + TextFormat.YELLOW + "- Spawn Mob");
                    sender.sendMessage(TextFormat.GREEN + "/mob removemobs" + TextFormat.YELLOW + "- Remove all Mobs");
                    sender.sendMessage(TextFormat.GREEN + "/mob removeitems" + TextFormat.YELLOW + "- Remove all items on ground");
                    sender.sendMessage(TextFormat.RED + "/mob version" + TextFormat.YELLOW + "- Show MobPlugin Version");
                } else {
                    switch (args[0]) {

                        case "summon":
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
                            sender.sendMessage(PluginPrefix + TextFormat.GREEN + " Version 1.0 working with MCPE 1.2.9");//Todo Automatic Updater?
                            break;
                        default:
                            sender.sendMessage(PluginPrefix + " Unknow command.");
                            break;
                    }
                }
            }
            return true;

        } else {
            sender.sendMessage(PluginPrefix + TextFormat.RED + "Only player can use this command!");
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

        if (!getConfig().getStringList("worldsSpawnDisabled").contains(level.getFolderName())) {
            levelsToSpawn.put(level.getId(), level);
        }
    }

    @cn.nukkit.event.EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
    public void onLevelUnload(LevelUnloadEvent e) {
        Level level = e.getLevel();

        levelsToSpawn.remove(level.getId());
    }
}
