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
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Skeleton;
import org.bukkit.entity.SkeletonHorse;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;

import gersom.TSB;

/**
 *
 * @author Gersom
 */
public class SkeletonEmperor extends Boss {
    private Skeleton skeletonEmperor = null;

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
        
        if (dropChance < 0.33) {
            // 33% chance to drop bow with MULTISHOT and FLAME
            Map<Enchantment, Integer> enchants = new HashMap<>();
            enchants.put(Enchantment.MULTISHOT, 1);
            enchants.put(Enchantment.FLAME, 1);
            ItemStack drop = createEnchantedItem(
                Material.BOW,
                plugin.getConfigs().getLangBossesItemBow(),
                enchants
            );
            event.getDrops().add(drop);
        } else if (dropChance < 0.66) {
            // 33% chance to drop bow with INFINITY and UNBREAKING
            Map<Enchantment, Integer> enchants = new HashMap<>();
            enchants.put(Enchantment.INFINITY, 1);
            enchants.put(Enchantment.UNBREAKING, 3);
            ItemStack drop = createEnchantedItem(
                Material.BOW,
                plugin.getConfigs().getLangBossesItemBow(),
                enchants
            );
            event.getDrops().add(drop);
        } else {
            // 33% chance to drop golden helmet
            Map<Enchantment, Integer> enchants = new HashMap<>();
            enchants.put(Enchantment.UNBREAKING, 3);
            enchants.put(Enchantment.FIRE_PROTECTION, 3);
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