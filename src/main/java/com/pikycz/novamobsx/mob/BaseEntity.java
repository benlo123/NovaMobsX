package com.pikycz.novamobsx.mob;

import cn.nukkit.Player;
import cn.nukkit.block.Block;
import cn.nukkit.block.BlockLiquid;
import cn.nukkit.entity.Entity;
import static cn.nukkit.entity.Entity.DATA_FLAGS;
import static cn.nukkit.entity.Entity.DATA_FLAG_ONFIRE;
import cn.nukkit.entity.EntityCreature;
import cn.nukkit.entity.projectile.EntityProjectile;
import cn.nukkit.event.entity.EntityDamageByEntityEvent;
import cn.nukkit.event.entity.EntityDamageEvent;
import cn.nukkit.event.entity.EntityMotionEvent;
import cn.nukkit.level.Level;
import cn.nukkit.level.format.FullChunk;
import cn.nukkit.math.AxisAlignedBB;
import cn.nukkit.math.NukkitMath;
import cn.nukkit.math.Vector2;
import cn.nukkit.math.Vector3;
import cn.nukkit.nbt.tag.CompoundTag;
import cn.nukkit.network.protocol.AddEntityPacket;
import cn.nukkit.potion.Effect;
import co.aikar.timings.Timings;
import com.pikycz.novamobsx.mob.ai.MobAI;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * @author PikyCZ
 */
public abstract class BaseEntity extends EntityCreature {

    public MobAI intelligence;

    // Target can be block or entity
    public Vector3 target;

    protected List<Block> blocksAround = new ArrayList<>();

    protected List<Block> collisionBlocks = new ArrayList<>();

    public boolean inWater = false;

    public boolean inLava = false;

    public boolean onClimbable = false;

    protected int stayTime = 0;

    protected int moveTime = 0;

    public abstract int getKillExperience();

    public BaseEntity(FullChunk chunk, CompoundTag nbt) {
        super(chunk, nbt);
    }

    public boolean hasGravity() {
        return getGravity() != 0;
    }

    public void setAI(MobAI intelligence) {
        this.intelligence = intelligence;
    }

    public MobAI getAI() {
        return intelligence;
    }

