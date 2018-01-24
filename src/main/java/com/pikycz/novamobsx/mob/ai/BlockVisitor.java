package com.pikycz.novamobsx.mob.ai;

import cn.nukkit.math.Vector3;

/**
 *
 * @author Jaroslav Pikart
 */
public interface BlockVisitor {

    public boolean isVisitable(Vector3 from, Vector3 to);

}
