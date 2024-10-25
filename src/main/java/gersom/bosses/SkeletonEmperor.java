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
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Skeleton;
import org.bukkit.entity.SkeletonHorse;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import gersom.TSB;
import gersom.utils.General;

/**
 *
 * @author Gersom
 */
public class SkeletonEmperor extends Boss {
    private BukkitTask horseParticlesTask;
    private UUID skeletonEmperorID = null;
    private final Random random = new Random();
    private BossBar bossBar;
    private Skeleton skeletonEmperor;
    private BukkitTask taskBossBar;
    private BukkitTask taskParticles;

    public SkeletonEmperor(TSB plugin) {
        super(plugin, "skeletonEmperor");
    }

    @SuppressWarnings("")
    public void generateSkeletonEmperor(World world, Location location) {
        this.skeletonEmperor = (Skeleton) world.spawnEntity(location, EntityType.SKELETON);
        
        // Add the skeleton's UUID to our set of custom skeletons
        skeletonEmperorID = skeletonEmperor.getUniqueId();

        skeletonEmperor.setCustomName(General.setColor("&6&l" + plugin.getConfigs().getLangBossEmperorName() + "&r&f"));
        skeletonEmperor.setCustomNameVisible(true);

        // Change scale Mob
        skeletonEmperor.getAttribute(Attribute.GENERIC_SCALE).setBaseValue(1.15);

        // Set health to 20 hearts (40 health points)
        double health = plugin.getConfigs().getBossEmperorHealth();
        skeletonEmperor.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(health);
        skeletonEmperor.setHealth(health);

        // Create and enchant golden helmet
        ItemStack goldenHelmet = new ItemStack(Material.GOLDEN_HELMET);
        ItemMeta helmetMeta = Objects.requireNonNull(goldenHelmet.getItemMeta());
        helmetMeta.addEnchant(Enchantment.UNBREAKING, 3, true); // Unbreaking III
        helmetMeta.addEnchant(Enchantment.PROTECTION, 4, true); // Protection IV
        helmetMeta.setUnbreakable(true); // irrompible
        goldenHelmet.setItemMeta(helmetMeta);

        // Create elytra with Unbreaking III
        ItemStack elytra = new ItemStack(Material.ELYTRA);
        ItemMeta elytraMeta = Objects.requireNonNull(elytra.getItemMeta());
        elytraMeta.addEnchant(Enchantment.UNBREAKING, 3, true); // Unbreaking III
        elytra.setItemMeta(elytraMeta);

        // Create and enchant bow
        ItemStack bowCustom = new ItemStack(Material.BOW);
        ItemMeta bowCustomMeta = Objects.requireNonNull(bowCustom.getItemMeta());
        bowCustomMeta.addEnchant(Enchantment.POWER, 35, true); // Power V
        bowCustomMeta.addEnchant(Enchantment.MULTISHOT, 1, true); // Multishot
        bowCustomMeta.addEnchant(Enchantment.FLAME, 1, true); // Flame
        bowCustomMeta.addEnchant(Enchantment.PUNCH, 2, true); // Punch I
        bowCustom.setItemMeta(bowCustomMeta);

        // Set the equipment on the skeleton
        skeletonEmperor.getEquipment().setHelmet(goldenHelmet);
        skeletonEmperor.getEquipment().setChestplate(elytra);
        skeletonEmperor.getEquipment().setItemInMainHand(bowCustom);
        
        // Ensure the equipment doesn't drop when the skeletonEmperor dies
        skeletonEmperor.getEquipment().setHelmetDropChance(0.0f);
        skeletonEmperor.getEquipment().setChestplateDropChance(0.0f);
        skeletonEmperor.getEquipment().setLeggingsDropChance(0.0f);
        skeletonEmperor.getEquipment().setBootsDropChance(0.0f);
        skeletonEmperor.getEquipment().setItemInMainHandDropChance(0.0f);
        skeletonEmperor.getEquipment().setItemInOffHandDropChance(0.0f);

        skeletonEmperor.setCanPickupItems(false); // Evita que recoja items
        // Make the skeleton persistent
        skeletonEmperor.setRemoveWhenFarAway(false);
        skeletonEmperor.setPersistent(true); // Asegura que no desaparezca

        // Add fire resistance effect
        skeletonEmperor.addPotionEffect(new PotionEffect(PotionEffectType.FIRE_RESISTANCE, Integer.MAX_VALUE, 0, false, true));

        // Crear y configurar la BossBar
        createBossBar();

        // Add dragon breath particles
        // createTaskPaticles();
        
        // Iniciar tarea para actualizar la barra de vida y los jugadores
        createTaskBossBar();
    }

