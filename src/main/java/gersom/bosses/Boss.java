package gersom.bosses;

import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDeathEvent;

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

    // Estos métodos abstractos deben ser implementados por cada jefe específico
    protected abstract int getExperience();
    protected abstract String getKillerCommand();
    protected abstract boolean isNearbyCommandEnabled();
    protected abstract int getNearbyCommandRadius();
    protected abstract String getNearbyCommand();
}