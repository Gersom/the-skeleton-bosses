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
import org.bukkit.entity.SkeletonHorse;
import org.bukkit.entity.Stray;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

import gersom.TSB;
import gersom.utils.General;

public class SkeletonWinterLord extends Boss {
    private Stray skeletonWinterLord = null;
    private int hasMinion = 0;

    public SkeletonWinterLord(TSB plugin) {
        super(plugin, "skeletonWinterLord");
    }

    @Override
    protected String getBossName() {
        return plugin.getConfigs().getLangBossWinterLordName();
    }

    @Override
    protected String getBossColor() {
        return plugin.getConfigs().getBossWinterLordColor();
    }

    @Override
    protected String getBossBarColor() {
        return plugin.getConfigs().getBossWinterLordBossbarColor();
    }

    @Override
    protected String getBossBarTitle() {
        return plugin.getConfigs().getBossWinterLordBossbarTitle();
    }

    @Override
    protected double getMaxHealth() {
        return plugin.getConfigs().getBossWinterLordHealth();
    }

    @Override
    protected Entity spawnBossEntity(World world, Location location) {
        skeletonWinterLord = (Stray) world.spawnEntity(location, EntityType.STRAY);
        return skeletonWinterLord;
    }

    @Override
    @SuppressWarnings("")
    protected void equipBoss() {
        // Crear equipamiento
        Map<Enchantment, Integer> helmetEnchants = new HashMap<>();
        helmetEnchants.put(Enchantment.UNBREAKING, 3);
        helmetEnchants.put(Enchantment.PROTECTION, 4);
        ItemStack helmet = createEnchantedItem(
            Material.IRON_HELMET,
            plugin.getConfigs().getLangBossesItemHelmet(),
            helmetEnchants
        );
        ItemMeta helmetMeta = helmet.getItemMeta();
        if (helmetMeta != null) {
            helmetMeta.setUnbreakable(true);
            helmet.setItemMeta(helmetMeta);
        }

        // Botas
        Map<Enchantment, Integer> bootsEnchants = new HashMap<>();
        bootsEnchants.put(Enchantment.FROST_WALKER, 2);
        // bootsEnchants.put(Enchantment.PROTECTION, 4);
        ItemStack boots = createEnchantedItem(
            Material.LEATHER_BOOTS,
            plugin.getConfigs().getLangBossesItemBoots(),
            bootsEnchants
        );
        ItemMeta bootsMeta = boots.getItemMeta();
        if (bootsMeta != null) {
            bootsMeta.setUnbreakable(true);
            boots.setItemMeta(bootsMeta);
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
        bowEnchants.put(Enchantment.POWER, plugin.getConfigs().getBossWinterLordPower());
        bowEnchants.put(Enchantment.MULTISHOT, 1);
        ItemStack bow = createEnchantedItem(
            Material.BOW,
            plugin.getConfigs().getLangBossesItemBow(),
            bowEnchants
        );

        // Equipar items
        skeletonWinterLord.getEquipment().setHelmet(helmet);
        skeletonWinterLord.getEquipment().setChestplate(elytra);
        skeletonWinterLord.getEquipment().setItemInMainHand(bow);
        skeletonWinterLord.getEquipment().setBoots(boots);

        // Configurar chances de drop
        skeletonWinterLord.getEquipment().setHelmetDropChance(0.0f);
        skeletonWinterLord.getEquipment().setChestplateDropChance(0.0f);
        skeletonWinterLord.getEquipment().setLeggingsDropChance(0.0f);
        skeletonWinterLord.getEquipment().setBootsDropChance(0.0f);
        skeletonWinterLord.getEquipment().setItemInMainHandDropChance(0.0f);
        skeletonWinterLord.getEquipment().setItemInOffHandDropChance(0.0f);
    }

    @Override
    @SuppressWarnings("")
    public void generateDrops(EntityDeathEvent event) {
        Map<Enchantment, Integer> enchantsHelmet = new HashMap<>();
        enchantsHelmet.put(Enchantment.UNBREAKING, 3);
        enchantsHelmet.put(Enchantment.FROST_WALKER, 2);
        ItemStack dropHelmet = createEnchantedItem(
            Material.LEATHER_BOOTS,
            plugin.getConfigs().getLangBossesItemBoots(),
            enchantsHelmet
        );
        
        ItemStack arrow = new ItemStack(Material.TIPPED_ARROW, 64);
        PotionMeta metaArrow = (PotionMeta) arrow.getItemMeta();
        PotionEffect strongSlownes = new PotionEffect(PotionEffectType.SLOWNESS, 500, 3, true, true, true);
        metaArrow.addCustomEffect(strongSlownes, true);
        arrow.setItemMeta(metaArrow);
        
        Map<Enchantment, Integer> enchantsBow = new HashMap<>();
        enchantsBow.put(Enchantment.INFINITY, 1);
        enchantsBow.put(Enchantment.UNBREAKING, 3);
        ItemStack dropBow = createEnchantedItem(
            Material.BOW,
            plugin.getConfigs().getLangBossesItemBow(),
            enchantsBow
        );

        event.getDrops().add(arrow);
        event.getDrops().add(dropHelmet);
        event.getDrops().add(dropBow);
    }

    private ItemMeta createIndestructibleEquip(ItemStack item, String itemName) {
        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            meta.setUnbreakable(true);
            meta.setDisplayName(General.setColor(getBossColor() + "Minion's " + itemName));
        }
        return meta;
    }

