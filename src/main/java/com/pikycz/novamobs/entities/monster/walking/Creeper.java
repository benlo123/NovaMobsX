package com.pikycz.novamobs.entities.monster.walking;

import cn.nukkit.entity.Entity;
import cn.nukkit.entity.EntityCreature;
import cn.nukkit.entity.EntityExplosive;
import cn.nukkit.entity.data.ByteEntityData;
import cn.nukkit.event.entity.EntityDamageByEntityEvent;
import cn.nukkit.event.entity.ExplosionPrimeEvent;
import cn.nukkit.item.Item;
import cn.nukkit.item.ItemSkull;
import cn.nukkit.level.Explosion;
import cn.nukkit.level.Sound;
import cn.nukkit.level.format.FullChunk;
import cn.nukkit.math.Vector3;
import cn.nukkit.nbt.tag.CompoundTag;
import com.pikycz.novamobs.NovaMobsX;
import com.pikycz.novamobs.entities.monster.WalkingMonster;
import com.pikycz.novamobs.utils.Utils;
import java.util.ArrayList;
import java.util.List;

public class Creeper extends WalkingMonster implements EntityExplosive {

    NovaMobsX plugin;

    public static final int NETWORK_ID = 33;

    public static int EXPLOSION_TIME = 48;
    public static float EXPLOSION_TARGET_DISTANCE_SQUARED = 6f * 6f;
    public static final int DATA_POWERED = 19;
    public static float BLAST_FORCE = 2.8f;

    private int bombTime = 0;

    public Creeper(FullChunk chunk, CompoundTag nbt) {
        super(chunk, nbt);
    }

    @Override
    public int getNetworkId() {
        return NETWORK_ID;
    }

    public double getAttackDistance() {
        return 2;
    }

    @Override
    public String getName() {
        return "Creeper";
    }

    @Override
    public float getWidth() {
        return 0.72f;
    }

    @Override
    public float getHeight() {
        return 1.8f;
    }

    @Override
    public void initEntity() {
        super.initEntity();

        if (this.namedTag.getBoolean("powered") || this.namedTag.getBoolean("IsPowered")) {
            this.dataProperties.putBoolean(DATA_POWERED, true);
        }
    }

    public boolean isPowered() {
        return this.getDataPropertyBoolean(DATA_POWERED);
    }

    public void setPowered() {
        this.namedTag.putBoolean("powered", true);
        this.setDataProperty(new ByteEntityData(DATA_POWERED, 1));
    }

    public void setPowered(boolean powered) {
        this.namedTag.putBoolean("powered", powered);
        this.setDataProperty(new ByteEntityData(DATA_POWERED, powered ? 1 : 0));
    }

    public int getBombTime() {
        return this.bombTime;
    }

    public void explode() {
        ExplosionPrimeEvent ev = new ExplosionPrimeEvent(this, BLAST_FORCE);
        this.server.getPluginManager().callEvent(ev);
        if (!ev.isCancelled()) {
            Explosion explosion = new Explosion(this, (float) ev.getForce(), this);
            if (ev.isBlockBreaking()) {
                explosion.explode();
            } else {
                explosion.explodeB();
            }
        }
        this.close();
    }

    @Override
    public boolean onUpdate(int currentTick) {
        if (super.onUpdate(currentTick)) {
            Vector3 target = getTarget();
            if (target != null && target instanceof EntityCreature && target.distanceSquared(this) < EXPLOSION_TARGET_DISTANCE_SQUARED) {
                if (bombTime++ >= EXPLOSION_TIME) {
                    this.explode();
                    return false;
                }
            } else if (bombTime > 0) {
                bombTime = Math.max(0, bombTime - 1);
                this.level.addSound(this, Sound.RANDOM_EXPLODE);
            }
            return true;
        } else {
            return false;
        }
    }

    @Override
    public Item[] getDrops() {
        List<Item> drops = new ArrayList<>();
        if (this.lastDamageCause instanceof EntityDamageByEntityEvent) {
            int bones = Utils.rand(0, 2); // drops 0-1 flint
            int arrows = Utils.rand(0, 2); // drops 0- gunpowder
            int bow = Utils.rand(0, 101) <= 9 ? 1 : 0; // with a 8,5% chance to RedstonDust
            int skull = Utils.rand(0, 101) <= 9 ? 1 : 0; // with a 8,5% chance to Skull is dropped
            for (int i = 0; i < bones; i++) {
                drops.add(Item.get(Item.FLINT, 0, 1));
            }
            for (int i = 0; i < arrows; i++) {
                drops.add(Item.get(Item.GUNPOWDER, 0, 1));
            }
            for (int i = 0; i < bow; i++) {
                drops.add(Item.get(Item.REDSTONE_DUST, 0, 1));
            }
            for (int i = 0; i < skull; i++) {
                drops.add(Item.get(ItemSkull.CREEPER_HEAD, 0, 1));
            }
        }
        return drops.toArray(new Item[drops.size()]);
    }

    @Override
    public int getKillExperience() {
        return 5; // gain 5 experience
    }

    @Override
    public void attackEntity(Entity player) {
    }

}
