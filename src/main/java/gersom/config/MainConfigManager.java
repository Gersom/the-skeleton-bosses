package gersom.config;

import java.util.List;

import gersom.TSB;

public class MainConfigManager {
    private final CustomConfig configFile;
    private LanguageManager languageManager;
    private final TSB plugin;

    public MainConfigManager(TSB plugin) {
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

    public String getPrefix() {
        return configFile.getString("prefix", "[TSB] ");
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
    public int getSpawnMinRadius() {
        return configFile.getInt("spawn.min_radius", 0);
    }
    public int getSpawnMaxRadius() {
        return configFile.getInt("spawn.max_radius", 10);
    }
    public double getSpawnChance() {
        return configFile.getInt("spawn.chance", 100) / 100;
    }
    public int getSpawnLocationX() {
        return configFile.getInt("spawn.location.x", 0);
    }
    public int getSpawnLocationY() {
        // This field is irrelevant, 
        // it is only there as a reference to generate
        // the Location variable, since position «y» 
        // will be the highest non-empty coordinate (impassable).
        return 150;
    }
    public int getSpawnLocationZ() {
        return configFile.getInt("spawn.location.z", 0);
    }

    // BOSSES
    public double  getSpawnKingPercentage() {
        double kpercentage = configFile.getInt("bosses.skeleton_king.percentage", 50);
        return kpercentage / 100;
    }
    public double  getSpawnEmperorPercentage() {
        return 1 - getSpawnKingPercentage();
    }
    // Experience of the bosses
    public int getBossesKingExp() {
        return configFile.getInt("bosses.skeleton_king.drop_experience", 350);
    }
    public int getBossesEmperorExp() {
        return configFile.getInt("bosses.skeleton_emperor.drop_experience", 250);
    }
    // Color Boss
    public String getBossSkeletonKingColor() {
        return configFile.getString("bosses.skeleton_king.color", "&d");
    }
    public String getBossSkeletonEmperorColor() {
        return configFile.getString("bosses.skeleton_emperor.color", "&6");
    }
    // Bossbar messages
    public String getBossesKingBossbarTitle() {
        return configFile.getString("bosses.skeleton_king.bossbar.title", "{boss_color} &l{boss_name} ({health}/{max_health})❤");
    }
    public String getBossesKingBossbarColor() {
        return configFile.getString("bosses.skeleton_king.bossbar.color", "PURPLE");
    }
    public String getBossesEmperorBossbarTitle() {
        return configFile.getString("bosses.skeleton_emperor.bossbar.title", "{boss_color} &l{boss_name} ({health}/{max_health})❤");
    }
    public String getBossesEmperorBossbarColor() {
        return configFile.getString("bosses.skeleton_emperor.bossbar.color", "YELLOW");
    }
    // Command of the bosses
    public Boolean getBossesCommandEnabled() {
        return configFile.getBoolean("commands_after_death", false);
    }
    public String getBossesKingCommand() {
        return configFile.getString("bosses.skeleton_king.command", "me killed the king");
    }
    public String getBossesEmperorCommand() {
        return configFile.getString("bosses.skeleton_emperor.command", "me killed the emperor");
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
    
    // BOSSES MESSAGES LANGUAGE
    // Name of the bosses
    public String getBossSkeletonEmperor() {
        return languageManager.getBossSkeletonEmperor();
    }
    public String getBossSkeletonKing() {
        return languageManager.getBossSkeletonKing();
    }
    
    // Messages of the bosses
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