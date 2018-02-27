package com.pikycz.novamobs.entities.animal.walking;

import cn.nukkit.Player;
import cn.nukkit.entity.Entity;
import cn.nukkit.entity.EntityCreature;
import cn.nukkit.entity.data.ByteEntityData;
import cn.nukkit.event.entity.EntityDamageByEntityEvent;
import cn.nukkit.item.Item;
import cn.nukkit.level.format.FullChunk;
import cn.nukkit.nbt.tag.CompoundTag;
import cn.nukkit.utils.DyeColor;
import com.pikycz.novamobs.entities.animal.WalkingAnimal;
import com.pikycz.novamobs.utils.Utils;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class Sheep extends WalkingAnimal {

    public static final int NETWORK_ID = 13;

    public static final int WHITE = 0;
    public static final int ORANGE = 1;
    public static final int MAGENTA = 2;
    public static final int LIGHT_BLUE = 3;
    public static final int YELLOW = 4;
    public static final int LIME = 5;
    public static final int PINK = 6;
    public static final int GRAY = 7;
    public static final int LIGHT_GRAY = 8;
    public static final int CYAN = 9;
    public static final int PURPLE = 10;
    public static final int BLUE = 11;
    public static final int BROWN = 12;
    public static final int GREEN = 13;
    public static final int RED = 14;
    public static final int BLACK = 15;

    public boolean sheared = false;
    public int color = 0;

    public Sheep(FullChunk chunk, CompoundTag nbt) {
        super(chunk, nbt);
    }

    @Override
    public int getNetworkId() {
        return NETWORK_ID;
    }

    @Override
    public String getName() {
        return "Sheep";
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
        if (isBaby()) {
            return 0.65f;
        }
        return 1.3f;
    }

    @Override
    public float getEyeHeight() {
        if (isBaby()) {
            return 0.65f;
        }
        return 1.1f;
    }

    @Override
    public boolean isBaby() {
        return this.getDataFlag(DATA_FLAGS, Entity.DATA_FLAG_BABY);
    }

    @Override
    public void initEntity() {
        this.setMaxHealth(8);
        if (!this.namedTag.contains("Color")) {
            this.setColor(this.randomColor());
        } else {
            this.setColor(this.namedTag.getByte("Color"));
        }

        if (!this.namedTag.contains("Sheared")) {
            this.namedTag.putByte("Sheared", 0);
        } else {
            this.sheared = this.namedTag.getBoolean("Sheared");
        }

        this.setDataFlag(0, 26, this.sheared);
    }

    @Override
    public void saveNBT() {
        super.saveNBT();
        this.namedTag.putByte("Color", this.color);
        this.namedTag.putBoolean("Sheared", this.sheared);
    }

    @Override
    public boolean targetOption(EntityCreature creature, double distance) {
        if (creature instanceof Player) {
            Player player = (Player) creature;
            return player.spawned && player.isAlive() && !player.closed && player.getInventory().getItemInHand().getId() == Item.WHEAT && distance <= 49;
        }
        return creature.isAlive() && creature.closed && distance <= 50;
    }

    @Override
    public Item[] getDrops() {
        List<Item> drops = new ArrayList<>();
        if (this.lastDamageCause instanceof EntityDamageByEntityEvent) {
            drops.add(Item.get(Item.WOOL, 0, 1)); // each time drops 1 wool
            int muttonDrop = Utils.rand(1, 3); // drops 1-2 muttons / cooked muttons
            for (int i = 0; i < muttonDrop; i++) {
                drops.add(Item.get(this.isOnFire() ? Item.COOKED_MUTTON : Item.RAW_MUTTON, 0, 1));
            }
        }
        return drops.toArray(new Item[drops.size()]);
    }

    public void setColor(int color) {
        this.color = color;
        this.setDataProperty(new ByteEntityData(DATA_COLOUR, color));
    }

    public int getColor() {
        return namedTag.getByte("Color");
    }

    private int randomColor() {
        ThreadLocalRandom random = ThreadLocalRandom.current();
        double rand = random.nextDouble(1, 100);

        if (rand <= 15) {
            return random.nextBoolean() ? DyeColor.BLACK.getDyeData() : random.nextBoolean() ? DyeColor.GRAY.getDyeData() : DyeColor.LIGHT_GRAY.getDyeData();
        }

        return DyeColor.WHITE.getDyeData();
    }

    @Override
    public int getKillExperience() {
        return Utils.rand(1, 4); // gain 1-3 experience
    }

}
