package com.pikycz.novamobsx.mob.animal;

import cn.nukkit.level.format.FullChunk;
import cn.nukkit.nbt.tag.CompoundTag;
import com.pikycz.novamobsx.mob.WalkingEntity;

/**
 * @author Jaroslav Pikart
 */
public abstract class WalkingAnimal extends WalkingEntity implements Animal {

    public WalkingAnimal(FullChunk chunk, CompoundTag nbt) {
        super(chunk, nbt);
    }

    @Override
    public int getNetworkId() {
        return NETWORK_ID;
    }

    @Override
    public boolean isBaby() {
        return false;
    }

    @Override
    protected void initEntity() {
        super.initEntity();
    }

    @Override
    public int getKillExperience() {
        return 0;
    }

}
