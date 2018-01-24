package com.pikycz.novamobsx.mob.animal.walking;

import cn.nukkit.entity.Entity;
import cn.nukkit.entity.EntityRideable;
import cn.nukkit.event.entity.EntityDamageByEntityEvent;
import cn.nukkit.item.Item;
import cn.nukkit.level.format.FullChunk;
import cn.nukkit.nbt.tag.CompoundTag;
import com.pikycz.novamobsx.mob.ai.CreatureFleeAI;
import com.pikycz.novamobsx.mob.ai.CreatureWanderAI;
import com.pikycz.novamobsx.mob.ai.FollowItemAI;
import com.pikycz.novamobsx.mob.ai.MobAIUnion;
import com.pikycz.novamobsx.mob.animal.WalkingAnimal;
import com.pikycz.novamobsx.utils.Utils;
import java.util.ArrayList;
import java.util.List;

public class Pig extends WalkingAnimal implements EntityRideable {

    public static final int NETWORK_ID = 12;

    public Pig(FullChunk chunk, CompoundTag nbt) {
        super(chunk, nbt);
        setAI(new MobAIUnion(new CreatureFleeAI(this, 32, 32, 32), new FollowItemAI(this, Item.CARROT + Item.POTATO + Item.BEETROOT, 49, 32), new CreatureWanderAI(this)));
    }

    @Override
    public int getNetworkId() {
        return NETWORK_ID;
    }

    @Override
    public String getName() {
        return "Pig";
    }

    @Override
    public float getWidth() {
        if (this.isBaby()) {
            return 0.45f;
        }
        return 0.9f;
    }

    @Override
    public float getHeight() {
        if (this.isBaby()) {
            return 0.45f;
        }
        return 0.9f;
    }

    @Override
    public float getEyeHeight() {
        if (this.isBaby()) {
            return 0.45f;
        }
        return 0.9f;
    }

    @Override
    public boolean isBaby() {
        return this.getDataFlag(DATA_FLAGS, Entity.DATA_FLAG_BABY);
    }

    @Override
    public void initEntity() {
        super.initEntity();
        this.setMaxHealth(10);
    }

    @Override
    public Item[] getDrops() {
        List<Item> drops = new ArrayList<>();
        if (this.lastDamageCause instanceof EntityDamageByEntityEvent) {
            int drop = Utils.rand(1, 4); // drops 1-3 raw porkchop / cooked porkchop when on fire
            for (int i = 0; i < drop; i++) {
                drops.add(Item.get(this.isOnFire() ? Item.COOKED_PORKCHOP : Item.RAW_PORKCHOP, 0, 1));
            }
        }
        return drops.toArray(new Item[drops.size()]);
    }

    @Override
    public int getKillExperience() {
        return Utils.rand(1, 4); // gain 1-3 experience
    }

    @Override
    public boolean mountEntity(Entity entity) {
        return false;
    }

}
