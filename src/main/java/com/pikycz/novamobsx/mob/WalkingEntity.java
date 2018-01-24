package com.pikycz.novamobsx.mob;

import cn.nukkit.level.format.FullChunk;
import cn.nukkit.nbt.tag.CompoundTag;

/**
 *
 * @author Jaroslav Pikart
 */
public abstract class WalkingEntity extends BaseEntity {

    public WalkingEntity(FullChunk chunk, CompoundTag nbt) {
        super(chunk, nbt);
    }

}
