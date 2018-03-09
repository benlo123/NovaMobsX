package com.pikycz.novamobs.entities.animal.wateranimal;

import cn.nukkit.item.Item;
import cn.nukkit.item.ItemDye;
import cn.nukkit.level.format.FullChunk;
import cn.nukkit.nbt.tag.CompoundTag;
import cn.nukkit.utils.DyeColor;
import com.pikycz.novamobs.entities.monster.WaterMonster;

/**
 * @author PikyCZ
 */
public class Squid extends WaterMonster {

    public static final int NETWORK_ID = 17;

    public Squid(FullChunk chunk, CompoundTag nbt) {
        super(chunk, nbt);
    }

    @Override
    public String getName() {
        return "Squid";
    }

    @Override
    public float getWidth() {
        return 0.8f;
    }

    @Override
    public float getHeight() {
        return 0.8f;
    }

    @Override
    public void initEntity() {
        super.initEntity();
        this.setMaxHealth(10);
    }

    @Override
    public Item[] getDrops() {
        return new Item[]{new ItemDye(DyeColor.BLACK.getDyeData())};
    }

    @Override
    public int getKillExperience() {
        return 1;
    }

    @Override
    public int getNetworkId() {
        return NETWORK_ID;
    }

}
