/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package gersom.generators;

import java.util.Objects;
import java.util.Random;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
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
public class SkeletonEmperor {
    private UUID skeletonEmperorID = null;
    private final TSB plugin;
    private final Random random = new Random();
    private BossBar bossBar;
    private Skeleton skeletonEmperor;
    private BukkitTask taskBossBar;
    private BukkitTask taskParticles;

    public SkeletonEmperor(TSB plugin) {
        this.plugin = plugin;
    }

    public void generateSkeletonEmperor(World world, Location location) {
        this.skeletonEmperor = (Skeleton) world.spawnEntity(location, EntityType.SKELETON);
        
        // Add the skeleton's UUID to our set of custom skeletons
        skeletonEmperorID = skeletonEmperor.getUniqueId();

        // Make the skeleton persistent
        skeletonEmperor.setRemoveWhenFarAway(false);

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
        goldenHelmet.setItemMeta(helmetMeta);

        // Create elytra with Unbreaking III
        ItemStack elytra = new ItemStack(Material.ELYTRA);
        ItemMeta elytraMeta = Objects.requireNonNull(elytra.getItemMeta());
        elytraMeta.addEnchant(Enchantment.UNBREAKING, 3, true); // Unbreaking III
        elytra.setItemMeta(elytraMeta);

        // Create and enchant bow
        ItemStack bowCustom = new ItemStack(Material.BOW);
        ItemMeta bowCustomMeta = Objects.requireNonNull(bowCustom.getItemMeta());
        bowCustomMeta.addEnchant(Enchantment.MULTISHOT, 1, true); // Multishot
        bowCustomMeta.addEnchant(Enchantment.FLAME, 1, true); // Flame
        bowCustomMeta.addEnchant(Enchantment.POWER, 30, true); // Power V
        bowCustomMeta.addEnchant(Enchantment.UNBREAKING, 3, true); // Unbreaking III
        bowCustomMeta.addEnchant(Enchantment.INFINITY, 1, true); // Infinity
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

        // Add fire resistance effect
        skeletonEmperor.addPotionEffect(new PotionEffect(PotionEffectType.FIRE_RESISTANCE, 999999, 0, false, true));

        // Crear y configurar la BossBar
        createBossBar();

        // Add dragon breath particles
        createTaskPaticles();
        
        // Iniciar tarea para actualizar la barra de vida y los jugadores
        createTaskBossBar();
    }

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

    public Skeleton getSkeletonEmperorEntity() {
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

    private void createTaskPaticles() {
        this.taskParticles = new BukkitRunnable() {
            @Override
            public void run() {
                if (skeletonEmperor == null || !skeletonEmperor.isValid() || skeletonEmperor.isDead()) {
                    cleanUp();
                    return;
                }
                skeletonEmperor.getWorld().spawnParticle(Particle.FLAME, skeletonEmperor.getLocation().add(0, 0, 0), 10, 0.3, 0.3, 0.3, 0.01);
            }
        }.runTaskTimer(plugin, 0L, 10L);
    }

    private void startUpdateTasks() {
        // Cancelar tareas existentes si las hay
        if (taskBossBar != null) {
            taskBossBar.cancel();
        }
        if (taskParticles != null) {
            taskParticles.cancel();
        }

        // Reiniciar las tareas
        createTaskPaticles();
        createTaskBossBar();
        
    }

    public void cleanUp() {
        if (taskBossBar != null) {
            taskBossBar.cancel();
            taskBossBar = null;
        }
        if (taskParticles != null) {
            taskParticles.cancel();
            taskParticles = null;
        }
        if (bossBar != null) {
            bossBar.removeAll();
            bossBar = null;
        }
        skeletonEmperor = null;
        // Remove the skeleton's UUID from our set
        skeletonEmperorID = null;
    }
}
