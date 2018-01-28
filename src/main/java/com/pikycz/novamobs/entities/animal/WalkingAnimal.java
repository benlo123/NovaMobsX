package com.pikycz.novamobs.entities.animal;

import cn.nukkit.Player;
import cn.nukkit.entity.Entity;
import cn.nukkit.entity.data.ShortEntityData;
import cn.nukkit.event.entity.EntityDamageEvent;
import cn.nukkit.event.entity.EntityDamageEvent.DamageCause;
import cn.nukkit.item.Item;
import cn.nukkit.level.format.FullChunk;
import cn.nukkit.level.particle.HeartParticle;
import cn.nukkit.math.Vector3;
import cn.nukkit.nbt.tag.CompoundTag;
import cn.nukkit.potion.Effect;

import co.aikar.timings.Timings;

import com.pikycz.novamobs.entities.WalkingEntity;

public abstract class WalkingAnimal extends WalkingEntity implements Animal {

    protected int inLoveTicks = 0;
    protected int spawnBabyDelay = 0; //TODO: spawn baby animal

    public WalkingAnimal(FullChunk chunk, CompoundTag nbt) {
        super(chunk, nbt);
    }

    @Override
    public double getSpeed() {
        return 0.8;
    }

    @Override
    protected void initEntity() {
        super.initEntity();

        if (this.getDataFlag(DATA_FLAG_BABY, 0)) {
            this.setDataFlag(DATA_FLAG_BABY, DATA_TYPE_BYTE);
        }
    }

    @Override
    public boolean isBaby() {
        return this.getDataFlag(DATA_FLAG_BABY, 0);
    }

    @Override
    public boolean entityBaseTick(int tickDiff) {
        boolean hasUpdate;
        Timings.entityBaseTickTimer.startTiming();

        hasUpdate = super.entityBaseTick(tickDiff);

        if (inLoveTicks > 0) {
            inLoveTicks--;

            if (inLoveTicks % 10 == 0) {
                Vector3 pos = new Vector3(this.x + (double) (this.level.rand.nextFloat() * this.getWidth() * 2.0F) - (double) this.getWidth(), this.y + 0.5D + (double) (this.level.rand.nextFloat() * this.getHeight()), this.z + (double) (this.level.rand.nextFloat() * this.getWidth() * 2.0F) - (double) this.getWidth());
                this.level.addParticle(new HeartParticle(pos));
            }
        }

        if (!this.hasEffect(Effect.WATER_BREATHING) && this.isInsideOfWater()) {
            hasUpdate = true;
            int airTicks = this.getDataPropertyShort(DATA_AIR) - tickDiff;
            if (airTicks <= -20) {
                airTicks = 0;
                this.attack(new EntityDamageEvent(this, DamageCause.DROWNING, 2));
            }
            this.setDataProperty(new ShortEntityData(DATA_AIR, airTicks));
        } else {
            this.setDataProperty(new ShortEntityData(DATA_AIR, 300));
        }

        Timings.entityBaseTickTimer.stopTiming();
        return hasUpdate;
    }

    @Override
    public boolean onUpdate(int currentTick) {
        if (this.closed) {
            return false;
        }

        if (!this.isAlive()) {
            if (++this.deadTicks >= 23) {
                this.close();
                return false;
            }
            return true;
        }

        int tickDiff = currentTick - this.lastUpdate;
        this.lastUpdate = currentTick;
        this.entityBaseTick(tickDiff);

        Vector3 target = this.updateMove(tickDiff);
        if (target instanceof Player) {
            if (this.distanceSquared(target) <= 2) {
                this.pitch = 22;
                this.x = this.lastX;
                this.y = this.lastY;
                this.z = this.lastZ;
            }
        } else if (target != null && (Math.pow(this.x - target.x, 2) + Math.pow(this.z - target.z, 2)) <= 1) {
            this.moveTime = 0;
        }
        return true;
    }

    public boolean onInteract(Entity entity, Item item) {
        //TODO: mating

        return false;
    }

    public void setInLove() {
        this.inLoveTicks = 600;
        this.setDataFlag(DATA_FLAGS, DATA_FLAG_INLOVE);
    }

    public boolean isBreedingItem(Item item) {
        return item != null && item.getId() == Item.WHEAT;
    }

}