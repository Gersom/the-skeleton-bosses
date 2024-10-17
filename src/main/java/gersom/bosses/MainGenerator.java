/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package gersom.bosses;

import java.util.Objects;
import java.util.Random;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import gersom.TSB;
import gersom.utils.Console;
import gersom.utils.General;

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
        UUID emperorUUID = plugin.getBossPersistenceManager().getBossUUID("skeletonEmperor");
        UUID kingUUID = plugin.getBossPersistenceManager().getBossUUID("skeletonKing");
        
        if (emperorUUID != null) {
            Entity entity = findEntityByUUID(emperorUUID);
            if (entity != null) {
                skeletonEmperor = new SkeletonEmperor(plugin);
                skeletonEmperor.setSkeletonEmperorID(emperorUUID);
                skeletonEmperor.recreateBossBar(entity);
            } else {
                plugin.getBossPersistenceManager().removeBossData("skeletonEmperor");
            }
        }
        
        if (kingUUID != null) {
            Entity entity = findEntityByUUID(kingUUID);
            if (entity != null) {
                skeletonKing = new SkeletonKing(plugin);
                skeletonKing.setSkeletonKingID(kingUUID);
                skeletonKing.recreateBossBar(entity);
            } else {
                plugin.getBossPersistenceManager().removeBossData("skeletonKing");
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

    public void onSuccessGenerated(Entity entity, String bossType, String senderType) {
        Location spawnLocation = entity.getLocation();
        String coords = String.format("(%.0f, %.0f, %.0f)", 
                                      spawnLocation.getX(), 
                                      spawnLocation.getY(), 
                                      spawnLocation.getZ());

        String bossName = "";
        String bossColor = "";
        String title = plugin.getConfigs().getLangBossesMsgSpawn();
        if (bossType.equals("skeletonEmperor")) {
            bossName = plugin.getConfigs().getLangBossEmperorName();
            bossColor = plugin.getConfigs().getBossEmperorColor();
        } else if (bossType.equals("skeletonKing")) {
            bossName = plugin.getConfigs().getLangBossKingName();
            bossColor = plugin.getConfigs().getBossKingColor();
        }

        title = title.replace("{prefix}", plugin.getConfigs().getPrefix());
        title = title.replace("{boss_name}", bossName);
        title = title.replace("{boss_color}", bossColor);
        title = title.replace("{coords}", coords);

        for (Player player : Bukkit.getOnlinePlayers()) {
            player.playSound(
                Objects.requireNonNull(player.getLocation()), 
                Sound.ENTITY_SKELETON_CONVERTED_TO_STRAY, 
                1, 
                1
            );

            if (senderType.equals("player")) {
                player.sendMessage(General.setColor(title));
            }
        }

        if (senderType.equals("console")) {
            Console.sendMessage(General.setColor(title));
        }

        if (senderType.equals("both")) {
            Bukkit.broadcastMessage(General.setColor(title));
        }
    }

    public void onSuccessDeath(String bossType, Player killer) {
        for (Player player : Bukkit.getOnlinePlayers()) {
            player.playSound(
                Objects.requireNonNull(player.getLocation()), 
                Sound.ENTITY_SKELETON_HORSE_DEATH, 
                1, 
                1
            );
        }

        String title;
        String bossName = "";
        String bossColor = "";
        String playerKiller = "";

        if (bossType.equals("skeletonEmperor")) {
            bossName = plugin.getConfigs().getLangBossEmperorName();
            bossColor = plugin.getConfigs().getBossEmperorColor();
        } else if (bossType.equals("skeletonKing")) {
            bossName = plugin.getConfigs().getLangBossKingName();
            bossColor = plugin.getConfigs().getBossKingColor();
        }

        if (killer != null) {
            title = plugin.getConfigs().getLangBossesMsgKilled();
            playerKiller = killer.getName();
        } else {
            title = plugin.getConfigs().getLangBossesMsgDeath();
        }

        title = title.replace("{prefix}", plugin.getConfigs().getPrefix());
        title = title.replace("{boss_name}", bossName);
        title = title.replace("{boss_color}", bossColor);
        title = title.replace("{player_killer}", playerKiller);

        Bukkit.broadcastMessage(General.setColor(title));
    }
    
    public void generateEmperor(World world, Location location) {
        if (skeletonEmperor == null || skeletonEmperor.getSkeletonEmperorID() == null) {
            skeletonEmperor = new SkeletonEmperor(plugin);
            skeletonEmperor.generateSkeletonEmperor(world, location);
            plugin.getBossPersistenceManager().saveBossData("skeletonEmperor", skeletonEmperor.getSkeletonEmperorID());

            onSuccessGenerated(
                skeletonEmperor.getSkeletonEmperorEntity(),
                "skeletonEmperor",
                "both"
            );
        }
    }

    public void generateKing(World world, Location location) {
        if (skeletonKing == null || skeletonKing.getSkeletonKingID() == null) {
            skeletonKing = new SkeletonKing(plugin);
            skeletonKing.generateSkeletonKing(world, location);
            plugin.getBossPersistenceManager().saveBossData("skeletonKing", skeletonKing.getSkeletonKingID());
            
            onSuccessGenerated(
                skeletonKing.getSkeletonKingEntity(),
                "skeletonKing",
                "both"
            );
        }
    }

    public void startAutoSpawnBoss(World world, Location center, int minRadius, int maxRadius, long interval) {
        this.taskAutoSpawn = new BukkitRunnable() {
            @Override
            public void run() {
                if ((skeletonKing == null || skeletonKing.getSkeletonKingID() == null) &&
                    (skeletonEmperor == null || skeletonEmperor.getSkeletonEmperorID() == null)) {
                    Location spawnLocation = getRandomLocation(world, center, minRadius, maxRadius);

                    Double chance = random.nextDouble();
                    if (chance < plugin.getConfigs().getSpawnChance()) {
                        // chance for King
                        if (chance < plugin.getConfigs().getBossKingPercentage()) {
                            generateKing(world, spawnLocation);
                        } 
                        
                        // chance for Emperor
                        else if (chance < plugin.getConfigs().getBossEmperorPercentage()) {
                            generateEmperor(world, spawnLocation);
                        }
                    }
                }
            }
        }.runTaskTimer(plugin, 0L, interval);
    }

    private Location getRandomLocation(World world, Location center, int minRadius, int maxRadius) {
        double angle = random.nextDouble() * 2 * Math.PI;
        double distance = minRadius + random.nextDouble() * (maxRadius - minRadius);
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
