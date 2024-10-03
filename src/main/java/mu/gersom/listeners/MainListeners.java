/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package mu.gersom.listeners;

import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.scheduler.BukkitRunnable;

import mu.gersom.MuMc;
import mu.gersom.utils.General;
import mu.gersom.utils.Vars;

/**
 *
 * @author Gersom
 */
public class MainListeners implements Listener {

    private final MuMc plugin;

    public MainListeners(MuMc plugin) {
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
                    player.sendMessage(General.setColor(
                        "&6&l" + Vars.prefix + plugin.getConfigs().getWelcomeMessage()
                    ));
                }
            }
        }.runTaskLater(plugin, 60L);
        // (20L = 20 ticks = 1 seg)
    }

    // Evento de muerte de un mob
    @EventHandler
    public void onMobDeath(EntityDeathEvent event) {
        if (event.getEntityType() == EntityType.SKELETON) {
            // Si el mob existe en el set de mobs
            if (plugin.getMainMobs().getSkeletonEmperor() != null) {
                if (plugin.getMainMobs().getSkeletonEmperor().getSkeletonEmperorID().equals(event.getEntity().getUniqueId())) {
                    plugin.getMainMobs().getSkeletonEmperor().onSkeletonEmperorDeath(event);
                }
            }
        }

        if (event.getEntityType() == EntityType.WITHER_SKELETON) {
            if (plugin.getMainMobs().getSkeletonKing() != null) {
                if (plugin.getMainMobs().getSkeletonKing().getSkeletonKingID().equals(event.getEntity().getUniqueId())) {
                    plugin.getMainMobs().getSkeletonKing().onSkeletonKingDeath(event);
                }
            }
        }
    }
    
}
