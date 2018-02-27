package com.pikycz.novamobs.config;

import com.pikycz.novamobs.NovaMobsX;
import java.util.List;

/**
 * @author PikyCZ
 */
class NovaConfig {

    NovaMobsX plugin;

    public List<String> disabledWorlds;

    //Spawning Settings
    public boolean SpawnAnimals;
    public boolean SpawnMonsters;
    public boolean SpawnNethermobs;
    public int maxSpawns;
    public int AutoSpawnTime;

    public NovaConfig() {
        this.disabledWorlds = (List<String>) plugin.getConfig().get("disabledWorlds");
        this.SpawnAnimals = plugin.getConfig().get("SpawnAnimals", true);
        this.SpawnMonsters = plugin.getConfig().get("SpawnMonsters", true);
        this.SpawnNethermobs = plugin.getConfig().get("SpawnNethermobs", true);
        this.AutoSpawnTime = (int) plugin.getConfig().get("AutoSpawnTime", 300);
    }

    public List<String> getEnableWorlds() {
        return this.disabledWorlds;
    }

    public boolean getEnableAnimals() {
        return this.SpawnAnimals;
    }

    public boolean getEnableMonsters() {
        return this.SpawnMonsters;
    }

    public boolean getEnableNetherMonsters() {
        return this.SpawnNethermobs;
    }

    public int getAutoSpawnTime() {
        return this.AutoSpawnTime;
    }
}
