package com.pikycz.novamobsx.mob.animal.walking;

import cn.nukkit.entity.Entity;
import cn.nukkit.item.Item;
import cn.nukkit.level.format.FullChunk;
import cn.nukkit.nbt.tag.CompoundTag;
import com.pikycz.novamobsx.mob.ai.CreatureFleeAI;
import com.pikycz.novamobsx.mob.ai.CreatureWanderAI;
import com.pikycz.novamobsx.mob.ai.FollowItemAI;
import com.pikycz.novamobsx.mob.ai.MobAIUnion;
import com.pikycz.novamobsx.mob.animal.WalkingAnimal;
import com.pikycz.novamobsx.utils.Utils;

public class Ocelot extends WalkingAnimal {

    public static final int NETWORK_ID = 22;

    public Ocelot(FullChunk chunk, CompoundTag nbt) {
        super(chunk, nbt);
        setAI(new MobAIUnion(new CreatureFleeAI(this, 32, 32, 32), new FollowItemAI(this, Item.RAW_FISH, 49, 32), new CreatureWanderAI(this)));
    }

    @Override
    public int getNetworkId() {
        return NETWORK_ID;
    }

    @Override
    public String getName() {
        return "Ocelot";
    }

    @Override
    public float getWidth() {
        if (this.isBaby()) {
            return 0.3f;
        }
        return 0.6f;
    }

    @Override
    public float getHeight() {
        if (this.isBaby()) {
            return 0.35f;
        }
        return 0.7f;
    }

    @Override
    public double getSpeed() {
        return 1.4;
    }

    @Override
    public boolean isBaby() {
        return this.getDataFlag(DATA_FLAGS, Entity.DATA_FLAG_BABY);
    }

    @Override
    protected void initEntity() {
        super.initEntity();
        this.setMaxHealth(10);
    }

    @Override
    public Item[] getDrops() {
        return new Item[0];
    }

    @Override
    public int getKillExperience() {
        return Utils.rand(1, 4); // gain 1-3 experience
    }

}
