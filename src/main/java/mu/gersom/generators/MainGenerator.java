/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package mu.gersom.generators;

import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.scheduler.BukkitRunnable;

import mu.gersom.MuMc;
import mu.gersom.utils.General;
import mu.gersom.utils.Vars;

/**
 *
 * @author Gersom
 */
public class MainGenerator {
    private SkeletonEmperor skeletonEmperor;
    private SkeletonKing skeletonKing;
    private final MuMc plugin;
    private final Random random = new Random();

    public MainGenerator(MuMc plugin) {
        this.plugin = plugin;
    }
    
    public void generateEmperor(World world, Location location) {
        skeletonEmperor = new SkeletonEmperor(plugin);
        skeletonEmperor.generateSkeletonEmperor(world, location);
        Bukkit.broadcastMessage(General.setColor(
            "&a" + Vars.prefix + "&6&l" + plugin.getConfigs().getBossSkeletonEmperor() + " &r&a" + plugin.getConfigs().getBossMessageSpawn()
        ));
    }

    public void generateKing(World world, Location location) {
        skeletonKing = new SkeletonKing(plugin);
        skeletonKing.generateSkeletonKing(world, location);
        Bukkit.broadcastMessage(General.setColor(
            "&a" + Vars.prefix + "&6&l" + plugin.getConfigs().getBossSkeletonKing() + " &r&a" + plugin.getConfigs().getBossMessageSpawn()
        ));
    }

    public SkeletonEmperor getSkeletonEmperor() {
        return skeletonEmperor;
    }

    public SkeletonKing getSkeletonKing() {
        return skeletonKing;
    }

    public void startAutoSpawnBoss(World world, Location center, int radius, long interval) {
        new BukkitRunnable() {
            @Override
            public void run() {
                if ((skeletonKing == null || skeletonKing.getSkeletonKingID() == null) &&
                    (skeletonEmperor == null || skeletonEmperor.getSkeletonEmperorID() == null)) {
                    Location spawnLocation = getRandomLocation(world, center, radius);

                    Double chance = random.nextDouble();
                    if (chance < plugin.getConfigs().getSpawnChance()) {
                        // chance for King
                        if (chance < plugin.getConfigs().getSpawnKingPercentage()) {
                            generateKing(world, spawnLocation);
                        } 
                        
                        // chance for Emperor
                        else if (chance < (plugin.getConfigs().getSpawnKingPercentage() + plugin.getConfigs().getSpawnEmperorPercentage())) {
                            generateEmperor(world, spawnLocation);
                        }
                    }
                }
            }
        }.runTaskTimer(plugin, 0L, interval);
    }

    private Location getRandomLocation(World world, Location center, int radius) {
        double angle = random.nextDouble() * 2 * Math.PI;
        double distance = random.nextDouble() * radius;
        int x = (int) (center.getX() + distance * Math.cos(angle));
        int z = (int) (center.getZ() + distance * Math.sin(angle));
        int y = world.getHighestBlockYAt(x, z);
        return new Location(world, x, y, z);
    }
}