    @SuppressWarnings("")
    private void equipMinion(Stray minion) {
        // Create and configure the armor items
        ItemStack helmet = new ItemStack(Material.IRON_HELMET);
        ItemStack chestplate = new ItemStack(Material.IRON_CHESTPLATE);
        ItemStack leggings = new ItemStack(Material.IRON_LEGGINGS);
        ItemStack boots = new ItemStack(Material.LEATHER_BOOTS);
        ItemStack sword = new ItemStack(Material.IRON_SWORD);
        ItemStack shield = new ItemStack(Material.SHIELD);

        ItemMeta helmetMeta = createIndestructibleEquip(helmet, "Helmet");
        helmet.setItemMeta(helmetMeta);

        ItemMeta chestplateMeta = createIndestructibleEquip(chestplate, "Chestplate");
        chestplateMeta.addEnchant(Enchantment.BLAST_PROTECTION, 10, true);
        chestplate.setItemMeta(chestplateMeta);

        ItemMeta swordMeta = createIndestructibleEquip(sword, "Sword");
        sword.setItemMeta(swordMeta);

        // Equip the minion
        minion.getEquipment().setHelmet(helmet);
        minion.getEquipment().setChestplate(chestplate);
        minion.getEquipment().setLeggings(leggings);
        minion.getEquipment().setBoots(boots);
        minion.getEquipment().setItemInMainHand(sword);
        minion.getEquipment().setItemInOffHand(shield);

        // Set all drop chances to 0
        minion.getEquipment().setHelmetDropChance(0.0f);
        minion.getEquipment().setChestplateDropChance(0.0f);
        minion.getEquipment().setLeggingsDropChance(0.0f);
        minion.getEquipment().setBootsDropChance(0.0f);
        minion.getEquipment().setItemInMainHandDropChance(0.0f);
    }

    // Add this new method to spawn Minions
    @SuppressWarnings("")
    public void spawnMinions(Player player, int levelMinion) {        
        World world = skeletonWinterLord.getWorld();
        Location playerLoc = player.getLocation();
        Vector direction = playerLoc.getDirection();
        
        // Calculate spawn position behind the player
        double distance = 1.0; // Distance behind player
        double spawnX = playerLoc.getX() - direction.getX() * distance;
        double spawnZ = playerLoc.getZ() - direction.getZ() * distance;
        double spawnY = world.getHighestBlockYAt((int)spawnX, (int)spawnZ) + 1;
        
        Location spawnLoc = new Location(world, spawnX, spawnY, spawnZ);

        // Spawn 3 Minions
        for (int i = 0; i < levelMinion; i++) {
            Stray minion = (Stray) world.spawnEntity(spawnLoc, EntityType.STRAY);
            
            minion.getAttribute(Attribute.GENERIC_SCALE).setBaseValue(0.65);

            // Configure minion getBossName()
            minion.setCustomName(General.setColor(
                getBossColor() + "&l" + plugin.getConfigs().getLangBossWinterLordMinion()
            ));
            minion.setCustomNameVisible(true);

            // Set minion attributes
            if (minion.getAttribute(Attribute.GENERIC_MAX_HEALTH) != null) {
                minion.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue((int) ((levelMinion * 2) - 1) + 10); // 15x hearts
            }
            if (minion.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED) != null) {
                minion.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED).setBaseValue(0.3); // Slightly faster than normal
            }
            minion.setHealth((int) ((levelMinion * 2) - 1) + 10);
            minion.getAttribute(Attribute.GENERIC_ATTACK_DAMAGE)
                .setBaseValue(4 + levelMinion);
            
            // Equip the minion with armor and weapons
            equipMinion(minion);
            
            // Adjust spawn location for next sentinel
            spawnLoc.add(random.nextDouble() - 1, 0, random.nextDouble() - 1);
        }
        
        // Play effects
        world.spawnParticle(Particle.SOUL_FIRE_FLAME, spawnLoc, 50, 1, 1, 1, 0.1);
        world.playSound(spawnLoc, org.bukkit.Sound.ENTITY_WITHER_SPAWN, 1.0f, 1.0f);
        
        hasMinion = hasMinion + 1;
    }

    // Add this method to handle damage events
    @SuppressWarnings("")
    public void generateMinions(EntityDamageByEntityEvent event) {
        if (!(event.getDamager() instanceof Player player)) return;
        
        double currentHealth = skeletonWinterLord.getHealth();
        double maxHealth = skeletonWinterLord.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue();
        double healthPercentage = (currentHealth / maxHealth) * 100;
        
        if (healthPercentage <= 25 && hasMinion < 3) {
            spawnMinions(player, 3);
        } else if (healthPercentage <= 50 && hasMinion < 2) {
            spawnMinions(player, 2);
        } else if (healthPercentage <= 75 && hasMinion == 0) {
            spawnMinions(player, 1);
        }
    }

    @Override
    public Stray getEntityBoss() {
        return skeletonWinterLord;
    }

    public void setEntityBoss(Stray entity) {
        skeletonWinterLord = entity;
    }

    @Override
    protected int getExperience() {
        return plugin.getConfigs().getBossWinterLordExp();
    }

    @Override
    protected List<String> getKillerCommands() {
        return plugin.getConfigs().getBossWinterLordCommands();
    }

    @Override
    protected boolean isNearbyCommandsEnabled() {
        return plugin.getConfigs().getBossWinterLordNearbyCommandsEnabled();
    }

    @Override
    protected int getNearbyCommandsRadius() {
        return plugin.getConfigs().getBossWinterLordNearbyCommandsRadius();
    }

    @Override
    protected List<String> getNearbyCommands() {
        return plugin.getConfigs().getBossWinterLordNearbyCommands();
    }

    @Override
    protected void createHorseParticles(SkeletonHorse horse) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
