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
import org.bukkit.World;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.SkeletonHorse;
import org.bukkit.entity.WitherSkeleton;
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
  public class SkeletonKing extends Boss {
    private BukkitTask horseParticlesTask;
    private UUID skeletonKingID = null;
    private final Random random = new Random();
    private BossBar bossBar;
    private WitherSkeleton skeletonKing;
    private BukkitTask taskBossBar;
    private BukkitTask taskParticles;

    public SkeletonKing(TSB plugin) {
        super(plugin, "skeletonKing");
    }

    @SuppressWarnings("")
    public void generateSkeletonKing(World world, Location location) {
        this.skeletonKing = (WitherSkeleton) world.spawnEntity(location, EntityType.WITHER_SKELETON);

        // Add the skeleton's UUID to our set of custom skeletons
        skeletonKingID = skeletonKing.getUniqueId();

        skeletonKing.setCustomName(General.setColor("&d&l" + plugin.getConfigs().getLangBossKingName() + "&r&f"));
        skeletonKing.setCustomNameVisible(true);

        // Change scale Mob
        skeletonKing.getAttribute(Attribute.GENERIC_SCALE).setBaseValue(1.25);
        skeletonKing.getAttribute(Attribute.GENERIC_ATTACK_DAMAGE).setBaseValue(
            plugin.getConfigs().getBossKingDamage()
        );

        // Set health to 20 hearts (40 health points)
        double health = plugin.getConfigs().getBossKingHealth();
        skeletonKing.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(health);
        skeletonKing.setHealth(health);

        // Create and enchant golden helmet
        ItemStack goldenHelmet = new ItemStack(Material.GOLDEN_HELMET);
        ItemMeta helmetMeta = Objects.requireNonNull(goldenHelmet.getItemMeta());
        helmetMeta.addEnchant(Enchantment.UNBREAKING, 3, true); // Unbreaking III
        helmetMeta.addEnchant(Enchantment.PROTECTION, 4, true); // Protection IV
        helmetMeta.setUnbreakable(true); // irrompible
        goldenHelmet.setItemMeta(helmetMeta);

        // Escudo
        ItemStack shield = new ItemStack(Material.SHIELD);
        ItemMeta shieldMeta = Objects.requireNonNull(shield.getItemMeta());
        shieldMeta.addEnchant(Enchantment.UNBREAKING, 3, true); // Unbreaking III
        shieldMeta.setUnbreakable(true); // irrompible
        shield.setItemMeta(shieldMeta);

        // Create elytra with Unbreaking III
        ItemStack elytra = new ItemStack(Material.ELYTRA);
        ItemMeta elytraMeta = Objects.requireNonNull(elytra.getItemMeta());
        elytraMeta.addEnchant(Enchantment.UNBREAKING, 3, true); // Unbreaking III
        elytra.setItemMeta(elytraMeta);

        // Create and enchant bow
        ItemStack swordCustom = new ItemStack(Material.NETHERITE_SWORD);
        ItemMeta swordCustomMeta = Objects.requireNonNull(swordCustom.getItemMeta());
        swordCustomMeta.addEnchant(Enchantment.UNBREAKING, 3, true); // Unbreaking III
        swordCustom.setItemMeta(swordCustomMeta);

        // Set the equipment on the skeleton
        skeletonKing.getEquipment().setHelmet(goldenHelmet);
        skeletonKing.getEquipment().setItemInOffHand(shield);
        skeletonKing.getEquipment().setChestplate(elytra);
        skeletonKing.getEquipment().setItemInMainHand(swordCustom);
        
        // Ensure the equipment doesn't drop when the skeleton dies
        skeletonKing.getEquipment().setHelmetDropChance(0.0f);
        skeletonKing.getEquipment().setChestplateDropChance(0.0f);
        skeletonKing.getEquipment().setLeggingsDropChance(0.0f);
        skeletonKing.getEquipment().setBootsDropChance(0.0f);
        skeletonKing.getEquipment().setItemInMainHandDropChance(0.0f);
        skeletonKing.getEquipment().setItemInOffHandDropChance(0.0f);

        skeletonKing.setCanPickupItems(false); // Evita que recoja items
        // Make the skeleton persistent
        skeletonKing.setRemoveWhenFarAway(false);
        skeletonKing.setPersistent(true); // Asegura que no desaparezca

        // Add fire resistance effect
        skeletonKing.addPotionEffect(new PotionEffect(PotionEffectType.FIRE_RESISTANCE, Integer.MAX_VALUE, 0, false, false));

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
            // 33% chance to drop NETHERITE_SWORD with SHARPNESS
            ItemStack sword = new ItemStack(Material.NETHERITE_SWORD);
            ItemMeta swordMeta = Objects.requireNonNull(sword.getItemMeta());
            swordMeta.setDisplayName(General.setColor(
                "&d&l" + plugin.getConfigs().getLangBossesItemSword() + " &r&d(" + plugin.getConfigs().getLangBossKingName() + ")" 
            ));
            swordMeta.addEnchant(Enchantment.SHARPNESS, 5, true);
            sword.setItemMeta(swordMeta);
            event.getDrops().add(sword);
        } else if (dropChance < 0.66) {
            // 33% chance to drop NETHERITE_SWORD with SWEEPING_EDGE
            ItemStack sword = new ItemStack(Material.NETHERITE_SWORD);
            ItemMeta swordMeta = Objects.requireNonNull(sword.getItemMeta());
            swordMeta.setDisplayName(General.setColor(
                "&d&l" + plugin.getConfigs().getLangBossesItemSword() + " &r&d(" + plugin.getConfigs().getLangBossKingName() + ")"
            ));
            swordMeta.addEnchant(Enchantment.SWEEPING_EDGE, 3, true);
            sword.setItemMeta(swordMeta);
            event.getDrops().add(sword);
        } else {
            // 33% chance to drop golden helmet with UNBREAKING 3 and FIRE_PROTECTION 3
            ItemStack helmet = new ItemStack(Material.GOLDEN_HELMET);
            ItemMeta helmetMeta = Objects.requireNonNull(helmet.getItemMeta());
            helmetMeta.setDisplayName(General.setColor(
                "&d&l" + plugin.getConfigs().getLangBossesItemHelmet() + " &r&d(" + plugin.getConfigs().getLangBossKingName() + ")"
            ));
            helmetMeta.addEnchant(Enchantment.UNBREAKING, 3, true);
            helmetMeta.addEnchant(Enchantment.FIRE_PROTECTION, 3, true);
            helmetMeta.addEnchant(Enchantment.PROTECTION, 4, true);
            helmet.setItemMeta(helmetMeta);
            event.getDrops().add(helmet);
        }
    }

    public void recreateBossBar(Entity entity) {
        if (entity instanceof WitherSkeleton witherSkeleton) {
            this.skeletonKing = witherSkeleton;
            
            // Recrear la BossBar
            createBossBar();

            // Reiniciar las tareas de actualización
            startUpdateTasks();
        }
    }

    // Método para obtener la ubicación actual del Skeleton King
    public Location getLocation() {
        return skeletonKing != null ? skeletonKing.getLocation() : null;
    }

    public WitherSkeleton getSkeletonKingEntity() {
        return this.skeletonKing;
    }

    public UUID getSkeletonKingID() {
        return skeletonKingID;
    }

    public void setSkeletonKingID(UUID uuid) {
        this.skeletonKingID = uuid;
    }

    // Private methods
    private void createBossBar() {
        String title = plugin.getConfigs().getBossKingBossbarTitle();
        title = title.replace("{boss_name}", plugin.getConfigs().getLangBossKingName());
        title = title.replace("{health}", "");
        title = title.replace("{max_health}", "");
        title = title.replace("{boss_color}", plugin.getConfigs().getBossKingColor());

        bossBar = Bukkit.createBossBar(
            General.setColor(title),
            General.generateBossBarColor(plugin.getConfigs().getBossKingBossbarColor()),
            BarStyle.SOLID
        );

        bossBar.setProgress(1.0);
        bossBar.setVisible(true);
    }

    @SuppressWarnings("")
    private void updateBossBar() {
        if (skeletonKing == null || bossBar == null) return;

        double health = skeletonKing.getHealth();
        AttributeInstance attribute = skeletonKing.getAttribute(Attribute.GENERIC_MAX_HEALTH);
        double maxHealth = (attribute != null) ? attribute.getBaseValue() : 0.0;

        String title = plugin.getConfigs().getBossKingBossbarTitle();
        title = title.replace("{boss_name}", plugin.getConfigs().getLangBossKingName());
        title = title.replace("{health}", String.format("%.0f", health));
        title = title.replace("{max_health}", String.format("%.0f", maxHealth));
        title = title.replace("{boss_color}", plugin.getConfigs().getBossKingColor());

        bossBar.setTitle(General.setColor(title));
        bossBar.setProgress(Math.max(0, Math.min(health / maxHealth, 1)));

        Location loc = skeletonKing.getLocation();
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
                if (skeletonKing == null || !skeletonKing.isValid() || skeletonKing.isDead()) {
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
    //             if (skeletonKing == null || !skeletonKing.isValid() || skeletonKing.isDead()) {
    //                 cleanUp();
    //                 return;
    //             }
    //             skeletonKing.getWorld().spawnParticle(Particle.DRAGON_BREATH, skeletonKing.getLocation().add(0, 0, 0), 10, 0.3, 0.3, 0.3, 0.01);
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

    public void generateHorse() {
        generateHorse("Skeleton King Horse", skeletonKing);
    }

    @Override
    protected void createHorseParticles(SkeletonHorse skeletonHorse) {
        this.horseParticlesTask = new BukkitRunnable() {
            @Override
            public void run() {
                if (skeletonHorse == null || !skeletonHorse.isValid() || skeletonHorse.isDead()) {
                    this.cancel();
                    return;
                }
                Location horseLoc = skeletonHorse.getLocation();
                
                // Fuego azul principal
                skeletonHorse.getWorld().spawnParticle(
                    Particle.DRAGON_BREATH,
                    horseLoc.getX(),
                    horseLoc.getY() + 0.5,
                    horseLoc.getZ(),
                    5, 0.2, 0.4, 0.2, 0.01
                );
                
                // Efecto espiral ascendente
                double angle = (System.currentTimeMillis() / 500.0) % (2 * Math.PI);
                double radius = 1;
                double x = horseLoc.getX() + radius * Math.cos(angle);
                double z = horseLoc.getZ() + radius * Math.sin(angle);
                
                skeletonHorse.getWorld().spawnParticle(
                    Particle.SOUL,
                    x,
                    horseLoc.getY() + 0.5,
                    z,
                    5, 0.05, 0.03, 0.05, 0
                );
            }
        }.runTaskTimer(plugin, 0L, 10L);
    }

    @Override
    public void cleanUp() {
        if (skeletonKing != null && skeletonKing.getVehicle() != null) {
            Entity horse = skeletonKing.getVehicle();
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
        skeletonKing = null;
        // Remove the skeleton's UUID from our set
        skeletonKingID = null;
    }

    @Override
    public Entity getEntity() {
        return skeletonKing;
    }

    @Override
    protected int getExperience() {
        return plugin.getConfigs().getBossKingExp();
    }

    @Override
    protected String getKillerCommand() {
        return plugin.getConfigs().getBossKingCommand();
    }

    @Override
    protected boolean isNearbyCommandEnabled() {
        return plugin.getConfigs().getBossKingNearbyCommandEnabled();
    }

    @Override
    protected int getNearbyCommandRadius() {
        return plugin.getConfigs().getBossKingNearbyCommandRadius();
    }

    @Override
    protected String getNearbyCommand() {
        return plugin.getConfigs().getBossKingNearbyCommand();
    }
}
 