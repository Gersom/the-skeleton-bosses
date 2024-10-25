package gersom.bosses;

import java.util.Map;
import java.util.Random;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.SkeletonHorse;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import gersom.TSB;
import gersom.utils.Console;
import gersom.utils.General;

public abstract class Boss {
    protected TSB plugin;
    protected String bossType;
    protected BossBar bossBar;
    protected BukkitTask taskBossBar;
    protected BukkitTask taskParticles;
    protected BukkitTask horseParticlesTask;
    protected UUID bossId;
    protected final Random random = new Random();

    public Boss(TSB plugin, String bossType) {
        this.plugin = plugin;
        this.bossType = bossType;
    }

    // Métodos abstractos que cada boss debe implementar
    protected abstract String getBossName();
    protected abstract String getBossColor();
    protected abstract String getBossBarColor();
    protected abstract String getBossBarTitle();
    protected abstract double getMaxHealth();
    protected abstract void equipBoss();
    protected abstract Entity spawnBossEntity(World world, Location location);
    // Declarar getEntity como método abstracto
    public abstract Entity getEntityBoss();
    // Métodos abstractos adicionales necesarios
    protected abstract int getExperience();
    protected abstract String getKillerCommand();
    protected abstract boolean isNearbyCommandEnabled();
    protected abstract int getNearbyCommandRadius();
    protected abstract String getNearbyCommand();
    // Método abstracto para que cada boss implemente sus propias partículas
    protected abstract void createHorseParticles(SkeletonHorse horse);

    // Método común para generar un boss
    public void generateBoss(World world, Location location) {
        Entity entity = spawnBossEntity(world, location);
        bossId = entity.getUniqueId();

        // Configurar nombre
        entity.setCustomName(General.setColor(getBossColor() + "&l" + getBossName() + "&r&f"));
        entity.setCustomNameVisible(true);

        // Configurar atributos base
        configureBossAttributes(entity);

        // Equipar al boss (específico para cada tipo)
        equipBoss();

        // Configurar persistencia
        configurePersistence(entity);

        // Crear barra de boss y efectos
        createBossBar();
        createTaskBossBar();
    }

    // Método común para configurar atributos base
    @SuppressWarnings("")
    protected void configureBossAttributes(Entity entity) {
        if (entity instanceof LivingEntity livingEntity) {
            // Escala
            livingEntity.getAttribute(Attribute.GENERIC_SCALE).setBaseValue(1.2);

            // Salud
            double health = getMaxHealth();
            livingEntity.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(health);
            livingEntity.setHealth(health);

            // Resistencia al fuego
            livingEntity.addPotionEffect(new PotionEffect(
                PotionEffectType.FIRE_RESISTANCE, 
                Integer.MAX_VALUE, 
                0, 
                false, 
                false
            ));
        }
    }

    // Método común para configurar persistencia
    protected void configurePersistence(Entity entity) {
        if (entity instanceof LivingEntity livingEntity) {
            livingEntity.setRemoveWhenFarAway(false);
            livingEntity.setPersistent(true);
            livingEntity.setCanPickupItems(false);
        }
    }

    // Método común para crear la barra de boss
    protected void createBossBar() {
        String title = getBossBarTitle()
        .replace("{boss_name}", getBossName())
        .replace("{health}", "")
        .replace("{max_health}", "")
        .replace("{boss_color}", getBossColor());
        
        if (plugin.getConfigs().getIsLogs()) {
            Console.sendMessage("&a" + plugin.getConfigs().getPrefix() + "&a" + "createBossBar");
            Console.sendMessage("&a" + plugin.getConfigs().getPrefix() + "&a" + "BossBar title: " + title);
        }

        bossBar = Bukkit.createBossBar(
            General.setColor(title),
            General.generateBossBarColor(getBossBarColor()),
            BarStyle.SOLID
        );

        bossBar.setProgress(1.0);
        bossBar.setVisible(true);
    }

