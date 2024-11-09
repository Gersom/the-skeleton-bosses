/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package gersom.bosses;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.World;
import org.bukkit.attribute.Attribute;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Skeleton;
import org.bukkit.entity.SkeletonHorse;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import gersom.TSB;
import gersom.utils.General;

/**
 *
 * @author Gersom
 */
public class SkeletonEmperor extends Boss {
    private Skeleton skeletonEmperor = null;
    private int hasGuard = 0;

    public SkeletonEmperor(TSB plugin) {
        super(plugin, "skeletonEmperor");
    }

    @Override
    protected String getBossName() {
        return plugin.getConfigs().getLangBossEmperorName();
    }

    @Override
    protected String getBossColor() {
        return plugin.getConfigs().getBossEmperorColor();
    }

    @Override
    protected String getBossBarColor() {
        return plugin.getConfigs().getBossEmperorBossbarColor();
    }

    @Override
    protected String getBossBarTitle() {
        return plugin.getConfigs().getBossEmperorBossbarTitle();
    }

    @Override
    protected double getMaxHealth() {
        return plugin.getConfigs().getBossEmperorHealth();
    }

    @Override
    protected Entity spawnBossEntity(World world, Location location) {
        skeletonEmperor = (Skeleton) world.spawnEntity(location, EntityType.SKELETON);
        return skeletonEmperor;
    }

    @Override
    @SuppressWarnings("")
    protected void equipBoss() {
        // Crear equipamiento
        Map<Enchantment, Integer> helmetEnchants = new HashMap<>();
        helmetEnchants.put(Enchantment.UNBREAKING, 3);
        helmetEnchants.put(Enchantment.PROTECTION, 4);
        ItemStack helmet = createEnchantedItem(
            Material.GOLDEN_HELMET,
            plugin.getConfigs().getLangBossesItemHelmet(),
            helmetEnchants
        );
        ItemMeta helmetMeta = helmet.getItemMeta();
        if (helmetMeta != null) {
            helmetMeta.setUnbreakable(true);
            helmet.setItemMeta(helmetMeta);
        }

        // Élitros
        Map<Enchantment, Integer> elytraEnchants = new HashMap<>();
        elytraEnchants.put(Enchantment.UNBREAKING, 3);
        ItemStack elytra = createEnchantedItem(
            Material.ELYTRA,
            "Elytra",
            elytraEnchants
        );

        // Arco
        Map<Enchantment, Integer> bowEnchants = new HashMap<>();
        bowEnchants.put(Enchantment.POWER, plugin.getConfigs().getBossEmperorPower());
        bowEnchants.put(Enchantment.MULTISHOT, 1);
        bowEnchants.put(Enchantment.FLAME, 1);
        bowEnchants.put(Enchantment.PUNCH, 2);
        ItemStack bow = createEnchantedItem(
            Material.BOW,
            plugin.getConfigs().getLangBossesItemBow(),
            bowEnchants
        );

        // Equipar items
        skeletonEmperor.getEquipment().setHelmet(helmet);
        skeletonEmperor.getEquipment().setChestplate(elytra);
        skeletonEmperor.getEquipment().setItemInMainHand(bow);

        // Configurar chances de drop
        skeletonEmperor.getEquipment().setHelmetDropChance(0.0f);
        skeletonEmperor.getEquipment().setChestplateDropChance(0.0f);
        skeletonEmperor.getEquipment().setLeggingsDropChance(0.0f);
        skeletonEmperor.getEquipment().setBootsDropChance(0.0f);
        skeletonEmperor.getEquipment().setItemInMainHandDropChance(0.0f);
        skeletonEmperor.getEquipment().setItemInOffHandDropChance(0.0f);
    }

