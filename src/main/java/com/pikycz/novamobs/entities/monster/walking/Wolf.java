package com.pikycz.novamobs.entities.monster.walking;

import cn.nukkit.entity.Entity;
import cn.nukkit.entity.EntityCreature;
import cn.nukkit.event.entity.EntityDamageByEntityEvent;
import cn.nukkit.event.entity.EntityDamageEvent;
import cn.nukkit.event.entity.EntityDamageEvent.DamageCause;
import cn.nukkit.item.Item;
import cn.nukkit.level.format.FullChunk;
import cn.nukkit.nbt.tag.CompoundTag;
import cn.nukkit.utils.DyeColor;

import com.pikycz.novamobs.entities.monster.WalkingMonster;

public class Wolf extends WalkingMonster {

    public static final int NETWORK_ID = 14;

    private static final String NBT_KEY_ANGRY = "Angry"; //0 - not angry, > 0 angry

    private int angry = 0;

    private DyeColor collarColor = DyeColor.RED; // red is default

    public Wolf(FullChunk chunk, CompoundTag nbt) {
        super(chunk, nbt);
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
        this.setDamage(new int[]{0, 3, 4, 5});

    }

    @Override
    public void saveNBT() {
        super.saveNBT();
        this.namedTag.putInt(NBT_KEY_ANGRY, this.angry);
    }

    @Override
    public boolean targetOption(EntityCreature creature, double distance) {
        return this.isAngry() && super.targetOption(creature, distance);
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
    public void attackEntity(Entity player) {
            if (this.attackDelay > 10 && this.distanceSquared(player) < 1.6) {
                this.attackDelay = 0;
                player.attack(new EntityDamageByEntityEvent(this, player, DamageCause.ENTITY_ATTACK, getDamage()));
            }
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
