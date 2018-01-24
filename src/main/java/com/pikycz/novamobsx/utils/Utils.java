package com.pikycz.novamobsx.utils;

import java.util.Random;

public class Utils {

    /*private static final Server SERVER = Server.getInstance();

    public static final void logServerInfo (String text) {
        SERVER.getLogger().info(TextFormat.GOLD + "[MobPlugin] " + text);
    }*/
    private static final Random random = new Random(System.currentTimeMillis());

    public static int rand(int min, int max) {
        if (min == max) {
            return max;
        }
        return min + random.nextInt(max - min);
    }

    public static boolean rand() {
        return random.nextBoolean();
    }

}