    @Override
    public void generateDrops(EntityDeathEvent event) {
        double dropChance = random.nextDouble();

        Map<Enchantment, Integer> enchantsHelmet = new HashMap<>();
        enchantsHelmet.put(Enchantment.UNBREAKING, 3);
        enchantsHelmet.put(Enchantment.FIRE_PROTECTION, 3);
        ItemStack dropHelmet = createEnchantedItem(
            Material.GOLDEN_HELMET,
            plugin.getConfigs().getLangBossesItemHelmet(),
            enchantsHelmet
        );
        event.getDrops().add(dropHelmet);

        if (dropChance < 0.5) {
            // 50% chance to drop bow with INFINITY and UNBREAKING
            Map<Enchantment, Integer> enchantsBow = new HashMap<>();
            enchantsBow.put(Enchantment.INFINITY, 1);
            enchantsBow.put(Enchantment.UNBREAKING, 3);
            ItemStack dropBow = createEnchantedItem(
                Material.BOW,
                plugin.getConfigs().getLangBossesItemBow(),
                enchantsBow
            );
            event.getDrops().add(dropBow);
        } else if (dropChance < 1) {
            // 50% chance to drop bow with MULTISHOT and FLAME
            Map<Enchantment, Integer> enchantsBow = new HashMap<>();
            enchantsBow.put(Enchantment.MULTISHOT, 1);
            enchantsBow.put(Enchantment.FLAME, 1);
            ItemStack dropBow = createEnchantedItem(
                Material.BOW,
                plugin.getConfigs().getLangBossesItemBow(),
                enchantsBow
            );
            event.getDrops().add(dropBow);
        }
    }

    @Override
    protected void createHorseParticles(SkeletonHorse horse) {
        this.horseParticlesTask = new BukkitRunnable() {
            @Override
            public void run() {
                if (horse == null || !horse.isValid() || horse.isDead()) {
                    this.cancel();
                    return;
                }
                Location horseLoc = horse.getLocation();
                
                // Partículas principales
                horse.getWorld().spawnParticle(
                    Particle.FLAME,
                    horseLoc.getX(),
                    horseLoc.getY() + 0.3,
                    horseLoc.getZ(),
                    5, 0.2, 0.4, 0.2, 0.01
                );
                
                // Efecto espiral
                double angle = (System.currentTimeMillis() / 500.0) % (2 * Math.PI);
                double radius = 1;
                double x = horseLoc.getX() + radius * Math.cos(angle);
                double z = horseLoc.getZ() + radius * Math.sin(angle);
                
                horse.getWorld().spawnParticle(
                    Particle.LARGE_SMOKE,
                    x,
                    horseLoc.getY() + 0.3,
                    z,
                    7, 0.05, 0.03, 0.05, 0.01
                );
            }
        }.runTaskTimer(plugin, 0L, 10L);
    }

