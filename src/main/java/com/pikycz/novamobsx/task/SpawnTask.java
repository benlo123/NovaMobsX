package com.pikycz.novamobsx.task;

import cn.nukkit.Server;
import cn.nukkit.scheduler.Task;
import cn.nukkit.scheduler.TaskHandler;

/**
 * @author Jaroslav Pikart
 */
public class SpawnTask {

    public static TaskHandler handler;

    public static void spawn() {

        handler = Server.getInstance().getScheduler().scheduleRepeatingTask(new Task() {

            @Override
            public void onRun(int i) {

            }
        }, 100);
    }
}
