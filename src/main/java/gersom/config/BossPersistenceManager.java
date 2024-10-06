package gersom.config;

import java.io.File;
import java.io.IOException;
import java.util.UUID;
import java.util.logging.Level;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import gersom.TSB;
import gersom.utils.Console;
import gersom.utils.Vars;

public class BossPersistenceManager {
    private final TSB plugin;
    private final File dataFile;
    private FileConfiguration dataConfig;

    public BossPersistenceManager(TSB plugin) {
        this.plugin = plugin;
        createDataFolder();
        this.dataFile = new File(plugin.getDataFolder() + File.separator + "data", "bosses_data.yml");
        this.loadData();
    }

    private void createDataFolder() {
        File dataFolder = new File(plugin.getDataFolder(), "data");
        if (!dataFolder.exists()) {
            dataFolder.mkdirs();
            Console.sendMessage("&aCreated data folder for " + Vars.name);
        }
    }

    private void loadData() {
        if (!dataFile.exists()) {
            try {
                dataFile.getParentFile().mkdirs();
                dataFile.createNewFile();
                Console.sendMessage("&aCreated new bosses_data.yml file");
            } catch (IOException e) {
                plugin.getLogger().severe("Could not create boss_data.yml");
                e.printStackTrace();
            }
        }
        this.dataConfig = YamlConfiguration.loadConfiguration(dataFile);
    }

    public void saveBossData(String bossType, UUID uuid) {
        dataConfig.set(bossType + ".uuid", uuid.toString());
        try {
            dataConfig.save(dataFile);
        } catch (IOException e) {
            plugin.getLogger().log(Level.SEVERE, "Could not save boss data to {0}", dataFile);
            e.printStackTrace();
        }
    }

    public UUID getBossUUID(String bossType) {
        String uuidString = dataConfig.getString(bossType + ".uuid");
        return uuidString != null ? UUID.fromString(uuidString) : null;
    }

    public void removeBossData(String bossType) {
        dataConfig.set(bossType, null);
        try {
            dataConfig.save(dataFile);
        } catch (IOException e) {
            plugin.getLogger().log(Level.SEVERE, "Could not save boss data to {0}", dataFile);
            e.printStackTrace();
        }
    }

    public void reloadData() {
        dataConfig = YamlConfiguration.loadConfiguration(dataFile);
    }
}