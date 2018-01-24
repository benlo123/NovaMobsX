package com.pikycz.novamobsx.mob.ai;

import cn.nukkit.entity.Entity;
import cn.nukkit.entity.mob.EntityMob;
import cn.nukkit.entity.passive.EntityAnimal;
import cn.nukkit.entity.passive.EntityTameable;
import cn.nukkit.math.Vector3;
import com.pikycz.novamobsx.mob.BaseEntity;
import java.util.HashSet;

/**
 *
 * @author Jaroslav Pikart
 */
public class NeutralMobAI extends PathFinderAI {

    public NeutralMobAI(BaseEntity minion, double trackingDistance, double activationDistance, double targetDistance) {
        super(minion, trackingDistance, activationDistance, targetDistance);
    }

    @Override
    public boolean isValidTarget(Vector3 target) {
        return target instanceof EntityMob || hostile.contains(target);
    }

    private HashSet<Entity> hostile = new HashSet<>();

    @Override
    public Vector3 findTarget() {
        if (searchTime > 0) {
            searchTime--;
            return null;
        }
        searchTime = RECALCULATE_TIME;
        double min = activationDistSqr;
        Entity closest = null;
        for (Entity entity : minion.level.getEntities()) {
            if (isValidTarget(entity)) {
                double distance = entity.distanceSquared(minion);
                if (distance < min) {
                    min = distance;
                    closest = entity;
                }
            }
        }
        return closest;
    }

    EntityAnimal animal;

    @Override
    public void processAttacker(Entity attacker) {
        // Do nothing
        if (animal instanceof EntityTameable) {
            EntityTameable tameable = (EntityTameable) animal;
            if (tameable.isTamed() && tameable.getOwner() == attacker) {
                return;
            }
        }
        hostile.add(attacker);
    }
}
