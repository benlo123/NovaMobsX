package com.pikycz.novamobs;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.command.CommandSender;
import cn.nukkit.entity.Entity;
import cn.nukkit.entity.item.EntityItem;
import cn.nukkit.event.Listener;
import cn.nukkit.level.Level;
import cn.nukkit.level.Location;
import cn.nukkit.level.Position;
import cn.nukkit.level.format.FullChunk;
import cn.nukkit.nbt.tag.CompoundTag;
import cn.nukkit.nbt.tag.DoubleTag;
import cn.nukkit.nbt.tag.FloatTag;
import cn.nukkit.nbt.tag.ListTag;
import cn.nukkit.plugin.PluginBase;
import cn.nukkit.utils.Config;
import cn.nukkit.utils.TextFormat;
import com.pikycz.novamobs.configsection.MainConfig;
import com.pikycz.novamobs.entities.BaseEntity;
import com.pikycz.novamobs.entities.animal.flying.*;
import com.pikycz.novamobs.entities.animal.walking.*;
import com.pikycz.novamobs.entities.monster.flying.*;
import com.pikycz.novamobs.entities.monster.walking.*;
import com.pikycz.novamobs.entities.projectile.*;
import com.pikycz.novamobs.task.AutoSpawnTask;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Timer;

public class NovaMobs extends PluginBase implements Listener {

    private static NovaMobs instance;

    public NovaMobs plugin;

    public final HashMap<Integer, Level> levelsToSpawn = new HashMap<>();

    public List<String> disabledWorlds;

    public String PluginPrefix = "&c[&bNova&6Mobs&c]";
    public String StringVersion = "&c[&aRe&cC&ar&be&5a&ct&2e&c]";
    public String IntVersion = "&c[&a1.2&c]";

    public static NovaMobs getInstance() {
        return instance;
    }

    @Override
    public void onLoad() {
        if (instance == null) {
            instance = this;
        }

        this.getServer().getLogger().info(TextFormat.colorize("&bL&fo&ea&cd&di&en&fg" + PluginPrefix + StringVersion));
        this.getServer().getLogger().info(TextFormat.colorize(IntVersion));
    }

    @Override
    @SuppressWarnings("deprecation")
    public void onEnable() {
        this.getServer().getPluginManager().registerEvents(this, this);

        //register Passive entities
        this.registerEntity("Bat", Bat.class); //Fly too high
        this.registerEntity("Chicken", Chicken.class);
        this.registerEntity("Cow", Cow.class);
        this.registerEntity("Donkey", Donkey.class);
        this.registerEntity("Horse", Horse.class);
        this.registerEntity("Mooshroom", Mooshroom.class);
        this.registerEntity("Mule", Mule.class);
        this.registerEntity("Ocelot", Ocelot.class);
        this.registerEntity("Pig", Pig.class);
        this.registerEntity("PolarBear", PolarBear.class);
        this.registerEntity("Rabbit", Rabbit.class);
        this.registerEntity("Sheep", Sheep.class);
        this.registerEntity("SkeletonHorse", SkeletonHorse.class);
        this.registerEntity("Villager", Villager.class);
        this.registerEntity("Wolf", Wolf.class);
        this.registerEntity("ZombieHorse", ZombieHorse.class);

        //register Monster entities
        this.registerEntity("Blaze", Blaze.class);
        this.registerEntity("EnderDragon", EnderDragon.class); //TODO: Spawn in End
        this.registerEntity("Wither", Wither.class);
        //this.registerEntity("ElderGuardian", ElderGuardian.class); //TODO: Spawn in Ocean palace swim , attack
        this.registerEntity("Ghast", Ghast.class); //TODO: Spawn in Nether
        //this.registerEntity("Guardian", Guardian.class); //TODO: Spawn in Ocean palace swim , attack
        this.registerEntity("CaveSpider", CaveSpider.class);
        this.registerEntity("Creeper", Creeper.class);
        this.registerEntity("Enderman", Enderman.class); //TODO: Move(teleport) , attack
        //this.registerEntity("MagmaCube", MagmaCube.class);//Spawn In Nether
        this.registerEntity("PigZombie", PigZombie.class);//Spawn in Nether
        this.registerEntity("SilverFish", Silverfish.class); //TODO: Spawn random from stone
        this.registerEntity("Skeleton", Skeleton.class);
        //this.registerEntity("Slime", Slime.class); //TODO: Make random spawn Slime (Big,Small)
        this.registerEntity("Spider", Spider.class);
        //this.registerEntity("Stray", Stray.class);
        this.registerEntity("Witch", Witch.class);
        this.registerEntity("Husk", Husk.class);
        this.registerEntity("Zombie", Zombie.class);
        this.registerEntity("ZombieVillager", ZombieVillager.class);

        // register the fireball entity
        Entity.registerEntity("BlueWitherSkull", BlueWitherSkull.class);
        Entity.registerEntity("BlazeFireBall", BlazeFireBall.class);
        Entity.registerEntity("DragonFireBall", DragonFireBall.class);
        Entity.registerEntity("GhastDireBall", GhastFireBall.class);

        this.getServer().getLogger().info("Register: Entites - Done.");

        //Config reading and writing
        Config config = new Config(
                new File(this.getDataFolder(), "config.yml"),
                Config.YAML,
                new LinkedHashMap<String, Object>() {
            {
                put("worlds-spawn-disabled", new ArrayList<String>());
                put("spawn-animals", true);
                put("spawn-mobs", true);
                put("auto-spawn-tick", 20);
            }
        });
        config.save();

        if (MainConfig.SpawnDelay > 0) {

            Timer timer = new Timer();

            timer.schedule(new AutoSpawnTask(this), 10000, 1000);
        }

        this.disabledWorlds = config.getStringList("worlds-spawn-disabled");

        for (Level level : Server.getInstance().getLevels().values()) {
            if (disabledWorlds.contains(level.getFolderName().toLowerCase())) {
                continue;
            }

            levelsToSpawn.put(level.getId(), level);
        }

    }

