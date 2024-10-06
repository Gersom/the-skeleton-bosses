/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package gersom.listeners;

import org.bukkit.Bukkit;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.scheduler.BukkitRunnable;

import gersom.TSB;

/**
 *
 * @author Gersom
 */
public class MainListeners implements Listener {

    private final TSB plugin;

    public MainListeners(TSB plugin) {
        this.plugin = plugin;
    }

    // Evento de entrada en el juego
    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        new BukkitRunnable() {
            @Override
            public void run() {
                if (player.isOnline()) {

                    if (plugin.getMainMobs().getSkeletonEmperor() != null && plugin.getMainMobs().getSkeletonEmperor().getSkeletonEmperorID() != null) {
                        plugin.getMainMobs().getSkeletonEmperor().onSuccessGenerated();
                    }
                    if (plugin.getMainMobs().getSkeletonKing() != null && plugin.getMainMobs().getSkeletonKing().getSkeletonKingID() != null) {
                        plugin.getMainMobs().getSkeletonKing().onSuccessGenerated();
                    }
                }
            }
        }.runTaskLater(plugin, 60L);
        // (20L = 20 ticks = 1 seg)
    }

    @EventHandler
    public void onMobDeath(EntityDeathEvent event) {
        if (event.getEntityType() == EntityType.SKELETON) {
            if (plugin.getMainMobs().getSkeletonEmperor() != null && plugin.getMainMobs().getSkeletonEmperor().getSkeletonEmperorID() != null) {
                if (plugin.getMainMobs().getSkeletonEmperor().getSkeletonEmperorID().equals(event.getEntity().getUniqueId())) {
                    plugin.getBossPersistenceManager().removeBossData("emperor");
                    Player killer = event.getEntity().getKiller();

                    if (killer != null) {
                        event.getDrops().clear(); // Clear default drops
                        event.setDroppedExp(plugin.getConfigs().getBossesEmperorExp());

                        if (plugin.getConfigs().getBossesCommandEnabled()) {
                            Bukkit.getServer().dispatchCommand(
                                Bukkit.getConsoleSender(), 
                                plugin.getConfigs().getBossesEmperorCommand().replace("{player_killed}", killer.getName())
                            );
                        } else {
                            plugin.getMainMobs().getSkeletonEmperor().onSkeletonEmperorDeath(event);
                        }
                    }

                    plugin.getMainMobs().getSkeletonEmperor().onSuccessDeath(killer);
                }
            }
        }

        if (event.getEntityType() == EntityType.WITHER_SKELETON) {
            if (plugin.getMainMobs().getSkeletonKing() != null && plugin.getMainMobs().getSkeletonKing().getSkeletonKingID() != null) {
                if (plugin.getMainMobs().getSkeletonKing().getSkeletonKingID().equals(event.getEntity().getUniqueId())) {
                    Player killer = event.getEntity().getKiller();
                    plugin.getBossPersistenceManager().removeBossData("king");

                    if (killer != null) {
                        event.getDrops().clear(); // Clear default drops
                        event.setDroppedExp(plugin.getConfigs().getBossesKingExp());

                        if (plugin.getConfigs().getBossesCommandEnabled()) {
                            Bukkit.getServer().dispatchCommand(
                                Bukkit.getConsoleSender(), 
                                plugin.getConfigs().getBossesKingCommand().replace("{player_killed}", killer.getName())
                            );
                        } else {
                            plugin.getMainMobs().getSkeletonKing().onSkeletonKingDeath(event);
                        }
                    }

                    plugin.getMainMobs().getSkeletonKing().onSuccessDeath(killer);
                }
            }
        }
    }
    
}
