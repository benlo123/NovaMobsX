package com.pikycz.novamobs.task;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.block.Block;
import cn.nukkit.entity.Entity;
import cn.nukkit.level.Level;
import cn.nukkit.level.Position;
import cn.nukkit.utils.Config;
import com.pikycz.novamobs.NovaMobs;
import com.pikycz.novamobs.configsection.MainConfig;

import com.pikycz.novamobs.entities.animal.walking.*;
import com.pikycz.novamobs.entities.animal.flying.*;
import com.pikycz.novamobs.entities.monster.flying.*;
import com.pikycz.novamobs.entities.monster.walking.*;
import com.pikycz.novamobs.utils.Utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import java.util.TimerTask;

/**
 *
 * @author PikyCZ
 */
public class AutoSpawnTask extends TimerTask {

    //public static HashMap<Integer, Integer> hostileMobsOverWorld = new HashMap<>();
    //public static HashMap<Integer, Integer> passiveMobsOverWorld = new HashMap<>();
    private Map<Integer, Integer> maxSpawns = new HashMap<>();

    public NovaMobs plugin;

    Config config;

    public AutoSpawnTask(NovaMobs plugin) {
        this.plugin = plugin;

        prepareMaxSpawns();
    }

    /*static {
        hostileMobsOverWorld.put(Creeper.NETWORK_ID, 3);
        hostileMobsOverWorld.put(Enderman.NETWORK_ID, 1);
        hostileMobsOverWorld.put(Skeleton.NETWORK_ID, 3);
        hostileMobsOverWorld.put(Spider.NETWORK_ID, 3);
        hostileMobsOverWorld.put(Zombie.NETWORK_ID, 5);

        passiveMobsOverWorld.put(Chicken.NETWORK_ID, 3);
        passiveMobsOverWorld.put(Cow.NETWORK_ID, 3);
        passiveMobsOverWorld.put(Pig.NETWORK_ID, 3);
        passiveMobsOverWorld.put(Sheep.NETWORK_ID, 3);
    }*/
    public void run() {

        List<Player> players = new ArrayList<>();

        for (Level level : plugin.levelsToSpawn.values()) {
            players.addAll(level.getPlayers().values());
        }

        Server.getInstance().getOnlinePlayers().forEach((name, player) -> {
            if (Utils.rand(1, 210) > 40) {
                return;
            }

            Position pos = player.getPosition();
            pos.x += this.getRandomSafeXZCoord(50, 26, 6);
            pos.z += this.getRandomSafeXZCoord(50, 26, 6);
            pos.y = this.getSafeYCoord(player.getLevel(), pos, 3);

            if (pos.y > 127 || pos.y < 1 || player.getLevel().getBlockIdAt((int) pos.x, (int) pos.y, (int) pos.z) == Block.AIR) {
                return;
            }

            int blockId = player.getLevel().getBlockIdAt((int) pos.x, (int) pos.y, (int) pos.z);
            int biomeId = player.getLevel().getBiomeId((int) pos.x, (int) pos.z);
            int blockBlockLight = player.getLevel().getBlockLightAt((int) pos.x, (int) pos.y, (int) pos.z);
            int blockSkyLight = player.getLevel().getBlockSkyLightAt((int) pos.x, (int) pos.y, (int) pos.z);
            int blockLightLevel = Math.max(blockBlockLight, blockSkyLight);

            /*if (blockId == Block.NETHER_BRICK_BLOCK || blockId == Block.NETHER_BRICK_FENCE || blockId == Block.NETHER_BRICKS_STAIRS || blockId == Block.SOUL_SAND || blockId == Block.NETHERRACK) {
                if (Utils.rand(1, 100) <= 20) {
                    this.createEntity("Blaze", pos.add(0, 2.8, 0));
                    return;
                }
                if (Utils.rand(1, 100) <= 20) {
                    this.createEntity("Ghast", pos.add(0, 5, 0));
                    return;
                }
                if (Utils.rand(1, 100) <= 30) {
                    this.createEntity("MagmaCube", pos.add(0, 2.2, 0));
                    return;
                }
                if (Utils.rand(1, 100) <= 50) {
                    this.createEntity("PigZombie", pos.add(0, 3.8, 0));
                    return;
                }
            }*/
            int time = player.getLevel().getTime() % Level.TIME_FULL;
            if (MainConfig.SpawnMobs = true) {
                if (time >= Level.TIME_NIGHT && time < Level.TIME_SUNRISE) {
                    if (blockLightLevel <= 3 && pos.y <= 63 && Utils.rand(1, 100) <= 20) {
                        this.createEntity("Bat", pos.add(0, 1.3, 0));
                        return;
                    }
                    if (blockLightLevel <= 3 && pos.y <= 63 && Utils.rand(1, 100) <= 20) {
                        this.createEntity("CaveSpider", pos.add(0, 1.8, 0));
                        return;
                    }
                    switch (Utils.rand(1, 6)) {
                        case 1:
                            this.createEntity("Creeper", pos.add(0, 2.8, 0));
                            break;
                        case 2:
                            this.createEntity("Enderman", pos.add(0, 3.8, 0));
                            break;
                        case 3:
                            this.createEntity("Skeleton", pos.add(0, 2.8, 0));
                            break;
                        case 4:
                            this.createEntity("Spider", pos.add(0, 2.12, 0));
                            break;
                        case 5:
                            this.createEntity("Zombie", pos.add(0, 2.8, 0));
                            break;
                        case 6:
                            this.createEntity("ZombieVillager", pos.add(0, 2.8, 0));
                            break;
                    }
                }

            } else {
                if (MainConfig.SpawnAnimals = true) {
                    if (blockId == Block.MYCELIUM && Utils.rand(1, 100) <= 70) {
                        this.createEntity("Mooshroom", pos.add(0, 2.12, 0));
                        return;
                    }
                    if ((blockId == Block.GRASS || blockId == Block.LEAVE) && Utils.rand(1, 100) <= 20) {
                        this.createEntity("Ocelot", pos.add(0, 1.9, 0));
                        return;
                    }
                    /*if (biomeId == Biome.SWAMP && Utils.rand(1, 100) <= 30) {
                    this.createEntity("Slime", pos.add(0, 2.2, 0));
                    return;
                    }
                    if (blockId == Block.GRASS && Utils.rand(1, 100) <= 30) {
                        if (biomeId == Biome.FOREST || biomeId == Biome.BIRCH_FOREST || biomeId == Biome.TAIGA) {
                            this.createEntity("Wolf", pos.add(0, 1.9, 0));
                            return;
                        }
                    }*/
                    switch (Utils.rand(1, 5)) {
                        case 1:
                            this.createEntity("Chicken", pos.add(0, 1.7, 0));
                            break;
                        case 2:
                            this.createEntity("Cow", pos.add(0, 2.3, 0));
                            break;
                        case 3:
                            this.createEntity("Pig", pos.add(0, 1.9, 0));
                            break;
                        case 4:
                            this.createEntity("Rabbit", pos.add(0, 1.75, 0));
                            break;
                        case 5:
                            this.createEntity("Sheep", pos.add(0, 2.3, 0));
                            break;
                    }
                }
            }
        });
    }