    private void registerEntity(String name, Class<? extends BaseEntity> clazz) {
        Entity.registerEntity(name, clazz, true);
    }

    /**
     * @param commandSender
     * @param cmd
     * @param label
     * @param args
     * @return
     */
    @Override
    public boolean onCommand(CommandSender sender, cn.nukkit.command.Command cmd, String label, String[] args) {
        if (cmd.getName().toLowerCase().equals("mob")) {

            if (args.length == 0) {
                sender.sendMessage(TextFormat.GOLD + "--NovaMobs 1.1--");
                sender.sendMessage(TextFormat.GREEN + "/mob summon <mob>" + TextFormat.YELLOW + "- Spawn Mob");
                sender.sendMessage(TextFormat.GREEN + "/mob removemobs" + TextFormat.YELLOW + "- Remove all Mobs");
                sender.sendMessage(TextFormat.GREEN + "/mob removeitems" + TextFormat.YELLOW + "- Remove all items on ground");
                sender.sendMessage(TextFormat.RED + "/mob version" + TextFormat.YELLOW + "- Show MobPlugin Version");
            } else {
                switch (args[0]) {

                    case "summon":
                        if (!(sender instanceof Player)) {
                            sender.sendMessage("You Must use only in game");
                        }
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
                            if ((ent = NovaMobs.create(mob, pos)) != null) {
                                ent.spawnToAll();
                                sender.sendMessage("Spawned " + mob + " to " + playerThatSpawns.getName());
                            } else {
                                sender.sendMessage("Unable to spawn " + mob);
                            }
                        } else {
                            sender.sendMessage("Unknown player " + (args.length == 3 ? args[2] : ((Player) sender).getName()));
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
                        sender.sendMessage("Removed " + count + " entities from all levels.");
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
                        sender.sendMessage("Removed " + count + " items on ground from all levels.");
                        break;
                    case "version":
                        sender.sendMessage(TextFormat.GREEN + "Version > 1.2 working with MCPE 1.2.2");//Todo Automatic Updater?
                        break;
                    default:
                        sender.sendMessage("Unkown command.");
                        break;
                }
            }
        }
        return true;

    }

    /**
     *
     * @param type
     * @param source
     * @param args
     * @return
     */
    public static Entity create(String type, Position source, Object... args) {
        FullChunk chunk = source.getLevel().getChunk((int) source.x >> 4, (int) source.z >> 4, true);

        CompoundTag nbt = new CompoundTag().putList(new ListTag<DoubleTag>("Pos").add(new DoubleTag("", source.x)).add(new DoubleTag("", source.y)).add(new DoubleTag("", source.z)))
                .putList(new ListTag<DoubleTag>("Motion").add(new DoubleTag("", 0)).add(new DoubleTag("", 0)).add(new DoubleTag("", 0)))
                .putList(new ListTag<FloatTag>("Rotation").add(new FloatTag("", source instanceof Location ? (float) ((Location) source).yaw : 0))
                        .add(new FloatTag("", source instanceof Location ? (float) ((Location) source).pitch : 0)));

        return Entity.createEntity(type, chunk, nbt, args);
    }
}
