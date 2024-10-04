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
            "&6" + Vars.prefix + "&l" + "&f&a"
        ));
    }

    public void generateKing(World world, Location location) {
        skeletonKing = new SkeletonKing(plugin);
        skeletonKing.generateSkeletonKing(world, location);
        Bukkit.broadcastMessage(General.setColor(
            "&d" + Vars.prefix + "&l" + "&f&a"
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
                    if (random.nextDouble() < 0.45) { // 45% chance for King
                        generateKing(world, spawnLocation);
                    } else { // 45% chance for Emperor
                        generateEmperor(world, spawnLocation);
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
