package com.pikycz.novamobs.task;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.block.Block;
import cn.nukkit.entity.Entity;
import cn.nukkit.level.Level;
import cn.nukkit.level.Position;
import cn.nukkit.level.generator.biome.Biome;
import cn.nukkit.utils.TextFormat;
import com.pikycz.novamobs.NovaMobsX;
import com.pikycz.novamobs.utils.Utils;
import java.util.ArrayList;
import java.util.List;

/**
 * @author PikyCZ
 */
public class AutoSpawnTask implements Runnable {

    public static NovaMobsX plugin;

    public static boolean spawnAnimals;
    public static boolean spawnMonsters;
    public static boolean spawnNethermobs;

    public AutoSpawnTask(NovaMobsX owner) {
        plugin = owner;
    }

    @Override
    public void run() {

        spawnAnimals = plugin.pluginConfig.getBoolean("spawnAnimals", true);
        spawnMonsters = plugin.pluginConfig.getBoolean("spawnMonsters", true);
        spawnNethermobs = plugin.pluginConfig.getBoolean("spawnNethermobs", true);

        List<Player> Players = new ArrayList<>();

        for (Level level : plugin.levelsToSpawn.values()) {
            Players.addAll(level.getPlayers().values());
        }

        Server.getInstance().getOnlinePlayers().forEach((name, player) -> {
            if (Server.getInstance().getOnlinePlayers().size() > 0) {

                plugin.getLogger().debug("Starting AutoSpawnTask");

                plugin.getLogger().debug("Found " + Server.getInstance().getOnlinePlayers().size() + " online");

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

                int time = player.getLevel().getTime() % Level.TIME_FULL;

                if (spawnMonsters) {
                    if (time >= Level.TIME_NIGHT && time < Level.TIME_SUNRISE && biomeId != Biome.HELL) {
                        if (blockId == Block.STONE) {
                            this.createEntity("CaveSpider", pos.add(0, 1.8, 0));
                            return;
                        }
                        if (biomeId == Biome.DESERT) {
                            this.createEntity("Husk", pos.add(0, 1.8, 0));
                            return;
                        }
                        if (biomeId == Biome.ICE_PLAINS) {
                            this.createEntity("Stray", pos.add(0, 3.8, 0));
                            return;
                        }
                        this.createEntity("Enderman", pos.add(0, 3.8, 0));
                        this.createEntity("Creeper", pos.add(0, 2.8, 0));
                        this.createEntity("Skeleton", pos.add(0, 2.8, 0));
                        this.createEntity("Spider", pos.add(0, 2.12, 0));
                        this.createEntity("Zombie", pos.add(0, 2.8, 0));
                        this.createEntity("ZombieVillager", pos.add(0, 2.8, 0));
                        this.createEntity("Witch", pos.add(0, 2.8, 0));
                        return;
                    }
                }

                if (spawnAnimals) {
                    if (time >= Level.TIME_SUNRISE && time < Level.TIME_SUNSET && biomeId != Biome.HELL) {
                        if (biomeId == Biome.MUSHROOM_ISLAND) {
                            this.createEntity("Mooshroom", pos.add(0, 2.12, 0));
                            return;
                        }
                        if (biomeId == Biome.ICE_PLAINS) {
                            this.createEntity("PolarBear", pos.add(0, 3.8, 0));
                            return;
                        }
                        if ((blockId == Block.LEAVE && biomeId == Biome.JUNGLE)) {
                            this.createEntity("Ocelot", pos.add(0, 1.9, 0));
                            return;
                        }
                        if (biomeId == Biome.SWAMP) {
                            this.createEntity("Slime", pos.add(0, 2.2, 0));
                            return;
                        }
                        if (biomeId == Biome.OCEAN) {
                            this.createEntity("ElderGuardian", pos.add(0, 2.2, 0));
                            this.createEntity("Guardian", pos.add(0, 2.2, 0));
                            this.createEntity("Squid", pos.add(0, 2.2, 0));
                            return;
                        }
                        if (biomeId == Biome.FOREST || biomeId == Biome.BIRCH_FOREST || biomeId == Biome.TAIGA) {
                            this.createEntity("Wolf", pos.add(0, 1.9, 0));
                            return;
                        }
                        if (biomeId == Biome.JUNGLE) {
                            this.createEntity("Parrot", pos.add(0, 1.9, 0));
                            return;
                        }
                        if (biomeId == Biome.PLAINS || biomeId == Biome.SAVANNA) {
                            this.createEntity("Horse", pos.add(0, 1.9, 0));
                            this.createEntity("Donkey", pos.add(0, 2.3, 0));
                            this.createEntity("Mule", pos.add(0, 2.3, 0));
                            return;
                        }
                        this.createEntity("Chicken", pos.add(0, 1.7, 0));
                        this.createEntity("Cow", pos.add(0, 2.3, 0));
                        this.createEntity("Pig", pos.add(0, 1.9, 0));
                        this.createEntity("Rabbit", pos.add(0, 1.75, 0));
                        this.createEntity("Sheep", pos.add(0, 2.3, 0));
                        return;
                    }
                }

                if (spawnNethermobs) {
                    if (biomeId == Biome.HELL) {
                        this.createEntity("Blaze", pos.add(0, 2.8, 0));
                        this.createEntity("Ghast", pos.add(0, 5, 0));
                        this.createEntity("MagmaCube", pos.add(0, 2.2, 0));
                        this.createEntity("PigZombie", pos.add(0, 3.8, 0));
                        if (blockLightLevel > 7) {
                            this.createEntity("WitherSkeleton", pos.add(0, 2.8, 0));
                        }
                        return;
                    }
                }
            } else {
                plugin.getLogger().debug(TextFormat.YELLOW + "No player online found " + Server.getInstance().getOnlinePlayers().size() + "/" + plugin.getServer().getMaxPlayers() + " Skipping auto spawn");
            }
        });
    }

    public void createEntity(Object type, Position pos, Object... args) {
        Entity entity = NovaMobsX.create((String) type, pos, args);
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
                if (y > 255) {
                    y = 256;
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
                if (y > 255) {
                    y = 256;
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