    // Método común para actualizar la barra de boss
    protected void updateBossBar() {
        Entity entity = getEntityBoss();
        if (entity == null || bossBar == null || !(entity instanceof LivingEntity livingEntity)) return;

        double health = livingEntity.getHealth();
        AttributeInstance attribute = livingEntity.getAttribute(Attribute.GENERIC_MAX_HEALTH);
        double maxHealth = (attribute != null) ? attribute.getBaseValue() : 0.0;

        String title = getBossBarTitle()
            .replace("{boss_name}", getBossName())
            .replace("{health}", String.format("%.0f", health))
            .replace("{max_health}", String.format("%.0f", maxHealth))
            .replace("{boss_color}", getBossColor());

        bossBar.setTitle(General.setColor(title));
        bossBar.setProgress(Math.max(0, Math.min(health / maxHealth, 1)));

        updateBossBarPlayers(livingEntity.getLocation());
    }

    // Método común para actualizar los jugadores que ven la barra de boss
    @SuppressWarnings("")
    protected void updateBossBarPlayers(Location location) {
        if (bossBar == null) return;
        
        for (Player player : location.getWorld().getPlayers()) {
            if (player.getLocation().distance(location) <= 50 && 
                player.getWorld() == location.getWorld()) {
                bossBar.addPlayer(player);
            } else {
                bossBar.removePlayer(player);
            }
        }
    }

    // Método común para crear la tarea de actualización de la barra de boss
    protected void createTaskBossBar() {
        if (plugin.getConfigs().getIsLogs()) {
            Console.sendMessage("createTaskBossBar");
            if (getEntityBoss() == null) Console.sendMessage("entity es nulo");
            if (bossBar == null) Console.sendMessage("bossBar es nulo");
        }
        this.taskBossBar = new BukkitRunnable() {
            @Override
            public void run() {
                if (!isEntityValid()) {
                    cleanUp();
                    return;
                }
                updateBossBar();
            }
        }.runTaskTimer(plugin, 0L, 20L);
    }

    // Método común para verificar si la entidad es válida
    protected boolean isEntityValid() {
        Entity entity = getEntityBoss();
        return entity != null && entity.isValid() && !entity.isDead();
    }

    // Método común para limpiar recursos
    public void cleanUp() {
        Entity entity = getEntityBoss();
        if (entity != null && entity.getVehicle() instanceof SkeletonHorse horse) {
            horse.remove();
        }

        if (taskBossBar != null) {
            taskBossBar.cancel();
            taskBossBar = null;
        }
        if (taskParticles != null) {
            taskParticles.cancel();
            taskParticles = null;
        }
        if (horseParticlesTask != null) {
            horseParticlesTask.cancel();
            horseParticlesTask = null;
        }
        if (bossBar != null) {
            bossBar.removeAll();
            bossBar = null;
        }

        bossId = null;
    }

    // Método común para recrear la barra de boss
    public void recreateBossBar(Entity entity) {
        if (this.bossBar != null) {
            if (plugin.getConfigs().getIsLogs()) {
                Console.sendMessage(plugin.getConfigs().getPrefix() + "Existe una bossBar asi que eliminamos todo");
            }
            this.bossBar.removeAll();
            this.bossBar = null;
        }
        
        createBossBar();
        // startUpdateTasks();
        createTaskBossBar();
    }

    // Método común para iniciar las tareas de actualización
    // protected void startUpdateTasks() {
    //     if (taskBossBar != null) {
    //         taskBossBar.cancel();
    //     }
    //     if (taskParticles != null) {
    //         taskParticles.cancel();
    //     }
    //     createTaskBossBar();
    // }

    // Métodos de utilidad
    public UUID getBossId() {
        return bossId;
    }

    public void setBossId(UUID uuid) {
        this.bossId = uuid;
    }

    public BossBar getBossBar() {
        return this.bossBar;
    }

