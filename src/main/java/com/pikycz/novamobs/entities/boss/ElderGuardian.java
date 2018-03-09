package com.pikycz.novamobs.entities.boss;

import cn.nukkit.Player;
import cn.nukkit.entity.Entity;
import cn.nukkit.entity.data.EntityMetadata;
import cn.nukkit.event.entity.EntityDamageByEntityEvent;
import cn.nukkit.event.entity.EntityDamageEvent;
import cn.nukkit.item.Item;
import cn.nukkit.level.format.FullChunk;
import cn.nukkit.nbt.tag.CompoundTag;
import cn.nukkit.network.protocol.AddEntityPacket;
import com.pikycz.novamobs.entities.monster.WaterMonster;
import com.pikycz.novamobs.utils.Utils;
import java.util.ArrayList;
import java.util.List;

/**
 * @author PikyCZ
 */
public class ElderGuardian extends WaterMonster {

    public static final int NETWORK_ID = 50;

    public ElderGuardian(FullChunk chunk, CompoundTag nbt) {
        super(chunk, nbt);
    }

    @Override
    protected void initEntity() {
        super.initEntity();
        this.setMaxHealth(80);

        this.setDamage(new int[]{0, 2, 3, 4});
    }

    @Override
    public String getName() {
        return "ElderGuardian";
    }

    @Override
    public float getWidth() {
        return 1.9975f;
    }

    @Override
    public float getHeight() {
        return 1.9975f;
    }

    @Override
    public int getKillExperience() {
        return 10;
    }

    @Override
    public int getNetworkId() {
        return NETWORK_ID;
    }

    @Override
    public void attackEntity(Entity player) {
        if (this.attackDelay > 10 && player.distanceSquared(this) <= 1) {
            this.attackDelay = 0;
            player.attack(new EntityDamageByEntityEvent(this, player, EntityDamageEvent.DamageCause.ENTITY_ATTACK, getDamage()));
        }
    }

    @Override
    public Item[] getDrops() {
        List<Item> drops = new ArrayList<>();
        if (this.lastDamageCause instanceof EntityDamageByEntityEvent) {
            int PRISMARINE_SHARD = Utils.rand(0, 3);
            int wetsponge = Utils.rand(0, 2);
            for (int i = 0; i < PRISMARINE_SHARD; i++) {
                drops.add(Item.get(Item.PRISMARINE_SHARD, 0, 1));
            }
            for (int i = 0; i < wetsponge; i++) {
                drops.add(Item.get(Item.SPONGE, 0, 1));
            }
        }
        return drops.toArray(new Item[drops.size()]);
    }

    @Override
    public void spawnTo(Player player) {
        AddEntityPacket pk = new AddEntityPacket();
        pk.type = ElderGuardian.NETWORK_ID;
        pk.entityUniqueId = this.getId();
        pk.entityRuntimeId = this.getId();
        pk.x = (float) this.x;
        pk.y = (float) this.y;
        pk.z = (float) this.z;
        pk.speedX = (float) this.motionX;
        pk.speedY = (float) this.motionY;
        pk.speedZ = (float) this.motionZ;
        pk.metadata = new EntityMetadata() {
            {
                this.putLong(Entity.DATA_FLAGS, Entity.DATA_FLAG_ELDER);
            }
        };
        player.dataPacket(pk);

        super.spawnTo(player);
    }

}