    private void prepareMaxSpawns() {
        maxSpawns.put(Bat.NETWORK_ID, this.config.getInt("max-spawns.bat", 0));
        maxSpawns.put(Blaze.NETWORK_ID, this.config.getInt("max-spawns.blaze", 0));
        maxSpawns.put(CaveSpider.NETWORK_ID, this.config.getInt("max-spawns.cave-spider", 0));
        maxSpawns.put(Chicken.NETWORK_ID, this.config.getInt("max-spawns.chicken", 2));
        maxSpawns.put(Cow.NETWORK_ID, this.config.getInt("max-spawns.cow", 1));
        maxSpawns.put(Creeper.NETWORK_ID, this.config.getInt("max-spawns.creeper", 0));
        maxSpawns.put(Donkey.NETWORK_ID, this.config.getInt("max-spawns.donkey", 0));
        maxSpawns.put(Enderman.NETWORK_ID, this.config.getInt("max-spawns.enderman", 0));
        maxSpawns.put(Ghast.NETWORK_ID, this.config.getInt("max-spawns.ghast", 0));
        maxSpawns.put(Horse.NETWORK_ID, this.config.getInt("max-spawns.horse", 0));
        maxSpawns.put(Mooshroom.NETWORK_ID, this.config.getInt("max-spawns.mooshroom", 0));
        maxSpawns.put(Mule.NETWORK_ID, this.config.getInt("max-spawns.mule", 0));
        maxSpawns.put(Ocelot.NETWORK_ID, this.config.getInt("max-spawns.ocelot", 1));
        maxSpawns.put(Parrot.NETWORK_ID, this.config.getInt("max-spawns.parrot", 0));
        maxSpawns.put(Pig.NETWORK_ID, this.config.getInt("max-spawns.pig", 2));
        maxSpawns.put(PigZombie.NETWORK_ID, this.config.getInt("max-spawns.pig-zombie", 0));
        maxSpawns.put(Rabbit.NETWORK_ID, this.config.getInt("max-spawns.rabbit", 2));
        maxSpawns.put(Silverfish.NETWORK_ID, this.config.getInt("max-spawns.silverfish", 0));
        maxSpawns.put(Sheep.NETWORK_ID, this.config.getInt("max-spawns.sheep", 2));
        maxSpawns.put(Skeleton.NETWORK_ID, this.config.getInt("max-spawns.skeleton", 1));
        maxSpawns.put(SkeletonHorse.NETWORK_ID, this.config.getInt("max-spawns.skeleton-horse", 0));
        maxSpawns.put(Spider.NETWORK_ID, this.config.getInt("max-spawns.spider", 1));
        maxSpawns.put(Wolf.NETWORK_ID, this.config.getInt("max-spawns.wolf", 0));
        maxSpawns.put(Zombie.NETWORK_ID, this.config.getInt("max-spawns.zombie", 1));
        maxSpawns.put(ZombieHorse.NETWORK_ID, this.config.getInt("max-spawns.zombie-horse", 0));
        maxSpawns.put(ZombieVillager.NETWORK_ID, this.config.getInt("max-spawns.zombie-villager", 0));
        maxSpawns.put(Husk.NETWORK_ID, this.config.getInt("max-spawns.husk", 0));
        maxSpawns.put(Stray.NETWORK_ID, this.config.getInt("max-spawns.stray", 0));

    }

