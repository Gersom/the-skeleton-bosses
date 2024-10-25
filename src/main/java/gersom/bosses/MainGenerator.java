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
            Console.sendMessage("emperorUUID: " + emperorUUID);
            if (entity != null) {
                skeletonEmperor = new SkeletonEmperor(plugin);
                skeletonEmperor.setBossId(emperorUUID);
                skeletonEmperor.recreateBossBar(entity);
                plugin.getBossPersistenceManager().saveBossData("skeletonEmperor", skeletonEmperor.getBossId(), entity.getLocation());
            }
        }
        
        if (kingUUID != null) {
            Entity entity = findEntityByUUID(kingUUID);
            Console.sendMessage("kingUUID: " + kingUUID);
            if (entity != null) {
                skeletonKing = new SkeletonKing(plugin);
                Console.sendMessage("King found, el entity no es nulo");
                skeletonKing.setBossId(kingUUID);
                skeletonKing.recreateBossBar(entity);
                plugin.getBossPersistenceManager().saveBossData("skeletonKing", skeletonKing.getBossId(), entity.getLocation());
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
                skeletonEmperor.getEntityBoss(),
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
                skeletonKing.getEntityBoss(),
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

                            if (plugin.getConfigs().getIsLogs()) {
                                Console.sendMessage("&e" + plugin.getConfigs().getPrefix() + "&e" + "&achance:" + chance);
                                Console.sendMessage("&e" + plugin.getConfigs().getPrefix() + "&e" + "&aBossKingPercentage:" + plugin.getConfigs().getBossKingPercentage());
                                Console.sendMessage("&e" + plugin.getConfigs().getPrefix() + "&e" + "&aBossEmperorPercentage:" + plugin.getConfigs().getBossEmperorPercentage());
                            }
                        }
                    }
                }
            }
        }.runTaskTimer(plugin, 0L, interval);
    }

    public void checkAndRecoverBosses() {
        String worldName = plugin.getConfigs().getSpawnWorld();
        World world = Bukkit.getWorld(worldName);

        if (world == null) {
            Console.sendMessage("&cError: The world specified in the configuration does not exist.");
            return;
        }

        boolean isLogs = plugin.getConfigs().getIsLogs();
        UUID emperorLogUUID = plugin.getBossPersistenceManager().getBossUUID("skeletonEmperor");
        UUID kingLogUUID = plugin.getBossPersistenceManager().getBossUUID("skeletonKing");

        if (emperorLogUUID != null) {
            if (isLogs) Console.sendMessage("&a" + plugin.getConfigs().getPrefix() + "&a" + "Skeleton Emperor registrado en bosses_data.yml");
        }
        if (kingLogUUID != null) {
            if (isLogs) Console.sendMessage("&a" + plugin.getConfigs().getPrefix() + "&a" + "Skeleton King registrado en bosses_data.yml");
        }
        if (emperorLogUUID == null && kingLogUUID == null) {
            // No hay jefes que necesiten ser recuperados
            if (isLogs) Console.sendMessage("&6" + plugin.getConfigs().getPrefix() + "&6" + "No hay ningun Boss registrado en bosses_data.yml");
            return;
        }

        if (isLogs && skeletonEmperor == null) {
            Console.sendMessage("&6" + plugin.getConfigs().getPrefix() + "&6" + "Skeleton Emperor: Class is null");
        } else if (isLogs && skeletonEmperor != null) {
            Console.sendMessage("&a" + plugin.getConfigs().getPrefix() + "&a" + "Skeleton Emperor: Class exists");

            if (skeletonEmperor.getBossBar() == null) {
                Console.sendMessage("&6" + plugin.getConfigs().getPrefix() + "&6" + "Skeleton Emperor: getBossBar is null");
            } else if (skeletonEmperor.getBossBar() != null) {
                Console.sendMessage("&a" + plugin.getConfigs().getPrefix() + "&a" + "Skeleton Emperor: getBossBar exists");
            }
        }

        if (isLogs && skeletonKing == null) {
            Console.sendMessage("&6" + plugin.getConfigs().getPrefix() + "&6" + "Skeleton King: Class is null");
        } else if (isLogs && skeletonKing != null) {
            Console.sendMessage("&a" + plugin.getConfigs().getPrefix() + "&a" + "Skeleton King: Class exists");

            if (skeletonKing.getBossBar() == null) {
                Console.sendMessage("&6" + plugin.getConfigs().getPrefix() + "&6" + "Skeleton King: getBossBar is null");
            } else if (skeletonKing.getBossBar() != null) {
                Console.sendMessage("&a" + plugin.getConfigs().getPrefix() + "&a" + "Skeleton King: getBossBar exists");
            }
        }
        
        // Verificar si el chunk está cargado antes de buscar la entidad
        for (Entity entity : world.getEntities()) {
            if (emperorLogUUID != null && entity instanceof Skeleton && entity.getUniqueId().equals(emperorLogUUID)) {
                if (skeletonEmperor == null) {
                    skeletonEmperor = new SkeletonEmperor(plugin);
                    skeletonEmperor.setEntityBoss((Skeleton) entity);
                    skeletonEmperor.setBossId(emperorLogUUID);
                    skeletonEmperor.recreateBossBar(entity);
                } else {
                    if (skeletonEmperor.getEntityBoss() == null) skeletonEmperor.setEntityBoss((Skeleton) entity);
                    if (skeletonEmperor.getBossId() == null) skeletonEmperor.setBossId(emperorLogUUID);
                    if (skeletonEmperor.getBossBar() == null) skeletonEmperor.recreateBossBar(entity);
                }
                plugin.getBossPersistenceManager().saveBossData("skeletonEmperor", emperorLogUUID, entity.getLocation());
                if (isLogs) Console.sendMessage(plugin.getConfigs().getPrefix() + "&aSkeleton Emperor was recovered successfully.");
                break;
            }

            if (kingLogUUID != null && entity instanceof WitherSkeleton && entity.getUniqueId().equals(kingLogUUID)) {
                if (skeletonKing == null) {
                    skeletonKing = new SkeletonKing(plugin);
                    skeletonKing.setEntityBoss((WitherSkeleton) entity);
                    skeletonKing.setBossId(kingLogUUID);
                    skeletonKing.recreateBossBar(entity);
                } else {
                    if (skeletonKing.getEntityBoss() == null) skeletonKing.setEntityBoss((WitherSkeleton) entity);
                    if (skeletonKing.getBossId() == null) skeletonKing.setBossId(kingLogUUID);
                    if (skeletonKing.getBossBar() == null) skeletonKing.recreateBossBar(entity);
                }
                plugin.getBossPersistenceManager().saveBossData("skeletonKing", kingLogUUID, entity.getLocation());
                if (isLogs) Console.sendMessage(plugin.getConfigs().getPrefix() + "&aSkeleton King was recovered successfully.");
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
