package com.pikycz.novamobsx.mob.ai;

import cn.nukkit.math.Vector3;
import com.pikycz.novamobsx.mob.BaseEntity;

/**
 *
 * @author Jaroslav Pikart
 */
public class DefaultEntityPathFinder extends PathFinder {

    public DefaultEntityPathFinder(BaseEntity entity, Vector3 target, double distance) {
        super(entity.hasGravity() ? new DefaultVisitor(entity.getLevel(), (int) Math.ceil(entity.getHeight())) : new FlyingVisitor(entity.getLevel(), (int) Math.ceil(entity.getHeight())), new RandomQueue(), distance == 0 ? new DefaultPathGoal() : new EquidistantPathGoal(distance), entity, target);
    }
}
