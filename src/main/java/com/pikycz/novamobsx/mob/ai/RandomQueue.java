/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pikycz.novamobsx.mob.ai;

import cn.nukkit.math.BlockVector3;
import com.pikycz.novamobsx.utils.FastRandom;
import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.Queue;

/**
 *
 * @author Jaroslav Pikart
 */
// An imprecise queue where further away entities make more random decisions
public class RandomQueue implements PathFinderQueue {

    public static int IMPRECISION = 8;

    public Queue<PathFinder.Node> getNewQueue(BlockVector3 target) {
        return new PriorityQueue<PathFinder.Node>(new Comparator<PathFinder.Node>() {
            @Override
            public int compare(PathFinder.Node a, PathFinder.Node b) {
                int aDistance = a.distance();
                int bDistance = b.distance();
                if (aDistance > 6) {
                    aDistance = FastRandom.random.random(aDistance - 4) + aDistance * 8 + a.getMoves();
                } else {
                    aDistance += a.getMoves();
                }
                if (bDistance > 6) {
                    bDistance = FastRandom.random.random(bDistance - 4) + bDistance * 8 + b.getMoves();
                } else {
                    bDistance += b.getMoves();
                }
                return Integer.compare(aDistance, bDistance);
            }
        });
    }
}
