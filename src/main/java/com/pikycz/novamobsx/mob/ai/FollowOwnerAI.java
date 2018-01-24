package com.pikycz.novamobsx.mob.ai;

import cn.nukkit.entity.Entity;
import cn.nukkit.entity.passive.EntityAnimal;
import cn.nukkit.entity.passive.EntityTameable;
import cn.nukkit.math.Vector3;
import com.pikycz.novamobsx.mob.BaseEntity;

/**
 *
 * @author Jaroslav Pikart
 */
public class FollowOwnerAI extends PathFinderAI {

    public FollowOwnerAI(BaseEntity minion, double trackingDistance, double activationDistance, double targetDistance) {
        super(minion, trackingDistance, activationDistance, targetDistance);
    }

    EntityAnimal animal;

    public EntityTameable getTamed() {
        return (EntityTameable) animal;
    }

    @Override
    public boolean isValidTarget(Vector3 target) {
        return target != null && target == getTamed().getOwner();
    }

    @Override
    public Vector3 findTarget() {
        return getTamed().getOwner();
    }

    @Override
    public void processAttacker(Entity attacker) {

    }
}
