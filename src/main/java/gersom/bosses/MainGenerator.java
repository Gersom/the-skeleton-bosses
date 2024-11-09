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
import org.bukkit.entity.Stray;
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
    private SkeletonWinterLord skeletonWinterLord;
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
        UUID winterLordUUID = plugin.getBossPersistenceManager().getBossUUID("skeletonWinterLord");
        
        if (emperorUUID != null) {
            Entity entity = findEntityByUUID(emperorUUID);
            if (plugin.getConfigs().getIsLogs()) {
                Console.sendMessage("emperorUUID: " + emperorUUID);
            }
            if (entity != null) {
                if (plugin.getConfigs().getIsLogs()) {
                    Console.sendMessage("Emperor found, entity is not null");
                }
                skeletonEmperor = new SkeletonEmperor(plugin);
                skeletonEmperor.setBossId(emperorUUID);
                skeletonEmperor.recreateBossBar(entity);
                plugin.getBossPersistenceManager().saveBossData("skeletonEmperor", skeletonEmperor.getBossId(), entity.getLocation());
            }
        }
        
        if (kingUUID != null) {
            Entity entity = findEntityByUUID(kingUUID);
            if (plugin.getConfigs().getIsLogs()) {
                Console.sendMessage("kingUUID: " + kingUUID);
            }
            if (entity != null) {
                if (plugin.getConfigs().getIsLogs()) {
                    Console.sendMessage("King found, entity is not null");
                }
                skeletonKing = new SkeletonKing(plugin);
                skeletonKing.setBossId(kingUUID);
                skeletonKing.recreateBossBar(entity);
                plugin.getBossPersistenceManager().saveBossData("skeletonKing", skeletonKing.getBossId(), entity.getLocation());
            }
        }

        if (winterLordUUID != null) {
            Entity entity = findEntityByUUID(winterLordUUID);
            if (plugin.getConfigs().getIsLogs()) {
                Console.sendMessage("winterLordUUID: " + winterLordUUID);
            }
            if (entity != null) {
                if (plugin.getConfigs().getIsLogs()) {
                    Console.sendMessage("Winter Lord found, entity is not null");
                }
                skeletonWinterLord = new SkeletonWinterLord(plugin);
                skeletonWinterLord.setBossId(winterLordUUID);
                skeletonWinterLord.recreateBossBar(entity);
                plugin.getBossPersistenceManager().saveBossData("skeletonWinterLord", skeletonWinterLord.getBossId(), entity.getLocation());
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

        switch (bossType) {
            case "skeletonEmperor" -> {
                bossName = plugin.getConfigs().getLangBossEmperorName();
                bossColor = plugin.getConfigs().getBossEmperorColor();
            }
            case "skeletonKing" -> {
                bossName = plugin.getConfigs().getLangBossKingName();
                bossColor = plugin.getConfigs().getBossKingColor();
            }
            case "skeletonWinterLord" -> {
                bossName = plugin.getConfigs().getLangBossWinterLordName();
                bossColor = plugin.getConfigs().getBossWinterLordColor();
            }
            default -> {
            }
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

        switch (bossType) {
            case "skeletonEmperor" -> {
                bossName = plugin.getConfigs().getLangBossEmperorName();
                bossColor = plugin.getConfigs().getBossEmperorColor();
            }
            case "skeletonKing" -> {
                bossName = plugin.getConfigs().getLangBossKingName();
                bossColor = plugin.getConfigs().getBossKingColor();
            }
            case "skeletonWinterLord" -> {
                bossName = plugin.getConfigs().getLangBossWinterLordName();
                bossColor = plugin.getConfigs().getBossWinterLordColor();
            }
            default -> {
            }
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

    public void generateWinterLord(World world, Location location) {
        if (skeletonWinterLord == null || skeletonWinterLord.getBossId() == null) {
            skeletonWinterLord = new SkeletonWinterLord(plugin);
            skeletonWinterLord.generateBoss(world, location);
            plugin.getBossPersistenceManager().saveBossData("skeletonWinterLord", skeletonWinterLord.getBossId(), location);
            
            onSuccessGenerated(
                skeletonWinterLord.getEntityBoss(),
                "skeletonWinterLord",
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
                UUID winterLordUUID = plugin.getBossPersistenceManager().getBossUUID("skeletonWinterLord");
    
                if (emperorUUID == null && kingUUID == null && winterLordUUID == null) {
                    if ((skeletonKing == null || skeletonKing.getBossId() == null) &&
                        (skeletonEmperor == null || skeletonEmperor.getBossId() == null) &&
                        (skeletonWinterLord == null || skeletonWinterLord.getBossId() == null)) {
                        
                        Location spawnLocation = getRandomLocation(world, center, minRadius, maxRadius);
                        Double chance = random.nextDouble();
    
                        if (chance < plugin.getConfigs().getSpawnChance()) {
                            // Obtener los porcentajes de configuración
                            double kingPercentage = plugin.getConfigs().getBossKingPercentage();
                            double emperorPercentage = plugin.getConfigs().getBossEmperorPercentage();
                            double winterLordPercentage = plugin.getConfigs().getBossWinterLordPercentage();
    
                            // Calcular la suma total de porcentajes
                            double totalPercentage = kingPercentage + emperorPercentage + winterLordPercentage;
    
                            // Si la suma total es 0, evitar división por cero
                            if (totalPercentage == 0) {
                                // Si todos son 0, dar igual probabilidad a cada uno
                                kingPercentage = 33.33;
                                emperorPercentage = 33.33;
                                winterLordPercentage = 33.34;
                                totalPercentage = 100;
                            }
    
                            // Normalizar los porcentajes
                            double normalizedKingPercentage = kingPercentage / totalPercentage;
                            double normalizedEmperorPercentage = emperorPercentage / totalPercentage;
                            double normalizedWinterLordPercentage = winterLordPercentage / totalPercentage;
    
                            // Generar un número aleatorio entre 0 y 1
                            double randomValue = random.nextDouble();
    
                            if (plugin.getConfigs().getIsLogs()) {
                                Console.sendMessage("&e" + plugin.getConfigs().getPrefix() + "&eNormalized percentages:");
                                Console.sendMessage("&e" + plugin.getConfigs().getPrefix() + "&eKing: " + (normalizedKingPercentage * 100) + "%");
                                Console.sendMessage("&e" + plugin.getConfigs().getPrefix() + "&eEmperor: " + (normalizedEmperorPercentage * 100) + "%");
                                Console.sendMessage("&e" + plugin.getConfigs().getPrefix() + "&eWinter Lord: " + (normalizedWinterLordPercentage * 100) + "%");
                                Console.sendMessage("&e" + plugin.getConfigs().getPrefix() + "&eRandom value: " + randomValue);
                            }
    
                            // Determinar qué boss generar basado en los porcentajes normalizados
                            if (randomValue < normalizedKingPercentage) {
                                generateKing(world, spawnLocation);
                            } else if (randomValue < normalizedKingPercentage + normalizedEmperorPercentage) {
                                generateEmperor(world, spawnLocation);
                            } else {
                                generateWinterLord(world, spawnLocation);
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
        UUID winterLordLogUUID = plugin.getBossPersistenceManager().getBossUUID("skeletonWinterLord");

        if (emperorLogUUID != null) {
            if (isLogs) Console.sendMessage("&a" + plugin.getConfigs().getPrefix() + "&a" + "Skeleton Emperor registrado en bosses_data.yml");
        }
        if (kingLogUUID != null) {
            if (isLogs) Console.sendMessage("&a" + plugin.getConfigs().getPrefix() + "&a" + "Skeleton King registrado en bosses_data.yml");
        }
        if (winterLordLogUUID != null) {
            if (isLogs) Console.sendMessage("&a" + plugin.getConfigs().getPrefix() + "&a" + "Skeleton Winter Lord registrado en bosses_data.yml");
        }
        if (emperorLogUUID == null && kingLogUUID == null && winterLordLogUUID == null) {
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

        if (isLogs && skeletonWinterLord == null) {
            Console.sendMessage("&6" + plugin.getConfigs().getPrefix() + "&6" + "Skeleton Winter Lord: Class is null");
        } else if (isLogs && skeletonWinterLord != null) {
            Console.sendMessage("&a" + plugin.getConfigs().getPrefix() + "&a" + "Skeleton Winter Lord: Class exists");

            if (skeletonWinterLord.getBossBar() == null) {
                Console.sendMessage("&6" + plugin.getConfigs().getPrefix() + "&6" + "Skeleton Winter Lord: getBossBar is null");
            } else if (skeletonWinterLord.getBossBar() != null) {
                Console.sendMessage("&a" + plugin.getConfigs().getPrefix() + "&a" + "Skeleton Winter Lord: getBossBar exists");
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

            if (winterLordLogUUID != null && entity instanceof Stray && entity.getUniqueId().equals(winterLordLogUUID)) {
                if (skeletonWinterLord == null) {
                    skeletonWinterLord = new SkeletonWinterLord(plugin);
                    skeletonWinterLord.setEntityBoss((Stray) entity);
                    skeletonWinterLord.setBossId(winterLordLogUUID);
                    skeletonWinterLord.recreateBossBar(entity);
                } else {
                    if (skeletonWinterLord.getEntityBoss() == null) skeletonWinterLord.setEntityBoss((Stray) entity);
                    if (skeletonWinterLord.getBossId() == null) skeletonWinterLord.setBossId(winterLordLogUUID);
                    if (skeletonWinterLord.getBossBar() == null) skeletonWinterLord.recreateBossBar(entity);
                }
                plugin.getBossPersistenceManager().saveBossData("skeletonWinterLord", winterLordLogUUID, entity.getLocation());
                if (isLogs) Console.sendMessage(plugin.getConfigs().getPrefix() + "&aSkeleton Winter Lord was recovered successfully.");
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

    public SkeletonWinterLord getSkeletonWinterLord() {
        return skeletonWinterLord;
    }

    public BukkitTask getTaskAutoSpawn() {
        return taskAutoSpawn;
    }
}
