package com.pikycz.novamobs.utils;

import cn.nukkit.math.NukkitRandom;
import cn.nukkit.math.Vector3;
import java.util.Random;

public class Utils {

    private static final Random random = new Random(System.currentTimeMillis());

    /**
     * Returns a random number between min (inkl.) and max (excl.) If you want a
     * number between 1 and 4 (inkl) you need to call rand (1, 5)
     *
     * @param min min inklusive value
     * @param max max exclusive value
     * @return
     */
    public static int rand(int min, int max) {
        if (min == max) {
            return max;
        }
        return min + random.nextInt(max - min);
    }

    /**
     * Returns random boolean
     *
     * @return a boolean random value either <code>true</code> or
     * <code>false</code>
     */
    public static boolean rand() {
        return random.nextBoolean();
    }

    public static Vector3 randomVector(Vector3 from, int xRadius, int yRadius, int zRadius) {
        NukkitRandom random = new NukkitRandom();

        return from.add(random.nextRange(-xRadius, xRadius), random.nextRange(-yRadius, yRadius), random.nextRange(-zRadius, zRadius));
    }

}