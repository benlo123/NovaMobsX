package com.pikycz.novamobsx;

import cn.nukkit.Player;
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
import cn.nukkit.utils.TextFormat;
import com.pikycz.novamobsx.mob.BaseEntity;
import com.pikycz.novamobsx.mob.animal.walking.*;

/**
 * @author PikyCZ
 */
public class NovaMobsX extends PluginBase implements Listener {

    private static NovaMobsX instance;

    public NovaMobsX plugin;

    public String PluginPrefix = "&c[&bNova&6Mobs&eX&c]";
    public String IntVersion = "&c[&a1.0&c]";

    public static NovaMobsX getInstance() {
        return instance;
    }

    @Override
    public void onLoad() {
        registerEntities();
    }

    public void onEnable() {
        if (instance == null) {
            instance = this;
        }
        this.getServer().getLogger().info(TextFormat.colorize("&bL&fo&ea&cd&di&en&fg" + PluginPrefix));
        this.getServer().getLogger().info(TextFormat.colorize(IntVersion));
    }

    private void registerEntities() {
        // register living entities
        //Entity.registerEntity(Bat.class.getSimpleName(), Bat.class);
        Entity.registerEntity(Chicken.class.getSimpleName(), Chicken.class);
        Entity.registerEntity(Cow.class.getSimpleName(), Cow.class);
        Entity.registerEntity(Donkey.class.getSimpleName(), Donkey.class);
        Entity.registerEntity(Horse.class.getSimpleName(), Horse.class);
        //Entity.registerEntity(Llama.class.getSimpleName(), Llama.class);
        Entity.registerEntity(Mooshroom.class.getSimpleName(), Mooshroom.class);
        Entity.registerEntity(Mule.class.getSimpleName(), Mule.class);
        Entity.registerEntity(Ocelot.class.getSimpleName(), Ocelot.class);
        //Entity.registerEntity(Parrot.class.getSimpleName(), Parrot.class);
        Entity.registerEntity(Pig.class.getSimpleName(), Pig.class);
        Entity.registerEntity(PolarBear.class.getSimpleName(), PolarBear.class);
        Entity.registerEntity(Rabbit.class.getSimpleName(), Rabbit.class);
        Entity.registerEntity(Sheep.class.getSimpleName(), Sheep.class);
        //Entity.registerEntity(Squid.class.getSimpleName(), Squid.class);
        //Entity.registerEntity(Villager.class.getSimpleName(), Villager.class);
    }

    public static Entity create(Object type, Position source, Object... args) {
        FullChunk chunk = source.getLevel().getChunk((int) source.x >> 4, (int) source.z >> 4, true);

        CompoundTag nbt = new CompoundTag().putList(new ListTag<DoubleTag>("Pos").add(new DoubleTag("", source.x)).add(new DoubleTag("", source.y)).add(new DoubleTag("", source.z)))
                .putList(new ListTag<DoubleTag>("Motion").add(new DoubleTag("", 0)).add(new DoubleTag("", 0)).add(new DoubleTag("", 0)))
                .putList(new ListTag<FloatTag>("Rotation").add(new FloatTag("", source instanceof Location ? (float) ((Location) source).yaw : 0))
                        .add(new FloatTag("", source instanceof Location ? (float) ((Location) source).pitch : 0)));

        return Entity.createEntity(type.toString(), chunk, nbt, args);
    }

    @Override
    public boolean onCommand(CommandSender sender, cn.nukkit.command.Command cmd, String label, String[] args) {

        if (sender instanceof Player) {

            if (cmd.getName().toLowerCase().equals("mob")) {

                if (!sender.hasPermission("novamobs.mob ")) {
                    sender.sendMessage(TextFormat.RED + "You don't have permission to use this command.");
                    return false;
                }

                if (args.length == 0) {
                    sender.sendMessage(TextFormat.GOLD + "--NovaMobsX 1.0--");
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
                            sender.sendMessage(TextFormat.GREEN + "Version > 1.0 working with MCPE 1.2.9");//Todo Automatic Updater?
                            break;
                        default:
                            sender.sendMessage("Unkown command.");
                            break;
                    }
                }
            }
            return true;

        } else {
            sender.sendMessage(TextFormat.RED + "Only player can use this command!");
            return true;
        }

    }
}
