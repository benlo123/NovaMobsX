package com.pikycz.novamobs.components;

import cn.nukkit.Player;

/**
 * from PureEntitiesX
 */
interface Shareable {

    public boolean shear(Player player);

    public boolean isSheared();

    public boolean setSheared(boolean sheared);

}
