package com.pikycz.novamobsx.mob.ai;

import cn.nukkit.math.BlockVector3;
import java.util.Queue;

/**
 * @author Jaroslav Pikart
 */
public interface PathFinderQueue {

    Queue<PathFinder.Node> getNewQueue(BlockVector3 target);
}
