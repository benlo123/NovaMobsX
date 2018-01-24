package com.pikycz.novamobsx.mob.animal.walking;

import cn.nukkit.event.entity.EntityDamageEvent;
import cn.nukkit.item.Item;
import cn.nukkit.level.format.FullChunk;
import cn.nukkit.nbt.tag.CompoundTag;
import cn.nukkit.utils.DyeColor;
import com.pikycz.novamobsx.mob.ai.CreatureWanderAI;
import com.pikycz.novamobsx.mob.ai.FollowItemAI;
import com.pikycz.novamobsx.mob.ai.FollowOwnerAI;
import com.pikycz.novamobsx.mob.ai.MobAIUnion;
import com.pikycz.novamobsx.mob.ai.NeutralMobAI;
import com.pikycz.novamobsx.mob.animal.WalkingAnimal;

public class Wolf extends WalkingAnimal {

    public static final int NETWORK_ID = 14;

    private static final String NBT_KEY_ANGRY = "Angry"; //0 - not angry, > 0 angry

    private int angry = 0;

    private DyeColor collarColor = DyeColor.RED; // red is default

    public Wolf(FullChunk chunk, CompoundTag nbt) {
        super(chunk, nbt);
        setAI(new MobAIUnion(new NeutralMobAI(this, 16, 16, 0), new FollowOwnerAI(this, 128, 128, 128), new FollowItemAI(this, Item.COOKED_FISH + Item.CLOWNFISH + Item.RAW_FISH, 49, 32), new CreatureWanderAI(this)));
    }

    @Override
    public int getNetworkId() {
        return NETWORK_ID;
    }

    @Override
    public String getName() {
        return "Wolf";
    }

    @Override
    public float getWidth() {
        return 0.6f;
    }

    @Override
    public float getHeight() {
        return 0.8f;
    }

    @Override
    public double getSpeed() {
        return 1.2;
    }

    @Override
    protected void initEntity() {
        super.initEntity();
        this.setMaxHealth(8);
        this.fireProof = false;

    }

    @Override
    public void saveNBT() {
        super.saveNBT();
        this.namedTag.putInt(NBT_KEY_ANGRY, this.angry);
    }

    public boolean isAngry() {
        return this.angry > 0;
    }

    public void setAngry(boolean angry) {
        this.angry = angry ? 1 : 0;
    }

    @Override
    public boolean attack(EntityDamageEvent ev) {
        boolean result = super.attack(ev);

        if (!ev.isCancelled()) {
            this.setAngry(true);
        }

        return result;
    }

    @Override
    public Item[] getDrops() {
        return new Item[0];
    }

    @Override
    public int getKillExperience() {
        return 3; // gain 3 experience
    }

}