    @Override
    public void generateDrops(EntityDeathEvent event) {
        double dropChance = random.nextDouble();
        
        if (dropChance < 0.33) {
            // 33% chance to drop bow with MULTISHOT and FLAME
            ItemStack bow = new ItemStack(Material.BOW);
            ItemMeta bowMeta = Objects.requireNonNull(bow.getItemMeta());
            bowMeta.setDisplayName(General.setColor(
                "&6&l" + plugin.getConfigs().getLangBossesItemBow() + " &r&6(" + plugin.getConfigs().getLangBossEmperorName() + ")"
            ));
            bowMeta.addEnchant(Enchantment.MULTISHOT, 1, true);
            bowMeta.addEnchant(Enchantment.FLAME, 1, true);
            bow.setItemMeta(bowMeta);
            event.getDrops().add(bow);
        } else if (dropChance < 0.66) {
            // 33% chance to drop bow with INFINITY and UNBREAKING 3
            ItemStack bow = new ItemStack(Material.BOW);
            ItemMeta bowMeta = Objects.requireNonNull(bow.getItemMeta());
            bowMeta.setDisplayName(General.setColor(
                "&6&l" + plugin.getConfigs().getLangBossesItemBow() + " &r&6(" + plugin.getConfigs().getLangBossEmperorName() + ")"
            ));
            bowMeta.addEnchant(Enchantment.INFINITY, 1, true);
            bowMeta.addEnchant(Enchantment.UNBREAKING, 3, true);
            bow.setItemMeta(bowMeta);
            event.getDrops().add(bow);
        } else {
            // 33% chance to drop golden helmet with UNBREAKING 3 and FIRE_PROTECTION 3
            ItemStack helmet = new ItemStack(Material.GOLDEN_HELMET);
            ItemMeta helmetMeta = Objects.requireNonNull(helmet.getItemMeta());
            helmetMeta.setDisplayName(General.setColor(
                "&6&l" + plugin.getConfigs().getLangBossesItemHelmet() + " &r&6(" + plugin.getConfigs().getLangBossEmperorName() + ")"
            ));
            helmetMeta.addEnchant(Enchantment.UNBREAKING, 3, true);
            helmetMeta.addEnchant(Enchantment.FIRE_PROTECTION, 3, true);
            helmet.setItemMeta(helmetMeta);
            event.getDrops().add(helmet);
        }
    }

    @SuppressWarnings("")
    public void generateHorse() {
        World world = skeletonEmperor.getWorld();
        Location location = skeletonEmperor.getLocation();
        
        // Generar el caballo esqueleto
        SkeletonHorse skeletonHorse = (SkeletonHorse) world.spawnEntity(location, EntityType.SKELETON_HORSE);
        skeletonHorse.setCustomName("Skeleton Emperor Horse");

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

        // Montar el Rey Esqueleto en el caballo
        skeletonHorse.addPassenger(skeletonEmperor);

        world.playSound(location, Sound.ENTITY_LIGHTNING_BOLT_IMPACT, 1.5f, 1.0f);

        crearTaskHorseParticles(skeletonHorse);
    }

    public void recreateBossBar(Entity entity) {
        if (entity instanceof Skeleton skeleton1) {
            this.skeletonEmperor = skeleton1;
            
            createBossBar();

            // Reiniciar las tareas de actualización
            startUpdateTasks();
        }
    }

    // Método para obtener la ubicación actual del Skeleton King
    public Location getLocation() {
        return skeletonEmperor != null ? skeletonEmperor.getLocation() : null;
    }

    @Override
    public Skeleton getEntity() {
        return this.skeletonEmperor;
    }

    public UUID getSkeletonEmperorID() {
        return skeletonEmperorID;
    }

    public void setSkeletonEmperorID(UUID uuid) {
        this.skeletonEmperorID = uuid;
    }

    // Private methods
    private void createBossBar() {
        String title = plugin.getConfigs().getBossEmperorBossbarTitle();
        title = title.replace("{boss_name}", plugin.getConfigs().getLangBossEmperorName());
        title = title.replace("{health}", "");
        title = title.replace("{max_health}", "");
        title = title.replace("{boss_color}", plugin.getConfigs().getBossEmperorColor());

        bossBar = Bukkit.createBossBar(
            General.setColor(title),
            General.generateBossBarColor(plugin.getConfigs().getBossEmperorBossbarColor()),
            BarStyle.SOLID
        );

        bossBar.setProgress(1.0);
        bossBar.setVisible(true);
    }

