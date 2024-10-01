package mu.gersom.config;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import mu.gersom.MuMc;

public class CustomConfig {
    private final MuMc plugin;
    private final String fileName;
    private FileConfiguration fileConfiguration = null;
    private File file = null;
    private final String folderName;

    public CustomConfig(String fileName, String folderName, MuMc plugin) {
        this.fileName = fileName;
        this.folderName = folderName;
        this.plugin = plugin;
    }

    public String getPath() { return this.fileName; }

    public void registerConfig() {
        if (folderName != null) {
            file = new File(plugin.getDataFolder() + File.separator + folderName, fileName);
        } else {
            file = new File(plugin.getDataFolder(), fileName);
        }

        if (!file.exists()) {
            if (folderName != null) {
                plugin.saveResource(folderName + File.separator + fileName, false);
            } else {
                plugin.saveResource(fileName, false);
            }
        }

        fileConfiguration = new YamlConfiguration();

        try {
            fileConfiguration.load(file);
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }
    }
    
    public void saveConfig() {
        try {
            fileConfiguration.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public FileConfiguration getConfig() {
        if (fileConfiguration == null) {
            registerConfig();
        }
        return fileConfiguration;
    }

    public boolean reloadConfig() {
        if (fileConfiguration == null) {
            if (folderName != null) {
                file = new File(plugin.getDataFolder() + File.separator + folderName, fileName);
            } else {
                file = new File(plugin.getDataFolder(), fileName);
            }
        }

        fileConfiguration = YamlConfiguration.loadConfiguration(file);

        if (file != null) {
            YamlConfiguration defConfig = YamlConfiguration.loadConfiguration(file);
            fileConfiguration.setDefaults(defConfig);
        }

        return true;
    }

    public String getString(String path, String defaultValue) {
        return fileConfiguration.getString(path, defaultValue);
    }

    public int getInt(String path, int defaultValue) {
        return fileConfiguration.getInt(path, defaultValue);
    }

    public boolean getBoolean(String path, boolean defaultValue) {
        return fileConfiguration.getBoolean(path, defaultValue);
    }

    public double getDouble(String path, double defaultValue) {
        return fileConfiguration.getDouble(path, defaultValue);
    }

    public List<String> getStringList(String path) {
        return fileConfiguration.getStringList(path);
    }
}