    public boolean entitySpawnAllowed(Level level, int networkId, String entityName) {
        int count = countEntity(level, networkId);
        return count < maxSpawns.get(networkId);
    }

    private int countEntity(Level level, int networkId) {
        int count = 0;
        for (Entity entity : level.getEntities()) {
            if (entity.isAlive() && entity.getNetworkId() == networkId) {
                count++;
            }
        }
        return count;
    }

    public void createEntity(String type, Position pos) {
        Entity entity = NovaMobs.create(type, pos);
        if (entity != null) {
            entity.spawnToAll();
        }
    }

    public int getRandomSafeXZCoord(int degree, int safeDegree, int correctionDegree) {
        int addX = Utils.rand(degree / 2 * -1, degree / 2);
        if (addX >= 0) {
            if (degree < safeDegree) {
                addX = safeDegree;
                addX += Utils.rand(correctionDegree / 2 * -1, correctionDegree / 2);
            }
        } else {
            if (degree > safeDegree) {
                addX = -safeDegree;
                addX += Utils.rand(correctionDegree / 2 * -1, correctionDegree / 2);
            }
        }
        return addX;
    }

    public int getSafeYCoord(Level level, Position pos, int needDegree) {
        int x = (int) pos.x;
        int y = (int) pos.y;
        int z = (int) pos.z;

        if (level.getBlockIdAt(x, y, z) == Block.AIR) {
            while (true) {
                y--;
                if (y > 127) {
                    y = 128;
                    break;
                }
                if (y < 1) {
                    y = 0;
                    break;
                }
                if (level.getBlockIdAt(x, y, z) != Block.AIR) {
                    int checkNeedDegree = needDegree;
                    int checkY = y;
                    while (true) {
                        checkY++;
                        checkNeedDegree--;
                        if (checkY > 255 || checkY < 1 || level.getBlockIdAt(x, checkY, z) != Block.AIR) {
                            break;
                        }

                        if (checkNeedDegree <= 0) {
                            return y;
                        }
                    }
                }
            }
        } else {
            while (true) {
                y++;
                if (y > 127) {
                    y = 128;
                    break;
                }

                if (y < 1) {
                    y = 0;
                    break;
                }

                if (level.getBlockIdAt(x, y, z) != Block.AIR) {
                    int checkNeedDegree = needDegree;
                    int checkY = y;
                    while (true) {
                        checkY--;
                        checkNeedDegree--;
                        if (checkY > 255 || checkY < 1 || level.getBlockIdAt(x, checkY, z) != Block.AIR) {
                            break;
                        }

                        if (checkNeedDegree <= 0) {
                            return y;
                        }
                    }
                }
            }
        }
        return y;
    }
}
