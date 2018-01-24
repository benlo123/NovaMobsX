package com.pikycz.novamobsx.mob.ai;

import cn.nukkit.math.BlockVector3;

/**
 * @author Jaroslav Pikart
 */
public class DefaultPathGoal implements PathGoal {

    @Override
    public int distance(PathFinder.Node node, BlockVector3 goal) {
        return Math.abs(node.getX() - goal.x) + Math.abs(node.getY() - goal.y) + Math.abs(node.getZ() - goal.z);
    }

}
