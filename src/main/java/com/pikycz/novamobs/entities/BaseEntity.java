package com.pikycz.novamobs.entities;

import cn.nukkit.Player;
import cn.nukkit.block.Block;
import cn.nukkit.entity.Entity;
import cn.nukkit.entity.EntityCreature;
import cn.nukkit.entity.data.ByteEntityData;
import cn.nukkit.entity.data.ShortEntityData;
import cn.nukkit.event.entity.EntityDamageByBlockEvent;
import cn.nukkit.event.entity.EntityDamageByEntityEvent;
import cn.nukkit.event.entity.EntityDamageEvent;
import cn.nukkit.event.entity.EntityDamageEvent.DamageCause;
import cn.nukkit.event.entity.EntityMotionEvent;
import cn.nukkit.level.Level;
import cn.nukkit.level.format.FullChunk;
import cn.nukkit.math.AxisAlignedBB;
import cn.nukkit.math.NukkitMath;
import cn.nukkit.math.Vector3;
import cn.nukkit.nbt.tag.CompoundTag;
import cn.nukkit.network.protocol.AddEntityPacket;
import cn.nukkit.potion.Effect;
import co.aikar.timings.Timings;
import com.pikycz.novamobs.entities.monster.Monster;
import com.pikycz.novamobs.entities.monster.flying.Blaze;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public abstract class BaseEntity extends EntityCreature {

    protected Vector3 target = null;

    protected int stayTime = 0;

    protected int moveTime = 0;

    private float maxJumpHeight = 1.2f; // default: 1 block jump height - this should be 2 for horses e.g.

    public float speed = 1.0f;

    public double moveMultifier = 1.0d;

    public abstract Vector3 updateMove(int tickDiff);

    protected Entity followTarget;

    public boolean inWater = false;

    public boolean inLava = false;

    public boolean onClimbable = false;

    protected boolean fireProof = false;

    private boolean friendly = false;

    private boolean movement = true;

    private boolean wallcheck = true;

    EntityDamageEvent source;

    protected List<Block> blocksAround = new ArrayList<>();

    protected List<Block> collisionBlocks = new ArrayList<>();

    public BaseEntity(FullChunk chunk, CompoundTag nbt) {
        super(chunk, nbt);
    }

    public abstract int getKillExperience();

    public boolean isFriendly() {
        return this.friendly;
    }

    public boolean isMovement() {
        return this.movement;
    }

    public boolean isKnockback() {
        return this.attackTime > 0;
    }

    public boolean isWallCheck() {
        return this.wallcheck;
    }

    public void setFriendly(boolean bool) {
        this.friendly = bool;
    }

    public void setMovement(boolean value) {
        this.movement = value;
    }

    public void setWallCheck(boolean value) {
        this.wallcheck = value;
    }

    public double getSpeed() {
        return this.speed;
    }

    public float getMaxJumpHeight() {
        return this.maxJumpHeight;
    }

    public Entity getTarget() {
        return this.followTarget != null ? this.followTarget : (this.target instanceof Entity ? (Entity) this.target : null);
    }

    public void setTarget(Entity target) {
        this.followTarget = target;

        this.moveTime = 0;
        this.stayTime = 0;
        this.target = null;
    }

    @Override
    protected void initEntity() {
        super.initEntity();

        if (this.namedTag.contains("Movement")) {
            this.setMovement(this.namedTag.getBoolean("Movement"));
        }

        if (this.namedTag.contains("WallCheck")) {
            this.setWallCheck(this.namedTag.getBoolean("WallCheck"));
        }

        this.setDataProperty(new ByteEntityData(DATA_FLAG_NO_AI, (byte) 1));
    }

    @Override
    public void saveNBT() {
        super.saveNBT();
        this.namedTag.putBoolean("Movement", this.isMovement());
        this.namedTag.putBoolean("WallCheck", this.isWallCheck());
    }

    @Override
    public void spawnTo(Player player) {
        if (!this.hasSpawned.containsKey(player.getLoaderId()) && player.usedChunks.containsKey(Level.chunkHash(this.chunk.getX(), this.chunk.getZ()))) {
            AddEntityPacket pk = new AddEntityPacket();
            pk.entityRuntimeId = this.getId();
            pk.entityUniqueId = this.getId();
            pk.type = this.getNetworkId();
            pk.x = (float) this.x;
            pk.y = (float) this.y;
            pk.z = (float) this.z;
            pk.speedX = pk.speedY = pk.speedZ = 0;
            pk.yaw = (float) this.yaw;
            pk.pitch = (float) this.pitch;
            pk.metadata = this.dataProperties;
            player.dataPacket(pk);

            this.hasSpawned.put(player.getLoaderId(), player);
        }
    }

    @Override
    protected void updateMovement() {
        if (this.lastX != this.x || this.lastY != this.y || this.lastZ != this.z || this.lastYaw != this.yaw || this.lastPitch != this.pitch) {
            this.lastX = this.x;
            this.lastY = this.y;
            this.lastZ = this.z;
            this.lastYaw = this.yaw;
            this.lastPitch = this.pitch;

            this.addMovement(this.x, this.y, this.z, this.yaw, this.pitch, this.yaw);
        }
    }

    public boolean targetOption(EntityCreature creature, double distance) {
        if (this instanceof Monster) {
            if (creature instanceof Player) {
                Player player = (Player) creature;
                return !player.closed && player.spawned && player.isAlive() && player.isSurvival() && distance <= 80;
            }
            return creature.isAlive() && !creature.closed && distance <= 80;
        }
        return false;
    }

    @Override
    public List<Block> getBlocksAround() {
        if (this.blocksAround == null) {
            int minX = NukkitMath.floorDouble(this.boundingBox.getMinX());
            int minY = NukkitMath.floorDouble(this.boundingBox.getMinY());
            int minZ = NukkitMath.floorDouble(this.boundingBox.getMinZ());
            int maxX = NukkitMath.ceilDouble(this.boundingBox.getMaxX());
            int maxY = NukkitMath.ceilDouble(this.boundingBox.getMaxY());
            int maxZ = NukkitMath.ceilDouble(this.boundingBox.getMaxZ());

            this.blocksAround = new ArrayList<>();

            for (int z = minZ; z <= maxZ; ++z) {
                for (int x = minX; x <= maxX; ++x) {
                    for (int y = minY; y <= maxY; ++y) {
                        Block block = this.level.getBlock(this.temporalVector.setComponents(x, y, z));
                        this.blocksAround.add(block);
                    }
                }
            }
        }

        return this.blocksAround;
    }

    @Override
    protected void checkBlockCollision() {
        Vector3 vector = new Vector3(0.0D, 0.0D, 0.0D);
        Iterator d = this.getBlocksAround().iterator();

        inWater = false;
        inLava = false;
        onClimbable = false;

        while (d.hasNext()) {
            Block block = (Block) d.next();

            if (block.hasEntityCollision()) {
                block.onEntityCollide(this);
                block.addVelocityToEntity(this, vector);
            }

            if (block.getId() == Block.WATER || block.getId() == Block.STILL_WATER) {
                inWater = true;
            } else if (block.getId() == Block.LAVA || block.getId() == Block.STILL_LAVA) {
                inLava = true;
            } else if (block.getId() == Block.LADDER || block.getId() == Block.VINE) {
                onClimbable = true;
            }
        }

        if (vector.lengthSquared() > 0.0D) {
            vector = vector.normalize();
            double d1 = 0.014D;
            this.motionX += vector.x * d1;
            this.motionY += vector.y * d1;
            this.motionZ += vector.z * d1;
        }
    }

    @Override
    public boolean entityBaseTick(int tickDiff) {

        Timings.entityMoveTimer.startTiming();

        this.setDataFlag(DATA_FLAGS, DATA_FLAG_BREATHING, !this.isInsideOfWater());

        boolean hasUpdate = super.entityBaseTick(tickDiff);

        this.blocksAround = null;
        this.justCreated = false;

        if (!this.effects.isEmpty()) {
            this.effects.values().stream().map((effect) -> {
                if (effect.canTick()) {
                    effect.applyEffect(this);
                }
                return effect;
            }).map((effect) -> {
                effect.setDuration(effect.getDuration() - tickDiff);
                return effect;
            }).filter((effect) -> (effect.getDuration() <= 0)).forEachOrdered((effect) -> {
                this.removeEffect(effect.getId());
            });
        }

        this.checkBlockCollision();

        if (this.isInsideOfSolid()) {
            hasUpdate = true;
            this.attack(new EntityDamageEvent(this, DamageCause.SUFFOCATION, 1));
        }

        if (this.y <= -16 && this.isAlive()) {
            hasUpdate = true;
            this.attack(new EntityDamageEvent(this, DamageCause.VOID, 10));
        }

        if (this.y < 10) {
            this.close();
        }

        if (((EntityDamageByBlockEvent) source).equals(Block.CACTUS)) {
            this.attack(new EntityDamageEvent(this, DamageCause.CONTACT, 1));
        }
        if (((EntityDamageByBlockEvent) source).equals(Block.LAVA)) {
            this.attack(new EntityDamageEvent(this, DamageCause.CONTACT, 1));
        }

        if (!this.hasEffect(Effect.WATER_BREATHING) && this.isInsideOfWater()) {
            if (this instanceof WaterEntity) {
                this.setDataProperty(new ShortEntityData(DATA_AIR, 400));
            } else {
                hasUpdate = true;
                int airTicks = this.getDataPropertyShort(DATA_AIR) - tickDiff;

                if (airTicks <= -20) {
                    airTicks = 0;
                    this.attack(new EntityDamageEvent(this, DamageCause.DROWNING, 2));
                }

                this.setDataProperty(new ShortEntityData(DATA_AIR, airTicks));
            }
        } else {
            if (this instanceof WaterEntity) {
                hasUpdate = true;
                int airTicks = this.getDataPropertyInt(DATA_AIR) - tickDiff;

                if (airTicks <= -20) {
                    airTicks = 0;
                    this.attack(new EntityDamageEvent(this, DamageCause.SUFFOCATION, 2));
                }

                this.setDataProperty(new ShortEntityData(DATA_AIR, airTicks));
            } else {
                this.setDataProperty(new ShortEntityData(DATA_AIR, 400));
            }
        }

        if (this.fireTicks > 0) {
            if (this.fireProof) {
                this.fireTicks -= 4 * tickDiff;
            } else {
                if (!this.hasEffect(Effect.FIRE_RESISTANCE) && (this.fireTicks % 20) == 0 || tickDiff > 20) {
                    EntityDamageEvent ev = new EntityDamageEvent(this, DamageCause.FIRE_TICK, 1);
                    this.attack(ev);
                }
                this.fireTicks -= tickDiff;
            }

            if (this.fireTicks <= 0) {
                this.extinguish();
            } else {
                this.setDataFlag(DATA_FLAGS, DATA_FLAG_ONFIRE, true);
                hasUpdate = true;
            }
        }

        if (this.moveTime > 0) {
            this.moveTime -= tickDiff;
        }

        if (this.attackTime > 0) {
            this.attackTime -= tickDiff;
        }

        if (this.noDamageTicks > 0) {
            this.noDamageTicks -= tickDiff;
            if (this.noDamageTicks < 0) {
                this.noDamageTicks = 0;
            }
        }

        this.age += tickDiff;
        this.ticksLived += tickDiff;

        Timings.entityMoveTimer.stopTiming();

        return hasUpdate;
    }

    @Override
    public boolean isInsideOfSolid() {
        Block block = this.level.getBlock(this.temporalVector.setComponents(NukkitMath.floorDouble(this.x), NukkitMath.floorDouble(this.y + this.getHeight() - 0.18f), NukkitMath.floorDouble(this.z)));
        AxisAlignedBB bb = block.getBoundingBox();
        return bb != null && block.isSolid() && !block.isTransparent() && bb.intersectsWith(this.getBoundingBox());
    }

    @Override
    public boolean attack(EntityDamageEvent source) {

        if (source instanceof EntityDamageByEntityEvent) {
            Entity attacker = ((EntityDamageByEntityEvent) source).getDamager();
            if (this.attackTime > 0 && attacker instanceof Player) {
                source.setCancelled(true);
                return false;
            }
        }

        Entity sourceOfDamage = ((EntityDamageByEntityEvent) source).getDamager();
        Vector3 motion = (new Vector3(this.x - sourceOfDamage.x, this.y - sourceOfDamage.y, this.z - sourceOfDamage.z)).normalize();
        this.motionX = motion.x * 0.19;
        this.motionZ = motion.z * 0.19;
        //TODO--

        if ((this instanceof FlyingEntity) && !(this instanceof Blaze)) {
            this.motionY = motion.y * 0.19;
        } else {
            this.motionY = 0.6;
        }

        super.attack(source);

        this.stayTime = 0;
        this.moveTime = 0;
        this.target = null;
        this.attackTime = 7;
        return true;
    }

    public List<Block> getCollisionBlocks() {
        return collisionBlocks;
    }

    public int getMaxFallHeight() {
        if (!(this.target instanceof Entity)) {
            return 3;
        } else {
            int i = (int) (this.getHealth() - this.getMaxHealth() * 0.33F);
            i = i - (3 - this.getServer().getDifficulty()) * 4;

            if (i < 0) {
                i = 0;
            }

            return i + 3;
        }
    }

    @Override
    public boolean setMotion(Vector3 motion) {
        if (!this.justCreated) {
            EntityMotionEvent ev = new EntityMotionEvent(this, motion);
            this.server.getPluginManager().callEvent(ev);
            if (ev.isCancelled()) {
                return false;
            }
        }

        this.motionX = motion.x;
        this.motionY = motion.y;
        this.motionZ = motion.z;
        return true;
    }

    @Override
    public boolean move(double dx, double dy, double dz) {
        Timings.entityMoveTimer.startTiming();

        double movX = dx * moveMultifier;
        double movY = dy;
        double movZ = dz * moveMultifier;

        AxisAlignedBB[] list = this.level.getCollisionCubes(this, this.level.getTickRate() > 1 ? this.boundingBox.getOffsetBoundingBox(dx, dy, dz) : this.boundingBox.addCoord(dx, dy, dz));
        if (this.isWallCheck()) {
            for (AxisAlignedBB bb : list) {
                dx = bb.calculateXOffset(this.boundingBox, dx);
            }
            this.boundingBox.offset(dx, 0, 0);

            for (AxisAlignedBB bb : list) {
                dz = bb.calculateZOffset(this.boundingBox, dz);
            }
            this.boundingBox.offset(0, 0, dz);
        }
        for (AxisAlignedBB bb : list) {
            dy = bb.calculateYOffset(this.boundingBox, dy);
        }
        this.boundingBox.offset(0, dy, 0);

        this.setComponents(this.x + dx, this.y + dy, this.z + dz);
        this.checkChunks();

        this.checkGroundState(movX, movY, movZ, dx, dy, dz);
        this.updateFallState(this.onGround);

        Timings.entityMoveTimer.stopTiming();
        return true;
    }

}
