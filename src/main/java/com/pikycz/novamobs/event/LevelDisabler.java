package com.pikycz.novamobs.event;

import cn.nukkit.event.EventPriority;
import cn.nukkit.event.level.LevelLoadEvent;
import cn.nukkit.event.level.LevelUnloadEvent;
import cn.nukkit.level.Level;
import java.util.HashMap;
import java.util.List;

/**
 * @author PikyCZ
 */
public class LevelDisabler {

    public final HashMap<Integer, Level> levelsToSpawn = new HashMap<>();
    private List<String> disabledWorlds;

    @cn.nukkit.event.EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
    public void onLevelLoad(LevelLoadEvent e) {
        Level level = e.getLevel();

        if (!disabledWorlds.contains(level.getFolderName())) {
            levelsToSpawn.put(level.getId(), level);
        }
    }

    @cn.nukkit.event.EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
    public void onLevelUnload(LevelUnloadEvent e) {
        Level level = e.getLevel();

        levelsToSpawn.remove(level.getId());
    }

}
