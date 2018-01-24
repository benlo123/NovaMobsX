/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pikycz.novamobsx.mob.ai;

import cn.nukkit.math.BlockVector3;

/**
 *
 * @author Jaroslav Pikart
 */
public interface PathGoal {

    int distance(PathFinder.Node node, BlockVector3 goal);
}
