/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package mu.gersom.generators;

import java.util.Random;
import java.util.UUID;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.World;
import org.bukkit.attribute.Attribute;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Skeleton;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import mu.gersom.MuMc;
import mu.gersom.utils.General;

/**
 *
 * @author Gersom
 */
public class SkeletonEmperor {
    private UUID skeletonEmperorID = null;
    private final MuMc plugin;
    private final Random random = new Random();

    public SkeletonEmperor(MuMc plugin) {
        this.plugin = plugin;
    }

    public void generateSkeletonEmperor(World world, Location location) {
        Skeleton skeleton = (Skeleton) world.spawnEntity(location, EntityType.SKELETON);
        
        // Add the skeleton's UUID to our set of custom skeletons
        skeletonEmperorID = skeleton.getUniqueId();

        // Make the skeleton persistent
        skeleton.setRemoveWhenFarAway(false);

        skeleton.setCustomName(General.setColor("&d&lSkeleton Emperor&f"));
        skeleton.setCustomNameVisible(true);

        // Change scale Mob
        skeleton.getAttribute(Attribute.GENERIC_SCALE).setBaseValue(1.15);

        // Set health to 20 hearts (40 health points)
        skeleton.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(140);
        skeleton.setHealth(140);

        // Create and enchant golden helmet
        ItemStack goldenHelmet = new ItemStack(Material.GOLDEN_HELMET);
        ItemMeta helmetMeta = goldenHelmet.getItemMeta();
        helmetMeta.addEnchant(Enchantment.UNBREAKING, 3, true); // Unbreaking III
        helmetMeta.addEnchant(Enchantment.PROTECTION, 4, true); // Protection IV
        goldenHelmet.setItemMeta(helmetMeta);

        // Create elytra with Unbreaking III
        ItemStack elytra = new ItemStack(Material.ELYTRA);
        ItemMeta elytraMeta = elytra.getItemMeta();
        elytraMeta.addEnchant(Enchantment.UNBREAKING, 3, true); // Unbreaking III
        elytra.setItemMeta(elytraMeta);

        // Create and enchant bow
        ItemStack bowCustom = new ItemStack(Material.BOW);
        ItemMeta bowCustomMeta = bowCustom.getItemMeta();
        bowCustomMeta.addEnchant(Enchantment.MULTISHOT, 1, true); // Multishot
        bowCustomMeta.addEnchant(Enchantment.FLAME, 1, true); // Flame
        bowCustomMeta.addEnchant(Enchantment.POWER, 30, true); // Power V
        bowCustomMeta.addEnchant(Enchantment.UNBREAKING, 3, true); // Unbreaking III
        bowCustomMeta.addEnchant(Enchantment.INFINITY, 1, true); // Infinity
        bowCustomMeta.addEnchant(Enchantment.PUNCH, 2, true); // Punch I
        bowCustom.setItemMeta(bowCustomMeta);

        // Set the equipment on the skeleton
        skeleton.getEquipment().setHelmet(goldenHelmet);
        skeleton.getEquipment().setChestplate(elytra);
        skeleton.getEquipment().setItemInMainHand(bowCustom);
        
        // Ensure the equipment doesn't drop when the skeleton dies
        skeleton.getEquipment().setHelmetDropChance(0.0f);
        skeleton.getEquipment().setChestplateDropChance(0.0f);
        skeleton.getEquipment().setLeggingsDropChance(0.0f);
        skeleton.getEquipment().setBootsDropChance(0.0f);
        skeleton.getEquipment().setItemInMainHandDropChance(0.0f);
        skeleton.getEquipment().setItemInOffHandDropChance(0.0f);

        // Add fire resistance effect
        skeleton.addPotionEffect(new PotionEffect(PotionEffectType.FIRE_RESISTANCE, 999999, 0, false, true));

        // Add dragon breath particles
        new BukkitRunnable() {
            @Override
            public void run() {
                if (skeleton.isDead()) {
                    this.cancel();
                    return;
                }
                skeleton.getWorld().spawnParticle(Particle.SOUL_FIRE_FLAME, skeleton.getLocation().add(0, 0, 0), 10, 0.3, 0.3, 0.3, 0.01);
            }
        }.runTaskTimer(plugin, 0L, 10L);
        
        // sender.sendMessage(General.setColor("&a " + Vars.prefix + "&a&l¡Esqueleto personalizado ha sido creado!"));
    }

    public void onSkeletonEmperorDeath(EntityDeathEvent event) {
        event.getDrops().clear(); // Clear default drops
            
        double dropChance = random.nextDouble();
        
        if (dropChance < 0.20) {
            // 20% chance to drop bow with MULTISHOT and FLAME
            ItemStack bow = new ItemStack(Material.BOW);
            ItemMeta bowMeta = bow.getItemMeta();
            bowMeta.setDisplayName(General.setColor("&6Skeleton Emperor Bow"));
            bowMeta.addEnchant(Enchantment.MULTISHOT, 1, true);
            bowMeta.addEnchant(Enchantment.FLAME, 1, true);
            bow.setItemMeta(bowMeta);
            event.getDrops().add(bow);
        } else if (dropChance < 0.50) {
            // 30% chance to drop bow with INFINITY and UNBREAKING 3
            ItemStack bow = new ItemStack(Material.BOW);
            ItemMeta bowMeta = bow.getItemMeta();
            bowMeta.setDisplayName(General.setColor("&6Skeleton Emperor Bow"));
            bowMeta.addEnchant(Enchantment.INFINITY, 1, true);
            bowMeta.addEnchant(Enchantment.UNBREAKING, 3, true);
            bow.setItemMeta(bowMeta);
            event.getDrops().add(bow);
        } else {
            // 50% chance to drop golden helmet with UNBREAKING 3 and FIRE_PROTECTION 3
            ItemStack helmet = new ItemStack(Material.GOLDEN_HELMET);
            ItemMeta helmetMeta = helmet.getItemMeta();
            helmetMeta.setDisplayName(General.setColor("&6Skeleton Emperor Helmet"));
            helmetMeta.addEnchant(Enchantment.UNBREAKING, 3, true);
            helmetMeta.addEnchant(Enchantment.FIRE_PROTECTION, 3, true);
            helmet.setItemMeta(helmetMeta);
            event.getDrops().add(helmet);
        }
        
        // Remove the skeleton's UUID from our set
        skeletonEmperorID = null;
    }

    public UUID getSkeletonEmperorID() {
        return skeletonEmperorID;
    }
}
