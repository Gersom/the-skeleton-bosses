package gersom.bosses;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.SkeletonHorse;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import gersom.TSB;

public abstract class Boss {
    protected TSB plugin;
    protected String bossType;

    public Boss(TSB plugin, String bossType) {
        this.plugin = plugin;
        this.bossType = bossType;
    }

    public abstract Entity getEntity();
    public abstract void generateDrops(EntityDeathEvent event);
    public abstract void cleanUp();

    public void handleDeath(EntityDeathEvent event) {
        plugin.getBossPersistenceManager().removeBossData(bossType);
        Player killer = event.getEntity().getKiller();

        event.getDrops().clear();
        event.setDroppedExp(getExperience());

        if (plugin.getConfigs().getBossesCommandEnabled()) {
            if (killer != null) {
                executeKillerCommand(killer);
            }
            executeNearbyPlayersCommands(event.getEntity().getLocation());
        } else {
            generateDrops(event);
        }

        cleanUp();
        plugin.getMainMobs().onSuccessDeath(bossType, killer);
    }

    protected void executeKillerCommand(Player killer) {
        String command = getKillerCommand();
        if (command != null && !command.trim().isEmpty()) {
            command = command.replace("{player_killer}", killer.getName());
            plugin.getServer().dispatchCommand(plugin.getServer().getConsoleSender(), command);
        }
    }
    
    @SuppressWarnings("")
    protected void executeNearbyPlayersCommands(Location location) {
        String command = getNearbyCommand();
        if (isNearbyCommandEnabled() && command != null && !command.trim().isEmpty()) {
            int radius = getNearbyCommandRadius();
    
            for (Entity entity : location.getWorld().getNearbyEntities(location, radius, radius, radius)) {
                if (entity instanceof Player nearbyPlayer) {
                    String playerCommand = command.replace("{player}", nearbyPlayer.getName());
                    plugin.getServer().dispatchCommand(plugin.getServer().getConsoleSender(), playerCommand);
                }
            }
        }
    }

    @SuppressWarnings("")
    protected void generateHorse(String horseName, Entity boss) {
        World world = boss.getWorld();
        Location location = boss.getLocation();
        
        // Generar el caballo esqueleto
        SkeletonHorse skeletonHorse = (SkeletonHorse) world.spawnEntity(location, EntityType.SKELETON_HORSE);
        skeletonHorse.setCustomName(horseName);

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

        // Montar el Boss en el caballo
        skeletonHorse.addPassenger(boss);

        world.playSound(location, Sound.ENTITY_LIGHTNING_BOLT_IMPACT, 1.5f, 1.0f);

        // Crear las partículas específicas para el tipo de boss
        createHorseParticles(skeletonHorse);
    }

    // Método abstracto para que cada boss implemente sus propias partículas
    protected abstract void createHorseParticles(SkeletonHorse horse);

    // Estos métodos abstractos deben ser implementados por cada jefe específico
    protected abstract int getExperience();
    protected abstract String getKillerCommand();
    protected abstract boolean isNearbyCommandEnabled();
    protected abstract int getNearbyCommandRadius();
    protected abstract String getNearbyCommand();
}