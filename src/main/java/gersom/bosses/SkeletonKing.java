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
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
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
    private int hasSentinels = 0;

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

        Map<Enchantment, Integer> enchants = new HashMap<>();
        enchants.put(Enchantment.UNBREAKING, 3);
        enchants.put(Enchantment.BLAST_PROTECTION, 3);
        ItemStack drop = createEnchantedItem(
            Material.GOLDEN_HELMET,
            plugin.getConfigs().getLangBossesItemHelmet(),
            enchants
        );
        event.getDrops().add(drop);
  
        if (dropChance < 0.5) {
            // 33% chance to drop NETHERITE_SWORD with SWEEPING_EDGE
            Map<Enchantment, Integer> enchantsSword = new HashMap<>();
            enchantsSword.put(Enchantment.SWEEPING_EDGE, 3);
            ItemStack dropSword = createEnchantedItem(
                Material.NETHERITE_SWORD,
                plugin.getConfigs().getLangBossesItemSword(),
                enchantsSword
            );
            event.getDrops().add(dropSword);
        } else if (dropChance < 1) {
            Map<Enchantment, Integer> enchantsSword = new HashMap<>();
            enchantsSword.put(Enchantment.SHARPNESS, 5);
            ItemStack dropSword = createEnchantedItem(
                Material.NETHERITE_SWORD,
                plugin.getConfigs().getLangBossesItemSword(),
                enchantsSword
            );
            event.getDrops().add(dropSword);
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
            meta.setDisplayName(General.setColor(getBossColor() + "Sentinel's Helmet"));
            helmet.setItemMeta(meta);
        }
        return helmet;
    }

    @SuppressWarnings("")
    private void equipSentinel(Skeleton sentinel, int levelSentinel) {
        // Create and configure the armor items
        ItemStack helmet = createIndestructibleHelmet();
        ItemStack chestplate = new ItemStack(Material.NETHERITE_CHESTPLATE);
        ItemStack leggings = new ItemStack(Material.NETHERITE_LEGGINGS);
        ItemStack boots = new ItemStack(Material.NETHERITE_BOOTS);
        ItemStack bow = new ItemStack(Material.BOW);
        ItemStack arrow = new ItemStack(Material.TIPPED_ARROW, 3);

        PotionMeta metaArrow = (PotionMeta) arrow.getItemMeta();
        PotionEffect strongSlownes = new PotionEffect(PotionEffectType.SLOWNESS, 200, levelSentinel, true, true, true);
        metaArrow.addCustomEffect(strongSlownes, true);
        arrow.setItemMeta(metaArrow);

        // Add enchantments to the bow
        ItemMeta bowMeta = bow.getItemMeta();
        if (bowMeta != null) {
            bowMeta.addEnchant(Enchantment.POWER, 1 + levelSentinel, true);
            bowMeta.addEnchant(Enchantment.INFINITY, 1, false);
            bowMeta.setDisplayName(General.setColor(getBossColor() + "Sentinel's Bow"));
            bow.setItemMeta(bowMeta);
        }

        // Equip the sentinel
        sentinel.getEquipment().setHelmet(helmet);
        sentinel.getEquipment().setChestplate(chestplate);
        sentinel.getEquipment().setLeggings(leggings);
        sentinel.getEquipment().setBoots(boots);
        sentinel.getEquipment().setItemInMainHand(bow);
        sentinel.getEquipment().setItemInOffHand(arrow);

        // Set all drop chances to 0
        sentinel.getEquipment().setHelmetDropChance(0.0f);
        sentinel.getEquipment().setChestplateDropChance(0.0f);
        sentinel.getEquipment().setLeggingsDropChance(0.0f);
        sentinel.getEquipment().setBootsDropChance(0.0f);
        sentinel.getEquipment().setItemInMainHandDropChance(0.0f);
    }

    // Add this new method to spawn Sentinels
    @SuppressWarnings("")
    public void spawnSentinels(Player player, int levelSentinel) {        
        World world = skeletonKing.getWorld();
        Location playerLoc = player.getLocation();
        Vector direction = playerLoc.getDirection();
        
        // Calculate spawn position behind the player
        double distance = 7.0; // Distance behind player
        double spawnX = playerLoc.getX() - direction.getX() * distance;
        double spawnZ = playerLoc.getZ() - direction.getZ() * distance;
        double spawnY = world.getHighestBlockYAt((int)spawnX, (int)spawnZ) + 1;
        
        Location spawnLoc = new Location(world, spawnX, spawnY, spawnZ);
        
        // Spawn 3 Sentinels
        for (int i = 0; i < levelSentinel; i++) {
            Skeleton sentinel = (Skeleton) world.spawnEntity(spawnLoc, EntityType.SKELETON);
            
            // Configure sentinel getBossName()
            sentinel.setCustomName(General.setColor(
                getBossColor() + "&l" + plugin.getConfigs().getLangBossKingSentinel()
            ));
            sentinel.setCustomNameVisible(true);

            // Set sentinel attributes
            if (sentinel.getAttribute(Attribute.GENERIC_MAX_HEALTH) != null) {
                sentinel.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue((int) ((levelSentinel * 2) - 1) + 10); // 15x hearts
            }
            if (sentinel.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED) != null) {
                sentinel.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED).setBaseValue(0.3); // Slightly faster than normal
            }
            sentinel.setHealth((int) ((levelSentinel * 2) - 1) + 10);
            
            // Equip the sentinel with armor and weapons
            equipSentinel(sentinel, levelSentinel);
            
            // Adjust spawn location for next sentinel
            spawnLoc.add(random.nextDouble() - 1, 0, random.nextDouble() - 1);
        }
        
        // Play effects
        world.spawnParticle(Particle.SOUL_FIRE_FLAME, spawnLoc, 50, 1, 1, 1, 0.1);
        world.playSound(spawnLoc, org.bukkit.Sound.ENTITY_WITHER_SPAWN, 1.0f, 1.0f);
        
        hasSentinels = hasSentinels + 1;
    }

    // Add this method to handle damage events
    @SuppressWarnings("")
    public void generateSentinels(EntityDamageByEntityEvent event) {
        if (!(event.getDamager() instanceof Player player)) return;
        
        double currentHealth = skeletonKing.getHealth();
        double maxHealth = skeletonKing.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue();
        double healthPercentage = (currentHealth / maxHealth) * 100;
        
        if (healthPercentage <= 25 && hasSentinels < 3) {
            spawnSentinels(player, 3);
        } else if (healthPercentage <= 50 && hasSentinels < 2) {
            spawnSentinels(player, 2);
        } else if (healthPercentage <= 75 && hasSentinels == 0) {
            spawnSentinels(player, 1);
        }
    }

    // Reset sentinel spawn state when the boss is generated
    @Override
    public void generateBoss(World world, Location location) {
        super.generateBoss(world, location);
        hasSentinels = 0;
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