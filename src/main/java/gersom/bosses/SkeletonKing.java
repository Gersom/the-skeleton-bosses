/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package gersom.bosses;

import java.util.HashMap;
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
import org.bukkit.entity.WitherSkeleton;
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
  public class SkeletonKing extends Boss {
    private WitherSkeleton skeletonKing = null;
    private boolean hasSpawnedMinions = false;

    public SkeletonKing(TSB plugin) {
        super(plugin, "skeletonKing");
    }

    @Override
    protected String getBossName() {
        return plugin.getConfigs().getLangBossKingName();
    }

    @Override
    protected String getBossColor() {
        return plugin.getConfigs().getBossKingColor();
    }

    @Override
    protected String getBossBarColor() {
        return plugin.getConfigs().getBossKingBossbarColor();
    }

    @Override
    protected String getBossBarTitle() {
        return plugin.getConfigs().getBossKingBossbarTitle();
    }

    @Override
    protected double getMaxHealth() {
        return plugin.getConfigs().getBossKingHealth();
    }

    @Override
    @SuppressWarnings("")
    protected Entity spawnBossEntity(World world, Location location) {
        skeletonKing = (WitherSkeleton) world.spawnEntity(location, EntityType.WITHER_SKELETON);
        
        // Configurar daño específico del Rey Esqueleto
        skeletonKing.getAttribute(Attribute.GENERIC_ATTACK_DAMAGE)
            .setBaseValue(plugin.getConfigs().getBossKingDamage());
        
        return skeletonKing;
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

        // Escudo
        Map<Enchantment, Integer> shieldEnchants = new HashMap<>();
        shieldEnchants.put(Enchantment.UNBREAKING, 3);
        ItemStack shield = createEnchantedItem(
            Material.SHIELD, 
            "Shield",
            shieldEnchants
        );
        ItemMeta shieldMeta = shield.getItemMeta();
        if (shieldMeta != null) {
            shieldMeta.setUnbreakable(true);
            shield.setItemMeta(shieldMeta);
        }

        // Élitros
        Map<Enchantment, Integer> elytraEnchants = new HashMap<>();
        elytraEnchants.put(Enchantment.UNBREAKING, 3);
        ItemStack elytra = createEnchantedItem(
            Material.ELYTRA,
            "Elytra",
            elytraEnchants
        );

        // Espada
        Map<Enchantment, Integer> swordEnchants = new HashMap<>();
        swordEnchants.put(Enchantment.UNBREAKING, 3);
        ItemStack sword = createEnchantedItem(
            Material.NETHERITE_SWORD,
            plugin.getConfigs().getLangBossesItemSword(),
            swordEnchants
        );

        // Equipar items
        skeletonKing.getEquipment().setHelmet(helmet);
        skeletonKing.getEquipment().setItemInOffHand(shield);
        skeletonKing.getEquipment().setChestplate(elytra);
        skeletonKing.getEquipment().setItemInMainHand(sword);

        // Configurar chances de drop
        skeletonKing.getEquipment().setHelmetDropChance(0.0f);
        skeletonKing.getEquipment().setChestplateDropChance(0.0f);
        skeletonKing.getEquipment().setLeggingsDropChance(0.0f);
        skeletonKing.getEquipment().setBootsDropChance(0.0f);
        skeletonKing.getEquipment().setItemInMainHandDropChance(0.0f);
        skeletonKing.getEquipment().setItemInOffHandDropChance(0.0f);
    }

    @Override
    public void generateDrops(EntityDeathEvent event) {
        double dropChance = random.nextDouble();

        if (dropChance < 0.33) {
            // 33% chance to drop NETHERITE_SWORD with SHARPNESS
            Map<Enchantment, Integer> enchants = new HashMap<>();
            enchants.put(Enchantment.SHARPNESS, 5);
            ItemStack drop = createEnchantedItem(
                Material.NETHERITE_SWORD,
                plugin.getConfigs().getLangBossesItemSword(),
                enchants
            );
            event.getDrops().add(drop);
        } else if (dropChance < 0.66) {
            // 33% chance to drop NETHERITE_SWORD with SWEEPING_EDGE
            Map<Enchantment, Integer> enchants = new HashMap<>();
            enchants.put(Enchantment.SWEEPING_EDGE, 3);
            ItemStack drop = createEnchantedItem(
                Material.NETHERITE_SWORD,
                plugin.getConfigs().getLangBossesItemSword(),
                enchants
            );
            event.getDrops().add(drop);
        } else {
            // 33% chance to drop golden helmet
            Map<Enchantment, Integer> enchants = new HashMap<>();
            enchants.put(Enchantment.UNBREAKING, 3);
            enchants.put(Enchantment.FIRE_PROTECTION, 3);
            enchants.put(Enchantment.PROTECTION, 4);
            ItemStack drop = createEnchantedItem(
                Material.GOLDEN_HELMET,
                plugin.getConfigs().getLangBossesItemHelmet(),
                enchants
            );
            event.getDrops().add(drop);
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
                    Particle.DRAGON_BREATH,
                    horseLoc.getX(),
                    horseLoc.getY() + 0.5,
                    horseLoc.getZ(),
                    5, 0.2, 0.4, 0.2, 0.01
                );
                
                // Efecto espiral
                double angle = (System.currentTimeMillis() / 500.0) % (2 * Math.PI);
                double radius = 1;
                double x = horseLoc.getX() + radius * Math.cos(angle);
                double z = horseLoc.getZ() + radius * Math.sin(angle);
                
                horse.getWorld().spawnParticle(
                    Particle.SOUL,
                    x,
                    horseLoc.getY() + 0.5,
                    z,
                    5, 0.05, 0.03, 0.05, 0
                );
            }
        }.runTaskTimer(plugin, 0L, 10L);
    }

    private ItemStack createIndestructibleHelmet() {
        ItemStack helmet = new ItemStack(Material.NETHERITE_HELMET);
        ItemMeta meta = helmet.getItemMeta();
        if (meta != null) {
            meta.setUnbreakable(true);
            meta.addEnchant(Enchantment.PROJECTILE_PROTECTION, 10, true);
            meta.setDisplayName(General.setColor(getBossColor() + "Minion's Helmet"));
            helmet.setItemMeta(meta);
        }
        return helmet;
    }

    @SuppressWarnings("")
    private void equipMinion(Skeleton minion) {
        // Create and configure the armor items
        ItemStack helmet = createIndestructibleHelmet();
        ItemStack chestplate = new ItemStack(Material.NETHERITE_CHESTPLATE);
        ItemStack leggings = new ItemStack(Material.NETHERITE_LEGGINGS);
        ItemStack boots = new ItemStack(Material.NETHERITE_BOOTS);
        ItemStack bow = new ItemStack(Material.BOW);

        // Add enchantments to the bow
        ItemMeta bowMeta = bow.getItemMeta();
        if (bowMeta != null) {
            bowMeta.addEnchant(Enchantment.POWER, 5, true);
            bowMeta.setDisplayName(General.setColor(getBossColor() + "Minion's Bow"));
            bow.setItemMeta(bowMeta);
        }

        // Equip the minion
        minion.getEquipment().setHelmet(helmet);
        minion.getEquipment().setChestplate(chestplate);
        minion.getEquipment().setLeggings(leggings);
        minion.getEquipment().setBoots(boots);
        minion.getEquipment().setItemInMainHand(bow);

        // Set all drop chances to 0
        minion.getEquipment().setHelmetDropChance(0.0f);
        minion.getEquipment().setChestplateDropChance(0.0f);
        minion.getEquipment().setLeggingsDropChance(0.0f);
        minion.getEquipment().setBootsDropChance(0.0f);
        minion.getEquipment().setItemInMainHandDropChance(0.0f);
    }

    // Add this new method to spawn minions
    @SuppressWarnings("")
    public void spawnMinions(Player player) {
        if (hasSpawnedMinions) return;
        
        World world = skeletonKing.getWorld();
        Location playerLoc = player.getLocation();
        Vector direction = playerLoc.getDirection();
        
        // Calculate spawn position behind the player
        double distance = 7.0; // Distance behind player
        double spawnX = playerLoc.getX() - direction.getX() * distance;
        double spawnZ = playerLoc.getZ() - direction.getZ() * distance;
        double spawnY = world.getHighestBlockYAt((int)spawnX, (int)spawnZ) + 1;
        
        Location spawnLoc = new Location(world, spawnX, spawnY, spawnZ);
        
        // Spawn 3 minions
        for (int i = 0; i < 3; i++) {
            Skeleton minion = (Skeleton) world.spawnEntity(spawnLoc, EntityType.SKELETON);
            
            // Configure minion getBossName()
            minion.setCustomName(General.setColor(getBossColor() + "Arquero Real "));
            minion.setCustomNameVisible(true);
            // minion.setRemoveWhenFarAway(false);
            // minion.setPersistent(true);

            // Set minion attributes
            if (minion.getAttribute(Attribute.GENERIC_MAX_HEALTH) != null) {
                minion.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(40.0); // 20 hearts
            }
            if (minion.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED) != null) {
                minion.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED).setBaseValue(0.3); // Slightly faster than normal
            }
            minion.setHealth(40.0);
            
            // Equip the minion with armor and weapons
            equipMinion(minion);
            
            // Adjust spawn location for next minion
            spawnLoc.add(random.nextDouble() - 0.5, 0, random.nextDouble() - 0.5);
        }
        
        // Play effects
        world.spawnParticle(Particle.SOUL_FIRE_FLAME, spawnLoc, 50, 1, 1, 1, 0.1);
        world.playSound(spawnLoc, org.bukkit.Sound.ENTITY_WITHER_SPAWN, 1.0f, 1.0f);
        
        hasSpawnedMinions = true;
    }

    // Add this method to handle damage events
    @SuppressWarnings("")
    public void onDamage(EntityDamageByEntityEvent event) {
        if (!(event.getDamager() instanceof Player player)) return;
        
        double currentHealth = skeletonKing.getHealth();
        double maxHealth = skeletonKing.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue();
        double healthPercentage = (currentHealth / maxHealth) * 100;
        
        if (healthPercentage <= 75 && !hasSpawnedMinions) {
            spawnMinions(player);
        }
    }

    // Reset minion spawn state when the boss is generated
    @Override
    public void generateBoss(World world, Location location) {
        super.generateBoss(world, location);
        hasSpawnedMinions = false;
    }

    
    @Override
    public WitherSkeleton getEntityBoss() {
        return skeletonKing;
    }

    public void setEntityBoss(WitherSkeleton entity) {
        skeletonKing = entity;
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