    @SuppressWarnings("")
    private void updateBossBar() {
        if (skeletonEmperor == null || bossBar == null) return;

        double health = skeletonEmperor.getHealth();
        AttributeInstance attribute = skeletonEmperor.getAttribute(Attribute.GENERIC_MAX_HEALTH);
        double maxHealth = (attribute != null) ? attribute.getBaseValue() : 0.0;

        String title = plugin.getConfigs().getBossEmperorBossbarTitle();
        title = title.replace("{boss_name}", plugin.getConfigs().getLangBossEmperorName());
        title = title.replace("{health}", String.format("%.0f", health));
        title = title.replace("{max_health}", String.format("%.0f", maxHealth));
        title = title.replace("{boss_color}", plugin.getConfigs().getBossEmperorColor());

        bossBar.setTitle(General.setColor(title));
        bossBar.setProgress(Math.max(0, Math.min(health / maxHealth, 1)));

        Location loc = skeletonEmperor.getLocation();
        for (Player player : loc.getWorld().getPlayers()) {
            if (player.getLocation().distance(loc) <= 50 && player.getWorld() == loc.getWorld()) {
                bossBar.addPlayer(player);
            } else {
                bossBar.removePlayer(player);
            }
        }
    }

    private void createTaskBossBar() {
        this.taskBossBar = new BukkitRunnable() {
            @Override
            public void run() {
                if (skeletonEmperor == null || !skeletonEmperor.isValid() || skeletonEmperor.isDead()) {
                    cleanUp();
                    return;
                }
                updateBossBar();
            }
        }.runTaskTimer(plugin, 0L, 20L);
    }

    // private void createTaskPaticles() {
    //     this.taskParticles = new BukkitRunnable() {
    //         @Override
    //         public void run() {
    //             if (skeletonEmperor == null || !skeletonEmperor.isValid() || skeletonEmperor.isDead()) {
    //                 cleanUp();
    //                 return;
    //             }
    //             skeletonEmperor.getWorld().spawnParticle(Particle.FLAME, skeletonEmperor.getLocation().add(0, 0, 0), 10, 0.3, 0.3, 0.3, 0.01);
    //         }
    //     }.runTaskTimer(plugin, 0L, 10L);
    // }

    private void startUpdateTasks() {
        // Cancelar tareas existentes si las hay
        if (taskBossBar != null) {
            taskBossBar.cancel();
        }
        if (taskParticles != null) {
            taskParticles.cancel();
        }

        // Reiniciar las tareas
        // createTaskPaticles();
        createTaskBossBar();
        
    }

    private void crearTaskHorseParticles(SkeletonHorse skeletonHorse) {
        // Agregar partículas de fuego azul alrededor del caballo
        horseParticlesTask = new BukkitRunnable() {
            @Override
            public void run() {
                if (skeletonHorse == null || !skeletonHorse.isValid() || skeletonHorse.isDead()) {
                    this.cancel();
                    return;
                }
                Location horseLoc = skeletonHorse.getLocation();
                
                // Fuego azul principal
                skeletonHorse.getWorld().spawnParticle(
                    Particle.FLAME,
                    horseLoc.getX(),
                    horseLoc.getY() + 0.3,
                    horseLoc.getZ(),
                    2, 0.2, 0.4, 0.2, 0.01
                );
                
                // Efecto espiral ascendente
                double angle = (System.currentTimeMillis() / 500.0) % (2 * Math.PI);
                double radius = 1;
                double x = horseLoc.getX() + radius * Math.cos(angle);
                double z = horseLoc.getZ() + radius * Math.sin(angle);
                
                skeletonHorse.getWorld().spawnParticle(
                    Particle.LARGE_SMOKE,
                    x,
                    horseLoc.getY() + 0.3,
                    z,
                    5, 0.05, 0.03, 0.05, 0.01
                );
            }
        }.runTaskTimer(plugin, 0L, 10L); // Actualizamos más frecuentemente para un efecto más suave
    }

    @Override
    public void cleanUp() {
        if (skeletonEmperor != null && skeletonEmperor.getVehicle() != null) {
            Entity horse = skeletonEmperor.getVehicle();
            if (horse instanceof SkeletonHorse) {
                horse.remove();
            }
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
        skeletonEmperor = null;
        // Remove the skeleton's UUID from our set
        skeletonEmperorID = null;
    }

    @Override
    protected int getExperience() {
        return plugin.getConfigs().getBossEmperorExp();
    }

    @Override
    protected String getKillerCommand() {
        return plugin.getConfigs().getBossEmperorCommand();
    }

    @Override
    protected boolean isNearbyCommandEnabled() {
        return plugin.getConfigs().getBossEmperorNearbyCommandEnabled();
    }

    @Override
    protected int getNearbyCommandRadius() {
        return plugin.getConfigs().getBossEmperorNearbyCommandRadius();
    }

    @Override
    protected String getNearbyCommand() {
        return plugin.getConfigs().getBossEmperorNearbyCommand();
    }
}
