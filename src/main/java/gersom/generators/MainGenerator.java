/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package gersom.generators;

import java.util.Random;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import gersom.TSB;

/**
 *
 * @author Gersom
 */
public class MainGenerator {
    private SkeletonEmperor skeletonEmperor;
    private SkeletonKing skeletonKing;
    private final TSB plugin;
    private final Random random = new Random();
    private BukkitTask taskAutoSpawn;

    public MainGenerator(TSB plugin) {
        this.plugin = plugin;
        loadExistingBosses();
    }

    private void loadExistingBosses() {
        UUID emperorUUID = plugin.getBossPersistenceManager().getBossUUID("emperor");
        UUID kingUUID = plugin.getBossPersistenceManager().getBossUUID("king");
        
        if (emperorUUID != null) {
            Entity entity = findEntityByUUID(emperorUUID);
            if (entity != null) {
                skeletonEmperor = new SkeletonEmperor(plugin);
                skeletonEmperor.setSkeletonEmperorID(emperorUUID);
                skeletonEmperor.recreateBossBar(entity);
            } else {
                plugin.getBossPersistenceManager().removeBossData("emperor");
            }
        }
        
        if (kingUUID != null) {
            Entity entity = findEntityByUUID(kingUUID);
            if (entity != null) {
                skeletonKing = new SkeletonKing(plugin);
                skeletonKing.setSkeletonKingID(kingUUID);
                skeletonKing.recreateBossBar(entity);
            } else {
                plugin.getBossPersistenceManager().removeBossData("king");
            }
        }
    }
    
    private Entity findEntityByUUID(UUID uuid) {
        for (World world : Bukkit.getWorlds()) {
            for (Entity entity : world.getEntities()) {
                if (entity.getUniqueId().equals(uuid)) {
                    return entity;
                }
            }
        }
        return null;
    }
    
    public void generateEmperor(World world, Location location) {
        if (skeletonEmperor == null || skeletonEmperor.getSkeletonEmperorID() == null) {
            skeletonEmperor = new SkeletonEmperor(plugin);
            skeletonEmperor.generateSkeletonEmperor(world, location);
            plugin.getBossPersistenceManager().saveBossData("emperor", skeletonEmperor.getSkeletonEmperorID());

            plugin.getMainMobs().getSkeletonEmperor().onSuccessGenerated();
        }
    }

    public void generateKing(World world, Location location) {
        if (skeletonKing == null || skeletonKing.getSkeletonKingID() == null) {
            skeletonKing = new SkeletonKing(plugin);
            skeletonKing.generateSkeletonKing(world, location);
            plugin.getBossPersistenceManager().saveBossData("king", skeletonKing.getSkeletonKingID());
            
            plugin.getMainMobs().getSkeletonKing().onSuccessGenerated();
        }
    }

    public void startAutoSpawnBoss(World world, Location center, int radius, long interval) {
        this.taskAutoSpawn = new BukkitRunnable() {
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
        int y = world.getHighestBlockYAt(x, z) + 1;
        return new Location(world, x, y, z);
    }

    public SkeletonEmperor getSkeletonEmperor() {
        return skeletonEmperor;
    }

    public SkeletonKing getSkeletonKing() {
        return skeletonKing;
    }

    public BukkitTask getTaskAutoSpawn() {
        return taskAutoSpawn;
    }
}
