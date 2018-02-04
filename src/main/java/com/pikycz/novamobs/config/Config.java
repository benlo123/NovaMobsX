package com.pikycz.novamobs.config;

import cn.nukkit.plugin.PluginBase;
import java.util.List;

/**
 * @author PikyCZ
 */
public class Config extends PluginBase {

    public int getMinRadius() {
        return getConfig().getInt("MINSPAWN_RADIUS");
    }

    public int getMaxRadius() {
        return getConfig().getInt("MAXSPAWN_RADIUS");
    }

    public int getMaxSpawnAnimal() {
        return getConfig().getInt("maxSpawns_Animals");
    }

    public int getMaxSpawnMob() {
        return getConfig().getInt("maxSpawns_Mobs");
    }

    public int getMaxSpawnNetherMobs() {
        return getConfig().getInt("maxSpawns_NetherMobs");
    }

    public int getSpawnDelay() {
        return getConfig().getInt("autoSpawnTime");
    }

    public List<String> getDisableWorld() {
        return getConfig().getStringList("worldsSpawnDisabled");
    }

    public boolean getSpawAnimals() {
        if (getConfig().get("spawnAnimals").equals("true")) {
            return true;
        } else {
            return false;
        }
    }

    public boolean getSpawnMonsters() {
        if (getConfig().get("spawnMobs").equals("true")) {
            return true;
        } else {
            return false;
        }
    }

    public boolean getSpawnNetherMonsters() {
        if (getConfig().get("spawnNethermobs").equals("true")) {
            return true;
        } else {
            return false;
        }
    }

    public Double getConfigVersion() {
        return getConfig().getDouble("ConfigVersion", 1.0);
    }

}
