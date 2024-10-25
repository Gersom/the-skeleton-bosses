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
import org.bukkit.entity.Skeleton;
import org.bukkit.entity.WitherSkeleton;
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
                skeletonEmperor.setBossId(emperorUUID);
                skeletonEmperor.recreateBossBar(entity);
            }
        }
        
        if (kingUUID != null) {
            Entity entity = findEntityByUUID(kingUUID);
            if (entity != null) {
                skeletonKing = new SkeletonKing(plugin);
                skeletonKing.setBossId(kingUUID);
                skeletonKing.recreateBossBar(entity);
            }
        }
    }
    
    @SuppressWarnings("")
    private Entity findEntityByUUID(UUID uuid) {
        String worldName = plugin.getConfigs().getSpawnWorld();
        World world = Bukkit.getWorld(worldName);

        if (world == null) {
            Console.sendMessage("&cError: The world specified in the configuration does not exist.");
            return null;
        }

        for (Entity entity : world.getEntities()) {
            if (entity.getUniqueId().equals(uuid)) {
                return entity;
            }
        }
        return null;
    }

    public void onSuccessGenerated(Entity entity, String bossType, String senderType, Player player) {
        Location spawnLocation;
        String bossName = "";
        String bossColor = "";

        if (entity != null) {
            spawnLocation = entity.getLocation();
        } else {
            spawnLocation = plugin.getBossPersistenceManager().getBossLocation(bossType);
            if (spawnLocation == null) {
                // If there's no location data, we can't show a message
                return;
            }
        }

        String coords = String.format("(%.0f, %.0f, %.0f)", 
                                      spawnLocation.getX(), 
                                      spawnLocation.getY(), 
                                      spawnLocation.getZ());

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

        if (senderType.equals("player")) {
            player.playSound(
                Objects.requireNonNull(player.getLocation()), 
                Sound.ENTITY_SKELETON_CONVERTED_TO_STRAY, 
                1, 
                1
            );
            player.sendMessage(General.setColor(title));
        }

        if (senderType.equals("console")) {
            Console.sendMessage(General.setColor(title));
        }

        if (senderType.equals("both")) {
            for (Player playerTmp : Bukkit.getOnlinePlayers()) {
                playerTmp.playSound(
                    Objects.requireNonNull(playerTmp.getLocation()), 
                    Sound.ENTITY_SKELETON_CONVERTED_TO_STRAY, 
                    1, 
                    1
                );
            }
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
        if (skeletonEmperor == null || skeletonEmperor.getBossId() == null) {
            skeletonEmperor = new SkeletonEmperor(plugin);
            skeletonEmperor.generateBoss(world, location);
            plugin.getBossPersistenceManager().saveBossData("skeletonEmperor", skeletonEmperor.getBossId(), location);

            onSuccessGenerated(
                skeletonEmperor.getEntity(),
                "skeletonEmperor",
                "both",
                null
            );
        }
    }

    public void generateKing(World world, Location location) {
        if (skeletonKing == null || skeletonKing.getBossId() == null) {
            skeletonKing = new SkeletonKing(plugin);
            skeletonKing.generateBoss(world, location);
            plugin.getBossPersistenceManager().saveBossData("skeletonKing", skeletonKing.getBossId(), location);
            
            onSuccessGenerated(
                skeletonKing.getEntity(),
                "skeletonKing",
                "both",
                null
            );
        }
    }

    public void startAutoSpawnBoss(World world, Location center, int minRadius, int maxRadius, long interval) {
        this.taskAutoSpawn = new BukkitRunnable() {
            @Override
            public void run() {
                // Verificar si ya existe un jefe en bosses_data.yml
                UUID emperorUUID = plugin.getBossPersistenceManager().getBossUUID("skeletonEmperor");
                UUID kingUUID = plugin.getBossPersistenceManager().getBossUUID("skeletonKing");

                if (emperorUUID == null && kingUUID == null) {
                    if ((skeletonKing == null || skeletonKing.getBossId() == null) &&
                        (skeletonEmperor == null || skeletonEmperor.getBossId() == null)) {
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
                            // Console.sendMessage(plugin.getConfigs().getPrefix() + "&achance:" + chance);
                            // Console.sendMessage(plugin.getConfigs().getPrefix() + "&aBossKingPercentage:" + plugin.getConfigs().getBossKingPercentage());
                            // Console.sendMessage(plugin.getConfigs().getPrefix() + "&aBossEmperorPercentage:" + plugin.getConfigs().getBossEmperorPercentage());
                        }
                    }
                }
            }
        }.runTaskTimer(plugin, 0L, interval);
    }

    public void checkAndRecoverBosses() {
        boolean needToCheckEmperor = (skeletonEmperor == null || skeletonEmperor.getEntity() == null);
        boolean needToCheckKing = (skeletonKing == null || skeletonKing.getEntity() == null);
    
        if (!needToCheckEmperor && !needToCheckKing) {
            // Ambos jefes están en memoria y tienen entidades válidas, no es necesario hacer nada
            return;
        }
    
        UUID emperorUUID = needToCheckEmperor ? plugin.getBossPersistenceManager().getBossUUID("skeletonEmperor") : null;
        UUID kingUUID = needToCheckKing ? plugin.getBossPersistenceManager().getBossUUID("skeletonKing") : null;
    
        if (emperorUUID == null && kingUUID == null) {
            // No hay jefes que necesiten ser recuperados
            return;
        }
    
        String worldName = plugin.getConfigs().getSpawnWorld();
        World world = Bukkit.getWorld(worldName);
    
        if (world == null) {
            Console.sendMessage("&cError: The world specified in the configuration does not exist.");
            return;
        }
    
        for (Entity entity : world.getEntities()) {
            if (needToCheckEmperor && entity instanceof Skeleton && emperorUUID != null && entity.getUniqueId().equals(emperorUUID)) {
                skeletonEmperor = new SkeletonEmperor(plugin);
                skeletonEmperor.setBossId(emperorUUID);
                skeletonEmperor.recreateBossBar(entity);
                plugin.getBossPersistenceManager().saveBossData("skeletonEmperor", emperorUUID, entity.getLocation());
                Console.sendMessage(plugin.getConfigs().getPrefix() + "&aSkeleton Emperor was found again.");
                needToCheckEmperor = false;
            } else if (needToCheckKing && entity instanceof WitherSkeleton && kingUUID != null && entity.getUniqueId().equals(kingUUID)) {
                skeletonKing = new SkeletonKing(plugin);
                skeletonKing.setBossId(kingUUID);
                skeletonKing.recreateBossBar(entity);
                plugin.getBossPersistenceManager().saveBossData("skeletonKing", kingUUID, entity.getLocation());
                Console.sendMessage(plugin.getConfigs().getPrefix() + "&aSkeleton King was found again.");
                needToCheckKing = false;
            }
    
            if (!needToCheckEmperor && !needToCheckKing) {
                // Si los 2 jefes han sido recuperados, podemos salir del bucle
                break;
            }
        }
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
