package com.pikycz.novamobs.entities.monster.flying;

import cn.nukkit.entity.Entity;
import cn.nukkit.item.Item;
import cn.nukkit.item.ItemSkull;
import cn.nukkit.level.format.FullChunk;
import cn.nukkit.nbt.tag.CompoundTag;

import com.pikycz.novamobs.entities.monster.FlyingMonster;

/**
 *
 * @author PikyCZ
 *
 */
public class EnderDragon extends FlyingMonster {

    public static final int NETWORK_ID = 53;

    public EnderDragon(FullChunk chunk, CompoundTag nbt) {
        super(chunk, nbt);
    }

    @Override
    public int getNetworkId() {
        return NETWORK_ID;
    }
    
    @Override
    public String getName() {
        return "EnderDragon";
    }

    @Override
    public float getWidth() {
        return 13f;
    }

    @Override
    public float getHeight() {
        return 4f;
    }

    @Override
    public void initEntity() {
        super.initEntity();
        this.fireProof = true;
        this.setMaxHealth(250);
        this.setDamage(new int[]{0, 0, 0, 0});
        this.kill();
    }

    @Override
    public Item[] getDrops() {
        return new Item[]{Item.get(Item.DRAGON_EGG), Item.get(ItemSkull.DRAGON_HEAD)};
    }

    @Override
    public int getKillExperience() {
        return 500;
    }

    @Override
    public void attackEntity(Entity player) {
        //todo
    }

}
