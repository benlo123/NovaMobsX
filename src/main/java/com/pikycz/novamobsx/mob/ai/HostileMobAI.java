package com.pikycz.novamobsx.mob.ai;

import cn.nukkit.Player;
import cn.nukkit.entity.Entity;
import cn.nukkit.math.Vector3;
import com.pikycz.novamobsx.mob.BaseEntity;
import java.util.Map;

/**
 *
 * @author Jaroslav Pikart
 */
public class HostileMobAI extends PathFinderAI {

    public HostileMobAI(BaseEntity minion, double trackingDistance, double activationDistance, double targetDistance) {
        super(minion, trackingDistance, activationDistance, targetDistance);
    }

    @Override
    public boolean isValidTarget(Vector3 target) {
        if (target instanceof BaseEntity) {
            if (target instanceof Player) {
                Player player = (Player) target;
                return !player.closed && player.spawned && player.isAlive() && !player.isCreative() && player.distanceSquared(minion) <= trackingDistSqr;
            }
            BaseEntity creature = (BaseEntity) target;
            return creature.isAlive() && !creature.closed && creature.distanceSquared(minion) <= trackingDistSqr;
        } else {
            return false;
        }
    }

    @Override
    public Vector3 findTarget() {
        if (searchTime > 0) {
            searchTime--;
            return null;
        }
        searchTime = RECALCULATE_TIME;
        double min = activationDistSqr;
        Player closest = null;
        for (Map.Entry<Long, Player> entry : minion.level.getPlayers().entrySet()) {
            Player player = entry.getValue();
            if (isValidTarget(player)) {
                double distance = player.distanceSquared(minion);
                if (distance < min) {
                    min = distance;
                    closest = player;
                }
            }
        }
        return closest;
    }

    Player player;

    @Override
    public void processAttacker(Entity attacker) {

    }
}
