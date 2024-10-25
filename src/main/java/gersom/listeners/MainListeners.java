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
import gersom.utils.Console;

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
                    checkAndNotifyBoss("skeletonEmperor", player);
                    checkAndNotifyBoss("skeletonKing", player);
                }
            }
        // (20L = 20 ticks = 1 seg)
        }.runTaskLater(plugin, 60L);
    }

    private void checkAndNotifyBoss(String bossType, Player player) {
        Entity bossEntity = null;
        
        if (bossType.equals("skeletonEmperor")) {
            if (plugin.getMainMobs().getSkeletonEmperor() != null && 
                plugin.getMainMobs().getSkeletonEmperor().getBossId() != null) {
                bossEntity = plugin.getMainMobs().getSkeletonEmperor().getEntityBoss();
            }
        } else if (bossType.equals("skeletonKing")) {
            if (plugin.getMainMobs().getSkeletonKing() != null && 
                plugin.getMainMobs().getSkeletonKing().getBossId() != null) {
                bossEntity = plugin.getMainMobs().getSkeletonKing().getEntityBoss();
            }
        }

        plugin.getMainMobs().onSuccessGenerated(bossEntity, bossType, "player", player);
    }

    // Event listener for when a mob dies
    @EventHandler
    public void onMobDeath(EntityDeathEvent event) {
        Boss boss = null;

        if (event.getEntityType() == EntityType.SKELETON) {
            if (plugin.getMainMobs().getSkeletonEmperor() != null &&
                plugin.getMainMobs().getSkeletonEmperor().getEntityBoss() != null &&
                plugin.getMainMobs().getSkeletonEmperor().getEntityBoss().getUniqueId().equals(event.getEntity().getUniqueId())) {
                boss = plugin.getMainMobs().getSkeletonEmperor();
            }
        } else if (event.getEntityType() == EntityType.WITHER_SKELETON) {
            if (plugin.getMainMobs().getSkeletonKing() != null &&
                plugin.getMainMobs().getSkeletonKing().getEntityBoss() != null &&
                plugin.getMainMobs().getSkeletonKing().getEntityBoss().getUniqueId().equals(event.getEntity().getUniqueId())) {
                boss = plugin.getMainMobs().getSkeletonKing();
            }
        }

        if (boss != null) {
            boss.handleDeath(event);
        }
    }

    // Event listener for when a mob takes damage
    @EventHandler
    public void onEntityDamage(EntityDamageByEntityEvent event) {
        Entity damagedEntity = event.getEntity();
        
        // Verifica si la entidad dañada es uno de nuestros jefes
        if (plugin.getMainMobs().getSkeletonEmperor() != null &&
            plugin.getMainMobs().getSkeletonEmperor().getBossId() != null &&
            plugin.getMainMobs().getSkeletonEmperor().getBossId().equals(damagedEntity.getUniqueId())) {
            
            // Verifica si el daño proviene de un proyectil
            if (event.getDamager() instanceof Projectile) {
                int evasionChance = plugin.getConfigs().getBossEmperorProjectileEvasion();
                if (random.nextInt(100) < evasionChance) {
                    event.setCancelled(true);  // Cancela el evento, evadiendo el daño
                }
            }

            if (plugin.getMainMobs().getSkeletonEmperor().getEntityBoss() == null) {
                if (plugin.getConfigs().getIsLogs()) {
                    Console.sendMessage("&c" + plugin.getConfigs().getPrefix() + "&c" + "onEntityDamage => .getSkeletonEmperor().getEntityBoss() is null");  
                }
            } else {
                // Handle horse respawn
                if (plugin.getMainMobs().getSkeletonEmperor().getEntityBoss().getVehicle() == null) {
                    plugin.getMainMobs().getSkeletonEmperor().generateHorse();
                }
            }

        }
        
        // Haz lo mismo para el Skeleton King
        if (plugin.getMainMobs().getSkeletonKing() != null &&
            plugin.getMainMobs().getSkeletonKing().getBossId() != null &&
            plugin.getMainMobs().getSkeletonKing().getBossId().equals(damagedEntity.getUniqueId())) {
            
            if (event.getDamager() instanceof Projectile) {
                int evasionChance = plugin.getConfigs().getBossKingProjectileEvasion();
                if (random.nextInt(100) < evasionChance) {
                    event.setCancelled(true);  // Cancela el evento, evadiendo el daño
                }
            }

            if (plugin.getMainMobs().getSkeletonKing().getEntityBoss() == null) {
                if (plugin.getConfigs().getIsLogs()) {
                    Console.sendMessage("&c" + plugin.getConfigs().getPrefix() + "&c" + "onEntityDamage => .getSkeletonKing().getEntityBoss() is null");  
                }
            } else {
                // Handle minion spawning
                plugin.getMainMobs().getSkeletonKing().onDamage(event);
                
                // Handle horse respawn
                if (plugin.getMainMobs().getSkeletonKing().getEntityBoss().getVehicle() == null) {
                    plugin.getMainMobs().getSkeletonKing().generateHorse();
                }
            }

        }
    }
}
