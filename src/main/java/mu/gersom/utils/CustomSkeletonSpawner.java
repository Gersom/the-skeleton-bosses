package mu.gersom.utils;

import java.util.HashSet;
import java.util.Objects;
import java.util.Random;
import java.util.Set;
import java.util.UUID;

import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.attribute.Attribute;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Skeleton;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import mu.gersom.MuMc;

public class CustomSkeletonSpawner implements Listener {

    private final MuMc plugin;
    private final Random random = new Random();
    private final Set<UUID> customSkeletons = new HashSet<>();
    
    public CustomSkeletonSpawner(MuMc plugin) {
        this.plugin = plugin;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    public void spawnCustomSkeleton(Player player) {
        Skeleton skeleton = (Skeleton) player.getWorld().spawnEntity(Objects.requireNonNull(player.getLocation()), EntityType.SKELETON);

        // Make the skeleton persistent
        skeleton.setRemoveWhenFarAway(false);

        // Add the skeleton's UUID to our set of custom skeletons
        customSkeletons.add(skeleton.getUniqueId());

        // Change scale Mob
        skeleton.getAttribute(Attribute.GENERIC_SCALE).setBaseValue(1.5);

        // Set health to 20 hearts (40 health points)
        skeleton.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(200);
        skeleton.setHealth(200);

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
        skeleton.addPotionEffect(new PotionEffect(PotionEffectType.FIRE_RESISTANCE, 999999, 0, false, false));

        // Add dragon breath particles
        new BukkitRunnable() {
            @Override
            public void run() {
                if (skeleton.isDead()) {
                    this.cancel();
                    return;
                }
                skeleton.getWorld().spawnParticle(Particle.DRAGON_BREATH, skeleton.getLocation().add(0, 1.5, 0), 10, 0.3, 0.3, 0.3, 0.01);
            }
        }.runTaskTimer(plugin, 0L, 5L);
        
        player.sendMessage(General.setColor("&a " + Vars.prefix + "&a&l¡Esqueleto personalizado ha sido creado!"));
    }

    @EventHandler
    public void onSkeletonDeath(EntityDeathEvent event) {
        if (event.getEntityType() == EntityType.SKELETON && customSkeletons.contains(event.getEntity().getUniqueId())) {
            event.getDrops().clear(); // Clear default drops
            
            double dropChance = random.nextDouble();
            
            if (dropChance < 0.25) {
                // 25% chance to drop bow with MULTISHOT and FLAME
                ItemStack bow = new ItemStack(Material.BOW);
                ItemMeta bowMeta = bow.getItemMeta();
                bowMeta.addEnchant(Enchantment.MULTISHOT, 1, true);
                bowMeta.addEnchant(Enchantment.FLAME, 1, true);
                bow.setItemMeta(bowMeta);
                event.getDrops().add(bow);
            } else if (dropChance < 0.50) {
                // 25% chance to drop bow with INFINITY and UNBREAKING 3
                ItemStack bow = new ItemStack(Material.BOW);
                ItemMeta bowMeta = bow.getItemMeta();
                bowMeta.addEnchant(Enchantment.INFINITY, 1, true);
                bowMeta.addEnchant(Enchantment.UNBREAKING, 3, true);
                bow.setItemMeta(bowMeta);
                event.getDrops().add(bow);
            } else {
                // 50% chance to drop golden helmet with UNBREAKING 3 and FIRE_PROTECTION 3
                ItemStack helmet = new ItemStack(Material.GOLDEN_HELMET);
                ItemMeta helmetMeta = helmet.getItemMeta();
                helmetMeta.addEnchant(Enchantment.UNBREAKING, 3, true);
                helmetMeta.addEnchant(Enchantment.FIRE_PROTECTION, 3, true);
                helmet.setItemMeta(helmetMeta);
                event.getDrops().add(helmet);
            }
            
            // Remove the skeleton's UUID from our set
            customSkeletons.remove(event.getEntity().getUniqueId());
        }
    }
}