    // Método común para crear equipo encantado
    protected ItemStack createEnchantedItem(Material material, String itemName, Map<Enchantment, Integer> enchantments) {
        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            meta.setDisplayName(General.setColor(
                getBossColor() + "&l" + itemName + " &r" + getBossColor() + 
                "(" + getBossName() + ")"
            ));
            
            enchantments.forEach((enchantment, level) -> 
                meta.addEnchant(enchantment, level, true)
            );
            
            item.setItemMeta(meta);
        }
        return item;
    }

    public void handleDeath(EntityDeathEvent event) {
        plugin.getBossPersistenceManager().removeBossData(bossType);
        Player killer = event.getEntity().getKiller();

        event.getDrops().clear();
        event.setDroppedExp(getExperience());

        if (plugin.getConfigs().getBossesCommandEnabled()) {
            if (killer != null) {
                executeKillerCommand(killer);
            }
            executeNearbyPlayersCommands(event.getEntity().getLocation());
        } else {
            generateDrops(event);
        }

        cleanUp();
        plugin.getMainMobs().onSuccessDeath(bossType, killer);
    }

    // Método abstracto que deben implementar las clases hijas
    public abstract void generateDrops(EntityDeathEvent event);

    protected void executeKillerCommand(Player killer) {
        String command = getKillerCommand();
        if (command != null && !command.trim().isEmpty()) {
            command = command.replace("{player_killer}", killer.getName());
            plugin.getServer().dispatchCommand(plugin.getServer().getConsoleSender(), command);
        }
    }
    
    @SuppressWarnings("")
    protected void executeNearbyPlayersCommands(Location location) {
        String command = getNearbyCommand();
        if (isNearbyCommandEnabled() && command != null && !command.trim().isEmpty()) {
            int radius = getNearbyCommandRadius();
    
            for (Entity entity : location.getWorld().getNearbyEntities(location, radius, radius, radius)) {
                if (entity instanceof Player nearbyPlayer) {
                    String playerCommand = command.replace("{player}", nearbyPlayer.getName());
                    plugin.getServer().dispatchCommand(plugin.getServer().getConsoleSender(), playerCommand);
                }
            }
        }
    }

    public void generateHorse() {
        Entity entity = getEntityBoss();
        if (entity != null) {
            generateHorse(getBossName() + " Horse", entity);
        }
    }

    @SuppressWarnings("")
    protected void generateHorse(String horseName, Entity boss) {
        World world = boss.getWorld();
        Location location = boss.getLocation();
        
        // Generar el caballo esqueleto
        SkeletonHorse skeletonHorse = (SkeletonHorse) world.spawnEntity(location, EntityType.SKELETON_HORSE);
        skeletonHorse.setCustomName(horseName);

        // Configurar atributos del caballo
        skeletonHorse.setAdult();
        skeletonHorse.setTamed(true);
        skeletonHorse.setDomestication(1);
        skeletonHorse.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(900.0);
        skeletonHorse.setHealth(900.0);
        skeletonHorse.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED).setBaseValue(0.3);
        skeletonHorse.setJumpStrength(1.0);
        skeletonHorse.getAttribute(Attribute.GENERIC_SCALE).setBaseValue(1.12);

        // Hacer que el caballo sea persistente
        skeletonHorse.setRemoveWhenFarAway(false);
        skeletonHorse.setPersistent(true);

        // Agregar montura al caballo
        skeletonHorse.getInventory().setSaddle(new ItemStack(Material.SADDLE));

        // Dar resistencia al fuego permanente al caballo
        skeletonHorse.addPotionEffect(new PotionEffect(PotionEffectType.FIRE_RESISTANCE, Integer.MAX_VALUE, 0, false, false));

        // Montar el Boss en el caballo
        skeletonHorse.addPassenger(boss);

        world.playSound(location, Sound.ENTITY_LIGHTNING_BOLT_IMPACT, 1.5f, 1.0f);

        // Crear las partículas específicas para el tipo de boss
        createHorseParticles(skeletonHorse);
    }
}