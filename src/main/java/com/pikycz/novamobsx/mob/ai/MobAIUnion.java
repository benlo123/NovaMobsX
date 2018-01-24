package com.pikycz.novamobsx.mob.ai;

import cn.nukkit.entity.Entity;
import cn.nukkit.math.Vector3;

/**
 *
 * @author Jaroslav Pikart
 */
public class MobAIUnion implements MobAI {

    private final MobAI[] tactics;
    private MobAI currentTactic;

    public MobAIUnion(MobAI... ai) {
        this.tactics = ai;
    }

    @Override
    public boolean isValidTarget(Vector3 target) {
        for (MobAI tactic : tactics) {
            if (tactic.isValidTarget(target)) {
                currentTactic = tactic;
                return true;
            }
        }
        return false;
    }

    @Override
    public Vector3 findTarget() {
        for (MobAI tactic : tactics) {
            Vector3 target = tactic.findTarget();
            if (isValidTarget(target)) {
                return target;
            }
        }
        return null;
    }

    @Override
    public Vector3 calculateNextMove(Vector3 target) {
        if (currentTactic != null) {
            return currentTactic.calculateNextMove(target);
        }
        return null;
    }

    @Override
    public void processAttacker(Entity attacker) {
        for (MobAI tactic : tactics) {
            tactic.processAttacker(attacker);
        }
    }
}
