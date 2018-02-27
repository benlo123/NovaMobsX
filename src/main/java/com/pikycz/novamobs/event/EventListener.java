package com.pikycz.novamobs.event;

import cn.nukkit.Player;
import cn.nukkit.block.Block;
import cn.nukkit.entity.Entity;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.Listener;
import cn.nukkit.event.block.BlockBreakEvent;
import cn.nukkit.event.entity.EntityDamageByEntityEvent;
import cn.nukkit.event.entity.EntityDeathEvent;
import cn.nukkit.event.player.PlayerInteractEntityEvent;
import com.pikycz.novamobs.NovaMobsX;
import com.pikycz.novamobs.entities.BaseEntity;
import com.pikycz.novamobs.entities.InteractEntity;
import com.pikycz.novamobs.entities.monster.walking.Silverfish;
import com.pikycz.novamobs.utils.Utils;

public class EventListener implements Listener {

    private int counter = 0;

    @EventHandler
    public void EntityDeathEvent(EntityDeathEvent ev) {
        if (ev.getEntity() instanceof BaseEntity) {
            BaseEntity baseEntity = (BaseEntity) ev.getEntity();
            if (baseEntity.getLastDamageCause() instanceof EntityDamageByEntityEvent) {
                Entity damager = ((EntityDamageByEntityEvent) baseEntity.getLastDamageCause()).getDamager();
                if (damager instanceof Player) {
                    Player player = (Player) damager;
                    int killExperience = baseEntity.getKillExperience();
                    if (killExperience > 0 && player.isSurvival()) {
                        player.addExperience(killExperience);
                    }
                }
            }
        }
    }

    @EventHandler
    public void BlockBreakEvent(BlockBreakEvent ev) {
        if (ev.isCancelled()) {
            return;
        }

        Block block = ev.getBlock();
        if ((block.getId() == Block.MONSTER_EGG)
                && block.getLevel().getBlockLightAt((int) block.x, (int) block.y, (int) block.z) < 12 && Utils.rand(1, 5) == 1) {

            Silverfish entity = (Silverfish) NovaMobsX.create("Silverfish", block.add(0.5, 0, 0.5));
            if (entity != null) {
                entity.spawnToAll();
            }
        }
    }

    @EventHandler
    public void PlayerInteractEntityEvent(PlayerInteractEntityEvent ev) {
        if (ev.getEntity() instanceof InteractEntity) {
            InteractEntity entity = (InteractEntity) ev.getEntity();
        }
    }
}
