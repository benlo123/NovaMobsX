package com.pikycz.novamobsx.mob.animal.walking;

import cn.nukkit.entity.Entity;
import static cn.nukkit.entity.Entity.DATA_FLAGS;
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

/**
 * @author Jaroslav Pikart
 */
public class Chicken extends WalkingAnimal {

    public static final int NETWORK_ID = 10;

    public Chicken(FullChunk chunk, CompoundTag nbt) {
        super(chunk, nbt);
        setAI(new MobAIUnion(new CreatureFleeAI(this, 32, 32, 32), new FollowItemAI(this, Item.SEEDS, 49, 32), new CreatureWanderAI(this)));
    }

    @Override
    public int getNetworkId() {
        return NETWORK_ID;
    }

    @Override
    public String getName() {
        return "Chicken";
    }

    @Override
    public float getWidth() {
        return 0.4f;
    }

    @Override
    public float getHeight() {
        if (this.isBaby()) {
            return 0.51f;
        }
        return 0.7f;
    }

    @Override
    public float getEyeHeight() {
        if (this.isBaby()) {
            return 0.51f;
        }
        return 0.7f;
    }

    @Override
    public float getDrag() {
        return 0.2f;
    }

    @Override
    public float getGravity() {
        return 0.04f;
    }

    @Override
    public boolean isBaby() {
        return this.getDataFlag(DATA_FLAGS, Entity.DATA_FLAG_BABY);
    }

    @Override
    public void initEntity() {
        super.initEntity();
        this.setMaxHealth(4);
    }

    @Override
    public Item[] getDrops() {
        List<Item> drops = new ArrayList<>();
        if (this.lastDamageCause instanceof EntityDamageByEntityEvent) {
            int featherDrop = Utils.rand(0, 3); // drops 0-2 feathers
            for (int i = 0; i < featherDrop; i++) {
                drops.add(Item.get(Item.FEATHER, 0, 1));
            }
            // chicken on fire: cooked chicken otherwise raw chicken
            drops.add(Item.get(this.isOnFire() ? Item.COOKED_CHICKEN : Item.RAW_CHICKEN, 0, 1));
        }
        return drops.toArray(new Item[drops.size()]);
    }

    @Override
    public int getKillExperience() {
        return Utils.rand(1, 4); // gain 1-3 experience
    }

}
