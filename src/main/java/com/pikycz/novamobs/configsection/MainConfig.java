package com.pikycz.novamobs.configsection;

import cn.nukkit.level.Level;
import cn.nukkit.utils.Config;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @author PikyCZ
 */
public class MainConfig {
    //remove

    public final HashMap<Integer, Level> levelsToSpawn = new HashMap<>();

    public List<String> disabledWorlds;

    public static boolean SpawnAnimals;
    public static boolean SpawnMobs;

    public static int SpawnDelay;

    Config config;

    @SuppressWarnings("unchecked")
    public List<String> getdisabledWorlds() {
        return this.config.get("worlds-spawn-disabled", new ArrayList());
    }

    public boolean getspawnAnimals() {
        return this.config.get("spawn-animals", true);
    }

    public boolean getspawnMobs() {
        return this.config.get("spawn-mobs", true);
    }

    public int getSpawnDelay() {
        return this.config.get("auto-spawn-tick", 20);
    }

    public void saveConfig() {
        this.config.save();
    }

}
