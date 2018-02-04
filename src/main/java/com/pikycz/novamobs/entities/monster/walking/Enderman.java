package com.pikycz.novamobs.entities.monster.walking;

import cn.nukkit.Player;
import cn.nukkit.entity.Entity;
import cn.nukkit.entity.EntityCreature;
import cn.nukkit.event.entity.EntityDamageByEntityEvent;
import cn.nukkit.event.entity.EntityDamageEvent.DamageCause;
import cn.nukkit.item.Item;
import cn.nukkit.level.format.FullChunk;
import cn.nukkit.nbt.tag.CompoundTag;

import com.pikycz.novamobs.entities.monster.WalkingMonster;
import com.pikycz.novamobs.utils.Utils;

import java.util.ArrayList;
import java.util.List;

public class Enderman extends WalkingMonster {

    public static final int NETWORK_ID = 38;

    public Enderman(FullChunk chunk, CompoundTag nbt) {
        super(chunk, nbt);
    }

    @Override
    public int getNetworkId() {
        return NETWORK_ID;
    }
    
    @Override
    public String getName() {
        return "Enderman";
    }

    @Override
    public float getWidth() {
        return 0.72f;
    }

    @Override
    public float getHeight() {
        return 2.8f;
    }

    @Override
    public double getSpeed() {
        return 1.21;
    }

    @Override
    protected void initEntity() {
        this.setMaxHealth(40);
        super.initEntity();

        this.setDamage(new int[]{0, 4, 7, 10});
    }

    @Override
    public void attackEntity(Entity player) {
        if (this.attackDelay > 10 && this.distanceSquared(player) < 1) {
            this.attackDelay = 0;
            player.attack(new EntityDamageByEntityEvent(this, player, DamageCause.ENTITY_ATTACK, getDamage()));
        }
    }
    
    public boolean targetOption(EntityCreature creature, float distance) {
        // enderman don't attack alone. they only attack when looked at
        return false;
    }

    @Override
    public Item[] getDrops() {
        List<Item> drops = new ArrayList<>();
        if (this.lastDamageCause instanceof EntityDamageByEntityEvent) {
            int enderPearls = Utils.rand(0, 3); // drops 0-2 enderpearls
            for (int i = 0; i < enderPearls; i++) {
                drops.add(Item.get(Item.ENDER_PEARL, 0, 1));
            }
        }
        return drops.toArray(new Item[drops.size()]);
    }

    @Override
    public int getKillExperience() {
        return 5; // gain 5 experience
    }
    
    public void playerLooksAt (Player player) {
        // if the player wears a pumpkin, the enderman doesn't attack the player
        //if (player.getInventory().getHelmet() instanceof BlockPumpkin) {
        //    this.setTarget(player);
        //}
    }

}
