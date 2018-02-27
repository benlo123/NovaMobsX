package com.pikycz.novamobs.entities.monster.walking;

import cn.nukkit.Player;
import cn.nukkit.entity.Entity;
import cn.nukkit.entity.projectile.EntityArrow;
import cn.nukkit.entity.projectile.EntityProjectile;
import cn.nukkit.event.entity.EntityDamageByEntityEvent;
import cn.nukkit.event.entity.EntityShootBowEvent;
import cn.nukkit.event.entity.ProjectileLaunchEvent;
import cn.nukkit.item.Item;
import cn.nukkit.item.ItemBow;
import cn.nukkit.level.Level;
import cn.nukkit.level.Location;
import cn.nukkit.level.Sound;
import cn.nukkit.level.format.FullChunk;
import cn.nukkit.math.Vector3;
import cn.nukkit.nbt.tag.CompoundTag;
import cn.nukkit.network.protocol.MobEquipmentPacket;
import co.aikar.timings.Timings;
import com.pikycz.novamobs.NovaMobsX;
import com.pikycz.novamobs.entities.monster.WalkingMonster;
import com.pikycz.novamobs.utils.Utils;
import java.util.ArrayList;
import java.util.List;

/**
 * @author PikyCZ
 */
public class Stray extends WalkingMonster {

    public static final int NETWORK_ID = 46;

    public Stray(FullChunk chunk, CompoundTag nbt) {
        super(chunk, nbt);
    }

    @Override
    public void initEntity() {
        super.initEntity();

        this.setMaxHealth(20);
    }

    @Override
    public int getNetworkId() {
        return NETWORK_ID;
    }

    @Override
    public String getName() {
        return "Stray";
    }

    @Override
    public float getWidth() {
        return 0.65f;
    }

    @Override
    public float getHeight() {
        return 1.8f;
    }

    /**
     * @param player
     */
    @Override
    public void attackEntity(Entity player) {
        if (this.attackDelay > 30 && Utils.rand(1, 32) < 4 && this.distanceSquared(player) <= 55) {
            this.attackDelay = 0;

            double f = 1.2;
            double yaw = this.yaw + Utils.rand(-220, 220) / 10;
            double pitch = this.pitch + Utils.rand(-120, 120) / 10;
            Location pos = new Location(this.x - Math.sin(Math.toRadians(yaw)) * Math.cos(Math.toRadians(pitch)) * 0.5, this.y + this.getHeight() - 0.18,
                    this.z + Math.cos(Math.toRadians(yaw)) * Math.cos(Math.toRadians(pitch)) * 0.5, yaw, pitch, this.level);
            Entity k = NovaMobsX.create("Arrow", pos, this, this.chunk);
            if (!(k instanceof EntityArrow)) {
                return;
            }

            EntityArrow arrow = (EntityArrow) k;
            arrow.setMotion(new Vector3(-Math.sin(Math.toRadians(yaw)) * Math.cos(Math.toRadians(pitch)) * f * f, -Math.sin(Math.toRadians(pitch)) * f * f,
                    Math.cos(Math.toRadians(yaw)) * Math.cos(Math.toRadians(pitch)) * f * f));

            EntityShootBowEvent ev = new EntityShootBowEvent(this, Item.get(Item.ARROW, 0, 1), arrow, f);
            this.server.getPluginManager().callEvent(ev);

            EntityProjectile projectile = ev.getProjectile();
            if (ev.isCancelled()) {
                projectile.kill();
            } else {
                ProjectileLaunchEvent launch = new ProjectileLaunchEvent(projectile);
                this.server.getPluginManager().callEvent(launch);
                if (launch.isCancelled()) {
                    projectile.kill();
                } else {
                    projectile.spawnToAll();
                    this.level.addSound(this, Sound.RANDOM_BOW);
                }
            }
        }
    }

    @Override
    public void spawnTo(Player player) {
        super.spawnTo(player);

        MobEquipmentPacket pk = new MobEquipmentPacket();
        pk.eid = this.getId();
        pk.item = new ItemBow();
        pk.hotbarSlot = 10;
        player.dataPacket(pk);
    }

    @Override
    public boolean entityBaseTick(int tickDiff) {
        boolean hasUpdate = false;

        Timings.entityBaseTickTimer.startTiming();

        hasUpdate = super.entityBaseTick(tickDiff);

        int time = this.getLevel().getTime() % Level.TIME_FULL;
        if (!this.isOnFire() && !this.level.isRaining() && !(time > Level.TIME_SUNSET && time >= Level.TIME_NIGHT && time < Level.TIME_SUNRISE)) {
            this.setOnFire(100);
        }

        Timings.entityBaseTickTimer.stopTiming();

        return hasUpdate;
    }

    @Override
    public Item[] getDrops() {
        List<Item> drops = new ArrayList<>();
        if (this.lastDamageCause instanceof EntityDamageByEntityEvent) {
            int bones = Utils.rand(0, 3); // drops 0-2 bones
            int arrows = Utils.rand(0, 3); // drops 0-2 arrows
            for (int i = 0; i < bones; i++) {
                drops.add(Item.get(Item.BONE, 0, 1));
            }
            for (int i = 0; i < arrows; i++) {
                drops.add(Item.get(Item.ARROW, 0, 1));
            }
        }
        return drops.toArray(new Item[drops.size()]);
    }

    @Override
    public int getKillExperience() {
        return 5; // gain 5 experience
    }

}
