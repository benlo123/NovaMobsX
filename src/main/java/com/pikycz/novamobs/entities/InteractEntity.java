package com.pikycz.novamobs.entities;

import cn.nukkit.entity.EntityInteractable;

/**
 * @author PikyCZ
 */
public interface InteractEntity extends EntityInteractable {

    public String getInteractButtonText();

    public boolean canDoInteraction();

}
