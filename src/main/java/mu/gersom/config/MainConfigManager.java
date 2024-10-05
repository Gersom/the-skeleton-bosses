package mu.gersom.config;

import java.util.List;

import mu.gersom.MuMc;

public class MainConfigManager {
    private final CustomConfig configFile;
    private LanguageManager languageManager;
    private final MuMc plugin;

    public MainConfigManager(MuMc plugin) {
        this.plugin = plugin;
        this.configFile = new CustomConfig("config.yml", null, plugin);
        this.configFile.registerConfig();
    }

    public void initialize() {
        this.languageManager = new LanguageManager(plugin);
    }

    public void reloadConfig() {
        configFile.reloadConfig();
        languageManager.reloadLanguageConfig();
    }

    public void setLanguage(String language) {
        configFile.getConfig().set("language", language);
        configFile.saveConfig();
        languageManager.reloadLanguageConfig();
    }

    public String getLanguage() {
        return configFile.getString("language", "en");
    }

    // Spawn data
    public Boolean getSpawnEnabled() {
        return configFile.getBoolean("spawn.enabled", false);
    }
    public String getSpawnWorld() {
        return configFile.getString("spawn.world", "world");
    }
    public int getSpawnInterval() {
        return configFile.getInt("spawn.interval", 20);
    }
    public int getSpawnRadius() {
        return configFile.getInt("spawn.radius", 10);
    }
    public double getSpawnChance() {
        return configFile.getInt("spawn.chance", 100) / 100;
    }
    public int getSpawnLocationX() {
        return configFile.getInt("spawn.location.x", 0);
    }
    public int getSpawnLocationY() {
        return configFile.getInt("spawn.location.y", 150);
    }
    public int getSpawnLocationZ() {
        return configFile.getInt("spawn.location.z", 0);
    }
    public double  getSpawnKingPercentage() {
        double kpercentage = configFile.getInt("spawn.king_percentage", 50);
        return kpercentage / 100;
    }
    public double  getSpawnEmperorPercentage() {
        return 1 - getSpawnKingPercentage();
    }

    // Delegate message methods to LanguageManager
    // Plugin messages
    public String getMsgPluginEnabled() {
        return languageManager.getMsgPluginEnabled();
    }
    public String getMsgPluginDisabled() {
        return languageManager.getMsgPluginDisabled();
    }

    // About messages
    public String getWelcomeMessage() {
        return languageManager.getWelcomeMessage();
    }
    public List<String> getDescriptionMessages() {
        return languageManager.getDescriptionMessages();
    }

    // Commands messages
    public String getListCommands() {
        return languageManager.getListCommands();
    }
    public String getPlayerOnlyCommand() {
        return languageManager.getPlayerOnlyCommand();
    }
    public String getHelpText() {
        return languageManager.getHelpText();
    }
    public String getReloadText() {
        return languageManager.getReloadText();
    }
    public String getCommandNotFound() {
        return languageManager.getCommandNotFound();
    }
    public String getNotPermission() {
        return languageManager.getNotPermission();
    }
    
    // Name of the bosses
    public String getBossSkeletonEmperor() {
        return languageManager.getBossSkeletonEmperor();
    }
    public String getBossSkeletonKing() {
        return languageManager.getBossSkeletonKing();
    }

    // Messages of the bosses
    public Boolean getBossesCommandEnabled() {
        return configFile.getBoolean("bosses.commands_after_death", false);
    }
    public String getBossesKingCommand() {
        return configFile.getString("bosses.king_command", "me killed the king");
    }
    public String getBossesEmperorCommand() {
        return configFile.getString("bosses.emperor_command", "me killed the emperor");
    }

    public String getBossMessageSpawn() {
        return languageManager.getBossMessageSpawn();
    }
    public String getBossMessageDeath() {
        return languageManager.getBossMessageDeath();
    }
    public String getBossMessageKilled() {
        return languageManager.getBossMessageKilled();
    }

    // Name of the objects left behind when he dies
    public String getBossItemBow() {
        return languageManager.getBossItemBow();
    }
    public String getBossItemSword() {
        return languageManager.getBossItemSword();
    }
    public String getBossItemHelmet() {
        return languageManager.getBossItemHelmet();
    }
    
}