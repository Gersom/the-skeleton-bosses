/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package gersom.listeners;

import java.util.Random;

import org.bukkit.entity.Entity;
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
                    checkAndNotifyBoss(player);
                }
            }
        // (20L = 20 ticks = 1 seg)
        }.runTaskLater(plugin, 60L);
    }

    private void checkAndNotifyBoss(Player player) {
        Entity bossEntity = null;
        String bossType = null;
        
        if (plugin.getMainMobs().getSkeletonEmperor() != null && 
            plugin.getMainMobs().getSkeletonEmperor().getBossId() != null) {
            bossEntity = plugin.getMainMobs().getSkeletonEmperor().getEntityBoss();
            bossType = "skeletonEmperor";
        } else if (plugin.getMainMobs().getSkeletonKing() != null && 
            plugin.getMainMobs().getSkeletonKing().getBossId() != null) {
            bossEntity = plugin.getMainMobs().getSkeletonKing().getEntityBoss();
            bossType = "skeletonKing";
        } else if (plugin.getMainMobs().getSkeletonWinterLord() != null && 
            plugin.getMainMobs().getSkeletonWinterLord().getBossId() != null) {
            bossEntity = plugin.getMainMobs().getSkeletonWinterLord().getEntityBoss();
            bossType = "skeletonWinterLord";
        }

        plugin.getMainMobs().onSuccessGenerated(bossEntity, bossType, "player", player);
    }

    // Event listener for when a mob dies
    @EventHandler
    public void onMobDeath(EntityDeathEvent event) {
        Boss boss = null;

        switch (event.getEntityType()) {
            case SKELETON -> {
                if (plugin.getMainMobs().getSkeletonEmperor() != null &&
                        plugin.getMainMobs().getSkeletonEmperor().getEntityBoss() != null &&
                        plugin.getMainMobs().getSkeletonEmperor().getEntityBoss().getUniqueId().equals(event.getEntity().getUniqueId())) {
                    boss = plugin.getMainMobs().getSkeletonEmperor();
                }
            }
            case WITHER_SKELETON -> {
                if (plugin.getMainMobs().getSkeletonKing() != null &&
                        plugin.getMainMobs().getSkeletonKing().getEntityBoss() != null &&
                        plugin.getMainMobs().getSkeletonKing().getEntityBoss().getUniqueId().equals(event.getEntity().getUniqueId())) {
                    boss = plugin.getMainMobs().getSkeletonKing();
                }
            }
            case STRAY -> {
                if (plugin.getMainMobs().getSkeletonWinterLord() != null &&
                        plugin.getMainMobs().getSkeletonWinterLord().getEntityBoss() != null &&
                        plugin.getMainMobs().getSkeletonWinterLord().getEntityBoss().getUniqueId().equals(event.getEntity().getUniqueId())) {
                    boss = plugin.getMainMobs().getSkeletonWinterLord();
                }
            }
            default -> {
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
        
        // Skeleton Emperor ha sido dañado
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
                // Handle Sentinels spawning
                plugin.getMainMobs().getSkeletonEmperor().generateGuards(event);

                // Handle horse respawn
                if (plugin.getMainMobs().getSkeletonEmperor().getEntityBoss().getVehicle() == null) {
                    plugin.getMainMobs().getSkeletonEmperor().generateHorse();
                }
            }

        }
        
        // Skeleton King ha sido dañado
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
                // Handle Sentinels spawning
                plugin.getMainMobs().getSkeletonKing().generateSentinels(event);
                
                // Handle horse respawn
                if (plugin.getMainMobs().getSkeletonKing().getEntityBoss().getVehicle() == null) {
                    plugin.getMainMobs().getSkeletonKing().generateHorse();
                }
            }

        }

        if (plugin.getMainMobs().getSkeletonWinterLord() != null &&
            plugin.getMainMobs().getSkeletonWinterLord().getBossId() != null) {
            
            Entity damager = event.getDamager();

            // Verificar si el daño proviene de una flecha
            if (damager instanceof Projectile projectile && damagedEntity instanceof Player player) {
                Entity shooter = (Entity) projectile.getShooter();
                
                // Verificar si la flecha fue disparada por el SkeletonWinterLord
                if (shooter != null && plugin.getMainMobs().getSkeletonWinterLord().getBossId().equals(shooter.getUniqueId())) {
                    // Establecer el nivel de congelamiento del jugador (Minecraft 1.17+)
                    player.setFreezeTicks(200); // Aproximadamente 7 segundos de congelamiento
                    
                    // Programar la actualización del efecto de congelamiento
                    new BukkitRunnable() {
                        int duration = 7; // duración en segundos
                        
                        @Override
                        public void run() {
                            if (duration <= 0 || !player.isOnline()) {
                                player.setFreezeTicks(0);
                                this.cancel();
                                return;
                            }
                            
                            // Mantener el efecto de congelamiento
                            player.setFreezeTicks(200);
                            duration--;
                        }
                    }.runTaskTimer(plugin, 0L, 20L); // Ejecutar cada segundo (20 ticks)
                }
            }
        

            // Skeleton Winter Lord ha sido dañado
            if (plugin.getMainMobs().getSkeletonWinterLord().getBossId().equals(damagedEntity.getUniqueId())) {
            
                if (event.getDamager() instanceof Projectile) {
                    int evasionChance = plugin.getConfigs().getBossWinterLordProjectileEvasion();
                    if (random.nextInt(100) < evasionChance) {
                        event.setCancelled(true);  // Cancela el evento, evadiendo el daño
                    }
                }

                plugin.getMainMobs().getSkeletonWinterLord().generateMinions(event);

                if (plugin.getMainMobs().getSkeletonWinterLord().getEntityBoss() == null) {
                    if (plugin.getConfigs().getIsLogs()) {
                        Console.sendMessage("&c" + plugin.getConfigs().getPrefix() + "&c" + "onEntityDamage => .getSkeletonWinterLord().getEntityBoss() is null");  
                    }
                }
            }
        }
    }
}
