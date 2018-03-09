package com.pikycz.novamobs.entities.monster.walking;

import cn.nukkit.Player;
import cn.nukkit.entity.Entity;
import cn.nukkit.event.entity.EntityDamageByEntityEvent;
import cn.nukkit.event.entity.EntityDamageEvent;
import cn.nukkit.item.Item;
import cn.nukkit.level.format.FullChunk;
import cn.nukkit.nbt.tag.CompoundTag;
import com.pikycz.novamobs.entities.monster.WalkingMonster;

/**
 * @author PikyCZ
 */
public class MagmaCube extends WalkingMonster {

    public static final int NETWORK_ID = 42;

    @Override
    public int getNetworkId() {
        return NETWORK_ID;
    }

    public MagmaCube(FullChunk chunk, CompoundTag nbt) {
        super(chunk, nbt);
    }

    @Override
    public String getName() {
        return "MagmaCube";
    }

    @Override
    protected void initEntity() {
        super.initEntity();
        this.setMaxHealth(16);
    }

    @Override
    public float getWidth() {
        return 2.04f;
    }

    @Override
    public float getHeight() {
        return 2.04f;
    }

    @Override
    public int getKillExperience() {
        return 2;
    }

    @Override
    public void attackEntity(Entity player) {
        if (!(player instanceof Player)) {
            return;
        }
        if (this.attackDelay > 10 && this.distanceSquared(player) < 1.2) {
            this.attackDelay = 0;
            player.attack(new EntityDamageByEntityEvent(this, player, EntityDamageEvent.DamageCause.ENTITY_ATTACK, getDamage()));
        }
    }

    @Override
    public Item[] getDrops() {
        return new Item[]{Item.get(Item.MAGMA_CREAM)};
    }

}