    @Override
    protected void initEntity() {
        super.initEntity();
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

    public Vector3 getTarget() {
        if (intelligence != null) {
            if (!isTargetValid(target)) {
                target = intelligence.findTarget();
            }
            return target != null ? target : this;
        }
        return null;
    }

    public boolean isTargetValid(Vector3 target) {
        return intelligence != null ? intelligence.isValidTarget(target) : false;
    }

    public void setTarget(Vector3 target) {
        this.target = target;
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
    public List<Block> getBlocksAround() {
        if (this.blocksAround == null) {
            int minX = NukkitMath.floorDouble(this.boundingBox.minX);
            int minY = NukkitMath.floorDouble(this.boundingBox.minY);
            int minZ = NukkitMath.floorDouble(this.boundingBox.minZ);
            int maxX = NukkitMath.ceilDouble(this.boundingBox.maxX);
            int maxY = NukkitMath.ceilDouble(this.boundingBox.maxY);
            int maxZ = NukkitMath.ceilDouble(this.boundingBox.maxZ);

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
    public boolean attack(EntityDamageEvent source) {
        if (source instanceof EntityDamageByEntityEvent) {
            Entity attacker = ((EntityDamageByEntityEvent) source).getDamager();
            if (this.attackTime > 0 && attacker instanceof Player) {
                source.setCancelled(true);
                return false;
            }
            if (intelligence != null) {
                if (attacker instanceof EntityProjectile && ((EntityProjectile) attacker).shootingEntity != null) {
                    attacker = ((EntityProjectile) attacker).shootingEntity;
                }
                intelligence.processAttacker(attacker);
            }
        }
        super.attack(source);
        this.target = null;
        this.attackTime = 7;
        return true;
    }

    public boolean attackTarget(EntityCreature target) {
        return false;
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

    public double getSpeed() {
        return 0.5;
    }

    protected boolean jump(double dx, double dz) {
        if (this.motionY == this.getGravity() * 2) {
            return this.level.getBlock(new Vector3(NukkitMath.floorDouble(this.x), (int) this.y, NukkitMath.floorDouble(this.z))) instanceof BlockLiquid;
        } else {
            if (this.level.getBlock(new Vector3(NukkitMath.floorDouble(this.x), (int) (this.y + 0.8), NukkitMath.floorDouble(this.z))) instanceof BlockLiquid) {
                this.motionY = this.getGravity() * 2;
                return true;
            }
        }

        if (!this.onGround) {
            return false;
        }
        if (this.motionY <= this.getGravity() * 5) {
            this.motionY = this.getGravity() * 5;
        } else {
            this.motionY += this.getGravity() * 0.25;
        }
        return true;
    }

    @Override
    public boolean onUpdate(int currentTick) {
        if (this.closed) {
            return false;
        }
        if (!this.isAlive()) {
            ++this.deadTicks;
            if (this.deadTicks >= 10) {
                this.despawnFromAll();
                if (!this.isPlayer) {
                    this.close();
                }
            }
            return this.deadTicks < 10;
        }
        int tickDiff = currentTick - this.lastUpdate;
        if (tickDiff <= 0) {
            return false;
        }
        this.lastUpdate = currentTick;
        boolean hasUpdate = this.entityBaseTick(tickDiff) | this.calculateAndPerformMove(tickDiff) != null;
        if (target != null && target instanceof EntityCreature) {
            attackTarget((EntityCreature) target);
        }
        return hasUpdate;
    }

    public Player getClosestPlayer(double requiredDistance) {
        double min = requiredDistance * requiredDistance;
        Player closest = null;
        for (Map.Entry<Long, Player> entry : level.getPlayers().entrySet()) {
            Player player = entry.getValue();
            double distance = player.distanceSquared(this);
            if (distance < min) {
                min = distance;
                closest = player;
            }
        }
        return closest;
    }

    public void defaultMovement(int tickDiff) {
        calculateAndPerformFlow(tickDiff);
        this.motionY -= this.getGravity() * tickDiff;
        this.updateMovement();
    }

    private boolean calculateAndPerformFlow(int tickDiff) {
        Block block = level.getBlock(this);
        int blockId = block.getId();
        this.move(this.motionX * tickDiff, this.motionY, this.motionZ * tickDiff);
        if (block instanceof BlockLiquid) {
            Vector3 flow = ((BlockLiquid) block).getFlowVector();
            this.motionX += flow.x * 0.05 * tickDiff;
            this.motionY += (this.getGravity() * 1.1 + flow.y * 0.05) * tickDiff;
            this.motionZ += flow.x * 0.05 * tickDiff;
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean entityBaseTick(int tickDiff) {

        Timings.entityMoveTimer.startTiming();

        boolean hasUpdate = false;

        this.blocksAround = null;
        this.justCreated = false;

        if (!this.isAlive()) {
            this.removeAllEffects();
            this.despawnFromAll();
            this.close();
            return false;
        }

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
            this.attack(new EntityDamageEvent(this, EntityDamageEvent.DamageCause.SUFFOCATION, 1));
        }

        if (this.y <= -16 && this.isAlive()) {
            hasUpdate = true;
            this.attack(new EntityDamageEvent(this, EntityDamageEvent.DamageCause.VOID, 10));
        }

        if (this.y < 10) {
            this.close();
        }

        if (this.fireTicks > 0) {
            if (this.fireProof) {
                this.fireTicks -= 4 * tickDiff;
            } else {
                if (!this.hasEffect(Effect.FIRE_RESISTANCE) && (this.fireTicks % 20) == 0 || tickDiff > 20) {
                    EntityDamageEvent ev = new EntityDamageEvent(this, EntityDamageEvent.DamageCause.FIRE_TICK, 1);
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

    public Vector3 calculateAndPerformMove(int tickDiff) {
        if (this.attackTime > 0) {
            this.defaultMovement(tickDiff);
            return null;
        }
        if (intelligence == null) {
            this.defaultMovement(tickDiff);
            return null;
        }

        if (!isTargetValid(target)) {
            target = intelligence.findTarget();
            if (target == null && !isTargetValid(target)) {
                this.defaultMovement(tickDiff);
                return null;
            }
        }
        Vector3 move = intelligence.calculateNextMove(target);
        if (move == this || move == null) {
            this.defaultMovement(tickDiff);
            return null;
        }

        double x = move.x - this.x;
        double y = move.y - this.y;
        double z = move.z - this.z;

        double diff = Math.abs(x) + Math.abs(z);
        if (hasGravity()) {
            if (move == target && (this.distance(move) <= (this.getWidth() + 0.0d) / 2 + 0.05)) {
                this.motionX = 0;
                this.motionZ = 0;
            } else {
                this.motionX = this.getSpeed() * 0.15 * (x / diff);
                this.motionZ = this.getSpeed() * 0.15 * (z / diff);
                this.yaw = Math.toDegrees(-Math.atan2(x / diff, z / diff));
                this.pitch = y == 0 ? 0 : Math.toDegrees(-Math.atan2(y, Math.sqrt(x * x + z * z)));
            }
            double dx = this.motionX * tickDiff;
            double dz = this.motionZ * tickDiff;
            boolean hasJumped;
            if (move.getFloorY() > getFloorY()) {
                hasJumped = this.jump(dx, dz);
            } else {
                hasJumped = false;
            }
            Vector2 be = new Vector2(this.x + dx, this.z + dz);
            this.move(dx, this.motionY * tickDiff, dz);
            Vector2 af = new Vector2(this.x, this.z);
            if (!hasJumped) {
                if (this.onGround) {
                    this.motionY = 0;
                } else if (this.motionY < -this.getGravity() * 4) {
                    if (!(this.level.getBlock(new Vector3(NukkitMath.floorDouble(this.x), (int) (this.y + 0.8), NukkitMath.floorDouble(this.z))) instanceof BlockLiquid)) {
                        this.motionY -= this.getGravity() * 1;
                    }
                } else {
                    this.motionY -= this.getGravity() * tickDiff;
                }
            }
        } else {
            if (move == target && (this.distance(move) <= (this.getWidth() + 0.0d) / 2 + 0.05)) {
                this.motionX = 0;
                this.motionZ = 0;
                this.motionY = 0;
            } else {
                this.motionX = this.getSpeed() * 0.15 * (x / diff);
                this.motionZ = this.getSpeed() * 0.15 * (z / diff);
                this.motionY = this.getSpeed() * 0.15 * (y / diff);
                this.yaw = Math.toDegrees(-Math.atan2(x / diff, z / diff));
                this.pitch = y == 0 ? 0 : Math.toDegrees(-Math.atan2(y, Math.sqrt(x * x + z * z)));
            }
            double dx = this.motionX * tickDiff;
            double dy = this.motionY * tickDiff;
            double dz = this.motionZ * tickDiff;
            this.move(dx, dy, dz);
        }
        calculateAndPerformFlow(tickDiff);
        this.updateMovement();
        return this.target;
    }

}