    private ItemMeta createIndestructibleEquip(ItemStack item, String itemName) {
        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            meta.setUnbreakable(true);
            meta.setDisplayName(General.setColor(getBossColor() + "Imperial Guard's " + itemName));
        }
        return meta;
    }

    @SuppressWarnings("")
    private void equipGuard(Skeleton guard) {
        // Create and configure the armor items
        ItemStack helmet = new ItemStack(Material.CHAINMAIL_HELMET);
        ItemStack chestplate = new ItemStack(Material.CHAINMAIL_CHESTPLATE);
        ItemStack leggings = new ItemStack(Material.CHAINMAIL_LEGGINGS);
        ItemStack boots = new ItemStack(Material.CHAINMAIL_BOOTS);
        ItemStack sword = new ItemStack(Material.IRON_SWORD);
        ItemStack shield = new ItemStack(Material.SHIELD);

        ItemMeta helmetMeta = createIndestructibleEquip(helmet, "Helmet");
        helmet.setItemMeta(helmetMeta);

        ItemMeta chestplateMeta = createIndestructibleEquip(chestplate, "Chestplate");
        chestplateMeta.addEnchant(Enchantment.BLAST_PROTECTION, 10, true);
        chestplate.setItemMeta(chestplateMeta);

        ItemMeta swordMeta = createIndestructibleEquip(sword, "Sword");
        swordMeta.addEnchant(Enchantment.FIRE_ASPECT, 2, false);
        sword.setItemMeta(swordMeta);

        // Equip the guard
        guard.getEquipment().setHelmet(helmet);
        guard.getEquipment().setChestplate(chestplate);
        guard.getEquipment().setLeggings(leggings);
        guard.getEquipment().setBoots(boots);
        guard.getEquipment().setItemInMainHand(sword);
        guard.getEquipment().setItemInOffHand(shield);

        // Set all drop chances to 0
        guard.getEquipment().setHelmetDropChance(0.0f);
        guard.getEquipment().setChestplateDropChance(0.0f);
        guard.getEquipment().setLeggingsDropChance(0.0f);
        guard.getEquipment().setBootsDropChance(0.0f);
        guard.getEquipment().setItemInMainHandDropChance(0.0f);
    }

    // Add this new method to spawn Guards
    @SuppressWarnings("")
    public void spawnGuards(Player player, int levelGuard) {        
        World world = skeletonEmperor.getWorld();
        Location playerLoc = player.getLocation();
        Vector direction = playerLoc.getDirection();
        
        // Calculate spawn position behind the player
        double distance = 4.0; // Distance behind player
        double spawnX = playerLoc.getX() - direction.getX() * distance;
        double spawnZ = playerLoc.getZ() - direction.getZ() * distance;
        double spawnY = world.getHighestBlockYAt((int)spawnX, (int)spawnZ) + 1;
        
        Location spawnLoc = new Location(world, spawnX, spawnY, spawnZ);
        
        // Spawn 3 Guards
        for (int i = 0; i < levelGuard; i++) {
            Skeleton guard = (Skeleton) world.spawnEntity(spawnLoc, EntityType.SKELETON);
            
            // Configure guard getBossName()
            guard.setCustomName(General.setColor(
                getBossColor() + "&l" + plugin.getConfigs().getLangBossEmperorGuard()
            ));
            guard.setCustomNameVisible(true);

            // Set guard attributes
            if (guard.getAttribute(Attribute.GENERIC_MAX_HEALTH) != null) {
                guard.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue((int) ((levelGuard * 2) - 1) + 10); // 15x hearts
            }
            if (guard.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED) != null) {
                guard.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED).setBaseValue(0.3); // Slightly faster than normal
            }
            guard.setHealth((int) ((levelGuard * 2) - 1) + 10);
            guard.getAttribute(Attribute.GENERIC_ATTACK_DAMAGE)
                .setBaseValue(4 + levelGuard);
            
            // Equip the guard with armor and weapons
            equipGuard(guard);
            
            // Adjust spawn location for next sentinel
            spawnLoc.add(random.nextDouble() - 1, 0, random.nextDouble() - 1);
        }
        
        // Play effects
        world.spawnParticle(Particle.FLAME, spawnLoc, 50, 1, 1, 1, 0.1);
        world.playSound(spawnLoc, org.bukkit.Sound.ENTITY_WITHER_SPAWN, 1.0f, 1.0f);
        
        hasGuard = hasGuard + 1;
    }

    // Add this method to handle damage events
    @SuppressWarnings("")
    public void generateGuards(EntityDamageByEntityEvent event) {
        if (!(event.getDamager() instanceof Player player)) return;
        
        double currentHealth = skeletonEmperor.getHealth();
        double maxHealth = skeletonEmperor.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue();
        double healthPercentage = (currentHealth / maxHealth) * 100;
        
        if (healthPercentage <= 25 && hasGuard < 3) {
            spawnGuards(player, 3);
        } else if (healthPercentage <= 50 && hasGuard < 2) {
            spawnGuards(player, 2);
        } else if (healthPercentage <= 75 && hasGuard == 0) {
            spawnGuards(player, 1);
        }
    }

    @Override
    public Skeleton getEntityBoss() {
        return skeletonEmperor;
    }

    public void setEntityBoss(Skeleton entity) {
        skeletonEmperor = entity;
    }

    @Override
    protected int getExperience() {
        return plugin.getConfigs().getBossEmperorExp();
    }

    @Override
    protected List<String> getKillerCommands() {
        return plugin.getConfigs().getBossEmperorCommands();
    }

    @Override
    protected boolean isNearbyCommandsEnabled() {
        return plugin.getConfigs().getBossEmperorNearbyCommandsEnabled();
    }

    @Override
    protected int getNearbyCommandsRadius() {
        return plugin.getConfigs().getBossEmperorNearbyCommandsRadius();
    }

    @Override
    protected List<String> getNearbyCommands() {
        return plugin.getConfigs().getBossEmperorNearbyCommands();
    }
}