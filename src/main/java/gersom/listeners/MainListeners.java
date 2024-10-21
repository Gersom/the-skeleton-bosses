/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package gersom.listeners;

import java.util.Random;

import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.scheduler.BukkitRunnable;

import gersom.TSB;
import gersom.bosses.Boss;

/**
 *
 * @author Gersom
 */
public class MainListeners implements Listener {

    private final TSB plugin;
    private final Random random = new Random();

    public MainListeners(TSB plugin) {
        this.plugin = plugin;
    }

    // Event listener for when a player joins the server
    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        new BukkitRunnable() {
            @Override
            public void run() {
                Player player = event.getPlayer();
                if (player.isOnline()) {
                    checkAndNotifyBoss("skeletonEmperor");
                    checkAndNotifyBoss("skeletonKing");
                }
            }
        // (20L = 20 ticks = 1 seg)
        }.runTaskLater(plugin, 60L);
    }

    private void checkAndNotifyBoss(String bossType) {
        Entity bossEntity = null;
        
        if (bossType.equals("skeletonEmperor")) {
            if (plugin.getMainMobs().getSkeletonEmperor() != null && 
                plugin.getMainMobs().getSkeletonEmperor().getSkeletonEmperorID() != null) {
                bossEntity = plugin.getMainMobs().getSkeletonEmperor().getSkeletonEmperorEntity();
            }
        } else if (bossType.equals("skeletonKing")) {
            if (plugin.getMainMobs().getSkeletonKing() != null && 
                plugin.getMainMobs().getSkeletonKing().getSkeletonKingID() != null) {
                bossEntity = plugin.getMainMobs().getSkeletonKing().getSkeletonKingEntity();
            }
        }

        plugin.getMainMobs().onSuccessGenerated(bossEntity, bossType, "player");
    }

    // Event listener for when a mob dies
    @EventHandler
    public void onMobDeath(EntityDeathEvent event) {
        Boss boss = null;

        if (event.getEntityType() == EntityType.SKELETON) {
            if (plugin.getMainMobs().getSkeletonEmperor() != null &&
                plugin.getMainMobs().getSkeletonEmperor().getEntity() != null &&
                plugin.getMainMobs().getSkeletonEmperor().getEntity().getUniqueId().equals(event.getEntity().getUniqueId())) {
                boss = plugin.getMainMobs().getSkeletonEmperor();
            }
        } else if (event.getEntityType() == EntityType.WITHER_SKELETON) {
            if (plugin.getMainMobs().getSkeletonKing() != null &&
                plugin.getMainMobs().getSkeletonKing().getEntity() != null &&
                plugin.getMainMobs().getSkeletonKing().getEntity().getUniqueId().equals(event.getEntity().getUniqueId())) {
                boss = plugin.getMainMobs().getSkeletonKing();
            }
        }

        if (boss != null) {
            boss.handleDeath(event);
        }
    }

    // Event listener for when a mob takes damage
    @EventHandler
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
        Entity damagedEntity = event.getEntity();
        
        // Verifica si la entidad dañada es uno de nuestros jefes
        if (plugin.getMainMobs().getSkeletonEmperor() != null &&
            plugin.getMainMobs().getSkeletonEmperor().getSkeletonEmperorID() != null &&
            plugin.getMainMobs().getSkeletonEmperor().getSkeletonEmperorID().equals(damagedEntity.getUniqueId())) {
            
            // Verifica si el daño proviene de un proyectil
            if (event.getDamager() instanceof Projectile) {
                int evasionChance = plugin.getConfigs().getBossEmperorProjectileEvasion();
                if (random.nextInt(100) < evasionChance) {
                    event.setCancelled(true);  // Cancela el evento, evadiendo el daño
                }
            }
        }
        
        // Haz lo mismo para el Skeleton King
        if (plugin.getMainMobs().getSkeletonKing() != null &&
            plugin.getMainMobs().getSkeletonKing().getSkeletonKingID() != null &&
            plugin.getMainMobs().getSkeletonKing().getSkeletonKingID().equals(damagedEntity.getUniqueId())) {
            
            if (event.getDamager() instanceof Projectile) {
                int evasionChance = plugin.getConfigs().getBossKingProjectileEvasion();
                if (random.nextInt(100) < evasionChance) {
                    event.setCancelled(true);  // Cancela el evento, evadiendo el daño
                }
            }
        }
    }
}
